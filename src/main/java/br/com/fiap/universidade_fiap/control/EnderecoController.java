package br.com.fiap.universidade_fiap.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindingResult;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

import br.com.fiap.universidade_fiap.repository.EnderecoRepository;
import br.com.fiap.universidade_fiap.model.Endereco;

@Controller
@RequestMapping({"", "/enderecos"})
public class EnderecoController {

    private final EnderecoRepository enderecoRepository;

    public EnderecoController(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    @GetMapping("/enderecos")
    public String listar(Model model) {
        model.addAttribute("enderecos", enderecoRepository.findAll());
        return "endereco/lista";
    }

    @GetMapping("/endereco/novo")
    public String novo(Model model) {
        model.addAttribute("endereco", new Endereco());
        return "endereco/form";
    }

    @PostMapping("/endereco/salvar")
    public String salvar(@Valid Endereco endereco, BindingResult result) {
        if (result.hasErrors()) {
            return "endereco/form";
        }
        enderecoRepository.save(endereco);
        return "redirect:/enderecos";
    }

    @GetMapping("/endereco/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("endereco", enderecoRepository.findById(id).orElseThrow());
        return "endereco/form";
    }

    @PostMapping("/endereco/atualizar/{id}")
    public String atualizar(@PathVariable Long id, @Valid Endereco endereco, BindingResult result) {
        if (result.hasErrors()) {
            return "endereco/form";
        }
        endereco.setId(id);
        enderecoRepository.save(endereco);
        return "redirect:/enderecos";
    }

    @GetMapping("/endereco/remover/{id}")
    public String remover(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            enderecoRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("erro", "Não é possível remover o endereço: ele está vinculado a outros registros.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("erro", "Falha ao remover o endereço. Tente novamente mais tarde.");
        }
        return "redirect:/enderecos";
    }

    @GetMapping("/endereco/detalhes/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        model.addAttribute("endereco", enderecoRepository.findById(id).orElseThrow());
        return "endereco/detalhes";
    }
}


