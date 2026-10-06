package br.edu.unirv.garagem;
import br.edu.unirv.garagem.model.*;
import br.edu.unirv.garagem.repository.*;
import br.edu.unirv.garagem.service.ReservaService;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
class ReservaServiceTest {
    IReservaRepository reservas; IVeiculoRepository veiculos; IPessoaRepository pessoas; ReservaService service;
    @BeforeEach void preparar() {
        reservas = mock(IReservaRepository.class); veiculos = mock(IVeiculoRepository.class); pessoas = mock(IPessoaRepository.class);
        when(veiculos.obterPorId(1)).thenReturn(Optional.of(new Veiculo()));
        when(pessoas.obterPorId(1)).thenReturn(Optional.of(new Pessoa()));
        service = new ReservaService(reservas, veiculos, pessoas);
    }
    Reserva reserva() {
        Reserva r = new Reserva(); r.setVeiculoId(1); r.setPessoaId(1);
        r.setDataInicio(LocalDate.of(2026,10,10)); r.setDataFim(LocalDate.of(2026,10,15)); return r;
    }
    @Test void conflitoNaoGrava() {
        Reserva r = reserva(); when(reservas.existeConflito(1,r.getDataInicio(),r.getDataFim(),0)).thenReturn(true);
        assertTrue(service.salvar(r).containsKey("dataInicio")); verify(reservas, never()).adicionar(any());
    }
    @Test void fimAnteriorEReferenciasInexistentesNaoGravam() {
        Reserva r = reserva(); r.setDataFim(r.getDataInicio().minusDays(1)); r.setPessoaId(99); r.setVeiculoId(99);
        assertEquals(Set.of("dataFim","pessoaId","veiculoId"), service.salvar(r).keySet());
        verify(reservas, never()).adicionar(any());
    }
    @Test void edicaoIgnoraProprioId() {
        Reserva r = reserva(); r.setId(7); assertTrue(service.salvar(r).isEmpty());
        verify(reservas).existeConflito(1, r.getDataInicio(), r.getDataFim(), 7); verify(reservas).atualizar(r);
    }
    @Test void statusUsaHoje() {
        Veiculo v = new Veiculo(); v.setId(1); when(veiculos.obterTodas()).thenReturn(List.of(v));
        when(reservas.existeConflito(1, LocalDate.now(), LocalDate.now(), 0)).thenReturn(true);
        assertEquals("Reservado", service.statusHoje().get(1));
    }
}

