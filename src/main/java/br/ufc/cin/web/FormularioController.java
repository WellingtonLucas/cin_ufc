package br.ufc.cin.web;

import static br.ufc.cin.util.Constants.MENSAGEM_JOGO_INEXISTENTE;
import static br.ufc.cin.util.Constants.REDIRECT_PAGINA_LISTAR_JOGO;
import static br.ufc.cin.util.Constants.USUARIO_LOGADO;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.cin.model.Formulario;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Opcao;
import br.ufc.cin.model.Pergunta;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.service.FormularioService;
import br.ufc.cin.service.JogoService;
import br.ufc.cin.service.UsuarioService;

@Controller
public class FormularioController {

	@Inject
	private JogoService jogoService;

	@Inject
	private FormularioService formularioService;

	@Inject
	private UsuarioService usuarioService;

	@RequestMapping(value = "/jogo/{id}/formulario", method = RequestMethod.GET)
	public String novoFormulario(Model model, HttpSession session, @PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class,id);
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Formulario formulario = new Formulario();
		Pergunta pergunta = new Pergunta();
		Opcao opcao = new Opcao();
		pergunta.addOpcao(opcao);
		formulario.addPergunta(pergunta);
		model.addAttribute("idJogo", id);
		model.addAttribute("formulario", formulario);
		model.addAttribute("action","cadastrar");
		
		return "jogo/formulario";

	}
	@RequestMapping(value = "/{id}/formulario/salvar", method = RequestMethod.POST)
	public String cadastroFormulario(@PathVariable("id") Integer idJogo,
			@ModelAttribute("formulario") Formulario formulario, Model model,
			HttpSession session, RedirectAttributes redirect, BindingResult result) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		if (jogo == null) {
			redirect.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		
		Usuario usuario = getUsuarioLogado(session);
		
		if (result.hasErrors()) {
			model.addAttribute("erro", "Erro ao cadastrar um formulário.");
			return "jogo/formulario";
		}
		formulario.setProfessor(usuarioService.find(Usuario.class, usuario.getId()));
		formulario.setJogo(jogo);
		formularioService.save(formulario);
		
		redirect.addFlashAttribute("info", "Formulário cadastrado com sucesso!");
		return  "redirect:/jogo/"+jogo.getId()+"/detalhes";
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
