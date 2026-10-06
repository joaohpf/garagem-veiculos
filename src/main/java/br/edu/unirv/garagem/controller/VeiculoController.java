package br.edu.unirv.garagem.controller;
import br.edu.unirv.garagem.model.Veiculo;
import br.edu.unirv.garagem.repository.IVeiculoRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
@RequestMapping("/veiculos")
public class VeiculoController {
    private final IVeiculoRepository repository;
    public VeiculoController(IVeiculoRepository repository) { this.repository = repository; }
    private Veiculo buscar(int id) { return repository.obterPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado")); }
    @GetMapping public String listar(Model model) { model.addAttribute("veiculos", repository.obterTodas()); return "veiculo/index"; }
    @GetMapping("/novo") public String novo(Model model) { model.addAttribute("veiculo", new Veiculo()); model.addAttribute("acao", "novo"); return "veiculo/VeiculoForm"; }
    @GetMapping("/{id}/editar") public String editar(@PathVariable int id, Model model) {
        model.addAttribute("veiculo", buscar(id)); model.addAttribute("acao", "editar"); return "veiculo/VeiculoForm";
    }
    @PostMapping("/novo") public String criar(@Valid @ModelAttribute Veiculo veiculo, BindingResult result, Model model, RedirectAttributes flash) {
        veiculo.setId(0); return salvar(veiculo, result, model, flash, "novo");
    }
    @PostMapping("/{id}/editar") public String atualizar(@PathVariable int id, @Valid @ModelAttribute Veiculo veiculo, BindingResult result, Model model, RedirectAttributes flash) {
        buscar(id); veiculo.setId(id); return salvar(veiculo, result, model, flash, "editar");
    }
    private String salvar(Veiculo veiculo, BindingResult result, Model model, RedirectAttributes flash, String acao) {
        if (repository.placaJaExiste(veiculo.getPlaca(), veiculo.getId())) result.rejectValue("placa", "duplicada", "Placa já cadastrada");
        if (result.hasErrors()) { model.addAttribute("acao", acao); return "veiculo/VeiculoForm"; }
        if (veiculo.getId() == 0) repository.adicionar(veiculo); else repository.atualizar(veiculo);
        flash.addFlashAttribute("sucesso", "Veículo salvo com sucesso!"); return "redirect:/veiculos";
    }
    @PostMapping("/{id}/excluir") public String excluir(@PathVariable int id, RedirectAttributes flash) {
        buscar(id); repository.remover(id); flash.addFlashAttribute("sucesso", "Veículo excluído!"); return "redirect:/veiculos";
    }
}

