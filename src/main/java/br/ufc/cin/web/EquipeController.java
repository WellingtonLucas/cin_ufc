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
import javax.validation.Valid;

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

import br.ufc.cin.model.Entrega;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.service.EntregaService;
import br.ufc.cin.service.EquipeService;
import br.ufc.cin.service.JogoService;
import br.ufc.cin.service.RodadaService;
import br.ufc.cin.service.UsuarioService;

@Controller
public class EquipeController {
	@Inject
	private UsuarioService usuarioService;

	@Inject
	private EquipeService equipeService;

	@Inject
	private JogoService jogoService;

	@Inject
	private RodadaService rodadaService;
	
	@Inject
	private EntregaService entregaService;
	
	@RequestMapping(value = "/jogo/{id}/equipe/nova", method = RequestMethod.GET)
	public String cadastrarFrom(@PathVariable("id") Integer id, Model model,
			HttpSession session, RedirectAttributes redirect) {
		Jogo jogo = jogoService.find(Jogo.class, id);
		Usuario usuario = getUsuarioLogado(session);
		if(jogo == null){
			redirect.addFlashAttribute("erro", "Jogo inexistente");
			return "redirect:/jogo/listar";
		}
		if(!usuario.equals(jogo.getProfessor())){
			redirect.addFlashAttribute("erro", "Você não possui permissão de acesso");
			return "redirect:/jogo/listar";
		}
		
		model.addAttribute("editor", "equipe");
		model.addAttribute("action", "cadastrar");
		model.addAttribute("idJogo", id);
		model.addAttribute("equipe", new Equipe());
		model.addAttribute("participantes", equipeService.alunosSemEquipe(jogo));
		return PAGINA_CADASTRAR_EQUIPE;
	}

