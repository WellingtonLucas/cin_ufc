package br.ufc.cin.web;

import static br.ufc.cin.util.Constants.MENSAGEM_EQUIPE_INEXISTENTE;
import static br.ufc.cin.util.Constants.MENSAGEM_EXCEPTION;
import static br.ufc.cin.util.Constants.MENSAGEM_FORM_NAO_CRIADOS;
import static br.ufc.cin.util.Constants.MENSAGEM_FORM_NAO_EXISTENTE;
import static br.ufc.cin.util.Constants.MENSAGEM_PERMISSAO_NEGADA;
import static br.ufc.cin.util.Constants.PAGINA_CADASTRAR_FORMULARIO;
import static br.ufc.cin.util.Constants.PAGINA_DETALHES_FORM;
import static br.ufc.cin.util.Constants.PAGINA_LISTAR_FORMULARIOS;
import static br.ufc.cin.util.Constants.PAGINA_RESPONDER_FORMULARIO;
import static br.ufc.cin.util.Constants.REDIRECT_PAGINA_FORMULARIO;
import static br.ufc.cin.util.Constants.REDIRECT_PAGINA_LISTAR_FORMULARIOS;
import static br.ufc.cin.util.Constants.REDIRECT_PAGINA_LISTAR_JOGO;
import static br.ufc.cin.util.Constants.USUARIO_LOGADO;

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

