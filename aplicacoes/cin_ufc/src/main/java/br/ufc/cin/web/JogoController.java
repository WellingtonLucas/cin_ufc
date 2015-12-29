package br.ufc.cin.web;


import static br.ufc.cin.util.Constants.MENSAGEM_EQUIPES_NAO_CRIADAS;
import static br.ufc.cin.util.Constants.MENSAGEM_ERRO_AO_CADASTRAR_JOGO;
import static br.ufc.cin.util.Constants.MENSAGEM_ERRO_UPLOAD;
import static br.ufc.cin.util.Constants.MENSAGEM_EXCEPTION;
import static br.ufc.cin.util.Constants.MENSAGEM_JOGO_ATUALIZADO;
import static br.ufc.cin.util.Constants.MENSAGEM_JOGO_CADASTRADO;
import static br.ufc.cin.util.Constants.MENSAGEM_JOGO_INEXISTENTE;
import static br.ufc.cin.util.Constants.MENSAGEM_JOGO_REMOVIDO;
import static br.ufc.cin.util.Constants.MENSAGEM_PERMISSAO_NEGADA;
import static br.ufc.cin.util.Constants.MENSAGEM_USUARIOS_NAO_ASSOCIADOS;
import static br.ufc.cin.util.Constants.PAGINA_ADD_PARTICIPANES_JOGO;
import static br.ufc.cin.util.Constants.PAGINA_CADASTRAR_JOGO;
import static br.ufc.cin.util.Constants.PAGINA_DETALHES_JOGO;
import static br.ufc.cin.util.Constants.PAGINA_LISTAR_EQUIPES;
import static br.ufc.cin.util.Constants.PAGINA_LISTAR_JOGO;
import static br.ufc.cin.util.Constants.PAGINA_LISTAR_USUARIOS;
import static br.ufc.cin.util.Constants.REDIRECT_PAGINA_LISTAR_JOGO;
import static br.ufc.cin.util.Constants.REDIRECT_PAGINA_NOVO_JOGO;
import static br.ufc.cin.util.Constants.USUARIO_LOGADO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.cin.model.Documento;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.service.DocumentoService;
import br.ufc.cin.service.EquipeService;
import br.ufc.cin.service.JogoService;
import br.ufc.cin.service.RegrasService;
import br.ufc.cin.service.UsuarioService;


@Controller
@RequestMapping("jogo")
public class JogoController {

	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private JogoService jogoService;
	
	@Autowired
	private DocumentoService documentoService;

	@Autowired
	private EquipeService equipeService;
	
