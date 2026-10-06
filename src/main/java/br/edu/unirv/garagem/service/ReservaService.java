package br.edu.unirv.garagem.service;
import br.edu.unirv.garagem.model.Reserva;
import br.edu.unirv.garagem.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
@Service
public class ReservaService {
    private final IReservaRepository reservas;
    private final IVeiculoRepository veiculos;
    private final IPessoaRepository pessoas;
    public ReservaService(IReservaRepository reservas, IVeiculoRepository veiculos, IPessoaRepository pessoas) {
        this.reservas = reservas; this.veiculos = veiculos; this.pessoas = pessoas;
    }
    // Validação e gravação no mesmo serviço: controller só apresenta o resultado.
    public synchronized Map<String, String> salvar(Reserva reserva) {
        Map<String, String> erros = new LinkedHashMap<>();
        if (reserva.getVeiculoId() == null || veiculos.obterPorId(reserva.getVeiculoId()).isEmpty())
            erros.put("veiculoId", "Veículo não encontrado");
        if (reserva.getPessoaId() == null || pessoas.obterPorId(reserva.getPessoaId()).isEmpty())
            erros.put("pessoaId", "Pessoa não encontrada");
        if (reserva.getDataInicio() == null) erros.put("dataInicio", "Informe o início");
        if (reserva.getDataFim() == null) erros.put("dataFim", "Informe o fim");
        if (reserva.getDataInicio() != null && reserva.getDataFim() != null) {
            if (reserva.getDataFim().isBefore(reserva.getDataInicio()))
                erros.put("dataFim", "Data de fim não pode ser anterior ao início");
            else if (reserva.getVeiculoId() != null && reservas.existeConflito(reserva.getVeiculoId(), reserva.getDataInicio(), reserva.getDataFim(), reserva.getId()))
                erros.put("dataInicio", "Este veículo já possui reserva nesse período");
        }
        if (erros.isEmpty()) {
            if (reserva.getId() == 0) reservas.adicionar(reserva); else reservas.atualizar(reserva);
        }
        return erros;
    }
    public Map<Integer, String> statusHoje() {
        Map<Integer, String> status = new LinkedHashMap<>();
        LocalDate hoje = LocalDate.now();
        veiculos.obterTodas().forEach(v -> status.put(v.getId(), reservas.existeConflito(v.getId(), hoje, hoje, 0) ? "Reservado" : "Disponível"));
        return status;
    }
}

