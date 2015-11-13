package br.ufc.cin.web;

import static br.ufc.cin.util.Constants.MENSAGEM_EQUIPE_INEXISTENTE;
import static br.ufc.cin.util.Constants.MENSAGEM_FORM_NAO_CRIADOS;
import static br.ufc.cin.util.Constants.MENSAGEM_FORM_NAO_EXISTENTE;
import static br.ufc.cin.util.Constants.MENSAGEM_JOGO_INEXISTENTE;
import static br.ufc.cin.util.Constants.MENSAGEM_PERMISSAO_NEGADA;
import static br.ufc.cin.util.Constants.PAGINA_CADASTRAR_FORMULARIO;
import static br.ufc.cin.util.Constants.PAGINA_DETALHES_FORM;
import static br.ufc.cin.util.Constants.PAGINA_LISTAR_FORMULARIOS;
import static br.ufc.cin.util.Constants.REDIRECT_PAGINA_LISTAR_FORMULARIOS;
import static br.ufc.cin.util.Constants.REDIRECT_PAGINA_LISTAR_JOGO;
import static br.ufc.cin.util.Constants.USUARIO_LOGADO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
	
	@Inject
	private EntregaService entregaService;
	

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
		
		Usuario usuario = getUsuarioLogado(session);
		
		model.addAttribute("usuario", usuario);
		model.addAttribute("formulario", new Formulario());
		model.addAttribute("action","cadastrar");
		
		return "formulario/formulario";
	}
	
	@RequestMapping(value = "/formulario/salvar", method = RequestMethod.POST)
	public String cadastroFormulario(@ModelAttribute("formulario") Formulario formulario, Model model,
			HttpSession session, RedirectAttributes redirect, BindingResult result) {
		
		if (result.hasErrors()) {
			redirect.addFlashAttribute("erro", "Erro na formação do formulário.");
			return "redirect:/formulario";
		}

		Usuario usuario = getUsuarioLogado(session);
		try {

			formulario.setProfessor(usuario);
			formularioService.save(formulario);	
		} catch (Exception e) {
			redirect.addFlashAttribute("erro", "Erro ao tentar salvar o formulário.");
			return "redirect:/formulario";
		}
		try {
			for (Pergunta pergunta : formulario.getPerguntas()) {
				pergunta.setFormulario(formulario);
			}
			perguntaService.salvar(formulario.getPerguntas());
		} catch (Exception e) {
			redirect.addFlashAttribute("erro", "Erro ao tentar salvar as perguntas do formulário.");
			return "redirect:/formulario";
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
			return "redirect:/formulario";
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
	
	@RequestMapping(value = "/formulario/editar", method = RequestMethod.POST)
	public String editar(@Valid Formulario formulario, Model model, 
			HttpSession session, RedirectAttributes redirect, BindingResult result) {
		
		if (result.hasErrors()) {
			redirect.addFlashAttribute("erro", "Erro ao editar formulário.");
			return "redirect:/formulario/"+formulario.getId()+"/editar";
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
			return "redirect:/formulario/listar";
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
		if(jogo==null){
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Formulario formulario = formularioService.find(Formulario.class, idForm);
		if (formulario == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EQUIPE_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_FORMULARIOS;
		}
		Entrega entrega = entregaService.find(Entrega.class, id);
		if (entrega == null) {
			redirectAttributes.addFlashAttribute("erro", "Entrega inexistente.");
			return "redirect:/jogo/"+ idJogo +"/rodadas";
		}
		Calendar calendario = Calendar.getInstance();
		Date data =  calendario.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		simpleDateFormat.format(data);
		long tempoAtual = data.getTime();
		if(tempoAtual < entrega.getRodada().getPrazoSubmissao().getTime()){
			redirectAttributes.addFlashAttribute("erro", "Período de submissão ainda não se encerrou!");
			return "redirect:/jogo/"+jogo.getId()+"/rodada/"+entrega.getRodada().getId()+"/detalhes";
		}
		Usuario usuario = getUsuarioLogado(session);
		if (usuario.equals(jogo.getProfessor()) &&  jogo.getProfessor().getFormulario().contains(formulario)
				&& entrega.getRodada().isStatus()) {			
			model.addAttribute("permissao", "professor");
		}else if(jogo.getAlunos().contains(usuario) && jogo.getProfessor().getFormulario().contains(formulario)
				&& entrega.getRodada().isStatus()) {
			model.addAttribute("permissao", "aluno");
		}else if(!entrega.getRodada().isStatus()){
			redirectAttributes.addFlashAttribute("erro", "A rodada se encerrou!");
			return "redirect:/jogo/"+jogo.getId()+"/rodada/"+entrega.getRodada().getId()+"/detalhes";
		}else{			
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		model.addAttribute("action", "responder");
		model.addAttribute("formulario", formulario);
		model.addAttribute("jogo", jogo);
		model.addAttribute("entrega", entrega);
		model.addAttribute("resposta", new Resposta());
		return "formulario/responder";
	}
	
	@RequestMapping(value = "/{idJogo}/entrega/{id}/formulario/{idForm}/responder", method = RequestMethod.POST)
	public String responder(@PathVariable("idJogo") Integer idJogo, @PathVariable("idForm") Integer idForm,	
			@PathVariable("id") Integer id, Model model, HttpSession session,
			@ModelAttribute("resposta") Resposta resposta, RedirectAttributes redirectAttributes,
			BindingResult result) {
		
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("erro", "Erro ao cadastrar um formulário.");
			return "redirect:/jogo/"+idJogo+"/formulario/"+idForm;
		}
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		if(jogo==null){
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Formulario formulario = formularioService.find(Formulario.class, idForm);
		if (formulario == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EQUIPE_INEXISTENTE);
			return "redirect:/jogo/"+ idJogo +"/formularios";
		}
		Entrega entrega = entregaService.find(Entrega.class, id);
		
		List<Opcao> opcoes = new ArrayList<Opcao>();
		for (Opcao opcao : resposta.getOpcoes()) {
			if(!(opcao.getId()==null))
				opcoes.add(opcaoService.find(Opcao.class, opcao.getId()));
		}
		if(opcoes.size()==0 || (formulario.getPerguntas().size() > opcoes.size())){
			redirectAttributes.addFlashAttribute("erro", "É necessário responder todas às perguntas para efetuar uma avaliação.");
			return  "redirect:/jogo/"+jogo.getId()+"/entrega/"+entrega.getId()+"/formulario/"+formulario.getId();
		}
		
		Usuario usuario = getUsuarioLogado(session);
		Calendar calendario = Calendar.getInstance();
		Date data =  calendario.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm:ss");
		simpleDateFormat.format(data);
		
		resposta.setOpcoes(opcoes);
		resposta.setFormulario(formulario);
		resposta.setUsuario(usuario);
		if(usuario.equals(jogo.getProfessor())){
			resposta.setEntregaGabarito(entrega);
		}else{
			resposta.setEntrega(entrega);
		}
		resposta.setDia(data);
		respostaService.save(resposta);
		
		if(usuario.equals(jogo.getProfessor())){
			entrega.setGabarito(respostaService.getRespostaByEntrega(entrega));
			entregaService.update(entrega);
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
