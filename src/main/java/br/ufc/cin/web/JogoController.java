package br.ufc.cin.web;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Professor;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.service.JogoService;
import br.ufc.cin.service.ProfessorService;
import br.ufc.cin.service.UsuarioService;
import br.ufc.cin.util.Constants;

@Controller
@RequestMapping("jogo")
public class JogoController {

	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private JogoService jogoService;
	
	@Inject
	private ProfessorService professorService;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return "redirect:/jogo/listar";
	}

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(Model model, HttpSession session) {
		Usuario usuario = getUsuarioLogado(session);
		Integer idUsuarioLogado = usuario.getId();
		model.addAttribute("jogos", jogoService.getJogoByProfessor(idUsuarioLogado));
		model.addAttribute("usuario", usuario);
		if (usuarioService.isDiretor(usuario)) {
			return Constants.PAGINA_LISTAR_JOGO;
		}
		return Constants.PAGINA_LISTAR_JOGO;

	}
	
	@RequestMapping(value ="/novo-jogo", method = RequestMethod.GET)
	public String cadastrarFrom(Model model){
		model.addAttribute("jogo", new Jogo());		
		return Constants.PAGINA_CADASTRAR_JOGO;
	}
	
	@RequestMapping(value = "/novo-jogo", method = RequestMethod.POST)
	public  String cadastrar(@ModelAttribute("jogo") Jogo jogo, HttpSession session){
		Usuario usuario = getUsuarioLogado(session);
		jogo.setProfessor(professorService.find(Professor.class, usuario.getId()));
		jogoService.save(jogo);		
		return Constants.REDIRECT_PAGINA_LISTAR_JOGO;
	}
	
	private Usuario getUsuarioLogado(HttpSession session) {
		if (session.getAttribute(Constants.USUARIO_LOGADO) == null) {
			Usuario usuario = usuarioService
					.getUsuarioByLogin(SecurityContextHolder.getContext()
							.getAuthentication().getName());
			session.setAttribute(Constants.USUARIO_LOGADO, usuario);
		}
		return (Usuario) session.getAttribute(Constants.USUARIO_LOGADO);
	}
}
