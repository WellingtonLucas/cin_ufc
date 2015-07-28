package br.ufc.cin.web;

import static br.ufc.cin.util.Constants.MENSAGEM_EQUIPE_CADASTRADA;
import static br.ufc.cin.util.Constants.MENSAGEM_EQUIPE_INEXISTENTE;
import static br.ufc.cin.util.Constants.MENSAGEM_EQUIPE_REMOVIDA;
import static br.ufc.cin.util.Constants.MENSAGEM_ERRO_AO_CADASTRAR_EQUIPE;
import static br.ufc.cin.util.Constants.MENSAGEM_JOGO_INEXISTENTE;
import static br.ufc.cin.util.Constants.MENSAGEM_PERMISSAO_NEGADA;
import static br.ufc.cin.util.Constants.PAGINA_CADASTRAR_EQUIPE;
import static br.ufc.cin.util.Constants.PAGINA_DETALHES_EQUIPE;
import static br.ufc.cin.util.Constants.REDIRECT_PAGINA_LISTAR_JOGO;
import static br.ufc.cin.util.Constants.USUARIO_LOGADO;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.service.EquipeService;
import br.ufc.cin.service.JogoService;
import br.ufc.cin.service.UsuarioService;

@Controller
public class EquipeController {
	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private EquipeService equipeService;
	
	@Inject
	private JogoService jogoService;
	
	@RequestMapping(value ="/jogo/{id}/equipe/nova", method = RequestMethod.GET)
	public String cadastrarFrom(@PathVariable("id") Integer id, Model model, HttpSession session){
		Jogo jogo = jogoService.find(Jogo.class, id);
		
		model.addAttribute("action", "cadastrar");
		model.addAttribute("idJogo", id);
		model.addAttribute("equipe", new Equipe());
		model.addAttribute("participantes", equipeService.alunosSemEquipe(jogo));
		
		return PAGINA_CADASTRAR_EQUIPE;
	}
	
