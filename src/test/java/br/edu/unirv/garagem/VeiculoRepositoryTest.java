package br.edu.unirv.garagem;
import br.edu.unirv.garagem.model.Veiculo;
import br.edu.unirv.garagem.repository.VeiculoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;
class VeiculoRepositoryTest {
    @TempDir Path pasta;
    @Test void crudPersistenciaEPlacaNormalizada() {
        Path arquivo = pasta.resolve("veiculos.json");
        VeiculoRepository repo = new VeiculoRepository(new ObjectMapper(), arquivo);
        assertTrue(repo.obterTodas().isEmpty()); assertTrue(Files.exists(arquivo));
        Veiculo v = new Veiculo(); v.setPlaca("ABC-1234"); v.setMarca("Fiat"); v.setModelo("Argo"); v.setAno(2022);
        repo.adicionar(v); assertEquals(1, v.getId());
        assertTrue(repo.placaJaExiste("abc1234", 0)); assertFalse(repo.placaJaExiste("ABC1234", v.getId()));
        VeiculoRepository reaberto = new VeiculoRepository(new ObjectMapper(), arquivo);
        assertEquals("Argo", reaberto.obterPorId(1).orElseThrow().getModelo());
        v.setModelo("Uno"); reaberto.atualizar(v); assertEquals("Uno", repo.obterPorId(1).orElseThrow().getModelo());
        repo.remover(1); assertTrue(reaberto.obterTodas().isEmpty());
    }
}

