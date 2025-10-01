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

import br.com.fiap.universidade_fiap.repository.FuncionarioRepository;
import br.com.fiap.universidade_fiap.repository.FilialRepository;
import br.com.fiap.universidade_fiap.model.Funcionario;

@Controller
@RequestMapping({"", "/funcionarios"})
public class FuncionarioController {

    private final FuncionarioRepository funcionarioRepository;
    private final FilialRepository filialRepository;

    public FuncionarioController(FuncionarioRepository funcionarioRepository, FilialRepository filialRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.filialRepository = filialRepository;
    }

    @GetMapping("/funcionarios")
    public String listar(Model model) {
        model.addAttribute("funcionarios", funcionarioRepository.findAll());
        return "funcionario/lista";
    }

    @GetMapping("/funcionario/novo")
    public String novo(Model model) {
        model.addAttribute("funcionario", new Funcionario());
        model.addAttribute("filiais", filialRepository.findAll());
        return "funcionario/form";
    }

    @PostMapping("/funcionario/salvar")
    public String salvar(@Valid Funcionario funcionario, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("filiais", filialRepository.findAll());
            return "funcionario/form";
        }
        funcionarioRepository.save(funcionario);
        return "redirect:/funcionarios";
    }

    @GetMapping("/funcionario/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("funcionario", funcionarioRepository.findById(id).orElseThrow());
            model.addAttribute("filiais", filialRepository.findAll());
            return "funcionario/form";
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("erro", "Registro de funcionário não encontrado.");
            return "redirect:/funcionarios";
        }
    }

    @PostMapping("/funcionario/atualizar/{id}")
    public String atualizar(@PathVariable Long id, @Valid Funcionario funcionario, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("filiais", filialRepository.findAll());
            return "funcionario/form";
        }
        funcionario.setId(id);
        funcionarioRepository.save(funcionario);
        return "redirect:/funcionarios";
    }

    @GetMapping("/funcionario/remover/{id}")
    public String remover(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            funcionarioRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("erro", "Não é possível remover o funcionário: ele está vinculado a outros registros.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("erro", "Falha ao remover o funcionário. Tente novamente mais tarde.");
        }
        return "redirect:/funcionarios";
    }

    @GetMapping("/funcionario/detalhes/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        model.addAttribute("funcionario", funcionarioRepository.findById(id).orElseThrow());
        return "funcionario/detalhes";
    }
}


