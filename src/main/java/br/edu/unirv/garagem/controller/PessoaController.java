package br.edu.unirv.garagem.controller;

import br.edu.unirv.garagem.model.Pessoa;
import br.edu.unirv.garagem.repository.IPessoaRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pessoas")
public class PessoaController {

    private final IPessoaRepository pessoaRepository;

    public PessoaController(IPessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pessoas", pessoaRepository.obterTodas());
        return "pessoa/index";
    }

    @GetMapping("/novo")
    public String formNovo(Model model) {
        model.addAttribute("pessoa", new Pessoa());
        model.addAttribute("acao", "novo");
        return "pessoa/PessoaForm";
    }

    @PostMapping("/novo")
    public String salvar(@Valid @ModelAttribute Pessoa pessoa,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (pessoaRepository.cpfJaExiste(pessoa.getCpf(), 0)) {
            result.rejectValue("cpf", "cpf.duplicado", "CPF já cadastrado");
        }
        if (result.hasErrors()) {
            model.addAttribute("acao", "novo");
            return "pessoa/PessoaForm";
        }
        pessoaRepository.adicionar(pessoa);
        redirectAttributes.addFlashAttribute("sucesso", "Pessoa cadastrada com sucesso!");
        return "redirect:/pessoas";
    }

    @GetMapping("/{id}/editar")
    public String formEditar(@PathVariable int id, Model model) {
        Pessoa pessoa = pessoaRepository.obterPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada: " + id));
        model.addAttribute("pessoa", pessoa);
        model.addAttribute("acao", "editar");
        return "pessoa/PessoaForm";
    }

    @PostMapping("/{id}/editar")
    public String atualizar(@PathVariable int id,
                            @Valid @ModelAttribute Pessoa pessoa,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        pessoa.setId(id);
        if (pessoaRepository.cpfJaExiste(pessoa.getCpf(), id)) {
            result.rejectValue("cpf", "cpf.duplicado", "CPF já cadastrado por outra pessoa");
        }
        if (result.hasErrors()) {
            model.addAttribute("acao", "editar");
            return "pessoa/PessoaForm";
        }
        pessoaRepository.atualizar(pessoa);
        redirectAttributes.addFlashAttribute("sucesso", "Pessoa atualizada com sucesso!");
        return "redirect:/pessoas";
    }

    @GetMapping("/{id}/excluir")
    public String confirmarExclusao(@PathVariable int id, Model model) {
        Pessoa pessoa = pessoaRepository.obterPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada: " + id));
        model.addAttribute("pessoa", pessoa);
        return "pessoa/confirmar-exclusao";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable int id, RedirectAttributes redirectAttributes) {
        pessoaRepository.remover(id);
        redirectAttributes.addFlashAttribute("sucesso", "Pessoa removida com sucesso!");
        return "redirect:/pessoas";
    }
}
