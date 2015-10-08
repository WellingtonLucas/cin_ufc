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

import br.ufc.cin.model.Documento;
import br.ufc.cin.model.Entrega;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Formulario;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.ReaberturaSubmissao;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.StatusRodadaEquipe;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.service.DocumentoService;
import br.ufc.cin.service.EntregaService;
import br.ufc.cin.service.EquipeService;
import br.ufc.cin.service.FormularioService;
import br.ufc.cin.service.JogoService;
import br.ufc.cin.service.ReaberturaSubmissaoService;
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
	
	@RequestMapping(value ="/jogo/{id}/rodadas", method = RequestMethod.GET)
	public String rodadas(@PathVariable("id") Integer id, Model model, HttpSession session,
			RedirectAttributes redirectAttributes){
		Usuario usuario = getUsuarioLogado(session);
		Jogo jogo = jogoService.find(Jogo.class, id);
		if(jogo == null){
			redirectAttributes.addFlashAttribute("erro", "Jogo inexistente.");
			return "redirect:/jogo/listar";
		}
		if(!jogo.getProfessor().equals(usuario) && !jogo.getAlunos().contains(usuario)){
			redirectAttributes.addFlashAttribute("erro", "Você não possui permissão de acesso.");
			return "redirect:/jogo/listar";
		}
		model.addAttribute("action", "rodadas");
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
			model.addAttribute("rodadas", jogo.getRodadas());
		}
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
	public  String cadastrar(@PathVariable("id") Integer id, @ModelAttribute("rodada") Rodada rodada, 
			BindingResult result, HttpSession session, RedirectAttributes redirectAttributes){
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
			if( rodada.getFormulario() == null){
				redirectAttributes.addFlashAttribute("erro", "Primeiramente crie formulários para associar a uma rodada.");
				return "redirect:/jogo/"+id+"/formularios";
			}
			Formulario formulario = formularioService.find(Formulario.class, rodada.getFormulario().getId());
			rodada.setFormulario(formulario);
			rodadaService.save(rodada);
			rodadaEquipeService.setStatusEquipeRodada(rodada, jogo.getEquipes(), true);
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
		
		Calendar calendario = Calendar.getInstance();
		Date data =  calendario.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		simpleDateFormat.format(data);
		long termino = rodada.getTermino().getTime();
		long tempoAtual = data.getTime();
		
		if((termino < tempoAtual) && rodada.isStatus()){
			return "redirect:/jogo/"+jogo.getId()+"/rodada/"+rodada.getId()+"/inativar";
		}
		if(tempoAtual > rodada.getPrazoSubmissao().getTime()){
			rodada.setStatusPrazo(false);
		}else{
			rodada.setStatusPrazo(true);
		}
		
		
		if (usuario.equals(jogo.getProfessor()) && jogo.getRodadas().contains(rodada)) {
			model.addAttribute("permissao", "professor");
		}else if(rodada.getJogo().getEquipes().contains(equipe)){
			StatusRodadaEquipe rodadaEquipe = rodadaEquipeService.atualizaStatusRodadaEquipe(reaberturaSubmissaoService.find(equipe, rodada));
			model.addAttribute("rodadaEquipe", rodadaEquipe);
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
	public String editarEquipe(@PathVariable("idJogo") Integer idJogo,
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

	@RequestMapping(value = "/{id}/rodada/editar", method = RequestMethod.POST)
	public String editar(@PathVariable("id") Integer id, @Valid Rodada rodada, BindingResult result, HttpSession session,
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
		
		Rodada roAnterior = rodadaService.find(Rodada.class, rodada.getId());
		rodada.setJogo(jogo);
		rodada.setFormulario(roAnterior.getFormulario());
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
			@PathVariable("idEquipe") Integer id, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Rodada rodada = rodadaService.find(Rodada.class, id);

		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
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
				rodadaService.delete(rodada);
			}catch(Exception e){
				redirectAttributes.addFlashAttribute("erro",
						"Houve algum problema ao tentar remover uma rodada.");
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
				&& jogo.getRodadas().contains(rodada) || jogo.getAlunos().contains(usuario)) {
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
		Rodada rodada = rodadaService.find(Rodada.class, id);
		model.addAttribute("action", "submissoes");
		if (rodada == null || !jogo.getRodadas().contains(rodada)) {
			redirectAttributes.addFlashAttribute("erro",
					"Rodada solicitada não existe.");
			return "redirect:/jogo/" + idJogo + "/rodadas";
		}
		Calendar calendario = Calendar.getInstance();
		Date data =  calendario.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		simpleDateFormat.format(data);
		long tempoAtual = data.getTime();
		if(tempoAtual < rodada.getPrazoSubmissao().getTime()){
			redirectAttributes.addFlashAttribute("erro", "Período de submissão ainda não se encerrou!");
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
		}else if(rodada.getJogo().getEquipes().contains(equipe)){
			model.addAttribute("permissao", "aluno");
		}else {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_PERMISSAO_NEGADA);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		model.addAttribute("editor", "rodada");
		model.addAttribute("rodada", rodada);
		model.addAttribute("entregas", entregas);
		model.addAttribute("jogo", jogo);
		return "rodada/submissoes";
	}
	
	@RequestMapping(value = "/jogo/{id}/rodada/{idRodada}/vincularFormulario", method = RequestMethod.GET)
	public String vincularFormulario(Model model, HttpSession session, @PathVariable("id") Integer id, 
			 @PathVariable("idRodada") Integer idRodada, RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, id);
		Rodada rodada = rodadaService.find(Rodada.class,idRodada);
		Usuario usuario = getUsuarioLogado(session);
		usuario = usuarioService.find(Usuario.class, usuario.getId());
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		if (rodada == null || !jogo.getRodadas().contains(rodada)) {
			redirectAttributes.addFlashAttribute("erro", "Rodada inexistente");
			return "redirect:/jogo/" + id + "/rodadas";
		}
		if(usuario.equals(jogo.getProfessor())){
			model.addAttribute("jogo", jogo);
			model.addAttribute("rodada", rodada);
			model.addAttribute("action","vincularFormularioRodada");
			model.addAttribute("permissao","professor");
			model.addAttribute("formularios", usuario.getFormulario());
			return "formulario/adicionarFormulario";
		}
		redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
		return "redirect:/jogo/" + id + "/rodada/"+rodada.getId()+"/detalhes";
	}
	
	@RequestMapping(value = "/jogo/rodada/formulario/vincular", method = RequestMethod.POST)
	public String vincularFormulario(Model model, HttpSession session, @ModelAttribute("rodada") Rodada rodada, 
			RedirectAttributes redirectAttributes, BindingResult result) {
		
		Rodada rodadaCompleta = rodadaService.find(Rodada.class, rodada.getId());
		
		if(result.hasErrors()){
			redirectAttributes.addFlashAttribute("erro",
					"Aconceteu algum erro ao associar formulario à rodada.");
			return "redirect:/jogo/"+rodadaCompleta.getJogo().getId()+"/rodada/"+rodadaCompleta.getId()+"/vincular";
		}
		Usuario usuario = getUsuarioLogado(session);
		if(usuario.equals(rodadaCompleta.getJogo().getProfessor())){
			Formulario formulario = rodada.getFormulario();
			if(formulario != null){
				formulario = formularioService.find(Formulario.class, formulario.getId());
				rodadaCompleta.setFormulario(formulario);
				try{
					rodadaService.update(rodadaCompleta);
				}catch(Exception e){
					redirectAttributes.addFlashAttribute("erro",
							"Houve um problema tecnico ao vincular o formulário à rodada!");
					return "redirect:/jogo/"+rodadaCompleta.getJogo().getId()+"/rodada/"+rodadaCompleta.getId()+"/vincular";
				}
				redirectAttributes.addFlashAttribute("info",
						"Formulário associado à rodada com sucesso!");
				return "redirect:/jogo/"+rodadaCompleta.getJogo().getId()+"/rodada/"+rodadaCompleta.getId()+"/detalhes";
			}else{
				redirectAttributes.addFlashAttribute("erro",
						"Selecione o formulário para associar à rodada.");
				return "redirect:/jogo/"+rodadaCompleta.getJogo().getId()+"/rodada/"+rodadaCompleta.getId()+"/vincular";
			}
		}else{
			redirectAttributes.addFlashAttribute("erro",
					"Você não possui permissão de acesso.");
			return "redirect:/jogo/"+rodadaCompleta.getJogo().getId()+"/rodada/detalhes";
		}
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

		Calendar calendario = Calendar.getInstance();
		Date data =  calendario.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		simpleDateFormat.format(data);
		long tempoAtual = data.getTime();
		
		if( tempoAtual > rodada.getPrazoSubmissao().getTime()){
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
