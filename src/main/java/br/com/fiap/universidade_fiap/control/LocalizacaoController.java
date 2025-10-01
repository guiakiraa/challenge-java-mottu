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

import br.com.fiap.universidade_fiap.repository.LocalizacaoRepository;
import br.com.fiap.universidade_fiap.repository.MotoRepository;
import br.com.fiap.universidade_fiap.model.Localizacao;

@Controller
@RequestMapping({"", "/localizacoes"})
public class LocalizacaoController {

    private final LocalizacaoRepository localizacaoRepository;
    private final MotoRepository motoRepository;

    public LocalizacaoController(LocalizacaoRepository localizacaoRepository, MotoRepository motoRepository) {
        this.localizacaoRepository = localizacaoRepository;
        this.motoRepository = motoRepository;
    }

    @GetMapping("/localizacoes")
    public String listar(Model model) {
        model.addAttribute("localizacoes", localizacaoRepository.findAll());
        return "localizacao/lista";
    }

    @GetMapping("/localizacao/novo")
    public String novo(Model model) {
        model.addAttribute("localizacao", new Localizacao());
        model.addAttribute("motos", motoRepository.findAll());
        return "localizacao/form";
    }

    @PostMapping("/localizacao/salvar")
    public String salvar(@Valid Localizacao localizacao, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("motos", motoRepository.findAll());
            return "localizacao/form";
        }
        localizacaoRepository.save(localizacao);
        return "redirect:/localizacoes";
    }

    @GetMapping("/localizacao/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("localizacao", localizacaoRepository.findById(id).orElseThrow());
            model.addAttribute("motos", motoRepository.findAll());
            return "localizacao/form";
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("erro", "Registro de localização não encontrado.");
            return "redirect:/localizacoes";
        }
    }

    @PostMapping("/localizacao/atualizar/{id}")
    public String atualizar(@PathVariable Long id, @Valid Localizacao localizacao, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("motos", motoRepository.findAll());
            return "localizacao/form";
        }
        localizacao.setId(id);
        localizacaoRepository.save(localizacao);
        return "redirect:/localizacoes";
    }

    @GetMapping("/localizacao/remover/{id}")
    public String remover(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            localizacaoRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("erro", "Não é possível remover a localização: ela está vinculada a outros registros.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("erro", "Falha ao remover a localização. Tente novamente mais tarde.");
        }
        return "redirect:/localizacoes";
    }

    @GetMapping("/localizacao/detalhes/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        model.addAttribute("localizacao", localizacaoRepository.findById(id).orElseThrow());
        return "localizacao/detalhes";
    }
}


