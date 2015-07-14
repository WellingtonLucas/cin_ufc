package br.ufc.cin.web;


import static br.ufc.cin.util.Constants.*;
import static br.ufc.cin.util.Constants.MENSAGEM_ERRO_AO_CADASTRAR_JOGO;
import static br.ufc.cin.util.Constants.MENSAGEM_ERRO_UPLOAD;
import static br.ufc.cin.util.Constants.MENSAGEM_JOGO_ATUALIZADO;
import static br.ufc.cin.util.Constants.MENSAGEM_JOGO_CADASTRADO;
import static br.ufc.cin.util.Constants.MENSAGEM_JOGO_INEXISTENTE;
import static br.ufc.cin.util.Constants.MENSAGEM_JOGO_REMOVIDO;
import static br.ufc.cin.util.Constants.MENSAGEM_PERMISSAO_NEGADA;
import static br.ufc.cin.util.Constants.MENSAGEM_USUARIOS_NAO_ASSOCIADOS;
import static br.ufc.cin.util.Constants.PAGINA_CADASTRAR_JOGO;
import static br.ufc.cin.util.Constants.PAGINA_DETALHES_JOGO;
import static br.ufc.cin.util.Constants.PAGINA_LISTAR_JOGO;
import static br.ufc.cin.util.Constants.PAGINA_LISTAR_USUARIOS;
import static br.ufc.cin.util.Constants.REDIRECT_PAGINA_LISTAR_JOGO;
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
import br.ufc.cin.service.JogoService;
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

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return REDIRECT_PAGINA_LISTAR_JOGO;
	}

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(Model model, HttpSession session) {
		Usuario usuario = getUsuarioLogado(session);
		Integer idUsuarioLogado = usuario.getId();
		model.addAttribute("jogos", jogoService.getJogoByProfessor(idUsuarioLogado));
		model.addAttribute("usuario", usuario);
		model.addAttribute("action", "home");
		if (usuarioService.isProfessor(usuario)) {
			return PAGINA_LISTAR_JOGO;
		}
		return PAGINA_LISTAR_JOGO;

	}
	
	@RequestMapping(value ="/novo-jogo", method = RequestMethod.GET)
	public String cadastrarFrom(Model model, HttpSession session){
		Usuario usuario =  getUsuarioLogado(session);
		model.addAttribute("action", "cadastrar");
		model.addAttribute("jogo", new Jogo());	
		model.addAttribute("participantes", usuarioService.getPossiveisParticipantes(usuario));
		
		return PAGINA_CADASTRAR_JOGO;
	}
	
	@RequestMapping(value = "/novo-jogo", method = RequestMethod.POST)
	public  String cadastrar(@RequestParam(value = "idParticipantes", required = false) List<String> idAlunos,
			@ModelAttribute("jogo") Jogo jogo, @RequestParam("anexos") List<MultipartFile> anexos, 
			BindingResult result, HttpSession session, RedirectAttributes redirect, Model model){
		
		Usuario usuario = getUsuarioLogado(session);
		model.addAttribute("participantes", usuarioService.getPossiveisParticipantes(usuario));
		model.addAttribute("action", "cadastrar");
		if (result.hasErrors()) {
			model.addAttribute("erro", MENSAGEM_ERRO_AO_CADASTRAR_JOGO);
			return PAGINA_CADASTRAR_JOGO;
		}
		
		jogo.setProfessor(usuarioService.find(Usuario.class, usuario.getId()));
		
		if(idAlunos != null && !idAlunos.isEmpty()) {
			List<Usuario> alunos = new ArrayList<Usuario>();
			for(String idaluno : idAlunos) {
				alunos.add(usuarioService.find(Usuario.class, new Integer(idaluno)));
			}
			jogo.setAlunos(alunos);
		}
		
		List<Documento> documentos = new ArrayList<Documento>();
		
		if(anexos != null && !anexos.isEmpty()) {
			for(MultipartFile anexo : anexos) {
				try {
					if(anexo.getBytes() != null && anexo.getBytes().length != 0) {
						Documento documento = new Documento();
						documento.setArquivo(anexo.getBytes());
						documento.setNomeOriginal(anexo.getOriginalFilename());
						documento.setNome(anexo.getName());
						documento.setExtensao(anexo.getContentType());
						documento.setJogo(jogo);
						documentos.add(documento);
					}
				} catch (IOException e) {
					model.addAttribute("erro", MENSAGEM_ERRO_UPLOAD);
					return PAGINA_CADASTRAR_JOGO;
				}
			}
		}
		jogoService.save(jogo);	
		
		if(!documentos.isEmpty()) {
			documentoService.salvar(documentos);
		}
		redirect.addFlashAttribute("info", MENSAGEM_JOGO_CADASTRADO);
		return REDIRECT_PAGINA_LISTAR_JOGO;
	}
	
	@RequestMapping(value = "/{id}/editar", method = RequestMethod.GET)
	public String editarForm(@PathVariable("id") Integer id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {		
		Jogo jogo = jogoService.find(Jogo.class, id);
		
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		
		Usuario usuario = getUsuarioLogado(session);
		
		if (usuario.getId() == jogo.getProfessor().getId()) {
			model.addAttribute("jogo", jogo);
			model.addAttribute("participantes", usuarioService.getPossiveisParticipantes(usuario));
		 	model.addAttribute("action", "editar");
			return PAGINA_CADASTRAR_JOGO;
		}

		redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
		return REDIRECT_PAGINA_LISTAR_JOGO;
	}
	
	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	public String editar(@RequestParam(value = "idParticipantes", required = false) List<String> idAlunos,
			@RequestParam("anexos") List<MultipartFile> anexos, @Valid Jogo jogo, 
			BindingResult result, Model model, HttpSession session,	RedirectAttributes redirect) {
		
		Usuario usuario = getUsuarioLogado(session);
		model.addAttribute("participantes", usuarioService.getPossiveisParticipantes(usuario));
		model.addAttribute("action", "editar");
		if (result.hasErrors()) {
			return PAGINA_CADASTRAR_JOGO;
		}
		
		jogo.setProfessor(usuarioService.find(Usuario.class, usuario.getId()));
		
		if(idAlunos != null && !idAlunos.isEmpty()) {
			List<Usuario> alunos = new ArrayList<Usuario>();
			
			for(String idaluno : idAlunos) {
				
				Usuario aluno = usuarioService.find(Usuario.class, new Integer(idaluno));
				aluno.addJogoParticipa(jogo);
				alunos.add(aluno);
				
			}
			jogo.setAlunos(alunos);
		}
		
		List<Documento> documentos = new ArrayList<Documento>();	
		documentos = documentoService.getDocumentoByProjeto(jogo);
		if(anexos != null && !anexos.isEmpty()) {
			
			for(MultipartFile anexo : anexos) {
				try {
					if(anexo.getBytes() != null && anexo.getBytes().length != 0) {
						Documento documento = new Documento();
						documento.setArquivo(anexo.getBytes());
						documento.setNomeOriginal(anexo.getOriginalFilename());
						documento.setExtensao(anexo.getContentType());
						documento.setNome(anexo.getName());
						documento.setJogo(jogo);						
						documentos.add(documento);
					}
				} catch (IOException e) {
					model.addAttribute("erro", MENSAGEM_ERRO_UPLOAD);
					return PAGINA_CADASTRAR_JOGO;
				}
			}
			if(!documentos.isEmpty()) {
				documentoService.salvar(documentos);
			}
		}else{
			model.addAttribute("erro", MENSAGEM_ADD_ANEXO);
			return PAGINA_CADASTRAR_JOGO;
		}
		
		jogoService.update(jogo);
		
		redirect.addFlashAttribute("info", MENSAGEM_JOGO_ATUALIZADO);
		return "redirect:/jogo/"+jogo.getId()+"/detalhes";
	}
	
	@RequestMapping(value = "/{id}/detalhes")
	public String verDetalhes(@PathVariable("id") Integer id, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		model.addAttribute("action", "detalhes");
		Jogo jogo = jogoService.find(Jogo.class, id);
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Usuario usuario = getUsuarioLogado(session);
		if (usuario.getId() == jogo.getProfessor().getId()) {			
			model.addAttribute("jogo", jogo);
			return PAGINA_DETALHES_JOGO;
		}else{			
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
	}
	
	@RequestMapping(value = "/{id}/excluir")
	public String excluir(@PathVariable("id") Integer id, HttpSession session, RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class,id);
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Usuario usuario = getUsuarioLogado(session);
		if (usuario.getId() == jogo.getProfessor().getId()) {
			jogoService.delete(jogo);
			redirectAttributes.addFlashAttribute("info", MENSAGEM_JOGO_REMOVIDO);
		} else {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
		}
		return REDIRECT_PAGINA_LISTAR_JOGO;

	}
	
	@RequestMapping(value = "/{id}/participantes", method = RequestMethod.GET)
	public String listarParticipantes(Model model, HttpSession session, @PathVariable("id") Integer id,RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class,id);
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		model.addAttribute("jogo", jogo);
		model.addAttribute("action","participantes");
		List<Usuario> usuarios = jogo.getAlunos();
		if(usuarios == null || usuarios.isEmpty()){
			model.addAttribute("erro", MENSAGEM_USUARIOS_NAO_ASSOCIADOS);
		}
		model.addAttribute("usuarios", usuarios);
		
		return PAGINA_LISTAR_USUARIOS;

	}
	
	@RequestMapping(value = "/{id}/equipes", method = RequestMethod.GET)
	public String listarEquipes(Model model, HttpSession session, @PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class,id);
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		model.addAttribute("jogo", jogo);
		model.addAttribute("action","equipes");
		List<Equipe> equipes = jogo.getEquipes();
		if(equipes == null || equipes.isEmpty()){
			model.addAttribute("erro", MENSAGEM_EQUIPES_NAO_CRIADAS);
		}
		model.addAttribute("equipes", equipes);
		
		return PAGINA_LISTAR_EQUIPES;

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