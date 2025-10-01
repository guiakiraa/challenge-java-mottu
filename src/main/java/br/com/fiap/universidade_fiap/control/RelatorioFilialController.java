package br.com.fiap.universidade_fiap.control;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.fiap.universidade_fiap.repository.FilialRepository;
import br.com.fiap.universidade_fiap.repository.FuncionarioRepository;
import br.com.fiap.universidade_fiap.repository.MotoRepository;

@Controller
@RequestMapping("/relatorios/filiais")
public class RelatorioFilialController {

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

    private final FilialRepository filialRepository;
    private final MotoRepository motoRepository;
    private final FuncionarioRepository funcionarioRepository;

    public RelatorioFilialController(
        FilialRepository filialRepository,
        MotoRepository motoRepository,
        FuncionarioRepository funcionarioRepository
    ) {
        this.filialRepository = filialRepository;
        this.motoRepository = motoRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    @GetMapping
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
}


