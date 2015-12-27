package br.ufc.cin.web;

import static br.ufc.cin.util.Constants.MENSAGEM_EQUIPE_CADASTRADA;
import static br.ufc.cin.util.Constants.MENSAGEM_EQUIPE_REMOVIDA;
import static br.ufc.cin.util.Constants.MENSAGEM_ERRO_AO_CADASTRAR_EQUIPE;
import static br.ufc.cin.util.Constants.MENSAGEM_EXCEPTION;
import static br.ufc.cin.util.Constants.PAGINA_ADD_PARTICIPANTES_EQUIPE;
import static br.ufc.cin.util.Constants.PAGINA_AVALIACAO_EQUIPE;
import static br.ufc.cin.util.Constants.PAGINA_AVALIACOES_EQUIPE;
import static br.ufc.cin.util.Constants.PAGINA_CADASTRAR_EQUIPE;
import static br.ufc.cin.util.Constants.PAGINA_DETALHES_EQUIPE;
import static br.ufc.cin.util.Constants.PAGINA_HISTORICO_EQUIPE;
import static br.ufc.cin.util.Constants.REDIRECT_PAGINA_LISTAR_JOGO;
import static br.ufc.cin.util.Constants.USUARIO_LOGADO;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.cin.model.Documento;
import br.ufc.cin.model.Entrega;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Formulario;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.NotaEquipeRodada;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.service.DocumentoService;
import br.ufc.cin.service.EntregaService;
import br.ufc.cin.service.EquipeService;
import br.ufc.cin.service.FormularioService;
import br.ufc.cin.service.JogoService;
import br.ufc.cin.service.NotaEquipeRodadaService;
import br.ufc.cin.service.RegrasService;
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
	private EntregaService entregaService;

	@Inject
	private DocumentoService documentoService;
	
	@Inject
	private RodadaService rodadaService;
	
	@Inject
	private FormularioService formularioService;
	
	@Inject
	private NotaEquipeRodadaService notaEquipeRodadaService;
	
	@Inject
	private RegrasService regrasService;
	
	@RequestMapping(value = "/jogo/{id}/equipe/nova", method = RequestMethod.GET)
	public String cadastrarFrom(@PathVariable("id") Integer id, Model model,
			HttpSession session, RedirectAttributes redirect) {
		try {
			Jogo jogo = jogoService.find(Jogo.class, id);
			Usuario usuario = getUsuarioLogado(session);
			regrasService.verificaJogo(jogo);
			regrasService.verificaSeProfessor(usuario, jogo);
			model.addAttribute("editor", "equipe");
			model.addAttribute("action", "cadastrar");
			model.addAttribute("idJogo", id);
			model.addAttribute("equipe", new Equipe());
			model.addAttribute("participantes", equipeService.alunosSemEquipe(jogo));
		} catch (IllegalArgumentException e) {
			redirect.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e) {
			redirect.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		return PAGINA_CADASTRAR_EQUIPE;
	}

	@RequestMapping(value = "/{idEquipe}/equipe/nova", method = RequestMethod.GET)
	public String cadastrar(@PathVariable("idEquipe") Integer id){
		return "redirect:/jogo/" + id + "/equipe/nova";
	}
	
	@RequestMapping(value = "/{idEquipe}/equipe/nova", method = RequestMethod.POST)
	public String cadastrar(
			@PathVariable("idEquipe") Integer id,
			@Valid Equipe equipe, BindingResult result,
			@RequestParam("anexo") MultipartFile anexo,
			HttpSession session, RedirectAttributes redirect, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("equipe", equipe);
			model.addAttribute("action", "cadastrar");
			model.addAttribute("editor", "equipe");
			model.addAttribute("erro", MENSAGEM_ERRO_AO_CADASTRAR_EQUIPE);
			return PAGINA_CADASTRAR_EQUIPE;
		}
		Jogo jogo = jogoService.find(Jogo.class, id);
		Documento imagem;
		Usuario usuario = getUsuarioLogado(session);
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaSeProfessor(usuario, jogo);
			equipeService.verificaNome(equipe);
			imagem = documentoService.verificaAnexoImagem(anexo, equipe);
			equipe.setLogo(imagem);
			equipe.setJogo(jogo);
			equipe.setSaldo(0F);
			equipe.setStatus(true);
			equipeService.save(equipe);
		} catch (IOException e) {
			redirect.addFlashAttribute("erro", MENSAGEM_ERRO_AO_CADASTRAR_EQUIPE);
			return "redirect:/jogo/" + id + "/equipe/nova";
		} catch (IllegalArgumentException e) {
			redirect.addFlashAttribute("erro", e.getMessage());
			return "redirect:/jogo/" + id + "/equipe/nova";
		} catch (Exception e) {
			redirect.addFlashAttribute("erro", MENSAGEM_ERRO_AO_CADASTRAR_EQUIPE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		redirect.addFlashAttribute("info", MENSAGEM_EQUIPE_CADASTRADA);
		return "redirect:/jogo/" + id + "/equipes";
	}

	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEquipe}", method = RequestMethod.GET)
	public String verDetalhes(@PathVariable("idJogo") Integer idJogo,
			@PathVariable("idEquipe") Integer idEquipe, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {

		Jogo jogo;
		Usuario usuario = getUsuarioLogado(session);
		Equipe equipe;
		String permissao;
		List<Entrega>  entregas;
		try {
			jogo = jogoService.find(Jogo.class, idJogo);
			equipe = equipeService.find(Equipe.class, idEquipe);
			regrasService.verificaJogo(jogo);
			regrasService.verificaParticipacao(usuario, jogo);
			regrasService.verificaEquipe(equipe);
			regrasService.verificaEquipeJogo(equipe, jogo);
			permissao = usuarioService.definePermissao(equipe, usuario);
			model.addAttribute("action", "detalhesEquipe");
			entregas = equipeService.getEntregasOrdenadasPorEquipe(equipe, jogo);
			model.addAttribute("equipe", equipe);
			model.addAttribute("entregas", entregas);
			model.addAttribute("jogo", jogo);
			model.addAttribute("permissao", permissao);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		return PAGINA_DETALHES_EQUIPE;
	}

	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEqui}/editar", method = RequestMethod.GET)
	public String editarEquipe(@PathVariable("idJogo") Integer idJogo,
			@PathVariable("idEqui") Integer idEqui, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Usuario usuario = getUsuarioLogado(session);
		Equipe equipe = equipeService.find(Equipe.class, idEqui);
		String permissao;
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaEquipe(equipe);
			regrasService.verificaEquipeJogo(equipe, jogo);
			regrasService.verificaMembroOuProfessorEquipe(usuario, equipe);
			permissao = usuarioService.definePermissao(equipe, usuario);
			model.addAttribute("jogo", jogo);
			model.addAttribute("equipe", equipe);
			model.addAttribute("editor", "equipe");
			model.addAttribute("action", "editar");
			model.addAttribute("permissao", permissao);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		return PAGINA_CADASTRAR_EQUIPE;
	}

	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEquipe}/editar", method = RequestMethod.POST)
	public String editar(
			@RequestParam(value = "idParticipantes", required = false) List<String> idAlunos,
			@RequestParam("anexo") MultipartFile anexo, @PathVariable("idEquipe") Integer idE, 
			@PathVariable("idJogo") Integer idJ, @Valid Equipe equipe, BindingResult result, HttpSession session,
			RedirectAttributes redirectAttributes, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("equipe", equipe);
			model.addAttribute("editor", "equipe");
			model.addAttribute("action", "editar");
			model.addAttribute("erro", MENSAGEM_ERRO_AO_CADASTRAR_EQUIPE);
			return PAGINA_CADASTRAR_EQUIPE;
		}
		Jogo jogo = jogoService.find(Jogo.class, idJ);
		Equipe oldEquipe = equipeService.find(Equipe.class, equipe.getId());
		Usuario usuario = getUsuarioLogado(session);
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaEquipe(oldEquipe);
			equipeService.verificaNome(equipe);
			regrasService.verificaEquipeJogo(oldEquipe, jogo);
			regrasService.verificaMembroOuProfessorEquipe(usuario, oldEquipe);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Documento imagem;
		try {
			imagem = documentoService.verificaAnexoImagem(anexo, equipe);
			if(imagem != null){
				if(oldEquipe.getLogo() != null){
					imagem.setId(oldEquipe.getLogo().getId());
				}
				oldEquipe.setLogo(imagem);
			}
			if(oldEquipe.getSaldo()==null){
				oldEquipe.setSaldo(0f);
			}
			oldEquipe.setNome(equipe.getNome());
			oldEquipe.setIdeiaDeNegocio(equipe.getIdeiaDeNegocio());
			equipeService.update(oldEquipe);
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return "redirect:/jogo/" + idJ + "/equipe/"+oldEquipe.getId()+"/editar";
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return "redirect:/jogo/" + idJ + "/equipe/"+oldEquipe.getId()+"/editar";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", "Aconteceu algum erro ao atualizar a equipe.");
			return "redirect:/jogo/" + idJ + "/equipe/"+oldEquipe.getId()+"/editar";
		}
		
		redirectAttributes.addFlashAttribute("info", "Equipe atualizada com sucesso.");
		return "redirect:/jogo/" + idJ + "/equipe/"+equipe.getId();
	}

	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEquipe}/excluir", method = RequestMethod.GET)
	public String excluir(@PathVariable("idJogo") Integer idJogo,
			@PathVariable("idEquipe") Integer idEquipe, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Equipe equipe = equipeService.find(Equipe.class, idEquipe);
		Usuario usuario = getUsuarioLogado(session);
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaEquipe(equipe);
			regrasService.verificaEquipeJogo(equipe, jogo);
			regrasService.verificaSeProfessor(usuario, jogo);
			equipeService.removeEquipe(jogo, equipe);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return "redirect:/jogo/" + idJogo + "/equipes";
		}
		redirectAttributes.addFlashAttribute("info",
				MENSAGEM_EQUIPE_REMOVIDA);
		return "redirect:/jogo/" + idJogo + "/equipes";
	}

	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEquipe}/usuario/{idUsuario}/desvincular", method = RequestMethod.GET)
	public String desvincularUsuario(
			@PathVariable("idUsuario") Integer idUsuario,
			@PathVariable("idEquipe") Integer idEquipe,
			@PathVariable("idJogo") Integer idJogo, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Equipe equipe = equipeService.find(Equipe.class, idEquipe);
		Usuario usuario = getUsuarioLogado(session);
		Usuario user = usuarioService.find(Usuario.class, idUsuario);
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaEquipe(equipe);
			regrasService.verificaEquipeJogo(equipe, jogo);
			regrasService.verificaSeProfessor(usuario, jogo);
			usuarioService.verificaUsuario(user);
			equipe.getAlunos().remove(user);
			equipeService.update(equipe);
			user.getEquipes().remove(equipe);
			usuarioService.update(user);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return "redirect:/jogo/" + idJogo + "/equipes";
		}
		redirectAttributes.addFlashAttribute("info",
				"Participante \""+user.getNome()+"\" desvinculado com sucesso.");
		return "redirect:/jogo/" + idJogo + "/equipe/" + idEquipe;
	}

	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEquipe}/inativar", method = RequestMethod.GET)
	public String inativarEquipe(@PathVariable("idEquipe") Integer idEquipe,
			@PathVariable("idJogo") Integer idJogo, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Equipe equipe = equipeService.find(Equipe.class, idEquipe);
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Usuario usuario = getUsuarioLogado(session);
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaEquipe(equipe);
			regrasService.verificaEquipeJogo(equipe, jogo);
			regrasService.verificaSeProfessor(usuario, jogo);
			equipe.setStatus(false);
			equipeService.update(equipe);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return "redirect:/jogo/" + idJogo + "/equipes";
		}
		redirectAttributes.addFlashAttribute("info", "Equipe inativada com sucesso.");
		return "redirect:/jogo/" + idJogo + "/equipe/" + idEquipe;

	}

	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEquipe}/ativar", method = RequestMethod.GET)
	public String ativarEquipe(@PathVariable("idEquipe") Integer idEquipe,
			@PathVariable("idJogo") Integer idJogo, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Equipe equipe = equipeService.find(Equipe.class, idEquipe);
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Usuario usuario = getUsuarioLogado(session);
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaEquipe(equipe);
			regrasService.verificaEquipeJogo(equipe, jogo);
			regrasService.verificaSeProfessor(usuario, jogo);
			equipe.setStatus(true);
			equipeService.update(equipe);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return "redirect:/jogo/" + idJogo + "/equipes";
		}
		redirectAttributes.addFlashAttribute("info", "Equipe ativada com sucesso.");
		return "redirect:/jogo/" + idJogo + "/equipe/" + idEquipe;

	}
	
	@RequestMapping(value = "/jogo/{id}/equipe/{idEquipe}/vincular", method = RequestMethod.GET)
	public String vincularParticipantes(Model model, HttpSession session, @PathVariable("id") Integer id, 
			 @PathVariable("idEquipe") Integer idEquipe, RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, id);
		Equipe equipe = equipeService.find(Equipe.class,idEquipe);
		Usuario usuario = getUsuarioLogado(session);
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaEquipe(equipe);
			regrasService.verificaEquipeJogo(equipe, jogo);
			regrasService.verificaSeProfessor(usuario, jogo);
			model.addAttribute("jogo", jogo);
			model.addAttribute("equipe", equipe);
			model.addAttribute("action","vincularEquipe");
			List<Usuario> usuarios = equipeService.alunosSemEquipe(jogo);
			if(usuarios == null || usuarios.isEmpty()){
				model.addAttribute("erro", "Todos os usuários já estão cadastrados.");
			}
			model.addAttribute("permissao", "professor");
			model.addAttribute("usuarios", usuarios);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return "redirect:/jogo/" + id + "/equipes";
		}
		return PAGINA_ADD_PARTICIPANTES_EQUIPE;
	}
	
	@RequestMapping(value = "jogo/equipe/participantes/vincular", method = RequestMethod.GET)
	public String vincular(){
		return REDIRECT_PAGINA_LISTAR_JOGO;
	}
	
	@RequestMapping(value = "jogo/equipe/participantes/vincular", method = RequestMethod.POST)
	public String vincular(Model model, HttpSession session, @ModelAttribute("equipe") Equipe equipe, 
			RedirectAttributes redirectAttributes, BindingResult result) {
		
		Equipe equipeCompleta = equipeService.find(Equipe.class, equipe.getId());
		if(result.hasErrors()){
			redirectAttributes.addFlashAttribute("erro",
					"Aconceteu algo inesperado ao associar participantes. Tente novamente.");
			return "redirect:/jogo/"+equipeCompleta.getJogo().getId()+"/equipe/"+equipeCompleta.getId()+"/vincular";
		}
		Usuario usuario = getUsuarioLogado(session);
		try {
			regrasService.verificaEquipe(equipeCompleta);
			regrasService.verificaSeProfessor(usuario, equipeCompleta.getJogo());
			equipeService.vincularParticipantes(equipeCompleta, equipe.getAlunos() );
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (IllegalStateException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return "redirect:/jogo/"+equipeCompleta.getJogo().getId()+"/equipe/"+equipeCompleta.getId()+"/vincular";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		redirectAttributes.addFlashAttribute("info", "Usuários associados à empresa com sucesso!");
		return "redirect:/jogo/"+equipeCompleta.getJogo().getId()+"/equipe/"+equipeCompleta.getId();
		
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEquipe}/avaliacoes", method = RequestMethod.GET)
	public String avaliacoes(@PathVariable("idJogo") Integer idJogo,
			@PathVariable("idEquipe") Integer idEquipe, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {

		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Usuario usuario = getUsuarioLogado(session);
		usuario = usuarioService.find(Usuario.class, usuario.getId());
		Equipe equipe = equipeService.find(Equipe.class, idEquipe);
		String permissao;
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaEquipe(equipe);
			regrasService.verificaEquipeJogo(equipe, jogo);
			regrasService.verificaParticipacao(usuario, jogo);
			permissao = usuarioService.definePermissao(equipe, usuario);
			List<Entrega> entregas = entregaService.getUltimasEntregasDaEquipeComGabarito(equipe);
			if(entregas.isEmpty() || entregas == null){
				redirectAttributes.addFlashAttribute("erro",
						"Ainda não existem avaliações efetuadas pelo docente para esta equipe.");
				return "redirect:/jogo/"+jogo.getId()+"/equipe/"+equipe.getId();
			}
			model.addAttribute("permissao", permissao);			
			model.addAttribute("action", "avaliacoes");
			model.addAttribute("equipe", equipe);
			model.addAttribute("jogo", jogo);
			model.addAttribute("entregas", entregas);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		
		return PAGINA_AVALIACOES_EQUIPE;
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEquipe}/entrega/{id}/formulario/{idForm}/avaliacao", method = RequestMethod.GET)
	public String avaliacao(@PathVariable("idJogo") Integer idJogo, 
			@PathVariable("idForm") Integer idForm,
			@PathVariable("id") Integer id, Model model, HttpSession session, 
			@PathVariable("idEquipe") Integer idEquipe,
			RedirectAttributes redirectAttributes) {

		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Equipe equipe = equipeService.find(Equipe.class, idEquipe);
		Formulario formulario = formularioService.find(Formulario.class, idForm);
		Entrega entrega = entregaService.find(Entrega.class, id);
		Usuario usuario = getUsuarioLogado(session);
		usuario = usuarioService.find(Usuario.class, usuario.getId());
		String permissao;
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaParticipacao(usuario, jogo);
			regrasService.verificaEquipe(equipe);
			regrasService.verificaFormulario(formulario);
			regrasService.verificaEntrega(entrega);
			permissao = usuarioService.definePermissao(equipe, usuario);
			if(!entrega.getRodada().isStatusRaking()){
				redirectAttributes.addFlashAttribute("erro", "Ainda não está disponível o gabarito.");
				return "redirect:/jogo/"+ idJogo +"/equipe/"+idEquipe+"/avaliacoes";
			}
			model.addAttribute("permissao", permissao);
			model.addAttribute("equipe", equipe);
			model.addAttribute("action", "avaliacao");
			model.addAttribute("formulario", formulario);
			model.addAttribute("jogo", jogo);
			model.addAttribute("entrega", entrega);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		return PAGINA_AVALIACAO_EQUIPE;
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEquipe}/historico", method = RequestMethod.GET)
	public String historicoEquipe(@PathVariable("idJogo") Integer idJogo, @PathVariable("idEquipe") Integer idEquipe, 
			Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Equipe equipe = equipeService.find(Equipe.class, idEquipe);
		Usuario usuario = getUsuarioLogado(session);
		usuario = usuarioService.find(Usuario.class, usuario.getId());
		String permissao = "";
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaRodadaInJogo(jogo);
			regrasService.verificaEquipe(equipe);
			regrasService.verificaEquipeJogo(equipe,jogo);
			regrasService.verificaParticipacao(usuario, jogo);
			permissao = usuarioService.definePermissao(jogo, usuario);
			List<Rodada> rodadas = rodadaService.ordenaPorInicio(jogo.getRodadas());
		    rodadaService.atualizaStatusRodadas(rodadas);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		List<NotaEquipeRodada> notasEquipeRodadas = notaEquipeRodadaService.buscarPorEquipe(equipe);
		if(notasEquipeRodadas == null){
			notasEquipeRodadas = equipeService.criarNotasEquipeRodadas(notasEquipeRodadas, equipe, permissao);
		}
		if(notasEquipeRodadas != null){
			notasEquipeRodadas = equipeService.atualizarNotasEquipeRodadas(notasEquipeRodadas, equipe, permissao);
		}
		model.addAttribute("action", "historicoEquipe");
		model.addAttribute("permissao", permissao);
		model.addAttribute("usuario", usuario);
		model.addAttribute("notasEquipeRodadas", notasEquipeRodadas);
		model.addAttribute("jogo", jogo);
		model.addAttribute("equipe", equipe);
		return PAGINA_HISTORICO_EQUIPE;
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEquipe}/fator/{idFator}", method = RequestMethod.GET)
	public String atualizarFator(@RequestParam("fatorDeAposta") String fatorString,
			@PathVariable("idEquipe") Integer idEquipe,
			@PathVariable("idJogo") Integer idJogo,
			@PathVariable("idFator") Integer idFator){
		return "redirect:/jogo/"+idJogo+"/equipe/"+idEquipe+"/historico";
	}

	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEquipe}/fator/{idFator}", method = RequestMethod.POST)
	public String atualizarFator(@RequestParam("fatorDeAposta") String fatorString,
			@PathVariable("idEquipe") Integer idEquipe,
			@PathVariable("idJogo") Integer idJogo,
			@PathVariable("idFator") Integer idFator,
			HttpSession session, RedirectAttributes redirectAttributes) {

		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Equipe equipe = equipeService.find(Equipe.class, idEquipe);
		NotaEquipeRodada notaEquipeRodada = notaEquipeRodadaService.find(NotaEquipeRodada.class, idFator);
		Float fator;
		Usuario usuario = getUsuarioLogado(session);
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaEquipe(equipe);
			regrasService.verificaEquipeJogo(equipe, jogo);
			regrasService.verificaSeProfessor(usuario, jogo);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		try {
			regrasService.verificaNoraEquipeRodada(notaEquipeRodada);
			fator = converteFator(fatorString);
			notaEquipeRodada.setFatorDeAposta(fator);
			notaEquipeRodadaService.update(notaEquipeRodada);	
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return "redirect:/jogo/"+idJogo+"/equipe/"+idEquipe+"/historico";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro","Erro ao atualizar fator de aposta da equipe.");
			return "redirect:/jogo/"+idJogo+"/equipe/"+idEquipe+"/historico";
		}
		redirectAttributes.addFlashAttribute("info","Fator atualizado com sucesso.");
		return "redirect:/jogo/"+jogo.getId()+"/equipe/"+equipe.getId()+"/historico";
	}
	
	private Float converteFator(String fatorString){
		fatorString = fatorString.replace(',', '.');
		return Float.parseFloat(fatorString);
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
