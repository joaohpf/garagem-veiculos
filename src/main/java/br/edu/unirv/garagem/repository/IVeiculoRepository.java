package br.edu.unirv.garagem.repository;
import br.edu.unirv.garagem.model.Veiculo;
import java.util.*;

public interface IVeiculoRepository {
    List<Veiculo> obterTodas();
    Optional<Veiculo> obterPorId(int id);
    void adicionar(Veiculo item);
    void atualizar(Veiculo item);
    void remover(int id);
    boolean placaJaExiste(String placa, int idIgnorado);
}

