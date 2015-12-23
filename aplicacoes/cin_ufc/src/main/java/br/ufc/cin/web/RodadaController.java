package br.ufc.cin.web;

import static br.ufc.cin.util.Constants.*;
import static br.ufc.cin.util.Constants.MENSAGEM_EXCEPTION;
import static br.ufc.cin.util.Constants.PAGINA_APOSTAS_RODADA;
import static br.ufc.cin.util.Constants.PAGINA_APOSTA_RODADA;
import static br.ufc.cin.util.Constants.PAGINA_DETALHES_FORM;
import static br.ufc.cin.util.Constants.PAGINA_DETALHES_RODADA;
import static br.ufc.cin.util.Constants.PAGINA_LISTAR_RODADAS;
import static br.ufc.cin.util.Constants.PAGINA_NOVA_RODADA;
import static br.ufc.cin.util.Constants.PAGINA_SUBMISSOES_RODADA;
import static br.ufc.cin.util.Constants.REDIRECT_PAGINA_LISTAR_JOGO;
import static br.ufc.cin.util.Constants.USUARIO_LOGADO;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.cin.model.Aposta;
import br.ufc.cin.model.Deposito;
import br.ufc.cin.model.Documento;
import br.ufc.cin.model.Entrega;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.ReaberturaSubmissao;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.StatusRodadaEquipe;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.service.ApostaService;
import br.ufc.cin.service.DocumentoService;
import br.ufc.cin.service.EntregaService;
import br.ufc.cin.service.EquipeService;
import br.ufc.cin.service.JogoService;
import br.ufc.cin.service.ReaberturaSubmissaoService;
import br.ufc.cin.service.RegrasService;
import br.ufc.cin.service.RodadaEquipeService;
import br.ufc.cin.service.RodadaService;
import br.ufc.cin.service.UsuarioService;

@Controller
public class RodadaController {
	
	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private JogoService jogoService;
	
	@Inject
	private RodadaService rodadaService;

	@Inject
	private EquipeService equipeService;
	
	@Inject
	private EntregaService entregaService;
	
	@Inject
	private DocumentoService documentoService;
	
	@Inject
	private RodadaEquipeService rodadaEquipeService;
	
	@Inject
	private ReaberturaSubmissaoService reaberturaSubmissaoService;
	
	@Inject
	private ApostaService apostaService;
	
	@Inject
	private RegrasService regrasService;
	
	@RequestMapping(value ="/jogo/{id}/rodadas", method = RequestMethod.GET)
	public String rodadas(@PathVariable("id") Integer id, Model model, HttpSession session,
			RedirectAttributes redirectAttributes){
		Jogo jogo = jogoService.find(Jogo.class, id);
		Usuario usuario = getUsuarioLogado(session);
		String permissao;
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaParticipacao(usuario, jogo);
			permissao = usuarioService.definePermissao(jogo, usuario);
			model.addAttribute("permissao", permissao);
			if((jogo.getRodadas() == null) || (jogo.getRodadas().isEmpty())){
				model.addAttribute("info", "Nenhuma rodada cadastrada no momento.");
			}else{
				List<Rodada> rodadas = rodadaService.ordenaPorInicio(jogo.getRodadas());
				model.addAttribute("rodadas", rodadas);
			}
			model.addAttribute("action", "rodadas");
			model.addAttribute("jogo", jogo);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		return PAGINA_LISTAR_RODADAS;
	}

	@RequestMapping(value ="/jogo/{id}/rodada/nova", method = RequestMethod.GET)
	public String novaRodada(@PathVariable("id") Integer id, Model model, HttpSession session,
			RedirectAttributes redirectAttributes){
		Usuario usuario = getUsuarioLogado(session);
		Jogo jogo = jogoService.find(Jogo.class, id);
		usuario = usuarioService.find(Usuario.class, usuario.getId());
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaSeProfessor(usuario, jogo);
			model.addAttribute("action", "cadastrar");
			model.addAttribute("editor", "rodada");
			model.addAttribute("jogo", jogo);
			model.addAttribute("formularios", usuario.getFormulario());
			model.addAttribute("rodada", new Rodada());
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		return PAGINA_NOVA_RODADA;
	}
	
