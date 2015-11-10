package br.ufc.cin.web;

import static br.ufc.cin.util.Constants.MENSAGEM_EQUIPE_CADASTRADA;
import static br.ufc.cin.util.Constants.MENSAGEM_EQUIPE_INEXISTENTE;
import static br.ufc.cin.util.Constants.MENSAGEM_EQUIPE_REMOVIDA;
import static br.ufc.cin.util.Constants.MENSAGEM_ERRO_AO_CADASTRAR_EQUIPE;
import static br.ufc.cin.util.Constants.MENSAGEM_ERRO_UPLOAD;
import static br.ufc.cin.util.Constants.MENSAGEM_JOGO_INEXISTENTE;
import static br.ufc.cin.util.Constants.MENSAGEM_PERMISSAO_NEGADA;
import static br.ufc.cin.util.Constants.PAGINA_CADASTRAR_EQUIPE;
import static br.ufc.cin.util.Constants.PAGINA_DETALHES_EQUIPE;
import static br.ufc.cin.util.Constants.REDIRECT_PAGINA_LISTAR_FORMULARIOS;
import static br.ufc.cin.util.Constants.REDIRECT_PAGINA_LISTAR_JOGO;
import static br.ufc.cin.util.Constants.USUARIO_LOGADO;

import java.io.IOException;
import java.util.ArrayList;
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
import br.ufc.cin.model.NotaEquipeRodada;
import br.ufc.cin.model.Resposta;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.service.CalculoNotaService;
import br.ufc.cin.service.DocumentoService;
import br.ufc.cin.service.EntregaService;
import br.ufc.cin.service.EquipeService;
import br.ufc.cin.service.FormularioService;
import br.ufc.cin.service.JogoService;
import br.ufc.cin.service.NotaEquipeRodadaService;
import br.ufc.cin.service.RespostaService;
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
	private CalculoNotaService calculoNotaService;
	
	@Inject
	private RespostaService respostaService;
	
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
			@PathVariable("id") Integer id,
			@ModelAttribute("equipe") Equipe equipe, BindingResult result,
			@RequestParam("anexos") List<MultipartFile> anexos,
			HttpSession session, RedirectAttributes redirect, Model model) {
		Jogo jogo = jogoService.find(Jogo.class, id);
		
		if (result.hasErrors()) {
			redirect.addFlashAttribute("erro", MENSAGEM_ERRO_AO_CADASTRAR_EQUIPE);
			return "redirect:/jogo/" + id + "/equipe/nova";
		}
		
		List<Documento> documentos = new ArrayList<Documento>();
		if(anexos != null && !anexos.isEmpty()) {
			if(anexos.size() > 1){
				redirect.addFlashAttribute("erro", "Selecione apenas um logo!");
				return "redirect:/jogo/"+id+"/equipe/nova";
			}
			for(MultipartFile anexo : anexos) {
				try {
					if(anexo.getBytes() != null && anexo.getBytes().length != 0) {
						Documento documento = new Documento();
						documento.setArquivo(anexo.getBytes());
						String data = new Date().getTime()+"";
						documento.setNomeOriginal(data+"-"+anexo.getOriginalFilename());
						documento.setNome(equipe.getNome()+"-"+"logo");
						documento.setExtensao(anexo.getContentType());
						if(!documentoService.verificaSeImagem(documento.getExtensao())){
							redirect.addFlashAttribute("erro", "O arquivo deve está com algum desses formatos: PNG ou JPEG "
									);
							return "redirect:/jogo/"+id+"/equipe/nova";
						}
						documentos.add(documento);
					}
				} catch (IOException e) {
					redirect.addFlashAttribute("erro", MENSAGEM_ERRO_UPLOAD);
					return "redirect:/jogo/"+id+"/equipe/nova";
				}
			}
		}
		if(!documentos.isEmpty()){
			documentoService.save(documentos.get(0));
			equipe.setLogo(documentos.get(0));
			
		}
		equipe.setJogo(jogo);
		equipe.setStatus(true);
		equipeService.save(equipe);

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
		List<Entrega>  entregas = equipeService.getEntregasOrdenadasPorEquipe(equipe, jogo);
		model.addAttribute("equipe", equipe);
		model.addAttribute("entregas", entregas);
		model.addAttribute("jogo", jogo);
		if (usuario.getId() == jogo.getProfessor().getId() && jogo.getEquipes().contains(equipe)) {
			model.addAttribute("permissao", "professor");
			return PAGINA_DETALHES_EQUIPE;
		}else if(equipe.getAlunos().contains(usuario) && jogo.getEquipes().contains(equipe)){
			model.addAttribute("permissao", "aluno");
			return PAGINA_DETALHES_EQUIPE;
		}else if(jogo.getAlunos().contains(usuario)){
			model.addAttribute("permissao", "jogador");
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
			@RequestParam("anexos") List<MultipartFile> anexos, 
			@PathVariable("id") Integer id, @Valid Equipe equipe, BindingResult result, HttpSession session,
			RedirectAttributes redirect) {
		Jogo jogo = jogoService.find(Jogo.class, id);

		if (result.hasErrors()) {
			redirect.addFlashAttribute("erro", "Erro ao editar equipe.");
			return "redirect:/jogo/" + id + "/equipe/" + equipe.getId()
					+ "/editar";
		}

		if (jogo == null) {
			redirect.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		List<Documento> documentos = new ArrayList<Documento>();
		if(anexos != null && !anexos.isEmpty()) {
			if(anexos.size() > 1){
				redirect.addFlashAttribute("erro", "Selecione apenas um logo!");
				return "redirect:/jogo/"+id+"/equipe/nova";
			}
			for(MultipartFile anexo : anexos) {
				try {
					if(anexo.getBytes() != null && anexo.getBytes().length != 0) {
						Documento documento = new Documento();
						documento.setArquivo(anexo.getBytes());
						String data = new Date().getTime()+"";
						documento.setNomeOriginal(data+"-"+anexo.getOriginalFilename());
						documento.setNome(equipe.getNome()+"-"+"logo");
						documento.setExtensao(anexo.getContentType());
						if(!documentoService.verificaSeImagem(documento.getExtensao())){
							redirect.addFlashAttribute("erro", "O arquivo deve está com algum desses formatos: PNG ou JPEG "
									);
							return "redirect:/jogo/"+id+"/equipe/nova";
						}
						documentos.add(documento);
					}
				} catch (IOException e) {
					redirect.addFlashAttribute("erro", MENSAGEM_ERRO_UPLOAD);
					return "redirect:/jogo/"+id+"/equipe/nova";
				}
			}
		}
		Equipe oldEquipe = equipeService.find(Equipe.class, equipe.getId());
		if(!documentos.isEmpty()){
			documentoService.save(documentos.get(0));
			oldEquipe.setLogo(documentos.get(0));
			
		}
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
					"Usuários associados à equipe com sucesso!");
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
		usuario = usuarioService.find(Usuario.class, usuario.getId());
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
		if(!jogo.getProfessor().equals(usuario)){
			Equipe equipeUsuarioLogado = equipeService.equipePorAlunoNoJogo(usuario, jogo);
			if(!equipe.equals(equipeUsuarioLogado)){
				redirectAttributes.addFlashAttribute("erro",
						"Você não possui permissão de acesso à avaliações de outras equipes!");
				return "redirect:/jogo/"+jogo.getId()+"/equipe/"+equipeUsuarioLogado.getId();
			}
		}else if (equipe == null || !jogo.getEquipes().contains(equipe)) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_EQUIPE_INEXISTENTE);
			return "redirect:/jogo/" + idJogo + "/equipes";
		}

		List<Entrega> entregas = entregaService.getUltimasEntregasDaEquipeComGabarito(equipe);
		
		if(entregas.isEmpty() || entregas == null){
			redirectAttributes.addFlashAttribute("erro",
					"Ainda não existem avaliações efetuadas pelo docente para esta equipe.");
			return "redirect:/jogo/"+jogo.getId()+"/equipe/"+equipe.getId();
		}
		if (usuario.equals(jogo.getProfessor()) && jogo.getEquipes().contains(equipe)) {
			model.addAttribute("permissao", "professor");
		}else if(equipe.getAlunos().contains(usuario) && jogo.getEquipes().contains(equipe)){
			model.addAttribute("permissao", "aluno");
		}else {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_PERMISSAO_NEGADA);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		model.addAttribute("action", "avaliacoes");
		model.addAttribute("equipe", equipe);
		model.addAttribute("jogo", jogo);
		model.addAttribute("entregas", entregas);
		return "equipe/avaliacoes";
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
			return REDIRECT_PAGINA_LISTAR_FORMULARIOS;
		}
		Entrega entrega = entregaService.find(Entrega.class, id);
		if (entrega == null) {
			redirectAttributes.addFlashAttribute("erro", "Entrega inexistente.");
			return "redirect:/jogo/"+ idJogo +"/rodadas";
		}
		if(!entrega.getRodada().isStatusRaking()){
			redirectAttributes.addFlashAttribute("erro", "Ainda não está disponível o gabarito.");
			return "redirect:/jogo/"+ idJogo +"/equipe/"+idEquipe+"/avaliacoes";
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
	
	@RequestMapping(value = "/jogo/{idJogo}/equipe/{idEquipe}/historico", method = RequestMethod.GET)
	public String historicoEquipe(@PathVariable("idJogo") Integer idJogo, @PathVariable("idEquipe") Integer idEquipe, 
			Model model, HttpSession session, RedirectAttributes redirectAttributes) {

		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		if(jogo == null){
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Equipe equipe = equipeService.find(Equipe.class, idEquipe);
		if(!jogo.getEquipes().contains(equipe)){
			redirectAttributes.addFlashAttribute("erro",
					"Equipe não existe ou não pertence ao jogo.");
			return "redirect:/jogo/"+idJogo+"/equipes";
		}
		if(jogo.getRodadas() == null || jogo.getRodadas().isEmpty()){
			redirectAttributes.addFlashAttribute("erro",
					"O jogo ainda não possui rodadas.");
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
		
		usuario = usuarioService.find(Usuario.class, usuario.getId());
		
		if (usuario.equals(jogo.getProfessor())) {			
			model.addAttribute("permissao", "professor");
		}else if(equipe.getAlunos().contains(usuario)){
			model.addAttribute("permissao", "alunoLogado");
		}else{			
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		List<Rodada> rodadas = rodadaService.ordenaPorInicio(jogo.getRodadas());
		rodadas = rodadaService.atualizaStatusRodadas(rodadas);
		
		List<NotaEquipeRodada> notasEquipeRodadas = notaEquipeRodadaService.buscarPorEquipe(equipe);
		if(notasEquipeRodadas == null){
			notasEquipeRodadas = criarNotasEquipeRodadas(notasEquipeRodadas, equipe);
		}
		if(notasEquipeRodadas != null){
			notasEquipeRodadas = atualizarNotasEquipeRodadas(notasEquipeRodadas, equipe);
		}
		model.addAttribute("usuario", usuario);
		model.addAttribute("notasEquipeRodadas", notasEquipeRodadas);
		model.addAttribute("jogo", jogo);
		model.addAttribute("equipe", equipe);
		return "equipe/historicoEquipeRodada";
	}

	private List<NotaEquipeRodada> atualizarNotasEquipeRodadas(
			List<NotaEquipeRodada> notasEquipeRodadas, Equipe equipe) {
		List<Entrega> entregas = entregaService.getUltimasEntregasDaEquipe(equipe);
		List<Resposta> respostas = new ArrayList<Resposta>();
		for (int i= notasEquipeRodadas.size(); i<entregas.size();i++) {
			Resposta resposta = respostaService.findUltimaRespostaPorEntrega(entregas.get(i).getUsuario(), entregas.get(i));
			if(resposta!= null){
				respostas.add(resposta);
			}
			if(!entregas.get(i).getRodada().isStatusRaking() && !respostas.isEmpty()){
				NotaEquipeRodada notaEquipeRodada = new NotaEquipeRodada();
				notaEquipeRodada.setEquipe(equipe);
				notaEquipeRodada.setRodada(entregas.get(i).getRodada());
				Float nota = calculoNotaService.calculoNotaEquipe(resposta);
				notaEquipeRodada.setValor(nota);
				notaEquipeRodada.setFatorDeAposta(nota);
				notaEquipeRodadaService.save(notaEquipeRodada);
				notasEquipeRodadas.add(notaEquipeRodada);
			}
		}
		return notasEquipeRodadas;
	}

	private List<NotaEquipeRodada> criarNotasEquipeRodadas(
			List<NotaEquipeRodada> notasEquipeRodadas, Equipe equipe) {
		notasEquipeRodadas = new ArrayList<NotaEquipeRodada>();
		List<Entrega> entregas = entregaService.getUltimasEntregasDaEquipe(equipe);
		List<Resposta> respostas = new ArrayList<Resposta>();
		for (Entrega entrega : entregas) {
			Resposta resposta = respostaService.findUltimaRespostaPorEntrega(entrega.getUsuario(), entrega);
			if(resposta!= null){
				respostas.add(resposta);
			}
			if(!entrega.getRodada().isStatusRaking() && !respostas.isEmpty()){
				NotaEquipeRodada notaEquipeRodada = new NotaEquipeRodada();
				notaEquipeRodada.setEquipe(equipe);
				notaEquipeRodada.setRodada(entrega.getRodada());
				Float nota = calculoNotaService.calculoNotaEquipe(resposta);
				notaEquipeRodada.setValor(nota);
				notaEquipeRodada.setFatorDeAposta(nota);
				notaEquipeRodadaService.save(notaEquipeRodada);
				notasEquipeRodadas.add(notaEquipeRodada);
			}
		}	
		
		return notasEquipeRodadas;
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
