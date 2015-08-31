package br.ufc.cin.web;

import static br.ufc.cin.util.Constants.MENSAGEM_EQUIPE_INEXISTENTE;
import static br.ufc.cin.util.Constants.MENSAGEM_FORM_NAO_EXISTENTE;
import static br.ufc.cin.util.Constants.MENSAGEM_JOGO_INEXISTENTE;
import static br.ufc.cin.util.Constants.MENSAGEM_PERMISSAO_NEGADA;
import static br.ufc.cin.util.Constants.PAGINA_CADASTRAR_FORMULARIO;
import static br.ufc.cin.util.Constants.PAGINA_DETALHES_FORM;
import static br.ufc.cin.util.Constants.REDIRECT_PAGINA_LISTAR_JOGO;
import static br.ufc.cin.util.Constants.USUARIO_LOGADO;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
import br.ufc.cin.model.Resposta;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.service.FormularioService;
import br.ufc.cin.service.JogoService;
import br.ufc.cin.service.OpcaoService;
import br.ufc.cin.service.PerguntaService;
import br.ufc.cin.service.RespostaService;
import br.ufc.cin.service.UsuarioService;

@Controller
public class FormularioController {

	@Inject
	private JogoService jogoService;

	@Inject
	private FormularioService formularioService;

	@Inject
	private PerguntaService perguntaService;

	@Inject
	private OpcaoService opcaoService;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private RespostaService respostaService;
	
	@RequestMapping(value = "/jogo/{id}/formulario", method = RequestMethod.GET)
	public String novoFormulario(Model model, HttpSession session, @PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class,id);
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Formulario formulario = new Formulario();
		
		model.addAttribute("idJogo", id);
		model.addAttribute("formulario", formulario);
		model.addAttribute("action","cadastrar");
		
