package br.edu.unirv.garagem.repository;
import br.edu.unirv.garagem.model.Reserva;
import java.util.*;
import java.time.LocalDate;
public interface IReservaRepository {
    List<Reserva> obterTodas();
    Optional<Reserva> obterPorId(int id);
    void adicionar(Reserva item);
    void atualizar(Reserva item);
    void remover(int id);
    boolean existeConflito(int veiculoId, LocalDate inicio, LocalDate fim, int idIgnorado);
}