	@RequestMapping(value = "/jogo/{id}/nova-rodada", method = RequestMethod.POST)
	public  String cadastrar(@PathVariable("id") Integer id, @RequestParam("tudo") String allIn, 
			@ModelAttribute("rodada") Rodada rodada, BindingResult result, 
			HttpSession session, RedirectAttributes redirectAttributes){
		if(result.hasErrors()){
			redirectAttributes.addFlashAttribute("erro", "Erro ao tentar salvar uma nova rodada.");
			return "redirect:/jogo/"+id+"/rodada/nova";
		}
		Jogo jogo = jogoService.find(Jogo.class, id);
		Usuario usuario = getUsuarioLogado(session);
		try{
			regrasService.verificaJogo(jogo);
			regrasService.verificaSeProfessor(usuario, jogo);
			rodada.setJogo(jogo);
			rodadaService.verificarDatas(rodada);
			rodadaService.salvar(rodada, allIn);
			rodadaEquipeService.setStatusEquipeRodada(rodada, jogo.getEquipes(), false);
		} catch (IllegalAccessError e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return "redirect:/jogo/"+id+"/rodada/nova";
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch(Exception e){
			redirectAttributes.addFlashAttribute("erro", "Erro ao tentar persistir os dados.");
			return "redirect:/jogo/"+id+"/rodada/nova";
		}
		redirectAttributes.addFlashAttribute("info", "Rodada "+rodada.getNome()+" cadastrada com sucesso.");
		return "redirect:/jogo/"+id+"/rodadas";
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/rodada/{id}/detalhes", method = RequestMethod.GET)
	public String verDetalhes(@PathVariable("idJogo") Integer idJogo,
			@PathVariable("id") Integer id, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Usuario usuario = getUsuarioLogado(session);
		Rodada rodada = rodadaService.find(Rodada.class, id);
		Equipe equipe;
		usuario = usuarioService.find(Usuario.class, usuario.getId());
		String permissao;
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaParticipacao(usuario, jogo);
			regrasService.verificaRodada(rodada);
			regrasService.verificaRodadaJogo(rodada, jogo);
			equipe = equipeService.equipePorAlunoNoJogo(usuario, jogo);
			rodada = rodadaService.atualizaStatusRodada(rodada);
			rodada = rodadaService.atualizaStatusPrazoRodada(rodada);
			rodada = rodadaService.atualizaStatusAvaliacao(rodada);
			permissao = usuarioService.definePermissao(jogo, usuario);
			if(equipe != null && rodada.getJogo().getEquipes().contains(equipe)){
				ReaberturaSubmissao reaberturaSubmissao = reaberturaSubmissaoService.find(equipe, rodada);
				if(reaberturaSubmissao != null){
					StatusRodadaEquipe rodadaEquipe = rodadaEquipeService.atualizaStatusRodadaEquipe(reaberturaSubmissao);
					model.addAttribute("rodadaEquipe", rodadaEquipe);
				}else{
					StatusRodadaEquipe rodadaEquipe =new StatusRodadaEquipe();
					rodadaEquipe.setAtiva(false);
					model.addAttribute("rodadaEquipe", rodadaEquipe);
				}
				model.addAttribute("equipe", equipe);
				model.addAttribute("reaberturaSubmissao", new ReaberturaSubmissao());
			}
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_EXCEPTION);
			return "redirect:/jogo/" + idJogo + "/rodadas";
		}
		
		List<Equipe> equipes = new ArrayList<Equipe>();
		try{
			equipes = rodadaEquipeService.atualizaStatusEquipesNaRodada(jogo.getEquipes(), rodada);
		}catch(Exception e){
			redirectAttributes.addFlashAttribute("erro",
					"Erro ao atualizar os status das equipes para a rodada.");
			return "redirect:/jogo/" + idJogo + "/rodadas";
		}
		try {
			equipes = reaberturaSubmissaoService.atualizarSolitacoesDeReabertura(equipes, rodada);	
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro",
					"Erro ao atualizar pedidos de reabertura de submissão.");
			return "redirect:/jogo/" + idJogo + "/rodadas";
		}
		model.addAttribute("permissao", permissao);
		model.addAttribute("action", "detalhesRodada");		
		model.addAttribute("editor", "rodada");
		model.addAttribute("rodada", rodada);
		model.addAttribute("jogo", jogo);
		model.addAttribute("equipes", equipes);
		return PAGINA_DETALHES_RODADA;
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/rodada/{id}/editar", method = RequestMethod.GET)
	public String editarRodada(@PathVariable("idJogo") Integer idJogo,
			@PathVariable("id") Integer id, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Rodada rodada = rodadaService.find(Rodada.class, id);
		Usuario usuario = getUsuarioLogado(session);
		usuario = usuarioService.find(Usuario.class, usuario.getId());
		try{
			regrasService.verificaJogo(jogo);
			regrasService.verificaSeProfessor(usuario, jogo);
			regrasService.verificaRodada(rodada);
			regrasService.verificaRodadaJogo(rodada, jogo);
			model.addAttribute("jogo", jogo);
			model.addAttribute("rodada", rodada);
			model.addAttribute("editor", "rodada");
			model.addAttribute("action", "editar");
			model.addAttribute("formularios", usuario.getFormulario());
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch(Exception e){
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		return PAGINA_NOVA_RODADA;
	}

	@RequestMapping(value = "/{idJogo}/rodada/editar", method = RequestMethod.POST)
	public String editar(@PathVariable("idJogo") Integer id, @RequestParam("tudo") String allIn,
			@Valid Rodada rodada, BindingResult result, HttpSession session,
			RedirectAttributes redirectAttributes, Model model) {
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("erro", "Erro ao editar rodada.");
			return "redirect:/jogo/" + id + "/rodada/" + rodada.getId()+ "/editar";
		}
		Jogo jogo = jogoService.find(Jogo.class, id);
		Usuario usuario = getUsuarioLogado(session);
		try{
			regrasService.verificaJogo(jogo);
			regrasService.verificaSeProfessor(usuario, jogo);
			regrasService.verificaRodadaJogo(rodada, jogo);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		try{
			rodadaService.verificarDatas(rodada);
			rodadaService.atualizar(rodada, allIn);
		} catch (IllegalAccessError e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return "redirect:/jogo/" + id + "/rodada/"+rodada.getId()+"/editar";
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return "redirect:/jogo/" + id + "/rodada/"+rodada.getId()+"/editar";
		} catch(Exception e){
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		redirectAttributes.addFlashAttribute("info",MENSAGEM_JOGO_ATUALIZADO);
		return "redirect:/jogo/" + id + "/rodada/"+rodada.getId()+"/detalhes";
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/rodada/{id}/excluir", method = RequestMethod.GET)
	public String excluir(@PathVariable("idJogo") Integer idJogo,
			@PathVariable("id") Integer id, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Rodada rodada = rodadaService.find(Rodada.class, id);
		Usuario usuario = getUsuarioLogado(session);
		try{
			regrasService.verificaJogo(jogo);
			regrasService.verificaSeProfessor(usuario, jogo);
			regrasService.verificaRodada(rodada);
			regrasService.verificaRodadaJogo(rodada, jogo);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		try{
			jogo.getRodadas().remove(rodada);
			jogoService.update(jogo);
			rodadaEquipeService.deletePor(rodada);
			rodadaService.delete(rodada);
		}catch(Exception e){
			redirectAttributes.addFlashAttribute("erro",MENSAGEM_EXCEPTION);
			return "redirect:/jogo/" + jogo.getId() + "/rodada/"+rodada.getId()+"/detalhes";
		}
		redirectAttributes.addFlashAttribute("info","Rodada removida com sucesso.");
		return "redirect:/jogo/" + idJogo + "/rodadas";
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/rodada/{idRodada}/equipe/{idEquipe}/ativar", method = RequestMethod.GET)
	public String ativarSubmissaoEquipeNaRodada(
			@PathVariable("idRodada") Integer idRodada,
			@PathVariable("idEquipe") Integer idEquipe,
			@PathVariable("idJogo") Integer idJogo, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Rodada rodada = rodadaService.find(Rodada.class, idRodada);
		Equipe equipe = equipeService.find(Equipe.class, idEquipe);
		Usuario usuario = getUsuarioLogado(session);
		try{
			regrasService.verificaJogo(jogo);
			regrasService.verificaSeProfessor(usuario, jogo);
			regrasService.verificaRodada(rodada);
			regrasService.verificaRodadaJogo(rodada, jogo);
			regrasService.verificaEquipe(equipe);
			regrasService.verificaEquipeJogo(equipe, jogo);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		StatusRodadaEquipe rodadaEquipe = rodadaEquipeService.find(equipe, rodada);
		try {
			equipeService.ativarSubmissaoEquipeRodada(equipe, rodada, rodadaEquipe);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro",MENSAGEM_EXCEPTION);
			return "redirect:/jogo/" + idJogo + "/rodada/" + idRodada+"/detalhes";
		}
		redirectAttributes.addFlashAttribute("info","Equipe ativada com sucesso.");
		return "redirect:/jogo/" + idJogo + "/rodada/" + idRodada+"/detalhes";
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/rodada/{idRodada}/equipe/{idEquipe}/inativar", method = RequestMethod.GET)
	public String desativarSubmissaoEquipeNaRodada(
			@PathVariable("idRodada") Integer idRodada,
			@PathVariable("idEquipe") Integer idEquipe,
			@PathVariable("idJogo") Integer idJogo, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Rodada rodada = rodadaService.find(Rodada.class, idRodada);
		Equipe equipe = equipeService.find(Equipe.class, idEquipe);
		Usuario usuario = getUsuarioLogado(session);
		try{
			regrasService.verificaJogo(jogo);
			regrasService.verificaSeProfessor(usuario, jogo);
			regrasService.verificaRodada(rodada);
			regrasService.verificaRodadaJogo(rodada, jogo);
			regrasService.verificaEquipe(equipe);
			regrasService.verificaEquipeJogo(equipe, jogo);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		StatusRodadaEquipe rodadaEquipe = rodadaEquipeService.find(equipe, rodada);
		try {
			equipeService.desativarSubmissaoEquipeRodada(equipe, rodada, rodadaEquipe);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro","Houve um erro interno ao inativar as submissões da equipe.");
			return "redirect:/jogo/" + idJogo + "/rodada/" + idRodada+"/detalhes";
		}
		redirectAttributes.addFlashAttribute("info","Equipe inativada com sucesso.");
		return "redirect:/jogo/" + idJogo + "/rodada/" + idRodada+"/detalhes";
	}

	@RequestMapping(value = "/jogo/{idJogo}/rodada/entrega", method = RequestMethod.POST)
	public String entregaDeUmaRodada(@ModelAttribute("rodada") Rodada rodada, @RequestParam("anexo") MultipartFile anexo, 
			BindingResult result, @PathVariable("idJogo") Integer idJogo,
			 HttpSession session, RedirectAttributes redirect, Model model){
		if (result.hasErrors()) {
			redirect.addFlashAttribute("erro", "Erro ao efetuar entrega.");
			return "redirect:/jogo/"+idJogo+"/rodada/"+rodada.getId()+"/detalhes";
		}
		rodada = rodadaService.find(Rodada.class, rodada.getId());
		Usuario usuario = getUsuarioLogado(session);
		usuario = usuarioService.find(Usuario.class, usuario.getId());
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Entrega entrega = new Entrega();
		Equipe equipe = equipeService.equipePorAlunoNoJogo(usuario, jogo);
		Documento documento;
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaParticipacao(usuario, jogo);
			documento = documentoService.verificaAnexoEntrega(anexo,usuario,rodada,jogo,equipe);
			documentoService.save(documento);
			entregaService.salvar(entrega, rodada, equipe, usuario, documento);
		} catch (IOException e) {
			redirect.addFlashAttribute("erro", MENSAGEM_ERRO_UPLOAD);
			return "redirect:/jogo/"+idJogo+"/rodada/"+rodada.getId()+"/detalhes";
		} catch (IllegalArgumentException e) {
			redirect.addFlashAttribute("erro", e.getMessage());
			return "redirect:/jogo/"+idJogo+"/rodada/"+rodada.getId()+"/detalhes";
		} catch (Exception e) {
			redirect.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return "redirect:/jogo/"+idJogo+"/rodada/"+rodada.getId()+"/detalhes";
		}
		if(!usuario.equals(jogo.getProfessor())){
			redirect.addFlashAttribute("info", "Entrega efetuada com sucesso.");
		}else{
			redirect.addFlashAttribute("info", "Modelo salvo com sucesso.");
		}
		return "redirect:/jogo/"+idJogo+"/rodada/"+rodada.getId()+"/detalhes";
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/rodada/{id}/submissoes", method = RequestMethod.GET)
	public String verSubmissoes(@PathVariable("idJogo") Integer idJogo,
			@PathVariable("id") Integer id, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		Jogo jogo;
		Usuario usuario = getUsuarioLogado(session);
		Rodada rodada; 
		List<Entrega> entregas;
		String permissao;
		Aposta aposta;
		try {
			jogo = jogoService.find(Jogo.class, idJogo);
			usuario = usuarioService.find(Usuario.class, usuario.getId());
			rodada = rodadaService.find(Rodada.class, id);
			regrasService.verificaJogo(jogo);
			regrasService.verificaParticipacao(usuario, jogo);
			permissao = usuarioService.definePermissao(jogo, usuario);
			model.addAttribute("permissao", permissao);
			regrasService.verificaRodada(rodada);
			regrasService.verificaRodadaJogo(rodada, jogo);
			rodadaService.atualizaStatusPrazoRodada(rodada);
			try {
				rodadaService.verificaStatusPrazoSubmissao(rodada);
				rodadaEquipeService.verificaSeTemSolicitacao(jogo.getEquipes(), rodada);
				entregas = entregaService.getUltimasEntregasDaRodada(rodada);
				entregaService.verificaExistenciaEntregas(entregas);
			} catch (IllegalArgumentException e) {
				redirectAttributes.addFlashAttribute("erro",e.getMessage());
				return "redirect:/jogo/"+jogo.getId()+"/rodada/"+rodada.getId()+"/detalhes";
			}
			entregas = entregaService.verificaSeRespondidas(entregas, usuario);
			aposta  = apostaService.findByUsuarioRodada(usuario, rodada);
			if(aposta == null){
				aposta = apostaService.criarAposta(usuario, rodada);
			}	
			model.addAttribute("action", "submissoes");
			model.addAttribute("editor", "rodada");
			model.addAttribute("rodada", rodada);
			model.addAttribute("entregas", entregas);
			model.addAttribute("jogo", jogo);
			model.addAttribute("aposta", aposta);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro",e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro",MENSAGEM_EXCEPTION);
			return "redirect:/jogo/"+idJogo+"/rodada/"+id+"/detalhes";
		}
		return PAGINA_SUBMISSOES_RODADA;
	}

	@RequestMapping(value = "/jogo/{idJogo}/rodada/{idRodada}/solicitarReabertura", method = RequestMethod.POST)
	public String solicitarReabertura(@ModelAttribute("reaberturaSubmissao") ReaberturaSubmissao reaberturaSubmissao, 
			BindingResult result, @PathVariable("idJogo") Integer idJogo,
			@PathVariable("idRodada") Integer idRodada,	 HttpSession session, RedirectAttributes redirect){
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Rodada rodada = rodadaService.find(Rodada.class, idRodada);
		Usuario usuario = getUsuarioLogado(session);
		Equipe equipe;
		if (result.hasErrors()) {
			redirect.addFlashAttribute("erro", "Erro ao solicitar reabertura da submissão de entrega.");
			return "redirect:/jogo/"+idJogo+"/rodada/"+rodada.getId()+"/detalhes";
		}
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaRodada(rodada);
			regrasService.verificaRodadaJogo(rodada, jogo);
			regrasService.verificaParticipacao(usuario, jogo);
			equipe = equipeService.equipePorAlunoNoJogo(usuario, jogo);
			regrasService.verificaEquipe(equipe);
			regrasService.verificaAlunoEquipe(usuario, equipe);
			regrasService.verificaRodadaInJogo(jogo);
		} catch (IllegalArgumentException e) {
			redirect.addFlashAttribute("erro",e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		try {
			rodada = rodadaService.atualizaStatusPrazoRodada(rodada);
			reaberturaSubmissaoService.solicitarReabertura(rodada, equipe, reaberturaSubmissao);	
		} catch (IllegalArgumentException e) {
			redirect.addFlashAttribute("erro",e.getMessage());
			return "redirect:/jogo/"+idJogo+"/rodada/"+rodada.getId()+"/detalhes";
		} catch (Exception e) {
			redirect.addFlashAttribute("erro",MENSAGEM_EXCEPTION);
			return "redirect:/jogo/"+idJogo+"/rodada/"+rodada.getId()+"/detalhes";
		}
		redirect.addFlashAttribute("info","Solicitação de reabertura de prazo de entrega enviado com sucesso.");
		return "redirect:/jogo/"+idJogo+"/rodada/"+rodada.getId()+"/detalhes";
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/rodada/{id}/equipe/{idEquipe}/apostar", method = RequestMethod.GET)
	public String apostar(@PathVariable("idJogo") Integer idJogo, @PathVariable("idEquipe") Integer idJEquipe,
			@PathVariable("id") Integer id, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Usuario usuario = getUsuarioLogado(session);
		Rodada rodada = rodadaService.find(Rodada.class, id);
		Equipe equipe = equipeService.find(Equipe.class, idJEquipe);
		String permissao;
		Aposta aposta;
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaRodada(rodada);
			regrasService.verificaEquipe(equipe);
			regrasService.verificaParticipacao(usuario, jogo);
			regrasService.verificaExistencia(rodada, equipe, jogo);
			usuario = usuarioService.find(Usuario.class, usuario.getId());
			permissao = usuarioService.definePermissao(jogo, usuario);
			model.addAttribute("permissao", permissao);			
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro",e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		try {
			rodadaService.verificaStatusRodada(rodada);
			rodadaService.atualizaStatusAvaliacao(rodada);
			rodadaService.verificaStatusAvaliacao(rodada);
			rodadaService.atualizaStatusPrazoRodada(rodada);
			rodadaService.verificaStatusPrazoSubmissao(rodada);
			rodadaEquipeService.verificaSeTemSolicitacao(jogo.getEquipes(), rodada);
			aposta  = apostaService.findByUsuarioRodada(usuario, rodada);
			if(aposta == null){
				aposta = apostaService.criarAposta(usuario, rodada);
			}
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro",e.getMessage());
			return "redirect:/jogo/"+jogo.getId()+"/rodada/"+rodada.getId()+"/detalhes";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		model.addAttribute("editor", "rodada");
		model.addAttribute("action", "detalhesRodada");
		model.addAttribute("rodada", rodada);
		model.addAttribute("equipe", equipe);
		model.addAttribute("jogo", jogo);
		model.addAttribute("aposta", aposta);
		model.addAttribute("deposito", new Deposito());
		return PAGINA_APOSTA_RODADA;
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/rodada/{idRodada}/apostar", method = RequestMethod.POST)
	public String apostar(@ModelAttribute("deposito") Deposito deposito, 
			BindingResult result, @PathVariable("idJogo") Integer idJogo,
			@PathVariable("idRodada") Integer idRodada,	RedirectAttributes redirectAttributes, HttpSession session ){
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Rodada rodada = rodadaService.find(Rodada.class, idRodada);
		Equipe equipeDestinio = equipeService.find(Equipe.class, deposito.getEquipe().getId());
		Usuario logado = getUsuarioLogado(session);
		try{
			regrasService.verificaJogo(jogo);
			regrasService.verificaParticipacao(logado, jogo);
			if(jogo.getProfessor().equals(logado)){
				redirectAttributes.addFlashAttribute("erro", "Você não pode apostar em equipes do jogo!");
				return "redirect:/jogo/"+jogo.getId()+"/rodada/"+rodada.getId()+"/equipe/"+equipeDestinio.getId()+"/apostar";
			}
			regrasService.verificaRodada(rodada);
			regrasService.verificaRodadaJogo(rodada, jogo);
			regrasService.verificaEquipe(equipeDestinio);
			regrasService.verificaEquipeJogo(equipeDestinio, jogo);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Aposta aposta;
		try{
			aposta = apostaService.findByUsuarioRodada(logado, rodada);
			apostaService.realizarDeposito(aposta, deposito);
		} catch(IllegalArgumentException e){
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return "redirect:/jogo/"+jogo.getId()+"/rodada/"+rodada.getId()+"/equipe/"+equipeDestinio.getId()+"/apostar";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_EXCEPTION);
			return "redirect:/jogo/"+jogo.getId()+"/rodada/"+rodada.getId()+"/equipe/"+equipeDestinio.getId()+"/apostar";
		}
		redirectAttributes.addFlashAttribute("info", "Depósito efetuado com sucesso.");
		return "redirect:/jogo/"+jogo.getId()+"/rodada/"+rodada.getId()+"/equipe/"+equipeDestinio.getId()+"/apostar";
	}

	@RequestMapping(value = "/jogo/{idJogo}/rodada/{id}/apostas", method = RequestMethod.GET)
	public String apostas(@PathVariable("idJogo") Integer idJogo, 
			@PathVariable("id") Integer id, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		Jogo jogo;
		Usuario usuario = getUsuarioLogado(session);
		Rodada rodada;
		List<Aposta> apostas;
		try {
			jogo = jogoService.find(Jogo.class, idJogo);
			rodada = rodadaService.find(Rodada.class, id);
			regrasService.verificaJogo(jogo);
			regrasService.verificaRodada(rodada);
			regrasService.verificaRodadaJogo(rodada, jogo);
			try {
				regrasService.verificaSeProfessor(usuario, jogo);
				apostas = apostaService.findByRodada(rodada);
				regrasService.verificaApostas(apostas);
			} catch (IllegalArgumentException e) {
				redirectAttributes.addFlashAttribute("erro",e.getMessage());
				return "redirect:/jogo/" + jogo.getId() + "/rodada/"+rodada.getId()+"/detalhes";
			}
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro",e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("erro",MENSAGEM_EXCEPTION);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		model.addAttribute("permissao", "professor");
		model.addAttribute("apostas", apostas);
		model.addAttribute("editor", "rodada");
		model.addAttribute("jogo", jogo);
		model.addAttribute("rodada", rodada);
		model.addAttribute("action", "detalhesRodada");
		return PAGINA_APOSTAS_RODADA;
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/rodada/{idRodada}/gerarNotas", method = RequestMethod.GET)
	public String gerarNotasRodada(@PathVariable("idJogo") Integer idJogo,
			@PathVariable("idRodada") Integer idRodada, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Usuario usuario = getUsuarioLogado(session);
		Rodada rodada = rodadaService.find(Rodada.class, idRodada);
		try {
			try {
				regrasService.verificaJogo(jogo);	
				regrasService.verificaSeProfessor(usuario, jogo);
				regrasService.verificaRodada(rodada);
				regrasService.verificaRodadaJogo(rodada,jogo);
			} catch (IllegalArgumentException e) {
				redirectAttributes.addFlashAttribute("erro",e.getMessage());
				return REDIRECT_PAGINA_LISTAR_JOGO;
			}
			rodadaService.verificaSeNaoPrazoSubmissao(rodada);
			rodada.setStatusNota(true);
			rodadaService.update(rodada);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro",
					e.getMessage());
			return "redirect:/jogo/" + idJogo + "/rodada/"+idRodada+"/detalhes";
		} catch (Exception e){
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_EXCEPTION);
			return "redirect:/jogo/" + idJogo + "/rodada/"+idRodada+"/detalhes";
		}
		redirectAttributes.addFlashAttribute("info",
				usuario.getNome()+" você pode conferir as notas das equipes e alterar os fatores de aposta. "
						+ "Basta ir na página de cada equipe.");
		return "redirect:/jogo/" + idJogo + "/rodada/"+idRodada+"/detalhes";
	}

	@RequestMapping(value = "/jogo/{idJogo}/rodada/{id}/formulario", method = RequestMethod.GET)
	public String formulario(@PathVariable("idJogo") Integer idJogo, 
			@PathVariable("id") Integer id, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Usuario usuario = getUsuarioLogado(session);
		Rodada rodada = rodadaService.find(Rodada.class, id);
		String permissao;
		try {
			regrasService.verificaJogo(jogo);
			regrasService.verificaParticipacao(usuario, jogo);
			regrasService.verificaRodada(rodada);
			regrasService.verificaRodadaJogo(rodada, jogo);
			permissao = usuarioService.definePermissao(jogo, usuario);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro",e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		
		model.addAttribute("permissao", permissao);
		model.addAttribute("jogo", jogo);
		model.addAttribute("rodada", rodada);
		model.addAttribute("formulario", rodada.getFormulario());
		model.addAttribute("action", "detalhesRodada");
		return PAGINA_DETALHES_FORM;
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