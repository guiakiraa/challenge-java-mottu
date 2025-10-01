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

import br.com.fiap.universidade_fiap.repository.FilialRepository;
import br.com.fiap.universidade_fiap.repository.MotoRepository;
import br.com.fiap.universidade_fiap.repository.FuncionarioRepository;
import br.com.fiap.universidade_fiap.repository.EnderecoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping({"", "/filiais"})
public class FilialController {

    private final FilialRepository filialRepository;
    private final MotoRepository motoRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final EnderecoRepository enderecoRepository;

    public FilialController(FilialRepository filialRepository, MotoRepository motoRepository, FuncionarioRepository funcionarioRepository, EnderecoRepository enderecoRepository) {
        this.filialRepository = filialRepository;
        this.motoRepository = motoRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.enderecoRepository = enderecoRepository;
    }

    @GetMapping("/filiais")
    public String listar(Model model) {
        model.addAttribute("filiais", filialRepository.findAll());
        return "filial/lista";
    }

    public static class LinhaRelatorioFilial {
        private Long filialId;
        private String filialNome;
        private long totalMotos;
        private long totalFuncionarios;

        public LinhaRelatorioFilial(Long filialId, String filialNome, long totalMotos, long totalFuncionarios) {
            this.filialId = filialId;
            this.filialNome = filialNome;
            this.totalMotos = totalMotos;
            this.totalFuncionarios = totalFuncionarios;
        }

        public Long getFilialId() { return filialId; }
        public String getFilialNome() { return filialNome; }
        public long getTotalMotos() { return totalMotos; }
        public long getTotalFuncionarios() { return totalFuncionarios; }
    }

    @GetMapping("/filiais/relatorio")
    public String relatorio(Model model) {
        List<LinhaRelatorioFilial> linhas = filialRepository.findAll().stream().map(f ->
            new LinhaRelatorioFilial(
                f.getId(),
                f.getNome(),
                motoRepository.countByFilial_Id(f.getId()),
                funcionarioRepository.countByFilial_Id(f.getId())
            )
        ).collect(Collectors.toList());

        long totalMotos = linhas.stream().mapToLong(LinhaRelatorioFilial::getTotalMotos).sum();
        long totalFuncionarios = linhas.stream().mapToLong(LinhaRelatorioFilial::getTotalFuncionarios).sum();

        model.addAttribute("linhas", linhas);
        model.addAttribute("totalMotos", totalMotos);
        model.addAttribute("totalFuncionarios", totalFuncionarios);
        return "relatorio/filiais";
    }

    @GetMapping({"/filial/novo", "/filiais/filial/novo"})
    public String novo(Model model) {
        model.addAttribute("filial", new br.com.fiap.universidade_fiap.model.Filial());
        model.addAttribute("enderecos", enderecoRepository.findAll());
        return "filial/form";
    }

    @PostMapping({"/filial/salvar", "/filiais/filial/salvar"})
    public String salvar(@Valid br.com.fiap.universidade_fiap.model.Filial filial, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("enderecos", enderecoRepository.findAll());
            return "filial/form";
        }
        filialRepository.save(filial);
        return "redirect:/filiais";
    }

    @GetMapping({"/filial/editar/{id}", "/filiais/filial/editar/{id}"})
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("filial", filialRepository.findById(id).orElseThrow());
            model.addAttribute("enderecos", enderecoRepository.findAll());
            return "filial/form";
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("erro", "Registro de filial não encontrado.");
            return "redirect:/filiais";
        }
    }

    @PostMapping({"/filial/atualizar/{id}", "/filiais/filial/atualizar/{id}"})
    public String atualizar(@PathVariable Long id, @Valid br.com.fiap.universidade_fiap.model.Filial filial, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("enderecos", enderecoRepository.findAll());
            return "filial/form";
        }
        filial.setId(id);
        filialRepository.save(filial);
        return "redirect:/filiais";
    }

    @GetMapping({"/filial/remover/{id}", "/filiais/filial/remover/{id}"})
    public String remover(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            filialRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("erro", "Não é possível remover a filial: ela está vinculada a outros registros.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("erro", "Falha ao remover a filial. Tente novamente mais tarde.");
        }
        return "redirect:/filiais";
    }

    @GetMapping({"/filial/detalhes/{id}", "/filiais/filial/detalhes/{id}"})
    public String detalhes(@PathVariable Long id, Model model) {
        model.addAttribute("filial", filialRepository.findById(id).orElseThrow());
        return "filial/detalhes";
    }
}


