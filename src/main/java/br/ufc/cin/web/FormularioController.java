package br.ufc.cin.web;

import static br.ufc.cin.util.Constants.MENSAGEM_EQUIPE_INEXISTENTE;
import static br.ufc.cin.util.Constants.MENSAGEM_FORM_NAO_EXISTENTE;
import static br.ufc.cin.util.Constants.MENSAGEM_JOGO_INEXISTENTE;
import static br.ufc.cin.util.Constants.MENSAGEM_PERMISSAO_NEGADA;
import static br.ufc.cin.util.Constants.PAGINA_CADASTRAR_FORMULARIO;
import static br.ufc.cin.util.Constants.PAGINA_DETALHES_FORM;
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
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Formulario;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Opcao;
import br.ufc.cin.model.Pergunta;
import br.ufc.cin.model.Resposta;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.service.EntregaService;
import br.ufc.cin.service.EquipeService;
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
	
	@Inject
	private EquipeService equipeService;
	
	@RequestMapping(value = "/jogo/{id}/formulario", method = RequestMethod.GET)
	public String novoFormulario(Model model, HttpSession session, @PathVariable("id") Integer id,
			 RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class,id);
		
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		
		model.addAttribute("idJogo", id);
		model.addAttribute("formulario", new Formulario());
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
		return  "redirect:/jogo/"+jogo.getId()+"/formulario/"+formulario.getId()+"/detalhes";
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
		if (usuario.equals(jogo.getProfessor()) &&  jogo.getProfessor().getFormulario().contains(formulario)) {			
			model.addAttribute("formulario", formulario);
			model.addAttribute("jogo", jogo);
			model.addAttribute("permissao", "professor");
			return PAGINA_DETALHES_FORM;
		}else{			
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
			return REDIRECT_PAGINA_LISTAR_JOGO;
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
			return "redirect:/jogo/"+ idJogo +"/formularios";
		}
		Entrega entrega = entregaService.find(Entrega.class, id);
		if (entrega == null) {
			redirectAttributes.addFlashAttribute("erro", "Entrega inexistente.");
			return "redirect:/jogo/"+ idJogo +"/rodadas";
		}
		
		Usuario usuario = getUsuarioLogado(session);
		if (usuario.equals(jogo.getProfessor()) &&  jogo.getProfessor().getFormulario().contains(formulario)
				&& entrega.getRodada().isStatus()) {			
			model.addAttribute("permissao", "professor");
		}else if(jogo.getAlunos().contains(usuario) && jogo.getProfessor().getFormulario().contains(formulario)
				&& entrega.getRodada().isStatus()) {
			model.addAttribute("permissao", "aluno");
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
			opcoes.add(opcaoService.find(Opcao.class, opcao.getId()));
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
	
	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEquipe}/entrega/{id}/formulario/{idForm}/avaliacao", method = RequestMethod.GET)
	public String avaliacao(@PathVariable("idJogo") Integer idJogo, @PathVariable("idForm") Integer idForm,
			@PathVariable("id") Integer id, Model model, HttpSession session, @PathVariable("idEquipe") Integer idEquipe,
			RedirectAttributes redirectAttributes) {

		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		if(jogo == null){
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Equipe equipe = equipeService.find(Equipe.class, idEquipe);
		if (equipe == null || !jogo.getEquipes().contains(equipe)) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_EQUIPE_INEXISTENTE);
			return "redirect:/jogo/" + idJogo + "/equipes";
		}
		Formulario formulario = formularioService.find(Formulario.class, idForm);
		if (formulario == null) {
			redirectAttributes.addFlashAttribute("erro","Formulário inexistente.");
			return "redirect:/jogo/"+ idJogo +"/formularios";
		}
		Entrega entrega = entregaService.find(Entrega.class, id);
		if (entrega == null) {
			redirectAttributes.addFlashAttribute("erro", "Entrega inexistente.");
			return "redirect:/jogo/"+ idJogo +"/rodadas";
		}
		
		Usuario usuario = getUsuarioLogado(session);
		if(!jogo.isStatus() && jogo.getAlunos().contains(usuario)){
			redirectAttributes.addFlashAttribute("erro",
					"Jogo inativado no momento. Para mais informações "+jogo.getProfessor().getEmail());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}else if(!jogo.getAlunos().contains(usuario) && !jogo.getProfessor().equals(usuario)){
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_PERMISSAO_NEGADA);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		
		usuario = usuarioService.find(Usuario.class, usuario.getId());
		Equipe equipeAluno = equipeService.equipePorAlunoNoJogo(usuario, jogo);

		if (usuario.equals(jogo.getProfessor()) &&  jogo.getProfessor().getFormulario().contains(formulario)) {			
			model.addAttribute("permissao", "professor");
			model.addAttribute("equipe", equipe);
		}else if(equipeAluno != null){
			model.addAttribute("permissao", "aluno");
			model.addAttribute("equipe", equipeAluno);
		}else{			
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		model.addAttribute("action", "avaliacao");
		model.addAttribute("formulario", formulario);
		model.addAttribute("jogo", jogo);
		model.addAttribute("entrega", entrega);

		return "equipe/avaliacao";
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