		return "formulario/formulario";

	}
	@RequestMapping(value = "/{idJogo}/formulario/salvar", method = RequestMethod.POST)
	public String cadastroFormulario(@PathVariable("idJogo") Integer idJogo,
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
			return "formulario/formulario";
		}
		formulario.setProfessor(usuario);
		formularioService.save(formulario);
		
		for (Pergunta pergunta : formulario.getPerguntas()) {
			pergunta.setFormulario(formulario);
		}
		
		perguntaService.salvar(formulario.getPerguntas());
		
		for (Pergunta pergunta : formulario.getPerguntas()) {
			for (Opcao opcao : pergunta.getOpcoes()) {
				opcao.setPergunta(pergunta);
			}
			opcaoService.salvar(pergunta.getOpcoes());
		}
		
		redirect.addFlashAttribute("info", "Formulário cadastrado com sucesso!");
		return  "redirect:/jogo/"+jogo.getId()+"/formulario/"+formulario.getId();
	}
	@RequestMapping(value = "/jogo/{idJogo}/formulario/{idForm}/editar", method = RequestMethod.GET)
	public String editarForm(@PathVariable("idJogo") Integer idJogo, @PathVariable("idForm") Integer idForm,
			Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Formulario formulario = formularioService.find(Formulario.class, idForm);
			if(formulario == null){
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_FORM_NAO_EXISTENTE);
			return "redirect:/formulario/listar";
		}
		Usuario usuario = getUsuarioLogado(session);
		
		if (usuario.getId() == jogo.getProfessor().getId() && formulario.getProfessor().getId() == usuario.getId()) {
			model.addAttribute("jogo", jogo);
			model.addAttribute("formulario", formulario);
		 	model.addAttribute("action", "editar");
			return PAGINA_CADASTRAR_FORMULARIO;
		}

		redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
		return REDIRECT_PAGINA_LISTAR_JOGO;
	}
	
	@RequestMapping(value = "/{idJogo}/formulario/editar", method = RequestMethod.POST)
	public String editar(@PathVariable("idJogo") Integer idJogo, @Valid Formulario formulario, 
			Model model, HttpSession session, RedirectAttributes redirect, BindingResult result) {
		
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		
		if (jogo == null) {
			redirect.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
	
		if (result.hasErrors()) {
			model.addAttribute("erro", "Erro ao editar formulário.");
			return "formulario/formulario";
		}
		formulario.setProfessor(jogo.getProfessor());
		
		for (Pergunta pergunta : formulario.getPerguntas()) {
				pergunta.setFormulario(formulario);
		}
		perguntaService.atualizar(formulario.getPerguntas());
		
		for (Pergunta pergunta : formulario.getPerguntas()) {
			for (Opcao opcao : pergunta.getOpcoes()) {
					opcao.setPergunta(pergunta);
			}
			opcaoService.atualizar(pergunta.getOpcoes());
		}
		
		formularioService.update(formulario);
		
		redirect.addFlashAttribute("info", "Formulário atualizado com sucesso!");
		return  "redirect:/jogo/"+jogo.getId()+"/formulario/"+formulario.getId();
	}

	@RequestMapping(value = "/jogo/{idJogo}/formulario/{idForm}/copiar", method = RequestMethod.GET)
	public String copiarForm(@PathVariable("idJogo") Integer idJogo, @PathVariable("idForm") Integer idForm,
			Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Formulario formulario = formularioService.find(Formulario.class, idForm);
			if(formulario == null){
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_FORM_NAO_EXISTENTE);
			return "redirect:/formulario/listar";
		}
		Usuario usuario = getUsuarioLogado(session);
		
		if (usuario.getId() == jogo.getProfessor().getId() && formulario.getProfessor().getId() == usuario.getId()) {
			model.addAttribute("jogo", jogo);
			model.addAttribute("formulario", formulario);
		 	model.addAttribute("action", "copiar");
			return PAGINA_CADASTRAR_FORMULARIO;
		}

		redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
		return REDIRECT_PAGINA_LISTAR_JOGO;
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/formulario/{idForm}/detalhes", method = RequestMethod.GET)
	public String verDetalhes(@PathVariable("idJogo") Integer idJogo, @PathVariable("idForm") Integer idForm,
			Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		
		Formulario formulario = formularioService.find(Formulario.class, idForm);
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		model.addAttribute("action", "detalhesFormulario");
		if (formulario == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EQUIPE_INEXISTENTE);
			return "redirect:/jogo/"+ idJogo +"/formularios";
		}
		if(jogo==null){
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Usuario usuario = getUsuarioLogado(session);
		if (usuario.getId() == jogo.getProfessor().getId() &&  jogo.getProfessor().getFormulario().contains(formulario)) {			
			model.addAttribute("formulario", formulario);
			model.addAttribute("jogo", jogo);
			return PAGINA_DETALHES_FORM;
		}else{			
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/formulario/{idForm}", method = RequestMethod.GET)
	public String modoResponder(@PathVariable("idJogo") Integer idJogo, @PathVariable("idForm") Integer idForm,
			Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		
		Formulario formulario = formularioService.find(Formulario.class, idForm);
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		model.addAttribute("action", "responder");
		if (formulario == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EQUIPE_INEXISTENTE);
			return "redirect:/jogo/"+ idJogo +"/formularios";
		}
		if(jogo==null){
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Usuario usuario = getUsuarioLogado(session);
		if (usuario.getId() == jogo.getProfessor().getId() &&  jogo.getProfessor().getFormulario().contains(formulario)) {			
			model.addAttribute("formulario", formulario);
			model.addAttribute("jogo", jogo);
			model.addAttribute("resposta", new Resposta());
			return "formulario/responder";
		}else{			
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
	}
	
	@RequestMapping(value = "/{idJogo}/formulario/{idForm}/responder", method = RequestMethod.POST)
	public String responder(@PathVariable("idJogo") Integer idJogo, @PathVariable("idForm") Integer idForm,	Model model, 
			HttpSession session, @ModelAttribute("resposta") Resposta resposta, RedirectAttributes redirectAttributes,
			BindingResult result) {
		
		Formulario formulario = formularioService.find(Formulario.class, idForm);
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		model.addAttribute("action", "responder");
		model.addAttribute("formulario", formulario);
		model.addAttribute("jogo", jogo);
		
		if (result.hasErrors()) {
			model.addAttribute("erro", "Erro ao cadastrar um formulário.");
			return "formulario/detalhes";
		}
		
		if (formulario == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EQUIPE_INEXISTENTE);
			return "redirect:/jogo/"+ idJogo +"/formularios";
		}
		if(jogo==null){
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		List<Opcao> opcoes = new ArrayList<Opcao>();
		for (Opcao opcao : resposta.getOpcoes()) {
			opcoes.add(opcaoService.find(Opcao.class, opcao.getId()));
		}
		resposta.setOpcoes(opcoes);
		Usuario usuario = getUsuarioLogado(session);
		resposta.setFormulario(formulario);
		resposta.setUsuario(usuario);
		respostaService.save(resposta);	
		redirectAttributes.addFlashAttribute("info", "Formulário respondido com sucesso!");
		return  "redirect:/jogo/"+jogo.getId()+"/formulario/"+formulario.getId()+"/detalhes";
	}
	
	
	@RequestMapping(value = "/jogo/{idJogo}/formulario/{id}/excluir")
	public String excluir(@PathVariable("id") Integer id,@PathVariable("idJogo") Integer idJogo, HttpSession session, RedirectAttributes redirectAttributes) {
		Formulario formulario = formularioService.find(Formulario.class,id);
		if (formulario == null) {
			redirectAttributes.addFlashAttribute("erro", "Formulário inexistente");
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Usuario usuario = getUsuarioLogado(session);
		if (usuario.getId() == formulario.getProfessor().getId()) {
			formularioService.delete(formulario);
			redirectAttributes.addFlashAttribute("info", "Formulário removido com sucesso.");
		} else {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
		}
		
		return "redirect:/jogo/"+ idJogo +"/formularios";

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
