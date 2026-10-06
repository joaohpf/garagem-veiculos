package br.edu.unirv.garagem;
import br.edu.unirv.garagem.controller.*;
import br.edu.unirv.garagem.model.*;
import br.edu.unirv.garagem.repository.*;
import br.edu.unirv.garagem.service.ReservaService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest({ReservaController.class, VeiculoController.class})
class PaginasTest {
    @Autowired MockMvc mvc;
    @MockBean IReservaRepository reservas;
    @MockBean IVeiculoRepository veiculos;
    @MockBean IPessoaRepository pessoas;
    @MockBean ReservaService service;
    @BeforeEach void preparar() {
        when(reservas.obterTodas()).thenReturn(List.of()); when(veiculos.obterTodas()).thenReturn(List.of());
        when(pessoas.obterTodas()).thenReturn(List.of()); when(service.statusHoje()).thenReturn(Map.of());
    }
    @Test void paginasRenderizam() throws Exception {
        mvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("reserva/index"));
        mvc.perform(get("/veiculos")).andExpect(status().isOk());
        mvc.perform(get("/veiculos/novo")).andExpect(status().isOk());
    }
    @Test void conflitoVoltaAoFormulario() throws Exception {
        when(service.salvar(any())).thenReturn(Map.of("dataInicio","Este veículo já possui reserva nesse período"));
        mvc.perform(post("/reservas/novo").param("veiculoId","1").param("pessoaId","1").param("dataInicio","2026-10-10").param("dataFim","2026-10-15"))
            .andExpect(status().isOk()).andExpect(model().attributeHasFieldErrors("reserva","dataInicio"));
    }
    @Test void dataMalformadaNaoChamaServico() throws Exception {
        mvc.perform(post("/reservas/novo").param("veiculoId","1").param("pessoaId","1").param("dataInicio","invalida").param("dataFim","2026-10-15"))
            .andExpect(status().isOk()).andExpect(model().attributeHasFieldErrors("reserva","dataInicio"));
        verify(service, never()).salvar(any());
    }
    @Test void placaDuplicadaNaoGrava() throws Exception {
        when(veiculos.placaJaExiste("ABC1234",0)).thenReturn(true);
        mvc.perform(post("/veiculos/novo").param("placa","ABC1234").param("marca","Fiat").param("modelo","Argo").param("ano","2022"))
            .andExpect(status().isOk()).andExpect(model().attributeHasFieldErrors("veiculo","placa"));
        verify(veiculos, never()).adicionar(any());
    }
}

