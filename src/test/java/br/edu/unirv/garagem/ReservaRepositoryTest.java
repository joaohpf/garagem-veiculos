package br.edu.unirv.garagem;
import br.edu.unirv.garagem.model.Reserva;
import br.edu.unirv.garagem.repository.ReservaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
class ReservaRepositoryTest {
    @TempDir Path pasta;
    ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
    ReservaRepository repository;
    Reserva reserva(int veiculo, String inicio, String fim) {
        Reserva r = new Reserva(); r.setVeiculoId(veiculo); r.setPessoaId(1);
        r.setDataInicio(LocalDate.parse(inicio)); r.setDataFim(LocalDate.parse(fim)); return r;
    }
    @BeforeEach void preparar() {
        repository = new ReservaRepository(mapper, pasta.resolve("reservas.json"));
        repository.adicionar(reserva(1, "2026-10-10", "2026-10-15"));
    }
    boolean conflito(int veiculo, String inicio, String fim, int ignorado) {
        return repository.existeConflito(veiculo, LocalDate.parse(inicio), LocalDate.parse(fim), ignorado);
    }
    @Test void limitesInclusivosEOutroVeiculo() {
        assertFalse(conflito(1, "2026-10-16", "2026-10-20", 0));
        assertTrue(conflito(1, "2026-10-14", "2026-10-18", 0));
        assertTrue(conflito(1, "2026-10-15", "2026-10-17", 0));
        assertTrue(conflito(1, "2026-10-01", "2026-10-31", 0));
        assertTrue(conflito(1, "2026-10-11", "2026-10-12", 0));
        assertFalse(conflito(2, "2026-10-10", "2026-10-15", 0));
        assertFalse(conflito(1, "2026-10-10", "2026-10-15", 1));
    }
    @Test void dadosSobrevivemANovaInstanciaEdicaoEExclusao() {
        ReservaRepository reaberto = new ReservaRepository(mapper, pasta.resolve("reservas.json"));
        assertEquals(LocalDate.parse("2026-10-10"), reaberto.obterPorId(1).orElseThrow().getDataInicio());
        Reserva editada = reaberto.obterPorId(1).orElseThrow(); editada.setDataFim(LocalDate.parse("2026-10-20"));
        reaberto.atualizar(editada);
        assertTrue(conflito(1, "2026-10-20", "2026-10-20", 0));
        reaberto.remover(1); assertTrue(repository.obterTodas().isEmpty());
    }
}

