package br.edu.unirv.garagem.repository;
import br.edu.unirv.garagem.model.Veiculo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.nio.file.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
@Repository
public class VeiculoRepository implements IVeiculoRepository {
    private final ObjectMapper mapper;
    private final Path arquivo;
    @Autowired
    public VeiculoRepository(ObjectMapper mapper) { this(mapper, Path.of("data/veiculos.json")); }
    public VeiculoRepository(ObjectMapper mapper, Path arquivo) { this.mapper = mapper; this.arquivo = arquivo; }
    private List<Veiculo> carregar() {
        try {
            if (!Files.exists(arquivo)) salvar(new ArrayList<>());
            return mapper.readValue(arquivo.toFile(), new TypeReference<List<Veiculo>>() {});
        } catch (IOException e) { throw new IllegalStateException("Erro ao ler " + arquivo, e); }
    }
    private void salvar(List<Veiculo> itens) {
        try {
            Files.createDirectories(arquivo.toAbsolutePath().getParent());
            Path temporario = Files.createTempFile(arquivo.toAbsolutePath().getParent(), "garagem-", ".tmp");
            try {
                mapper.writerWithDefaultPrettyPrinter().writeValue(temporario.toFile(), itens);
                Files.move(temporario, arquivo.toAbsolutePath(), StandardCopyOption.REPLACE_EXISTING);
            } finally { Files.deleteIfExists(temporario); }
        } catch (IOException e) { throw new IllegalStateException("Erro ao salvar " + arquivo, e); }
    }
    public synchronized List<Veiculo> obterTodas() { return carregar(); }
    public synchronized Optional<Veiculo> obterPorId(int id) { return carregar().stream().filter(i -> i.getId() == id).findFirst(); }
    public synchronized void adicionar(Veiculo item) {
        List<Veiculo> itens = carregar();
        item.setId(itens.stream().mapToInt(Veiculo::getId).max().orElse(0) + 1);
        itens.add(item); salvar(itens);
    }
    public synchronized void atualizar(Veiculo item) {
        List<Veiculo> itens = carregar();
        for (int i = 0; i < itens.size(); i++) {
            if (itens.get(i).getId() == item.getId()) { itens.set(i, item); salvar(itens); return; }
        }
        throw new IllegalArgumentException("Veiculo não encontrado");
    }
    public synchronized void remover(int id) { List<Veiculo> itens = carregar(); itens.removeIf(i -> i.getId() == id); salvar(itens); }
    private String normalizar(String placa) { return placa == null ? "" : placa.replaceAll("[^a-zA-Z0-9]", "").toUpperCase(Locale.ROOT); }
    public synchronized boolean placaJaExiste(String placa, int idIgnorado) {
        return carregar().stream().anyMatch(v -> v.getId() != idIgnorado && normalizar(v.getPlaca()).equals(normalizar(placa)));
    }
}

