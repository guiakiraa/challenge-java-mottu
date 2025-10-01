package br.com.fiap.universidade_fiap.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.fiap.universidade_fiap.repository.MotoRepository;
import br.com.fiap.universidade_fiap.repository.FilialRepository;

import java.util.List;
import br.com.fiap.universidade_fiap.model.Moto;

@Controller
@RequestMapping({"", "/motos"})
public class MotoController {

    private final MotoRepository motoRepository;
    private final FilialRepository filialRepository;

    public MotoController(MotoRepository motoRepository, FilialRepository filialRepository) {
        this.motoRepository = motoRepository;
        this.filialRepository = filialRepository;
    }

    @GetMapping("/motos")
    public String listar(Model model) {
        model.addAttribute("motos", motoRepository.findAll());
        return "moto/lista";
    }

    @GetMapping("/motos/busca")
    public String buscar(
        @RequestParam(required = false) String placa,
        @RequestParam(required = false) String modelo,
        @RequestParam(required = false) Integer ano,
        @RequestParam(required = false) String tipoCombustivel,
        @RequestParam(required = false) Long filialId,
        Model model
    ) {
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

    @GetMapping("/moto/novo")
    public String novo(Model model) {
        model.addAttribute("moto", new Moto());
        model.addAttribute("filiais", filialRepository.findAll());
        return "moto/form";
    }

    @PostMapping("/moto/salvar")
    public String salvar(@Valid Moto moto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("filiais", filialRepository.findAll());
            return "moto/form";
        }
        motoRepository.save(moto);
        return "redirect:/motos";
    }

    @GetMapping("/moto/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("moto", motoRepository.findById(id).orElseThrow());
            model.addAttribute("filiais", filialRepository.findAll());
            return "moto/form";
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("erro", "Registro de moto não encontrado.");
            return "redirect:/motos";
        }
    }

    @PostMapping("/moto/atualizar/{id}")
    public String atualizar(@PathVariable Long id, @Valid Moto moto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("filiais", filialRepository.findAll());
            return "moto/form";
        }
        moto.setId(id);
        motoRepository.save(moto);
        return "redirect:/motos";
    }

    @GetMapping("/moto/remover/{id}")
    public String remover(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            motoRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("erro", "Não é possível remover a moto: ela está vinculada a outros registros.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("erro", "Falha ao remover a moto. Tente novamente mais tarde.");
        }
        return "redirect:/motos";
    }

    @GetMapping("/moto/detalhes/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        model.addAttribute("moto", motoRepository.findById(id).orElseThrow());
        return "moto/detalhes";
    }
}


