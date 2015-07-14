package br.ufc.cin.web;

import static br.ufc.cin.util.Constants.REDIRECT_PAGINA_LOGIN;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.cin.model.Usuario;
import br.ufc.cin.service.UsuarioService;

@Controller
@RequestMapping("usuario")
public class UsuarioController {
	
	@Inject
	private UsuarioService usuarioService;
	
	@RequestMapping(value = "/cadastre-se", method = RequestMethod.POST)
	public String cadastrarPessoa(HttpSession session, Model model, @Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result, RedirectAttributes redirect) {
		usuario.setHabilitado(true);
		if (result.hasErrors()) {
			model.addAttribute("cadastro", true);
			return "login";
		}

		ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
		
		
		usuario.setSenha(encoder.encodePassword(usuario.getSenha(), ""));
		usuario.setPapel("ROLE_USUARIO");
		usuarioService.save(usuario);
		
		redirect.addFlashAttribute("info", "Parabéns, seu cadastro esta realizado.");
		return REDIRECT_PAGINA_LOGIN;
	}
	
}
