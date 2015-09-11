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
import br.ufc.cin.service.EquipeService;
import br.ufc.cin.service.JogoService;
import br.ufc.cin.service.UsuarioService;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private JogoService jogoService;

	@Inject
	private EquipeService equipeService;

	@RequestMapping(value = "/cadastre-se", method = RequestMethod.POST)
	public String cadastrarPessoa(HttpSession session, Model model,
			@Valid @ModelAttribute("usuario") Usuario usuario,
			BindingResult result, RedirectAttributes redirect) {
		
		if (result.hasErrors()) {
			model.addAttribute("cadastro", true);
			return "login";
		}
		if(usuarioService.getUsuarioByEmail(usuario.getEmail()) != null){
			redirect.addFlashAttribute("erro",
					"Erro, já existe um usuário cadastrado com esse email, por favor escolha outro.");
			return REDIRECT_PAGINA_LOGIN;
		}
			
		usuario.setHabilitado(true);
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
			return "redirect:/jogo/" + idJogo + "/participantes";
		}
		Usuario logado = getUsuarioLogado(session);
		model.addAttribute("usuario", logado);
		model.addAttribute("jogo", jogo);
		model.addAttribute("action", "detalhesUsuario");
		model.addAttribute("usuarioParticipante", usuario);
		model.addAttribute("equipe", equipeService.equipePorAlunoNoJogo(usuario, jogo));
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
		Usuario perfilAnterior = usuarioService.find(Usuario.class,usuario.getId());
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar seus dados.");
			return "redirect:/usuario/perfil";
		}
		if(!(usuario.getSenha().isEmpty())){
			ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
			perfilAnterior.setSenha(encoder.encodePassword(usuario.getSenha(), ""));
		}
		perfilAnterior.setNome(usuario.getNome());
		perfilAnterior.setSobreNome(usuario.getSobreNome());
		perfilAnterior.setMatricula(usuario.getMatricula());
		perfilAnterior.setCurso(usuario.getCurso());
		perfilAnterior.setEmail(usuario.getEmail());
		usuarioService.update(perfilAnterior);
		return "redirect:/usuario/perfil";
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