	@RequestMapping(value = "/{id}/equipe/nova", method = RequestMethod.POST)
	public String cadastrar(
			@RequestParam(value = "idParticipantes", required = false) List<String> idAlunos,
			@PathVariable("id") Integer id,
			@ModelAttribute("equipe") Equipe equipe, BindingResult result,
			HttpSession session, RedirectAttributes redirect, Model model) {
		Jogo jogo = jogoService.find(Jogo.class, id);

		
		if (result.hasErrors()) {
			redirect.addFlashAttribute("erro", MENSAGEM_ERRO_AO_CADASTRAR_EQUIPE);
			return "redirect:/jogo/" + id + "/equipe/nova";
		}

		equipe.setJogo(jogo);
		List<Usuario> alunos = new ArrayList<Usuario>();
		equipe.setStatus(true);
		equipeService.save(equipe);

		if (idAlunos != null && !idAlunos.isEmpty()) {

			for (String idaluno : idAlunos) {
				Usuario aluno = usuarioService.find(Usuario.class, new Integer(
						idaluno));
				aluno.addEquipe(equipe);
				alunos.add(aluno);
			}

		}
		if (!alunos.isEmpty()) {
			usuarioService.salvar(alunos);
		}
		redirect.addFlashAttribute("info", MENSAGEM_EQUIPE_CADASTRADA);
		return "redirect:/jogo/" + id + "/equipes";
	}

	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEquipe}", method = RequestMethod.GET)
	public String verDetalhes(@PathVariable("idJogo") Integer idJogo,
			@PathVariable("idEquipe") Integer idEquipe, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {

		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
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
		
		Equipe equipe = equipeService.find(Equipe.class, idEquipe);
		model.addAttribute("action", "detalhesEquipe");
		if (equipe == null || !jogo.getEquipes().contains(equipe)) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_EQUIPE_INEXISTENTE);
			return "redirect:/jogo/" + idJogo + "/equipes";
		}
		model.addAttribute("equipe", equipe);
		model.addAttribute("jogo", jogo);
		if (usuario.getId() == jogo.getProfessor().getId() && jogo.getEquipes().contains(equipe)) {
			model.addAttribute("permissao", "professor");
			return PAGINA_DETALHES_EQUIPE;
		}else if(equipe.getAlunos().contains(usuario) && jogo.getEquipes().contains(equipe)){
			model.addAttribute("permissao", "membro");
			return PAGINA_DETALHES_EQUIPE;
		}else if(jogo.getAlunos().contains(usuario)){
			model.addAttribute("permissao", "outro");
			return PAGINA_DETALHES_EQUIPE;
		}else {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_PERMISSAO_NEGADA);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
	}

	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEqui}/editar", method = RequestMethod.GET)
	public String editarEquipe(@PathVariable("idJogo") Integer idJogo,
			@PathVariable("idEqui") Integer idEqui, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
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
		
		Equipe equipe = equipeService.find(Equipe.class, idEqui);
		if (equipe == null || !jogo.getEquipes().contains(equipe)) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_EQUIPE_INEXISTENTE);
			return "redirect:/equipe/equipes";
		}

		if (usuario.equals(jogo.getProfessor())) {
			model.addAttribute("jogo", jogo);
			model.addAttribute("equipe", equipe);
			model.addAttribute("editor", "equipe");
			model.addAttribute("action", "editar");
			model.addAttribute("permissao", "professor");
			return PAGINA_CADASTRAR_EQUIPE;
		}else if(jogo.getAlunos().contains(usuario) && equipe.getAlunos().contains(usuario)){
			model.addAttribute("jogo", jogo);
			model.addAttribute("equipe", equipe);
			model.addAttribute("editor", "equipe");
			model.addAttribute("action", "editar");
			model.addAttribute("permissao", "membro");
			return PAGINA_CADASTRAR_EQUIPE;
		}

		redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
		return REDIRECT_PAGINA_LISTAR_JOGO;
	}

	@RequestMapping(value = "/{id}/equipe/editar", method = RequestMethod.POST)
	public String editar(
			@RequestParam(value = "idParticipantes", required = false) List<String> idAlunos,
			@PathVariable("id") Integer id, @Valid Equipe equipe, BindingResult result, HttpSession session,
			RedirectAttributes redirect, Model model) {
		Jogo jogo = jogoService.find(Jogo.class, id);
		model.addAttribute("editor", "equipe");
		model.addAttribute("action", "editar");

		if (result.hasErrors()) {
			redirect.addFlashAttribute("erro", "Erro ao editar equipe.");
			return "redirect:/jogo/" + id + "/equipe/" + equipe.getId()
					+ "/editar";
		}

		if (jogo == null) {
			redirect.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Equipe oldEquipe = equipeService.find(Equipe.class, equipe.getId());
		oldEquipe.setNome(equipe.getNome());
		oldEquipe.setIdeiaDeNegocio(equipe.getIdeiaDeNegocio());
		equipeService.update(oldEquipe);
		redirect.addFlashAttribute("info", "Equipe atualizada com sucesso.");
		return "redirect:/jogo/" + id + "/equipe/"+equipe.getId();
	}

	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEquipe}/excluir", method = RequestMethod.GET)
	public String excluir(@PathVariable("idJogo") Integer idJogo,
			@PathVariable("idEquipe") Integer idEquipe, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Equipe equipe = equipeService.find(Equipe.class, idEquipe);

		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		if (equipe == null) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_EQUIPE_INEXISTENTE);
			return "redirect:/jogo/" + idJogo + "/equipes";
		}

		Usuario usuario = getUsuarioLogado(session);
		if (usuario.equals(jogo.getProfessor())
				&& jogo.getEquipes().contains(equipe)) {

			for (Usuario aluno : equipe.getAlunos()) {
				aluno.getEquipes().remove(equipe);
				usuarioService.update(aluno);
			}
			jogo.getEquipes().remove(equipe);
			jogoService.update(jogo);
			if(jogo.getRodadas()!= null)
				rodadaService.removerEquipeDasRodadas(jogo.getRodadas(), equipe);
			
			try{
				equipeService.delete(equipe);
			}catch(Exception e){
				redirectAttributes.addFlashAttribute("erro",
						"Erro ao remover equipe. Contacte o administrador do sistema.");
				return "redirect:/jogo/" + idJogo + "/equipes";
			}
			redirectAttributes.addFlashAttribute("info",
					MENSAGEM_EQUIPE_REMOVIDA);
		} else {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_PERMISSAO_NEGADA);
		}
		return "redirect:/jogo/" + idJogo + "/equipes";

	}

	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEquipe}/usuario/{idUsuario}/desvincular", method = RequestMethod.GET)
	public String desvincularUsuario(
			@PathVariable("idUsuario") Integer idUsuario,
			@PathVariable("idEquipe") Integer idEquipe,
			@PathVariable("idJogo") Integer idJogo, HttpSession session,
			RedirectAttributes redirectAttributes) {

		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Equipe equipe = equipeService.find(Equipe.class, idEquipe);
		if (equipe == null) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_EQUIPE_INEXISTENTE);
			return "redirect:/jogo/" + jogo.getId() + "/equipes";
		}
		Usuario user = usuarioService.find(Usuario.class, idUsuario);
		if (user == null) {
			redirectAttributes.addFlashAttribute("erro",
					"Participante inexistente");
			return "redirect:/jogo/" + jogo.getId() + "/equipe/"
					+ equipe.getId();
		}
		
		Usuario usuario = getUsuarioLogado(session);
		if (usuario.equals(jogo.getProfessor())	&& jogo.getEquipes().contains(equipe)) {
			equipe.getAlunos().remove(user);
			equipeService.update(equipe);
			user.getEquipes().remove(equipe);
			usuarioService.update(user);
			redirectAttributes.addFlashAttribute("info",
					"Participante \""+user.getNome()+"\" desvinculado com sucesso.");
		} else {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_PERMISSAO_NEGADA);
		}
		return "redirect:/jogo/" + idJogo + "/equipe/" + idEquipe;
	}

	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEquipe}/inativar", method = RequestMethod.GET)
	public String inativarEquipe(@PathVariable("idEquipe") Integer idEquipe,
			@PathVariable("idJogo") Integer idJogo, HttpSession session,
			RedirectAttributes redirectAttributes) {

		Equipe equipe = equipeService.find(Equipe.class, idEquipe);
		Jogo jogo = jogoService.find(Jogo.class, idJogo);

		if (equipe == null) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_EQUIPE_INEXISTENTE);
			return "redirect:/jogo/" + jogo.getId() + "/equipes";
		}
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}

		Usuario usuario = getUsuarioLogado(session);
		if (usuario.equals(jogo.getProfessor())
				&& jogo.getEquipes().contains(equipe)) {
			equipe.setStatus(false);
			equipeService.update(equipe);
			redirectAttributes.addFlashAttribute("info",
					"Equipe inativada com sucesso.");
		} else {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_PERMISSAO_NEGADA);
		}
		return "redirect:/jogo/" + idJogo + "/equipe/" + idEquipe;

	}

	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEquipe}/ativar", method = RequestMethod.GET)
	public String ativarEquipe(@PathVariable("idEquipe") Integer idEquipe,
			@PathVariable("idJogo") Integer idJogo, HttpSession session,
			RedirectAttributes redirectAttributes) {

		Equipe equipe = equipeService.find(Equipe.class, idEquipe);
		Jogo jogo = jogoService.find(Jogo.class, idJogo);

		if (equipe == null) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_EQUIPE_INEXISTENTE);
			return "redirect:/jogo/" + jogo.getId() + "/equipes";
		}
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}

		Usuario usuario = getUsuarioLogado(session);
		if (usuario.equals(jogo.getProfessor())
				&& jogo.getEquipes().contains(equipe)) {
			equipe.setStatus(true);
			equipeService.update(equipe);
			redirectAttributes.addFlashAttribute("info",
					"Equipe ativada com sucesso.");
		} else {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_PERMISSAO_NEGADA);
		}
		return "redirect:/jogo/" + idJogo + "/equipe/" + idEquipe;

	}
	
	@RequestMapping(value = "/jogo/{id}/equipe/{idEquipe}/vincular", method = RequestMethod.GET)
	public String vincularParticipantes(Model model, HttpSession session, @PathVariable("id") Integer id, 
			 @PathVariable("idEquipe") Integer idEquipe, RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, id);
		Equipe equipe = equipeService.find(Equipe.class,idEquipe);
		Usuario usuario = getUsuarioLogado(session);
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		if (equipe == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EQUIPE_INEXISTENTE);
			return "redirect:/jogo/" + id + "/equipes";
		}
		if(usuario.equals(jogo.getProfessor())){
			model.addAttribute("jogo", jogo);
			model.addAttribute("equipe", equipe);
			model.addAttribute("action","vincularEquipe");
			List<Usuario> usuarios = equipeService.alunosSemEquipe(jogo);
			
			if(usuarios == null || usuarios.isEmpty()){
				model.addAttribute("erro", "Todos os usuários já estão cadastrados.");
			}
	
			model.addAttribute("usuarios", usuarios);
			return "equipe/adicionarParticipante";
		}
		redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
		return "redirect:/jogo/" + id + "/equipe/"+equipe.getId();
	}
	
	@RequestMapping(value = "jogo/equipe/participantes/vincular", method = RequestMethod.POST)
	public String vincular(Model model, HttpSession session, @ModelAttribute("equipe") Equipe equipe, 
			RedirectAttributes redirectAttributes, BindingResult result) {
		
		Equipe equipeCompleta = equipeService.find(Equipe.class, equipe.getId());
		if(result.hasErrors()){
			redirectAttributes.addFlashAttribute("erro",
					"Aconceteu algum erro ao associar usuários.");
			return "redirect:/jogo/"+equipeCompleta.getJogo().getId()+"/equipe/"+equipeCompleta.getId()+"/vincular";
		}
		boolean flag = false;
		for (Usuario aluno : equipe.getAlunos()) {
			if(aluno.getId() != null){
				aluno = usuarioService.find(Usuario.class, aluno.getId());
				aluno.addEquipe(equipeCompleta);
				flag = true;
			}
		}
		if(flag){
			equipeService.update(equipeCompleta);
			redirectAttributes.addFlashAttribute("info",
					"Usuários associados à equipe com sucesso!.");
			return "redirect:/jogo/"+equipeCompleta.getJogo().getId()+"/equipe/"+equipeCompleta.getId();
		}else{
			redirectAttributes.addFlashAttribute("erro",
					"Selecione usuários para associar à equipe.");
			return "redirect:/jogo/"+equipeCompleta.getJogo().getId()+"/equipe/"+equipeCompleta.getId()+"/vincular";
		}
		
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEquipe}/avaliacoes", method = RequestMethod.GET)
	public String avaliacoes(@PathVariable("idJogo") Integer idJogo,
			@PathVariable("idEquipe") Integer idEquipe, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {

		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
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
		
		Equipe equipe = equipeService.find(Equipe.class, idEquipe);
		if (equipe == null || !jogo.getEquipes().contains(equipe)) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_EQUIPE_INEXISTENTE);
			return "redirect:/jogo/" + idJogo + "/equipes";
		}

		List<Entrega> entregas = entregaService.getUltimasEntregasDaEquipe(equipe);
		if(entregas.isEmpty() || entregas == null){
			redirectAttributes.addFlashAttribute("erro",
					"Ainda não existem avaliações para esta equipe.");
			return "redirect:/jogo/"+jogo.getId()+"/equipe/"+equipe.getId();
		}
		if (usuario.equals(jogo.getProfessor()) && jogo.getEquipes().contains(equipe)) {
			model.addAttribute("permissao", "professor");
		}else if(equipe.getAlunos().contains(usuario) && jogo.getEquipes().contains(equipe)){
			model.addAttribute("permissao", "membro");
		}else {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_PERMISSAO_NEGADA);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		model.addAttribute("action", "detalhesEquipe");
		model.addAttribute("equipe", equipe);
		model.addAttribute("jogo", jogo);
		model.addAttribute("entregas", entregas);
		return "equipe/avaliacoes";
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