import br.ufc.cin.model.Entrega;
import br.ufc.cin.model.Formulario;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Opcao;
import br.ufc.cin.model.Pergunta;
import br.ufc.cin.model.Resposta;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.service.EntregaService;
import br.ufc.cin.service.FormularioService;
import br.ufc.cin.service.JogoService;
import br.ufc.cin.service.OpcaoService;
import br.ufc.cin.service.PerguntaService;
import br.ufc.cin.service.RegrasService;
import br.ufc.cin.service.RespostaService;
import br.ufc.cin.service.RodadaService;
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
	
	@Inject
	private EntregaService entregaService;
	
	@Inject
	private RodadaService rodadaService;
	
	@Inject
	private RegrasService regrasService;
	
	@RequestMapping(value = "/formularios", method = RequestMethod.GET)
	public String listarFormularios(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		model.addAttribute("action","formularios");
		
		Usuario usuario = getUsuarioLogado(session);
		usuario = usuarioService.find(Usuario.class, usuario.getId());
		List<Formulario> formularios = usuario.getFormulario();
		if(formularios == null || formularios.isEmpty()){
			model.addAttribute("erro", MENSAGEM_FORM_NAO_CRIADOS);
		}
		model.addAttribute("action", "formularios");
		model.addAttribute("permissao", "professorForm");
		model.addAttribute("formularios", formularios);
		return PAGINA_LISTAR_FORMULARIOS;
	}
	
	@RequestMapping(value = "/formulario", method = RequestMethod.GET)
	public String novoFormulario(Model model, HttpSession session,
			 RedirectAttributes redirectAttributes) {
		getUsuarioLogado(session);
		model.addAttribute("formulario", new Formulario());
		model.addAttribute("action","cadastrar");
		
		return PAGINA_CADASTRAR_FORMULARIO;
	}
	
	@RequestMapping(value = "/formulario", method = RequestMethod.POST)
	public String cadastroFormulario(@Valid@ModelAttribute("formulario") Formulario formulario, Model model,
			HttpSession session, RedirectAttributes redirect, BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("erro", "Verifique os campos obrigatórios e tente novamente.");
			model.addAttribute("action","erroCadastro");
			model.addAttribute("formulario", formulario);
			return PAGINA_CADASTRAR_FORMULARIO;
		}
		Usuario usuario = getUsuarioLogado(session);
		try {
			formularioService.verificaCamposObrigatorios(formulario);
			formulario.setProfessor(usuario);
			formularioService.save(formulario);
		}catch(IllegalArgumentException e){
			model.addAttribute("erro", e.getMessage());
			model.addAttribute("action","erroCadastro");
			model.addAttribute("formulario", formulario);
			return PAGINA_CADASTRAR_FORMULARIO;
		}catch (Exception e) {
			redirect.addFlashAttribute("erro", "Erro ao tentar salvar o formulário.");
			return REDIRECT_PAGINA_FORMULARIO;
		}
		try {
			for (Pergunta pergunta : formulario.getPerguntas()) {
				pergunta.setFormulario(formulario);
			}
			perguntaService.salvar(formulario.getPerguntas());
		} catch (Exception e) {
			redirect.addFlashAttribute("erro", "Erro ao tentar salvar as perguntas do formulário.");
			return REDIRECT_PAGINA_FORMULARIO;
		}
		try {
			for (Pergunta pergunta : formulario.getPerguntas()) {
				for (Opcao opcao : pergunta.getOpcoes()) {
					opcao.setPergunta(pergunta);
				}
				opcaoService.salvar(pergunta.getOpcoes());
			}
		} catch (Exception e) {
			redirect.addFlashAttribute("erro", "Erro ao tentar salvar as opções das perguntas do formulário.");
			return REDIRECT_PAGINA_FORMULARIO;
		}
		redirect.addFlashAttribute("info", "Formulário cadastrado com sucesso!");
		return  "redirect:/formulario/"+formulario.getId()+"/detalhes";
	}
	
	@RequestMapping(value = "/formulario/{idForm}/editar", method = RequestMethod.GET)
	public String editarForm(@PathVariable("idForm") Integer idForm,
			Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		
		Formulario formulario = formularioService.find(Formulario.class, idForm);
		if(formulario == null){
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_FORM_NAO_EXISTENTE);
			return REDIRECT_PAGINA_LISTAR_FORMULARIOS;
		}
		Usuario usuario = getUsuarioLogado(session);
		
		if (formulario.getProfessor().equals(usuario)) {
			model.addAttribute("formulario", formulario);
		 	model.addAttribute("action", "editar");
			return PAGINA_CADASTRAR_FORMULARIO;
		}

		redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
		return REDIRECT_PAGINA_LISTAR_FORMULARIOS;
	}
	
	@RequestMapping(value = "/formulario/{idForm}/editar", method = RequestMethod.POST)
	public String editar(@PathVariable("idForm") Integer idForm, @Valid Formulario formulario, Model model, 
			HttpSession session, RedirectAttributes redirect, BindingResult result) {
		
		if (result.hasErrors()) {
			model.addAttribute("erro", "Verifique os campos obrigatórios e tente novamente.");
			model.addAttribute("action","editar");
			model.addAttribute("formulario", formulario);
			return PAGINA_CADASTRAR_FORMULARIO;
		}
		try {
			formularioService.verificaCamposObrigatorios(formulario);
		} catch (IllegalArgumentException e) {
			model.addAttribute("erro", e.getMessage());
			model.addAttribute("action","editar");
			model.addAttribute("formulario", formulario);
			return PAGINA_CADASTRAR_FORMULARIO;	
		}
			
		formulario.setProfessor(getUsuarioLogado(session));
		
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
		try {
			formularioService.update(formulario);	
		} catch (Exception e) {
			redirect.addFlashAttribute("erro", "Erro ao atualizar o formulário!");
			return "redirect:/formulario/"+formulario.getId()+"/editar";
		}
		
		redirect.addFlashAttribute("info", "Formulário atualizado com sucesso!");
		return  "redirect:/formulario/"+formulario.getId()+"/detalhes";
	}

	@RequestMapping(value = "/formulario/{idForm}/copiar", method = RequestMethod.GET)
	public String copiarForm(@PathVariable("idForm") Integer idForm,
			Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		Formulario formulario = formularioService.find(Formulario.class, idForm);
			if(formulario == null){
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_FORM_NAO_EXISTENTE);
			return REDIRECT_PAGINA_LISTAR_FORMULARIOS;
		}
		Usuario usuario = getUsuarioLogado(session);
		
		if (formulario.getProfessor().equals(usuario)) {
			model.addAttribute("formulario", formulario);
		 	model.addAttribute("action", "copiar");
			return PAGINA_CADASTRAR_FORMULARIO;
		}
		redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
		return REDIRECT_PAGINA_LISTAR_JOGO;
	}
	
	@RequestMapping(value = "/formulario/{idForm}/detalhes", method = RequestMethod.GET)
	public String verDetalhes(@PathVariable("idForm") Integer idForm,
			Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		
		Formulario formulario = formularioService.find(Formulario.class, idForm);
		if (formulario == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EQUIPE_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_FORMULARIOS;
		}
		model.addAttribute("action", "detalhesFormulario");
		Usuario usuario = getUsuarioLogado(session);
		if (formulario.getProfessor().equals(usuario)) {			
			model.addAttribute("formulario", formulario);
			model.addAttribute("permissao", "professorForm");
			return PAGINA_DETALHES_FORM;
		}else{			
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
			return REDIRECT_PAGINA_LISTAR_FORMULARIOS;
		}
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/entrega/{id}/formulario/{idForm}", method = RequestMethod.GET)
	public String modoResponder(@PathVariable("idJogo") Integer idJogo, @PathVariable("idForm") Integer idForm,
			@PathVariable("id") Integer id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {

		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Usuario usuario = getUsuarioLogado(session);
		Entrega entrega = entregaService.find(Entrega.class, id);
		Formulario formulario = formularioService.find(Formulario.class, idForm);
		String permissao;
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaParticipacao(usuario, jogo);
			regrasService.verificaEntrega(entrega);
			regrasService.verificaFormulario(formulario);
			permissao = usuarioService.definePermissao(jogo, usuario);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		try {
			rodadaService.atualizaStatusRodada(entrega.getRodada());
			rodadaService.verificaStatusRodada(entrega.getRodada());
			rodadaService.atualizaStatusPrazoRodada(entrega.getRodada());
			rodadaService.verificaStatusPrazoSubmissao(entrega.getRodada());
			rodadaService.atualizaStatusAvaliacao(entrega.getRodada());
			rodadaService.verificaStatusAvaliacao(entrega.getRodada());
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return "redirect:/jogo/"+jogo.getId()+"/rodada/"+entrega.getRodada().getId()+"/detalhes";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return "redirect:/jogo/"+jogo.getId()+"/rodada/"+entrega.getRodada().getId()+"/detalhes";
		}
		
		model.addAttribute("permissao", permissao);
		model.addAttribute("action", "responder");
		model.addAttribute("formulario", formulario);
		model.addAttribute("jogo", jogo);
		model.addAttribute("entrega", entrega);
		model.addAttribute("resposta", new Resposta());
		return PAGINA_RESPONDER_FORMULARIO;
	}
	
	@RequestMapping(value = "/{idJogo}/entrega/{id}/formulario/{idForm}/responder", method = RequestMethod.GET)
	public String responder(@PathVariable("idJogo") Integer idJogo, @PathVariable("idForm") Integer idForm,	
			@PathVariable("id") Integer id) {
		return "redirect:/jogo/"+idJogo+"/entrega/"+id+"/formulario/"+idForm;
	}
	@RequestMapping(value = "/{idJogo}/entrega/{id}/formulario/{idForm}/responder", method = RequestMethod.POST)
	public String responder(@PathVariable("idJogo") Integer idJogo, @PathVariable("idForm") Integer idForm,	
			@PathVariable("id") Integer id, Model model, HttpSession session,
			@ModelAttribute("resposta") Resposta resposta, RedirectAttributes redirectAttributes,
			BindingResult result) {
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("erro", "Erro ao responder questionário.");
			return "redirect:/jogo/"+idJogo+"/formulario/"+idForm;
		}
		Jogo jogo;
		Formulario formulario;
		Entrega entrega;
		Usuario usuario = getUsuarioLogado(session);
		try {
			jogo = jogoService.find(Jogo.class, idJogo);
			formulario = formularioService.find(Formulario.class, idForm);
			entrega = entregaService.find(Entrega.class, id);
			regrasService.verificaJogo(jogo);
			regrasService.verificaParticipacao(usuario, jogo);
			regrasService.verificaEntrega(entrega);
			regrasService.verificaFormulario(formulario);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return  REDIRECT_PAGINA_LISTAR_JOGO;
		}
		try {
			respostaService.salvar(resposta,jogo, formulario, usuario, entrega);	
		} catch (IllegalAccessError e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return "redirect:/jogo/"+jogo.getId()+"/entrega/"+entrega.getId()+"/formulario/"+formulario.getId();
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return  "redirect:/jogo/"+jogo.getId()+"/entrega/"+entrega.getId()+"/formulario/"+formulario.getId();
		}
		
		redirectAttributes.addFlashAttribute("info", "Entrega da equipe "+entrega.getEquipe().getNome()+" avalidada.");
		return  "redirect:/jogo/"+jogo.getId()+"/rodada/"+entrega.getRodada().getId()+"/submissoes";
	}

	@RequestMapping(value = "/formulario/{id}/excluir")
	public String excluir(@PathVariable("id") Integer id,
			HttpSession session, RedirectAttributes redirectAttributes) {
		Formulario formulario = formularioService.find(Formulario.class,id);
		if (formulario == null) {
			redirectAttributes.addFlashAttribute("erro", "Formulário inexistente");
			return REDIRECT_PAGINA_LISTAR_FORMULARIOS;
		}
		Usuario usuario = getUsuarioLogado(session);
		if (usuario.equals(formulario.getProfessor())) {
			try {
				formularioService.delete(formulario);	
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("erro", "Erro ao tentar remover o formulário");
				return  "redirect:/formulario/"+formulario.getId()+"/detalhes";
			}
		} else {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
			return REDIRECT_PAGINA_LISTAR_FORMULARIOS;
		}
		redirectAttributes.addFlashAttribute("info", "Formulário removido com sucesso.");
		return REDIRECT_PAGINA_LISTAR_FORMULARIOS;

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
