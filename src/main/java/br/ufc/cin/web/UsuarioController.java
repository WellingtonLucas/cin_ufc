package br.ufc.cin.web;

import static br.ufc.cin.util.Constants.REDIRECT_PAGINA_LOGIN;
import static br.ufc.cin.util.Constants.USUARIO_LOGADO;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.service.JogoService;
import br.ufc.cin.service.UsuarioService;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private JogoService jogoService;

	@RequestMapping(value = "/cadastre-se", method = RequestMethod.POST)
	public String cadastrarPessoa(HttpSession session, Model model,
			@Valid @ModelAttribute("usuario") Usuario usuario,
			BindingResult result, RedirectAttributes redirect) {
		usuario.setHabilitado(true);
		if (result.hasErrors()) {
			model.addAttribute("cadastro", true);
			return "login";
		}

		ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
		usuario.setSenha(encoder.encodePassword(usuario.getSenha(), ""));
		usuario.setPapel("ROLE_USUARIO");
		usuarioService.save(usuario);

		redirect.addFlashAttribute("info",
				"Parabéns, seu cadastro esta realizado.");
		return REDIRECT_PAGINA_LOGIN;
	}

	@RequestMapping(value = "/{id}/detalhes/{idJogo}", method = RequestMethod.GET)
	public String verDetalhes(@PathVariable("id") Integer id,
			@PathVariable("idJogo") Integer idJogo, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Usuario usuario = usuarioService.find(Usuario.class, id);
		if (usuario == null) {
			redirectAttributes.addFlashAttribute("erro", "Usuário inexistente");
			return "redirect:/usuario/" + id + "/detalhes";
		}
		model.addAttribute("jogo", jogo);
		model.addAttribute("action", "detalhesUsuario");
		model.addAttribute("equipe", usuario.getEquipe());
		model.addAttribute("usuario", usuario);
		return "jogador/usuario";
	}

	@RequestMapping(value = "/perfil", method = RequestMethod.GET)
	public String perfil(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		Usuario usuario = getUsuarioLogado(session);
		model.addAttribute("usuario", usuario);
		return "jogador/perfil";
	}
	
	@RequestMapping(value = "/atualizar", method = RequestMethod.POST)
	public String atualizar(Model model, HttpSession session, RedirectAttributes redirectAttributes, 
			@Valid Usuario usuario, BindingResult result) {
		
		if (result.hasErrors()) {
			model.addAttribute("usuario", getUsuarioLogado(session));
			model.addAttribute("erro", "Erro ao atualizar seus dados.");
			return "jogador/perfil";
		}
		ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
		usuario.setSenha(encoder.encodePassword(usuario.getSenha(), ""));
		usuarioService.update(usuario);
		model.addAttribute("usuario", usuarioService.find(Usuario.class,usuario.getId()));
		return "jogador/perfil";
	}
	
	private Usuario getUsuarioLogado(HttpSession session) {
		if (session.getAttribute(USUARIO_LOGADO) == null) {
			Usuario usuario = usuarioService
					.getUsuarioByEmail(SecurityContextHolder.getContext()
							.getAuthentication().getName());
			session.setAttribute(USUARIO_LOGADO, usuario);
		}
		return (Usuario) session.getAttribute(USUARIO_LOGADO);
	}
}