	@Autowired
	private RegrasService regrasService;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return REDIRECT_PAGINA_LISTAR_JOGO;
	}

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(Model model, HttpSession session) {
		Usuario usuario = getUsuarioLogado(session);
		usuario = usuarioService.find(Usuario.class, usuario.getId());
		List<Jogo> jogos = jogoService.getJogoByProfessor(usuario.getId());
		if(jogos == null){
			model.addAttribute("info", "Você ainda não criou jogos.");
		}
		List<Jogo> jogosParticipa = new ArrayList<Jogo>();
		boolean flag = false;
		if(usuario.getJogoParticipa().isEmpty() || usuario.getJogoParticipa() == null){
			model.addAttribute("infoParticipa", "Você ainda não está participando de um jogo.");
		}else{
			for (Jogo jogo : usuario.getJogoParticipa()) {
				if (jogo.isStatus()) {
					jogosParticipa.add(jogo);
					flag = true;
				}
			}
			if(!flag){
				model.addAttribute("infoParticipa", "Nenhum jogo em que você é participante está ativo no momento.");
			}
		}
		model.addAttribute("jogos", jogos);
		model.addAttribute("jogosParticipa", jogosParticipa);
		model.addAttribute("usuario", usuario);
		model.addAttribute("action", "home");
		
		return PAGINA_LISTAR_JOGO;
	}
	
	@RequestMapping(value ="/novo-jogo", method = RequestMethod.GET)
	public String cadastrarFrom(Model model, HttpSession session){
		model.addAttribute("action", "cadastrar");
		model.addAttribute("jogo", new Jogo());	
		model.addAttribute("editor", "jogo");
		return PAGINA_CADASTRAR_JOGO;
	}
	
	@RequestMapping(value = "/novo-jogo", method = RequestMethod.POST)
	public String cadastrar(@Valid Jogo jogo, BindingResult result, @RequestParam("anexos") List<MultipartFile> anexos, 
			@RequestParam("logo") MultipartFile logo, HttpSession session, RedirectAttributes redirect, Model model){
		if (result.hasErrors()) {
			model.addAttribute("erro", MENSAGEM_ERRO_AO_CADASTRAR_JOGO);
			model.addAttribute("action", "cadastrar");
			model.addAttribute("jogo", jogo);
			model.addAttribute("editor", "jogo");
			return PAGINA_CADASTRAR_JOGO;
		}
		Usuario usuario = getUsuarioLogado(session);
		if(jogo.getDescricao() == null || jogo.getDescricao().isEmpty()){
			model.addAttribute("erro", "A descrição do jogo é obrigatória!");
			model.addAttribute("error_descricao", "A descrição do jogo é obrigatória!");
			model.addAttribute("action", "cadastrar");
			model.addAttribute("jogo", jogo);	
			model.addAttribute("editor", "jogo");
			model.addAttribute("usuario", usuario);
			return PAGINA_CADASTRAR_JOGO;
		}
		Documento imagem;
		try {
			imagem = documentoService.verificaAnexoImagem(logo, jogo);
			documentoService.verificaArquivos(anexos);
			jogoService.verificaDatas(jogo);
			jogoService.verificaNomeSemestre(jogo);
			jogo.setImagem(imagem);
			jogo.setProfessor(usuarioService.find(Usuario.class, usuario.getId()));
			jogoService.save(jogo);
		} catch (IllegalArgumentException e) {
			model.addAttribute("erro", e.getMessage());
			model.addAttribute("action", "cadastrar");
			model.addAttribute("jogo", jogo);	
			model.addAttribute("editor", "jogo");
			model.addAttribute("usuario", usuario);
			return PAGINA_CADASTRAR_JOGO;
		} catch (Exception e) {
			redirect.addFlashAttribute("erro", "Erro ao tentar salvar o jogo.");
			return REDIRECT_PAGINA_NOVO_JOGO;
		}
		List<Documento> documentos = new ArrayList<Documento>();
		try {
			documentos = documentoService.criaDocumentos(anexos,jogo);
			if( documentos != null && !documentos.isEmpty()){
				documentoService.salvar(documentos);
			}
		} catch (IOException e1) {
			redirect.addFlashAttribute("erro", MENSAGEM_ERRO_UPLOAD);
			return REDIRECT_PAGINA_NOVO_JOGO;
		} catch (Exception e) {
			redirect.addFlashAttribute("erro", MENSAGEM_ERRO_UPLOAD);
			return REDIRECT_PAGINA_NOVO_JOGO;
		}
		
		redirect.addFlashAttribute("info", MENSAGEM_JOGO_CADASTRADO);
		return "redirect:/jogo/"+jogo.getId()+"/detalhes";
	}
	
	@RequestMapping(value = "/{id}/editar", method = RequestMethod.GET)
	public String editarForm(@PathVariable("id") Integer id, Model model, HttpSession session, 
			RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, id);
		Usuario usuario = getUsuarioLogado(session);
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaSeProfessor(usuario, jogo);
		}catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		model.addAttribute("editor", "jogo");		
		model.addAttribute("jogo", jogo);
	 	model.addAttribute("action", "editar");
		return PAGINA_CADASTRAR_JOGO;
	}
	
	@RequestMapping(value = "/{id}/editar", method = RequestMethod.POST)
	public String editar(@PathVariable("id") Integer id, @RequestParam("anexos") List<MultipartFile> anexos, @RequestParam("logo") MultipartFile imagem, 
			@Valid Jogo jogo, BindingResult result, HttpSession session, RedirectAttributes redirect, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("editor", "jogo");
			model.addAttribute("erro", MENSAGEM_ERRO_AO_CADASTRAR_JOGO);
			model.addAttribute("action", "cadastrar");
			model.addAttribute("jogo", jogo);
			model.addAttribute("editor", "jogo");
			return PAGINA_CADASTRAR_JOGO;
		}
		if(jogo.getDescricao() == null || jogo.getDescricao().isEmpty()){
			redirect.addFlashAttribute("erro", "A descrição do jogo é obrigatória!");
			redirect.addFlashAttribute("error_descricao", "A descrição do jogo é obrigatória!");
			return "redirect:/jogo/"+jogo.getId()+"/editar";
		}
		Usuario usuario = getUsuarioLogado(session);
		Jogo oldJogo = jogoService.find(Jogo.class, jogo.getId());
		List<Documento> documentos = new ArrayList<Documento>();
		try {
			jogoService.verificaDatas(jogo);
			jogoService.verificaNomeSemestre(jogo);
			regrasService.verificaSeProfessor(usuario, oldJogo);
			documentoService.verificaArquivos(anexos);
			documentos = documentoService.criaDocumentos(anexos, oldJogo);
			Documento logo = documentoService.verificaAnexoImagem(imagem, oldJogo);
			if(logo != null){
				if(oldJogo.getImagem() != null){
					logo.setId(oldJogo.getImagem().getId());
				}
				oldJogo.setImagem(logo);
			}
			if(documentos != null && !documentos.isEmpty()) {
				documentoService.salvar(documentos);
			}
		} catch (IOException e1) {
			redirect.addFlashAttribute("erro", MENSAGEM_ERRO_AO_CADASTRAR_JOGO);
			return "redirect:/jogo/"+jogo.getId()+"/editar";
		} catch (IllegalArgumentException e) {
			redirect.addFlashAttribute("erro", e.getMessage());
			return "redirect:/jogo/"+jogo.getId()+"/editar";
		} catch (Exception e) {
			redirect.addFlashAttribute("erro", MENSAGEM_ERRO_AO_CADASTRAR_JOGO);
			return "redirect:/jogo/"+jogo.getId()+"/editar";
		}
	
		oldJogo.setDescricao(jogo.getDescricao());
		oldJogo.setRegras(jogo.getRegras());
		oldJogo.setNomeDoCurso(jogo.getNomeDoCurso());
		oldJogo.setSemestre(jogo.getSemestre());
		oldJogo.setInicio(jogo.getInicio());
		oldJogo.setTermino(jogo.getTermino());
		try{
			jogoService.update(oldJogo);
		}catch(Exception e){
			redirect.addFlashAttribute("erro", "Erro ao atualizar os dados do jogo.");
			return "redirect:/jogo/"+jogo.getId()+"/editar";
		}
		redirect.addFlashAttribute("info", MENSAGEM_JOGO_ATUALIZADO);
		return "redirect:/jogo/"+jogo.getId()+"/detalhes";
	}
	
	@RequestMapping(value = "/{id}/detalhes", method = RequestMethod.GET)
	public String verDetalhes(@PathVariable("id") Integer id, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		
		Jogo jogo = jogoService.find(Jogo.class, id);
		Usuario usuario = getUsuarioLogado(session);
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaParticipacao(usuario, jogo);
			String permissao = usuarioService.definePermissao(jogo, usuario);
			model.addAttribute("permissao", permissao);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", "Algo inesperado aconteceu, tente novamente.");
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		model.addAttribute("action", "detalhesJogo");
		model.addAttribute("jogo", jogo);
		return PAGINA_DETALHES_JOGO;
	}
	
	@RequestMapping(value = "/{id}/excluir", method = RequestMethod.GET)
	public String excluir(@PathVariable("id") Integer id, HttpSession session, RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class,id);
		Usuario usuario = getUsuarioLogado(session);
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaSeProfessor(usuario, jogo);
			jogoService.delete(jogo);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return  "redirect:/jogo/"+jogo.getId()+"/detalhes";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		
		redirectAttributes.addFlashAttribute("info", MENSAGEM_JOGO_REMOVIDO);
		return REDIRECT_PAGINA_LISTAR_JOGO;
	}
	
	@RequestMapping(value = "/{id}/participantes", method = RequestMethod.GET)
	public String listarParticipantes(Model model, HttpSession session, @PathVariable("id") Integer id,
			RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class,id);
		Usuario usuario = getUsuarioLogado(session);
		String permissao;
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaSeProfessor(usuario, jogo);
			permissao = usuarioService.definePermissao(jogo, usuario);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		
		if(jogo.getAlunos() == null || jogo.getAlunos().isEmpty()){
			model.addAttribute("erro", MENSAGEM_USUARIOS_NAO_ASSOCIADOS);
		}
		model.addAttribute("jogo", jogo);
		model.addAttribute("action","participantesJogo");
		model.addAttribute("permissao", permissao);
		model.addAttribute("usuarios", jogo.getAlunos());
		return PAGINA_LISTAR_USUARIOS;
	}
	
	@RequestMapping(value = "/{id}/equipes", method = RequestMethod.GET)
	public String listarEquipes(Model model, HttpSession session, @PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class,id);
		Usuario usuario = getUsuarioLogado(session);
		String permissao;
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaParticipacao(usuario, jogo);
			permissao = usuarioService.definePermissao(jogo, usuario);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		List<Equipe> equipes = jogo.getEquipes();
		if(equipes == null || equipes.isEmpty()){
			model.addAttribute("erro", MENSAGEM_EQUIPES_NAO_CRIADAS);
		}
		model.addAttribute("permissao",permissao);
		model.addAttribute("jogo", jogo);
		model.addAttribute("action","equipes");
		model.addAttribute("equipes", equipes);
		
		return PAGINA_LISTAR_EQUIPES;
	}
	
		
	@RequestMapping(value = "/{id}/vincular", method = RequestMethod.GET)
	public String vincularParticipantes(Model model, HttpSession session, @PathVariable("id") Integer id, 
			RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class,id);
		Usuario usuario = getUsuarioLogado(session);
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaSeProfessor(usuario, jogo);
		} catch (IllegalArgumentException e){
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		List<Usuario> usuarios = usuarioService.getPossiveisParticipantes(getUsuarioLogado(session), jogo);
		
		if(usuarios == null || usuarios.isEmpty()){
			model.addAttribute("erro", "Todos os usuários já estão cadastrados.");
		}
		model.addAttribute("permissao","professor");
		model.addAttribute("jogo", jogo);
		model.addAttribute("action","vincularAoJogo");
		model.addAttribute("usuarios", usuarios);
		return PAGINA_ADD_PARTICIPANES_JOGO;
	}

	@RequestMapping(value = "/participantes/vincular", method = RequestMethod.GET)
	public String vincularParticipantes(){
		return "redirect:/jogo/listar";
	}
	
	@RequestMapping(value = "/participantes/vincular", method = RequestMethod.POST)
	public String vincular(Model model, HttpSession session, @ModelAttribute("jogo") Jogo jogo, 
			RedirectAttributes redirectAttributes, BindingResult result) {
		Jogo jogoCompleto = jogoService.find(Jogo.class, jogo.getId());
		if(result.hasErrors()){
			redirectAttributes.addFlashAttribute("erro",
					"Conceteu algum erro ao associar usuários.");
			return "redirect:/jogo/"+jogo.getId()+"/vincular";
		}
		List<Usuario> alunos = new ArrayList<Usuario>();
		Usuario usuario = getUsuarioLogado(session);
		try {
			regrasService.verificaJogo(jogoCompleto);
			regrasService.verificaSeProfessor(usuario, jogoCompleto);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro",e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		for (Usuario aluno : jogo.getAlunos()) {
			if(aluno.getId() != null){
				aluno = usuarioService.find(Usuario.class, aluno.getId());
				aluno.addJogoParticipa(jogoCompleto);
				alunos.add(aluno);
			}
		}
		if(!alunos.isEmpty()){
			jogoService.update(jogoCompleto);
			redirectAttributes.addFlashAttribute("info",
					"Usuários associados ao jogo com sucesso!");
			return "redirect:/jogo/"+jogo.getId()+"/participantes";
		}else{
			redirectAttributes.addFlashAttribute("erro",
					"Selecione usuários para associar ao jogo.");
			return "redirect:/jogo/"+jogo.getId()+"/vincular";
		}
		
	}
	
	@RequestMapping(value = "/{idJogo}/participantes/{idUsuario}/desvincular", method = RequestMethod.GET)
	public String desvincularUsuario(@PathVariable("idUsuario") Integer idUsuario,			
			@PathVariable("idJogo") Integer idJogo, HttpSession session,
			RedirectAttributes redirectAttributes, Model model) {
		Usuario user = usuarioService.find(Usuario.class, idUsuario);
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		if (user == null) {
			redirectAttributes.addFlashAttribute("erro",
					"Participante inexistente");
			return "redirect:/jogo/" + jogo.getId() + "/participantes";
		}
		Usuario usuario = usuarioService.find(Usuario.class, getUsuarioLogado(session).getId());
		Equipe equipe = equipeService.equipePorAlunoNoJogo(usuario, jogo);
		if (usuario.equals(jogo.getProfessor())) {
			user.getJogoParticipa().remove(jogo);
			if(equipe != null){
				equipe.getAlunos().remove(user);
				equipeService.update(equipe);
				user.getEquipes().remove(equipe);
			}
			jogo.getAlunos().remove(user);
			jogoService.update(jogo);
			usuarioService.update(user);
			redirectAttributes.addFlashAttribute("info",
					"Participante desvinculado com sucesso.");
		} else {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_PERMISSAO_NEGADA);
		}
		return "redirect:/jogo/" + idJogo + "/participantes";
	}

	@RequestMapping(value = "/{idJogo}/ativar", method = RequestMethod.GET)
	public String ativarJogo(@PathVariable("idJogo") Integer idJogo, HttpSession session,
			RedirectAttributes redirectAttributes) {

		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Usuario usuario = getUsuarioLogado(session);
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaSeProfessor(usuario, jogo);
			jogo.setStatus(true);
			jogoService.update(jogo);
			redirectAttributes.addFlashAttribute("info", "Jogo ativado com sucesso.");
		} catch (IllegalArgumentException e){
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		return "redirect:/jogo/" + idJogo + "/detalhes";
	}

	@RequestMapping(value = "/{idJogo}/inativar", method = RequestMethod.GET)
	public String inativarJogo(@PathVariable("idJogo") Integer idJogo, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Usuario usuario = getUsuarioLogado(session);
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaSeProfessor(usuario, jogo);
			jogo.setStatus(false);
			jogoService.update(jogo);
			redirectAttributes.addFlashAttribute("info", "Jogo inativado com sucesso.");
		} catch (IllegalArgumentException e){
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		return "redirect:/jogo/" + idJogo + "/detalhes";
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
