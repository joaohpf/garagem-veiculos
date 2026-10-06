package br.edu.unirv.garagem.repository;
import br.edu.unirv.garagem.model.Reserva;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.nio.file.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
@Repository
public class ReservaRepository implements IReservaRepository {
    private final ObjectMapper mapper;
    private final Path arquivo;
    @Autowired
    public ReservaRepository(ObjectMapper mapper) { this(mapper, Path.of("data/reservas.json")); }
    public ReservaRepository(ObjectMapper mapper, Path arquivo) { this.mapper = mapper; this.arquivo = arquivo; }
    private List<Reserva> carregar() {
        try {
            if (!Files.exists(arquivo)) salvar(new ArrayList<>());
            return mapper.readValue(arquivo.toFile(), new TypeReference<List<Reserva>>() {});
        } catch (IOException e) { throw new IllegalStateException("Erro ao ler " + arquivo, e); }
    }
    private void salvar(List<Reserva> itens) {
        try {
            Files.createDirectories(arquivo.toAbsolutePath().getParent());
            Path temporario = Files.createTempFile(arquivo.toAbsolutePath().getParent(), "garagem-", ".tmp");
            try {
                mapper.writerWithDefaultPrettyPrinter().writeValue(temporario.toFile(), itens);
                Files.move(temporario, arquivo.toAbsolutePath(), StandardCopyOption.REPLACE_EXISTING);
            } finally { Files.deleteIfExists(temporario); }
        } catch (IOException e) { throw new IllegalStateException("Erro ao salvar " + arquivo, e); }
    }
    public synchronized List<Reserva> obterTodas() { return carregar(); }
    public synchronized Optional<Reserva> obterPorId(int id) { return carregar().stream().filter(i -> i.getId() == id).findFirst(); }
    public synchronized void adicionar(Reserva item) {
        List<Reserva> itens = carregar();
        item.setId(itens.stream().mapToInt(Reserva::getId).max().orElse(0) + 1);
        itens.add(item); salvar(itens);
    }
    public synchronized void atualizar(Reserva item) {
        List<Reserva> itens = carregar();
        for (int i = 0; i < itens.size(); i++) {
            if (itens.get(i).getId() == item.getId()) { itens.set(i, item); salvar(itens); return; }
        }
        throw new IllegalArgumentException("Reserva não encontrado");
    }
    public synchronized void remover(int id) { List<Reserva> itens = carregar(); itens.removeIf(i -> i.getId() == id); salvar(itens); }
    public synchronized boolean existeConflito(int veiculoId, LocalDate inicio, LocalDate fim, int idIgnorado) {
        return carregar().stream().anyMatch(r -> r.getId() != idIgnorado && r.getVeiculoId() == veiculoId
            && !inicio.isAfter(r.getDataFim()) && !fim.isBefore(r.getDataInicio()));
    }
}

