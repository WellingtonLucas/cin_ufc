package br.ufc.cin.web;

import static br.ufc.cin.util.Constants.MENSAGEM_ERRO_UPLOAD;
import static br.ufc.cin.util.Constants.MENSAGEM_JOGO_INEXISTENTE;
import static br.ufc.cin.util.Constants.MENSAGEM_PERMISSAO_NEGADA;
import static br.ufc.cin.util.Constants.REDIRECT_PAGINA_LISTAR_JOGO;
import static br.ufc.cin.util.Constants.USUARIO_LOGADO;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.cin.model.Aposta;
import br.ufc.cin.model.Deposito;
import br.ufc.cin.model.Documento;
import br.ufc.cin.model.Entrega;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Formulario;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.ReaberturaSubmissao;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.StatusRodadaEquipe;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.service.ApostaService;
import br.ufc.cin.service.DocumentoService;
import br.ufc.cin.service.EntregaService;
import br.ufc.cin.service.EquipeService;
import br.ufc.cin.service.FormularioService;
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
	private FormularioService formularioService;

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
		if(jogo == null){
			redirectAttributes.addFlashAttribute("erro", "Jogo inexistente.");
			return "redirect:/jogo/listar";
		}
		Usuario usuario = getUsuarioLogado(session);
		if(!jogo.getProfessor().equals(usuario) && !jogo.getAlunos().contains(usuario)){
			redirectAttributes.addFlashAttribute("erro", "Você não possui permissão de acesso.");
			return "redirect:/jogo/listar";
		}
		
		if(jogo.getAlunos().contains(usuario) && !jogo.isStatus()){
			redirectAttributes.addFlashAttribute("erro", "Jogo não está ativo no momento. "
					+ "Para maiores informações "+jogo.getProfessor().getEmail());
			return "redirect:/jogo/listar";
			
		}else if(jogo.getProfessor().equals(usuario)){
			model.addAttribute("permissao", "professor");
			if((jogo.getRodadas() == null) || (jogo.getRodadas().isEmpty())){
				model.addAttribute("info", "Nenhuma rodada cadastrada no momento.");
			}else{
				model.addAttribute("rodadas", jogo.getRodadas());
			}
		}else if(jogo.getAlunos().contains(usuario)){
			model.addAttribute("permissao", "aluno");
		}
		if((jogo.getRodadas() == null) || (jogo.getRodadas().isEmpty())){
			model.addAttribute("info", "Nenhuma rodada cadastrada no momento.");
		}else{
			List<Rodada> rodadas = rodadaService.ordenaPorInicio(jogo.getRodadas());
			model.addAttribute("rodadas", rodadas);
		}
		model.addAttribute("action", "rodadas");
		model.addAttribute("jogo", jogo);
		return "rodada/listar";
	}

	@RequestMapping(value ="/jogo/{id}/rodada/nova", method = RequestMethod.GET)
	public String novaRodada(@PathVariable("id") Integer id, Model model, HttpSession session,
			RedirectAttributes redirectAttributes){
		Usuario usuario = getUsuarioLogado(session);
		Jogo jogo = jogoService.find(Jogo.class, id);
		if(jogo == null){
			redirectAttributes.addFlashAttribute("erro", "Jogo inexistente.");
			return "redirect:/jogo/listar";
		}
		usuario = usuarioService.find(Usuario.class, usuario.getId());
		if(jogo.getProfessor().equals(usuario)){
			model.addAttribute("action", "cadastrar");
			model.addAttribute("editor", "rodada");
			model.addAttribute("jogo", jogo);
			model.addAttribute("formularios", usuario.getFormulario());
			model.addAttribute("rodada", new Rodada());
			return "rodada/novaRodada";
		}
		redirectAttributes.addFlashAttribute("erro", "Você não possui permissão para criar uma rodada.");
		return "redirect:/jogo"+jogo.getId()+"/rodadas";
	}
	
	@RequestMapping(value = "/jogo/{id}/nova-rodada", method = RequestMethod.POST)
	public  String cadastrar(@PathVariable("id") Integer id, @RequestParam("allIn") String allIN, 
			@ModelAttribute("rodada") Rodada rodada, BindingResult result, 
			HttpSession session, RedirectAttributes redirectAttributes){
		Jogo jogo = jogoService.find(Jogo.class, id);
		if(jogo == null){
			redirectAttributes.addFlashAttribute("erro", "Jogo inexistente.");
			return "redirect:/jogo/listar";
		}
		if(result.hasErrors()){
			redirectAttributes.addFlashAttribute("erro", "Erro ao tentar salvar uma nova rodada.");
			return "redirect:/jogo/"+id+"/rodada/nova";
		}
		
		try{
			rodada.setJogo(jogo);
			rodada.setStatus(false);
			rodada.setStatusPrazo(true);
			rodada.setStatusAvaliacao(false);
			rodada.setStatusRaking(false);
			rodada.setStatusNota(true);
			if(allIN.equals("sim")){
				rodada.setAllIn(true);
			}else{
				rodada.setAllIn(false);
			}
			if( rodada.getFormulario() == null){
				redirectAttributes.addFlashAttribute("erro", "Primeiramente crie formulários para associar a uma rodada.");
				return "redirect:/formularios";
			}
			Formulario formulario = formularioService.find(Formulario.class, rodada.getFormulario().getId());
			rodada.setFormulario(formulario);
			rodadaService.save(rodada);
			rodadaEquipeService.setStatusEquipeRodada(rodada, jogo.getEquipes(), false);
		}catch(Exception e){
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
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Usuario usuario = getUsuarioLogado(session);
		if(!jogo.isStatus() && jogo.getAlunos().contains(usuario)){
			redirectAttributes.addFlashAttribute("erro",
					"Jogo inativo no momento. Para mais informações "+jogo.getProfessor().getEmail());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}else if(!jogo.getProfessor().equals(usuario) && !jogo.getAlunos().contains(usuario)){
			redirectAttributes.addFlashAttribute("erro",
					"Você não possui permissão de acesso");
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Rodada rodada = rodadaService.find(Rodada.class, id);
		model.addAttribute("action", "detalhesRodada");
		if (rodada == null || !jogo.getRodadas().contains(rodada)) {
			redirectAttributes.addFlashAttribute("erro",
					"Rodada solicitada não existe.");
			return "redirect:/jogo/" + idJogo + "/rodadas";
		}
		
		usuario = usuarioService.find(Usuario.class, usuario.getId());
		Equipe equipe = equipeService.equipePorAlunoNoJogo(usuario, jogo);
		
		rodada = rodadaService.atualizaStatusRodada(rodada);
		
		rodada = rodadaService.atualizaStatusPrazoRodada(rodada);
		
		rodada = rodadaService.atualizaStatusAvaliacao(rodada);
		
		if (usuario.equals(jogo.getProfessor()) && jogo.getRodadas().contains(rodada)) {
			model.addAttribute("permissao", "professor");
		}else if(equipe != null && rodada.getJogo().getEquipes().contains(equipe)){
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
			model.addAttribute("permissao", "aluno");
			model.addAttribute("reaberturaSubmissao", new ReaberturaSubmissao());
		}else if(jogo.getAlunos().contains(usuario)){
			model.addAttribute("permissao", "jogador");			
		}else {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_PERMISSAO_NEGADA);
			return REDIRECT_PAGINA_LISTAR_JOGO;
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
		
		model.addAttribute("editor", "rodada");
		model.addAttribute("rodada", rodada);
		model.addAttribute("jogo", jogo);
		model.addAttribute("equipes", equipes);
		return "rodada/detalhes";
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/rodada/{id}/editar", method = RequestMethod.GET)
	public String editarRodada(@PathVariable("idJogo") Integer idJogo,
			@PathVariable("id") Integer id, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Rodada rodada = rodadaService.find(Rodada.class, id);
		if (rodada == null || !jogo.getRodadas().contains(rodada)) {
			redirectAttributes.addFlashAttribute("erro",
					"Rodada inexistente.");
			return "redirect:/jogo/"+jogo.getId()+"/rodadas";
		}
		Usuario usuario = getUsuarioLogado(session);
		usuario = usuarioService.find(Usuario.class, usuario.getId());
		if (usuario.equals(jogo.getProfessor())) {
			model.addAttribute("jogo", jogo);
			model.addAttribute("rodada", rodada);
			model.addAttribute("editor", "rodada");
			model.addAttribute("action", "editar");
			model.addAttribute("formularios", usuario.getFormulario());
			return "rodada/novaRodada";
		}
		redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
		return REDIRECT_PAGINA_LISTAR_JOGO;
	}

	@RequestMapping(value = "/{idJogo}/rodada/editar", method = RequestMethod.POST)
	public String editar(@PathVariable("idJogo") Integer id, @RequestParam("tudo") String allIN,
			@Valid Rodada rodada, BindingResult result, HttpSession session,
			RedirectAttributes redirect, Model model) {

		if (result.hasErrors()) {
			redirect.addFlashAttribute("erro", "Erro ao editar rodada.");
			return "redirect:/jogo/" + id + "/rodada/" + rodada.getId()
					+ "/editar";
		}
		
		Jogo jogo = jogoService.find(Jogo.class, id);
		if (jogo == null) {
			redirect.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		if((rodada.getPrazoSubmissao().getTime() < rodada.getInicio().getTime()) 
				|| (rodada.getPrazoSubmissao().getTime() > rodada.getTermino().getTime())){
			redirect.addFlashAttribute("error_prazoSubmissao", "O prazo deve está entre o Início e Término da rodada .");
			redirect.addFlashAttribute("erro", "Não foi possível atualizar a rodada.");
			return "redirect:/jogo/" + id + "/rodada/" + rodada.getId()
					+ "/editar";
		}

		Formulario formulario = formularioService.find(Formulario.class, rodada.getFormulario().getId());
		rodada.setFormulario(formulario);
		rodada.setJogo(jogo);
		if(allIN.equals("sim")){
			rodada.setAllIn(true);
		}else{
			rodada.setAllIn(false);
		}
		if(rodada.getModelo()!= null)
			rodada.setModelo(documentoService.find(Documento.class, rodada.getModelo().getId()));
		try{
			rodadaService.update(rodada);
			redirect.addFlashAttribute("info", "Rodada atualizada com sucesso.");
		}catch(Exception e){
			redirect.addFlashAttribute("erro", "Não foi possível atualizar a rodada.");
		}
		return "redirect:/jogo/" + id + "/rodada/"+rodada.getId()+"/detalhes";
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/rodada/{id}/excluir", method = RequestMethod.GET)
	public String excluir(@PathVariable("idJogo") Integer idJogo,
			@PathVariable("id") Integer id, HttpSession session,
			RedirectAttributes redirectAttributes) {
		
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Rodada rodada = rodadaService.find(Rodada.class, id);
		if (rodada == null || !jogo.getRodadas().contains(rodada)) {
			redirectAttributes.addFlashAttribute("erro",
					"Rodada inexistente");
			return "redirect:/jogo/" + idJogo + "/rodadas";
		}
		
		Usuario usuario = getUsuarioLogado(session);
		if (usuario.equals(jogo.getProfessor())
				&& jogo.getRodadas().contains(rodada)) {
			try{
				jogo.getRodadas().remove(rodada);
				jogoService.update(jogo);
				rodadaEquipeService.deletePor(rodada);
				rodadaService.delete(rodada);
			}catch(Exception e){
				redirectAttributes.addFlashAttribute("erro",
						"Houve algum problema ao tentar remover uma rodada.");
				return "redirect:/jogo/" + jogo.getId() + "/rodada/"+rodada.getId()+"/detalhes";
			}
			redirectAttributes.addFlashAttribute("info",
					"Rodada removida com sucesso.");
		} else {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_PERMISSAO_NEGADA);
		}
		return "redirect:/jogo/" + idJogo + "/rodadas";
	}

	@RequestMapping(value = "/jogo/{idJogo}/rodada/{id}/inativar", method = RequestMethod.GET)
	public String inativarRodada(@PathVariable("id") Integer id,
			@PathVariable("idJogo") Integer idJogo, HttpSession session,
			RedirectAttributes redirectAttributes) {

		Jogo jogo = jogoService.find(Jogo.class, idJogo);

		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Rodada rodada = rodadaService.find(Rodada.class, id);
		if (rodada == null || !jogo.getRodadas().contains(rodada)) {
			redirectAttributes.addFlashAttribute("erro",
					"Rodada inexistente");
			return "redirect:/jogo/" + idJogo + "/rodadas";
		}
		
		Usuario usuario = getUsuarioLogado(session);
		if (usuario.equals(jogo.getProfessor())
				&& jogo.getRodadas().contains(rodada)) {
			rodada.setStatus(false);
			rodadaService.update(rodada);
			redirectAttributes.addFlashAttribute("info",
					"Rodada encerrada.");
		} else {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_PERMISSAO_NEGADA);
		}
		return "redirect:/jogo/" + idJogo + "/rodada/" + id+"/detalhes";
	}

	@RequestMapping(value = "/jogo/{idJogo}/rodada/{id}/ativar", method = RequestMethod.GET)
	public String ativarRodada(@PathVariable("id") Integer id,
			@PathVariable("idJogo") Integer idJogo, HttpSession session,
			RedirectAttributes redirectAttributes) {

		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Rodada rodada = rodadaService.find(Rodada.class, id);
		if (rodada == null || !jogo.getRodadas().contains(rodada)) {
			redirectAttributes.addFlashAttribute("erro",
					"Rodada inexistente");
			return "redirect:/jogo/" + idJogo + "/rodadas";
		}
		
		Usuario usuario = getUsuarioLogado(session);
		if (usuario.equals(jogo.getProfessor())
				&& jogo.getRodadas().contains(rodada)) {
			rodada.setStatus(true);
			rodadaService.update(rodada);
			redirectAttributes.addFlashAttribute("info",
					"Rodada iniciada com sucesso.");
		} else {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_PERMISSAO_NEGADA);
		}
		return "redirect:/jogo/" + idJogo + "/rodada/" + id +"/detalhes";
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/rodada/{idRodada}/equipe/{idEquipe}/ativar", method = RequestMethod.GET)
	public String ativarSubmissaoEquipeNaRodada(
			@PathVariable("idRodada") Integer idRodada,
			@PathVariable("idEquipe") Integer idEquipe,
			@PathVariable("idJogo") Integer idJogo, HttpSession session,
			RedirectAttributes redirectAttributes) {

		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Rodada rodada = rodadaService.find(Rodada.class, idRodada);
		if (rodada == null) {
			redirectAttributes.addFlashAttribute("erro","Rodada inexistente.");
			return "redirect:/jogo/" + jogo.getId() + "/equipes";
		}
		Equipe equipe = equipeService.find(Equipe.class, idEquipe);
		if (equipe == null) {
			redirectAttributes.addFlashAttribute("erro",
					"Equipe inexistente");
			return "redirect:/jogo/" + jogo.getId() + "/rodada/"+rodada.getId()+"/detalhes";
		}

		Usuario usuario = getUsuarioLogado(session);
		if (usuario.equals(jogo.getProfessor())	&& rodada.getJogo().getEquipes().contains(equipe)) {
			  
			StatusRodadaEquipe rodadaEquipe = rodadaEquipeService.find(equipe, rodada);
			if(rodadaEquipe == null){
				rodadaEquipe = new StatusRodadaEquipe();
				rodadaEquipe.setEquipe(equipe);
				rodadaEquipe.setRodada(rodada);
			}
			try {
				rodadaEquipe.setAtiva(true);
				equipe.addStatusRodadaEquipe(rodadaEquipe);				
				equipeService.update(equipe);
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("erro",
						"Houve um erro interno ao ativar a equipe.");
			}
			redirectAttributes.addFlashAttribute("info",
					"Equipe ativada com sucesso.");
		} else {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_PERMISSAO_NEGADA);
		}
		return "redirect:/jogo/" + idJogo + "/rodada/" + idRodada+"/detalhes";
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/rodada/{idRodada}/equipe/{idEquipe}/inativar", method = RequestMethod.GET)
	public String desativarSubmissaoEquipeNaRodada(
			@PathVariable("idRodada") Integer idRodada,
			@PathVariable("idEquipe") Integer idEquipe,
			@PathVariable("idJogo") Integer idJogo, HttpSession session,
			RedirectAttributes redirectAttributes) {

		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Rodada rodada = rodadaService.find(Rodada.class, idRodada);
		if (rodada == null) {
			redirectAttributes.addFlashAttribute("erro","Rodada inexistente.");
			return "redirect:/jogo/" + jogo.getId() + "/equipes";
		}
		Equipe equipe = equipeService.find(Equipe.class, idEquipe);
		if (equipe == null) {
			redirectAttributes.addFlashAttribute("erro",
					"Equipe inexistente");
			return "redirect:/jogo/" + jogo.getId() + "/rodada/"+rodada.getId()+"/detalhes";
		}

		Usuario usuario = getUsuarioLogado(session);
		if (usuario.equals(jogo.getProfessor())	&& rodada.getJogo().getEquipes().contains(equipe)) {
			  
			StatusRodadaEquipe rodadaEquipe = rodadaEquipeService.find(equipe, rodada);
			if(rodadaEquipe == null){
				rodadaEquipe = new StatusRodadaEquipe();
				rodadaEquipe.setEquipe(equipe);
				rodadaEquipe.setRodada(rodada);
			}
			try {
				rodadaEquipe.setAtiva(false);
				equipe.addStatusRodadaEquipe(rodadaEquipe);				
				equipeService.update(equipe);
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("erro",
						"Houve um erro interno ao inativar as submissões da equipe.");
			}
			redirectAttributes.addFlashAttribute("info",
					"Equipe inativada com sucesso.");
		} else {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_PERMISSAO_NEGADA);
		}
		return "redirect:/jogo/" + idJogo + "/rodada/" + idRodada+"/detalhes";
	}

	@RequestMapping(value = "/jogo/{idJogo}/rodada/entrega", method = RequestMethod.POST)
	public String entregaDeUmaRodada(@ModelAttribute("rodada") Rodada rodada, @RequestParam("anexos") List<MultipartFile> anexos, 
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
		List<Documento> documentos = new ArrayList<Documento>();
		Equipe equipe = equipeService.equipePorAlunoNoJogo(usuario, jogo);
		if(anexos != null && !anexos.isEmpty()) {
			if(anexos.size() > 1){
				redirect.addFlashAttribute("erro", "Selecione apenas um anexo!");
				return "redirect:/jogo/"+idJogo+"/rodada/"+rodada.getId()+"/detalhes";
			}
			for(MultipartFile anexo : anexos) {
				try {
					if(anexo.getBytes() != null && anexo.getBytes().length != 0) {
						Documento documento = new Documento();
						documento.setArquivo(anexo.getBytes());
						documento.setNomeOriginal(anexo.getOriginalFilename());
						if(!usuario.equals(jogo.getProfessor())){
							documento.setNome(equipe.getNome()+"-"+rodada.getNome());
						}else{
							documento.setNome("MODELO-"+rodada.getNome());
						}
						documento.setExtensao(anexo.getContentType());
						if(!documentoService.verificaExtensao(documento.getExtensao())){
							redirect.addFlashAttribute("erro", "O arquivo deve está com algum desses formatos: "
									+ "doc, docx, pdf, odt ou fodt!");
							return "redirect:/jogo/"+idJogo+"/rodada/"+rodada.getId()+"/detalhes";
						}
						documentos.add(documento);
					}
				} catch (IOException e) {
					redirect.addFlashAttribute("erro", MENSAGEM_ERRO_UPLOAD);
					return "redirect:/jogo/"+idJogo+"/rodada/"+rodada.getId()+"/detalhes";
				}
			}
		}
		if(!documentos.isEmpty()) {
			documentoService.save(documentos.get(0));
			if(usuario.equals(jogo.getProfessor())){
				rodada.setModelo(documentos.get(0));
				rodadaService.update(rodada);
			}
			entrega.setDocumento(documentos.get(0));
			entrega.setRodada(rodada);
			entrega.setUsuario(usuario);
			if(!usuario.equals(jogo.getProfessor())){
				entrega.setEquipe(equipe);
			}
			Calendar calendario = Calendar.getInstance();
			Date data =  calendario.getTime();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm:ss");
			simpleDateFormat.format(data);
			entrega.setDia(data);
			
			entregaService.save(entrega);
			if(!usuario.equals(jogo.getProfessor())){
				redirect.addFlashAttribute("info", "Entrega efetuada com sucesso.");
			}else{
				redirect.addFlashAttribute("info", "Modelo salvo com sucesso.");
			}
			return "redirect:/jogo/"+idJogo+"/rodada/"+rodada.getId()+"/detalhes";
		}else{
			redirect.addFlashAttribute("erro", "Selecione um documento!");
			return "redirect:/jogo/"+idJogo+"/rodada/"+rodada.getId()+"/detalhes";
		}
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/rodada/{id}/submissoes", method = RequestMethod.GET)
	public String verSubmissoes(@PathVariable("idJogo") Integer idJogo,
			@PathVariable("id") Integer id, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {

		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Usuario usuario = getUsuarioLogado(session);
		try {
			regrasService.verificaParticipacao(usuario, jogo);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro",e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		
		Rodada rodada = rodadaService.find(Rodada.class, id);
		model.addAttribute("action", "submissoes");
		if (rodada == null || !jogo.getRodadas().contains(rodada)) {
			redirectAttributes.addFlashAttribute("erro",
					"Rodada solicitada não existe.");
			return "redirect:/jogo/" + idJogo + "/rodadas";
		}
		
		Calendar calendario = Calendar.getInstance();
		long tempoAtual = calendario.getTimeInMillis();
		if(tempoAtual < rodada.getPrazoSubmissao().getTime()){
			redirectAttributes.addFlashAttribute("erro", "Período de submissão ainda não se encerrou!");
			return "redirect:/jogo/"+jogo.getId()+"/rodada/"+rodada.getId()+"/detalhes";
		}
		
		boolean status = rodadaEquipeService.verificaSeTemSolicitacao(jogo.getEquipes(), rodada);
		if(status){
			redirectAttributes.addFlashAttribute("erro", "Aguarte até o prazo final de prorrogação dos prazos.");
			return "redirect:/jogo/"+jogo.getId()+"/rodada/"+rodada.getId()+"/detalhes";
		}
		
		usuario = usuarioService.find(Usuario.class, usuario.getId());
		Equipe equipe = equipeService.equipePorAlunoNoJogo(usuario, jogo);
		List<Entrega> entregas = entregaService.getUltimasEntregasDaRodada(rodada);
		if(entregas.isEmpty() || entregas == null){
			redirectAttributes.addFlashAttribute("erro", "Não existem entregas para está rodada até o momento.");
			return "redirect:/jogo/"+jogo.getId()+"/rodada/"+rodada.getId()+"/detalhes";
		}
		entregas = entregaService.verificaSeRespondidas(entregas, usuario);
		
		if (usuario.equals(jogo.getProfessor()) && jogo.getRodadas().contains(rodada)) {
			model.addAttribute("permissao", "professor");
		}else if(equipe != null && rodada.getJogo().getEquipes().contains(equipe)){
			model.addAttribute("permissao", "aluno");
		}else {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_PERMISSAO_NEGADA);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Aposta aposta  = apostaService.findByUsuarioRodada(usuario, rodada);
		if(aposta == null){
			aposta = apostaService.criarAposta(usuario, rodada);
		}
		model.addAttribute("editor", "rodada");
		model.addAttribute("rodada", rodada);
		model.addAttribute("entregas", entregas);
		model.addAttribute("jogo", jogo);
		model.addAttribute("aposta", aposta);
		return "rodada/submissoes";
	}

	@RequestMapping(value = "/jogo/{idJogo}/rodada/{idRodada}/equipe/{idEquipe}/solicitarReabertura", method = RequestMethod.POST)
	public String solicitarReabertura(@ModelAttribute("reaberturaSubmissao") ReaberturaSubmissao reaberturaSubmissao, 
			BindingResult result, @PathVariable("idJogo") Integer idJogo, @PathVariable("idEquipe") Integer idEquipe,
			@PathVariable("idRodada") Integer idRodada,	 HttpSession session, RedirectAttributes redirect){
		
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		if (jogo == null) {
			redirect.addFlashAttribute("erro",
					MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Rodada rodada = rodadaService.find(Rodada.class, idRodada);
		if (rodada == null) {
			redirect.addFlashAttribute("erro","Rodada inexistente.");
			return "redirect:/jogo/" + jogo.getId() + "/detalhes";
		}
		Equipe equipe = equipeService.find(Equipe.class, idEquipe);
		if (equipe == null) {
			redirect.addFlashAttribute("erro",
					"Equipe inexistente");
			return "redirect:/jogo/" + jogo.getId() + "/rodada/"+rodada.getId()+"/detalhes";
		}
		if (result.hasErrors()) {
			redirect.addFlashAttribute("erro", "Erro ao solicitar reabertura da submissão de entrega.");
			return "redirect:/jogo/"+idJogo+"/rodada/"+rodada.getId()+"/detalhes";
		}
		Usuario usuario = getUsuarioLogado(session);
		if(!jogo.getAlunos().contains(usuario) || !equipe.getAlunos().contains(usuario)){
			redirect.addFlashAttribute("erro", "Você não possui permissão para isso.");
			return "redirect:/jogo/"+idJogo+"/rodada/"+rodada.getId()+"/detalhes";
		}		

		rodada = rodadaService.atualizaStatusPrazoRodada(rodada);
		if( !rodada.isStatusPrazo()){
			redirect.addFlashAttribute("erro","Uma solicitação de reabertura do prazo de submissão deve ser feita antes do encerramento deste.");
		}else{
			ReaberturaSubmissao oldReaberturaSubmissao = reaberturaSubmissaoService.find(equipe, rodada);
			if(oldReaberturaSubmissao == null){
				reaberturaSubmissao.setStatus(true);
				reaberturaSubmissao.setEquipe(equipeService.find(Equipe.class, reaberturaSubmissao.getEquipe().getId()));
				reaberturaSubmissao.setRodada(rodadaService.find(Rodada.class, reaberturaSubmissao.getRodada().getId()));
			
				try {
					reaberturaSubmissaoService.update(reaberturaSubmissao);
					equipe.addReaberturaSubmissao(reaberturaSubmissao);
					equipeService.update(equipe);			
				} catch (Exception e) {
					redirect.addFlashAttribute("erro","Erro interno ao solicitar reabertura de prazo da rodada.");
				}
				redirect.addFlashAttribute("info","Solicitação de reabertura de prazo de entrega enviado com sucesso.");
			}else{
				redirect.addFlashAttribute("info","Já existe um pedido de prorrogação para esta equipe.");
			}
		}
		return "redirect:/jogo/"+idJogo+"/rodada/"+rodada.getId()+"/detalhes";
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/rodada/{id}/equipe/{idEquipe}/apostar", method = RequestMethod.GET)
	public String apostar(@PathVariable("idJogo") Integer idJogo, @PathVariable("idEquipe") Integer idJEquipe,
			@PathVariable("id") Integer id, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {

		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Usuario usuario = getUsuarioLogado(session);
		try {
			regrasService.verificaParticipacao(usuario, jogo);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro",e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		
		Rodada rodada = rodadaService.find(Rodada.class, id);
		Equipe equipe = equipeService.find(Equipe.class, idJEquipe);
		try {
			regrasService.verificaExistencia(rodada, equipe, jogo);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro",e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		
		Calendar calendario = Calendar.getInstance();
		long tempoAtual = calendario.getTimeInMillis();
		if(tempoAtual < rodada.getPrazoSubmissao().getTime()){
			redirectAttributes.addFlashAttribute("erro", "Período de submissão ainda não se encerrou!");
			return "redirect:/jogo/"+jogo.getId()+"/rodada/"+rodada.getId()+"/detalhes";
		}
		
		boolean status = rodadaEquipeService.verificaSeTemSolicitacao(jogo.getEquipes(), rodada);
		if(status){
			redirectAttributes.addFlashAttribute("erro", "Aguarte até o prazo final de prorrogação dos prazos.");
			return "redirect:/jogo/"+jogo.getId()+"/rodada/"+rodada.getId()+"/detalhes";
		}
		
		usuario = usuarioService.find(Usuario.class, usuario.getId());
		
		if (usuario.equals(jogo.getProfessor()) && jogo.getRodadas().contains(rodada)) {
			model.addAttribute("permissao", "professor");
		}else if(equipe != null && rodada.getJogo().getEquipes().contains(equipe)){
			model.addAttribute("permissao", "aluno");
		}else {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_PERMISSAO_NEGADA);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Aposta aposta  = apostaService.findByUsuarioRodada(usuario, rodada);
		if(aposta == null){
			aposta = apostaService.criarAposta(usuario, rodada);
		}
		model.addAttribute("editor", "rodada");
		model.addAttribute("action", "detalhesRodada");
		model.addAttribute("rodada", rodada);
		model.addAttribute("equipe", equipe);
		model.addAttribute("jogo", jogo);
		model.addAttribute("aposta", aposta);
		model.addAttribute("deposito", new Deposito());
		return "rodada/aposta";
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/rodada/{idRodada}/apostar", method = RequestMethod.POST)
	public String apostar(@ModelAttribute("deposito") Deposito deposito, 
			BindingResult result, @PathVariable("idJogo") Integer idJogo,
			@PathVariable("idRodada") Integer idRodada,	RedirectAttributes redirectAttributes, HttpSession session ){
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Rodada rodada = rodadaService.find(Rodada.class, idRodada);
		if (rodada == null || !jogo.getRodadas().contains(rodada)) {
			redirectAttributes.addFlashAttribute("erro",
					"Rodada solicitada não existe.");
			return "redirect:/jogo/" + idJogo + "/rodadas";
		}
		Equipe equipeDestinio = equipeService.find(Equipe.class, deposito.getEquipe().getId());
		if(equipeDestinio == null || !jogo.getEquipes().contains(equipeDestinio)){
			redirectAttributes.addFlashAttribute("erro",
					"Equipe solicitada não existe.");
			return "redirect:/jogo/" + idJogo + "/equipes";
		}
		Usuario logado = getUsuarioLogado(session);
		if(jogo.getProfessor().equals(logado)){
			redirectAttributes.addFlashAttribute("erro", "Você não pode apostar em equipes do jogo!");
			return "redirect:/jogo/"+jogo.getId()+"/rodada/"+rodada.getId()+"/equipe/"+equipeDestinio.getId()+"/apostar";
		}
		
		Aposta aposta = apostaService.findByUsuarioRodada(logado, rodada);
		try{
			apostaService.realizarDeposito(aposta, deposito);
		}catch(IllegalArgumentException e){
			redirectAttributes.addFlashAttribute("erro", e.getMessage());
			return "redirect:/jogo/"+jogo.getId()+"/rodada/"+rodada.getId()+"/equipe/"+equipeDestinio.getId()+"/apostar";
		}
		redirectAttributes.addFlashAttribute("info", "Depósito efetuado com sucesso.");
		return "redirect:/jogo/"+jogo.getId()+"/rodada/"+rodada.getId()+"/equipe/"+equipeDestinio.getId()+"/apostar";
	}

	@RequestMapping(value = "/jogo/{idJogo}/rodada/{id}/apostas", method = RequestMethod.GET)
	public String apostas(@PathVariable("idJogo") Integer idJogo, 
			@PathVariable("id") Integer id, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {

		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Usuario usuario = getUsuarioLogado(session);
		try {
			regrasService.verificaParticipacao(usuario, jogo);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro",e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Rodada rodada = rodadaService.find(Rodada.class, id);
		if (rodada == null) {
			redirectAttributes.addFlashAttribute("erro","Rodada inexistente.");
			return "redirect:/jogo/" + jogo.getId() + "/rodadas";
		}
		List<Aposta> apostas = apostaService.findByRodada(rodada);
		if (apostas == null) {
			redirectAttributes.addFlashAttribute("erro","Não existem apostas até o momento.");
			return "redirect:/jogo/" + jogo.getId() + "/rodada/"+rodada.getId()+"/detalhes";
		}
		if(usuario.equals(jogo.getProfessor())){
			model.addAttribute("permissao", "professor");
		}else {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
			return "redirect:/jogo/" + jogo.getId() + "/rodada/"+rodada.getId()+"/detalhes";
		}
		model.addAttribute("apostas", apostas);
		model.addAttribute("jogo", jogo);
		model.addAttribute("rodada", rodada);
		model.addAttribute("action", "detalhesRodada");
		return "rodada/apostas";
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/rodada/{idRodada}/gerarNotas", method = RequestMethod.GET)
	public String gerarNotasRodada(@PathVariable("idJogo") Integer idJogo,
			@PathVariable("idRodada") Integer idRodada, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Usuario usuario = getUsuarioLogado(session);
		Rodada rodada = rodadaService.find(Rodada.class, idRodada);
		try {
			regrasService.verificaJogo(jogo);	
			regrasService.verificaSeProfessor(usuario, jogo);
			regrasService.verificaRodadaJogo(rodada,jogo);
			rodada.setStatusNota(true);
			rodadaService.update(rodada);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro",
					e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		} catch (Exception e){
			redirectAttributes.addFlashAttribute("erro",
					"Erro ao tentar gerar as notas.");
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
		String permissao = "";
		try {
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
		return "formulario/detalhes";
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