	@RequestMapping(value = "/{id}/equipe/nova", method = RequestMethod.POST)
	public  String cadastrar(@RequestParam(value = "idParticipantes", required = false) List<String> idAlunos,
			@PathVariable("id") Integer id,	@ModelAttribute("equipe") Equipe equipe, BindingResult result, HttpSession session, 
			RedirectAttributes redirect, Model model){
		Jogo jogo = jogoService.find(Jogo.class, id);
		
		model.addAttribute("action", "cadastrar");
		
		if (result.hasErrors()) {
			model.addAttribute("erro", MENSAGEM_ERRO_AO_CADASTRAR_EQUIPE);
			return "redirect:/jogo/"+ id +"/equipe/nova";
		}
		
		equipe.setJogo(jogo);
		List<Usuario> alunos = new ArrayList<Usuario>();
		equipe.setStatus(true);
		equipeService.save(equipe);
		
		if(idAlunos != null && !idAlunos.isEmpty()) {
			
			for(String idaluno : idAlunos) {
				Usuario aluno = usuarioService.find(Usuario.class, new Integer(idaluno));
				aluno.setEquipe(equipe);
				alunos.add(aluno);
			}
			
		}
		if(!alunos.isEmpty()){
			usuarioService.salvar(alunos);
		}
		redirect.addFlashAttribute("info", MENSAGEM_EQUIPE_CADASTRADA);
		return "redirect:/jogo/"+ id +"/equipes";
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEquipe}", method = RequestMethod.GET)
	public String verDetalhes(@PathVariable("idJogo") Integer idJogo,@PathVariable("idEquipe") Integer idEquipe,
			Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		model.addAttribute("action", "detalhes");
		
		Equipe equipe = equipeService.find(Equipe.class, idEquipe);
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		model.addAttribute("action", "detalhes");
		if (equipe == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EQUIPE_INEXISTENTE);
			return "redirect:/jogo/"+ idJogo +"/equipes";
		}
		if(jogo==null){
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Usuario usuario = getUsuarioLogado(session);
		if (usuario.getId() == jogo.getProfessor().getId() &&  jogo.getEquipes().contains(equipe)) {			
			model.addAttribute("equipe", equipe);
			model.addAttribute("jogo", jogo);
			return PAGINA_DETALHES_EQUIPE;
		}else{			
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEquipe}/excluir", method = RequestMethod.GET)
	public String excluir(@PathVariable("idJogo") Integer idJogo, @PathVariable("idEquipe") Integer idEquipe, 
			HttpSession session, RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Equipe equipe = equipeService.find(Equipe.class, idEquipe);
		
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		if (equipe == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EQUIPE_INEXISTENTE);
			return "redirect:/jogo/"+ idJogo +"/equipes";
		}
		
		Usuario usuario = getUsuarioLogado(session);
		if (usuario.getId() == jogo.getProfessor().getId() && jogo.getEquipes().contains(equipe)) {
			
			for (Usuario aluno : equipe.getAlunos()) {
				aluno.setEquipe(null);
				usuarioService.update(aluno);
			}
			jogo.getEquipes().remove(equipe);
			jogoService.update(jogo);
			equipeService.delete(equipe);	
			redirectAttributes.addFlashAttribute("info", MENSAGEM_EQUIPE_REMOVIDA);
		} else {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
		}
		return "redirect:/jogo/"+ idJogo +"/equipes";

	}
	
	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEquipe}/usuario/{idUsuario}/desvincular", method = RequestMethod.GET)
	public String desvincularUsuario(@PathVariable("idUsuario") Integer idUsuario, @PathVariable("idEquipe") Integer idEquipe, 
			@PathVariable("idJogo") Integer idJogo, HttpSession session, RedirectAttributes redirectAttributes) {
	
		Usuario user = usuarioService.find(Usuario.class, idUsuario);
		Equipe equipe = equipeService.find(Equipe.class, idEquipe);
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		
		if (user == null) {
			redirectAttributes.addFlashAttribute("erro", "Participante inexistente");
			return "redirect:/jogo/"+ jogo.getId() +"/equipes/"+equipe.getId();
		}
		if (equipe == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EQUIPE_INEXISTENTE);
			return "redirect:/jogo/"+ jogo.getId() +"/equipes";
		}
		if(jogo==null){
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		
		Usuario usuario = getUsuarioLogado(session);
		if (usuario.getId() == jogo.getProfessor().getId() && jogo.getEquipes().contains(equipe)) {
			user.setEquipe(null);
			usuarioService.update(user);
			redirectAttributes.addFlashAttribute("info", "Participante desvinculado com sucesso.");
		} else {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
		}
		return "redirect:/jogo/"+ idJogo +"/equipe/"+ idEquipe;

	}

	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEquipe}/inativar", method = RequestMethod.GET)
	public String inativarEquipe(@PathVariable("idEquipe") Integer idEquipe, 
			@PathVariable("idJogo") Integer idJogo, HttpSession session, RedirectAttributes redirectAttributes) {
	
		Equipe equipe = equipeService.find(Equipe.class, idEquipe);
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		
		if (equipe == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EQUIPE_INEXISTENTE);
			return "redirect:/jogo/"+ jogo.getId() +"/equipes";
		}
		if(jogo==null){
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		
		Usuario usuario = getUsuarioLogado(session);
		if (usuario.getId() == jogo.getProfessor().getId() && jogo.getEquipes().contains(equipe)) {
			equipe.setStatus(false);
			equipeService.update(equipe);
			redirectAttributes.addFlashAttribute("info", "Equipe inativada com sucesso.");
		} else {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
		}
		return "redirect:/jogo/"+ idJogo +"/equipe/"+ idEquipe;

	}

	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEquipe}/ativar", method = RequestMethod.GET)
	public String ativarEquipe(@PathVariable("idEquipe") Integer idEquipe, 
			@PathVariable("idJogo") Integer idJogo, HttpSession session, RedirectAttributes redirectAttributes) {
	
		Equipe equipe = equipeService.find(Equipe.class, idEquipe);
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		
		if (equipe == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EQUIPE_INEXISTENTE);
			return "redirect:/jogo/"+ jogo.getId() +"/equipes";
		}
		if(jogo==null){
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		
		Usuario usuario = getUsuarioLogado(session);
		if (usuario.getId() == jogo.getProfessor().getId() && jogo.getEquipes().contains(equipe)) {
			equipe.setStatus(true);
			equipeService.update(equipe);
			redirectAttributes.addFlashAttribute("info", "Equipe ativada com sucesso.");
		} else {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
		}
		return "redirect:/jogo/"+ idJogo +"/equipe/"+ idEquipe;

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
