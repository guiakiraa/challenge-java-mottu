package br.com.fiap.universidade_fiap.control;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.fiap.universidade_fiap.model.Funcao;
import br.com.fiap.universidade_fiap.model.Usuario;
import br.com.fiap.universidade_fiap.repository.FuncaoRepository;
import br.com.fiap.universidade_fiap.repository.UsuarioRepository;

@Controller
public class UsuarioController {
	
	@Autowired
	private FuncaoRepository repF;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private UsuarioRepository repU;
	
	@GetMapping("/usuario/novo")
	public ModelAndView retornarCadUsuario() {
		
		ModelAndView mv = new ModelAndView("/usuario/novo");
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		Optional<Usuario> op = repU.findByUsername(auth.getName());
		
		if(op.isPresent()) {
			mv.addObject("usuario_logado", op.get());
		}
		
		mv.addObject("usuario", new Usuario());
		mv.addObject("lista_funcoes", repF.findAll());
		
		return mv;
		
	}
	
	@PostMapping("/insere_usuario")
	public ModelAndView inserirUsuario(Usuario usuario, @RequestParam(name = "id_funcao") Long id_funcao) {

		usuario.setSenha(encoder.encode(usuario.getSenha()));

		Set<Funcao> funcoes = new HashSet<Funcao>();

		if (id_funcao != null) {

			funcoes.add(repF.findById(id_funcao).orElse(null));

		}

		usuario.setFuncoes(funcoes);

		repU.save(usuario);

		return new ModelAndView("redirect:/index");

	}
	
	
	

}
