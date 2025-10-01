package br.com.fiap.universidade_fiap.control;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.fiap.universidade_fiap.model.Moto;
import br.com.fiap.universidade_fiap.repository.FilialRepository;
import br.com.fiap.universidade_fiap.repository.MotoRepository;

@Controller
@RequestMapping("/motos/busca")
public class MotoBuscaController {

    private final MotoRepository motoRepository;
    private final FilialRepository filialRepository;

    public MotoBuscaController(MotoRepository motoRepository, FilialRepository filialRepository) {
        this.motoRepository = motoRepository;
        this.filialRepository = filialRepository;
    }

    @GetMapping
    public String buscar(
        @RequestParam(required = false) String placa,
        @RequestParam(required = false) String modelo,
        @RequestParam(required = false) Integer ano,
        @RequestParam(required = false) String tipoCombustivel,
        @RequestParam(required = false) Long filialId,
        Model model
    ) {
        // Normalizar strings vazias para null para ativar a lógica opcional da query
        String placaFiltro = (placa != null && placa.isBlank()) ? null : placa;
        String modeloFiltro = (modelo != null && modelo.isBlank()) ? null : modelo;
        String combustivelFiltro = (tipoCombustivel != null && tipoCombustivel.isBlank()) ? null : tipoCombustivel;

        List<Moto> resultado = motoRepository.buscarAvancado(
            placaFiltro,
            modeloFiltro,
            ano,
            combustivelFiltro,
            filialId
        );
        model.addAttribute("resultado", resultado);
        model.addAttribute("filiais", filialRepository.findAll());
        model.addAttribute("placa", placa);
        model.addAttribute("modelo", modelo);
        model.addAttribute("ano", ano);
        model.addAttribute("tipoCombustivel", tipoCombustivel);
        model.addAttribute("filialId", filialId);
        return "moto/busca";
    }
}


