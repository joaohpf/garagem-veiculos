package br.edu.unirv.garagem.controller;
import br.edu.unirv.garagem.model.*;
import br.edu.unirv.garagem.repository.*;
import br.edu.unirv.garagem.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;
import java.util.stream.Collectors;
@Controller
public class ReservaController {
    private final IReservaRepository reservas;
    private final IVeiculoRepository veiculos;
    private final IPessoaRepository pessoas;
    private final ReservaService service;
    public ReservaController(IReservaRepository reservas, IVeiculoRepository veiculos, IPessoaRepository pessoas, ReservaService service) {
        this.reservas = reservas; this.veiculos = veiculos; this.pessoas = pessoas; this.service = service;
    }
    private Reserva buscar(int id) { return reservas.obterPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva não encontrada")); }
    private void carregar(Model model) {
        List<Veiculo> listaVeiculos = veiculos.obterTodas(); List<Pessoa> listaPessoas = pessoas.obterTodas();
        model.addAttribute("veiculos", listaVeiculos); model.addAttribute("pessoas", listaPessoas);
        model.addAttribute("veiculosPorId", listaVeiculos.stream().collect(Collectors.toMap(Veiculo::getId, v -> v)));
        model.addAttribute("pessoasPorId", listaPessoas.stream().collect(Collectors.toMap(Pessoa::getId, p -> p)));
        model.addAttribute("reservas", reservas.obterTodas()); model.addAttribute("statusHoje", service.statusHoje());
    }
    @GetMapping({"/", "/reservas"}) public String index(Model model) {
        model.addAttribute("reserva", new Reserva()); carregar(model); return "reserva/index";
    }
    @GetMapping("/reservas/{id}/editar") public String editar(@PathVariable int id, Model model) {
        model.addAttribute("reserva", buscar(id)); carregar(model); return "reserva/index";
    }
    @PostMapping("/reservas/novo") public String criar(@Valid @ModelAttribute Reserva reserva, BindingResult result, Model model, RedirectAttributes flash) {
        reserva.setId(0); return salvar(reserva, result, model, flash);
    }
    @PostMapping("/reservas/{id}/editar") public String atualizar(@PathVariable int id, @Valid @ModelAttribute Reserva reserva, BindingResult result, Model model, RedirectAttributes flash) {
        Reserva existente = buscar(id);
        reserva.setId(id); reserva.setVeiculoId(existente.getVeiculoId()); reserva.setPessoaId(existente.getPessoaId());
        return salvar(reserva, result, model, flash);
    }
    private String salvar(Reserva reserva, BindingResult result, Model model, RedirectAttributes flash) {
        if (!result.hasErrors()) service.salvar(reserva).forEach((campo, mensagem) -> result.rejectValue(campo, "reserva.invalida", mensagem));
        if (result.hasErrors()) { carregar(model); return "reserva/index"; }
        flash.addFlashAttribute("sucesso", "Reserva salva com sucesso!"); return "redirect:/";
    }
    @PostMapping("/reservas/{id}/excluir") public String excluir(@PathVariable int id, RedirectAttributes flash) {
        buscar(id); reservas.remover(id); flash.addFlashAttribute("sucesso", "Reserva cancelada!"); return "redirect:/";
    }
}

