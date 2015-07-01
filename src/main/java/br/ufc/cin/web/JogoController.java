package br.ufc.cin.web;


import static br.ufc.cin.util.Constants.MENSAGEM_ERRO_UPLOAD;
import static br.ufc.cin.util.Constants.MENSAGEM_JOGO_ATUALIZADO;
import static br.ufc.cin.util.Constants.MENSAGEM_JOGO_CADASTRADO;
import static br.ufc.cin.util.Constants.MENSAGEM_JOGO_INEXISTENTE;
import static br.ufc.cin.util.Constants.MENSAGEM_PERMISSAO_NEGADA;
import static br.ufc.cin.util.Constants.PAGINA_CADASTRAR_JOGO;
import static br.ufc.cin.util.Constants.PAGINA_DETALHES_JOGO;
import static br.ufc.cin.util.Constants.PAGINA_LISTAR_JOGO;
import static br.ufc.cin.util.Constants.REDIRECT_PAGINA_LISTAR_JOGO;
import static br.ufc.cin.util.Constants.*;

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
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Professor;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.service.DocumentoService;
import br.ufc.cin.service.JogoService;
import br.ufc.cin.service.ProfessorService;
import br.ufc.cin.service.UsuarioService;


@Controller
@RequestMapping("jogo")
public class JogoController {

	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private JogoService jogoService;
	
	@Inject
	private ProfessorService professorService;
	
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
		if (usuarioService.isProfessor(usuario)) {
			return PAGINA_LISTAR_JOGO;
		}
		return PAGINA_LISTAR_JOGO;

	}
	
	@RequestMapping(value ="/novo-jogo", method = RequestMethod.GET)
	public String cadastrarFrom(Model model){
		model.addAttribute("jogo", new Jogo());		
		return PAGINA_CADASTRAR_JOGO;
	}
	
	@RequestMapping(value = "/novo-jogo", method = RequestMethod.POST)
	public  String cadastrar(@ModelAttribute("jogo") Jogo jogo, @RequestParam("anexos") List<MultipartFile> anexos, 
			BindingResult result, HttpSession session, RedirectAttributes redirect, Model model){
		Usuario usuario = getUsuarioLogado(session);
		model.addAttribute("action", "cadastrar");
		if (result.hasErrors()) {
			return PAGINA_CADASTRAR_JOGO;
		}
		
		jogo.setProfessor(professorService.find(Professor.class, usuario.getId()));
		
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
		if (usuario.getId() == jogo.getProfessor().getId_professor()) {
			model.addAttribute("jogo", jogo);
			//model.addAttribute("participantes", pessoaService.getParticipantes(usuario));
			model.addAttribute("action", "editar");
			return PAGINA_CADASTRAR_JOGO;
		}

		redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
		return REDIRECT_PAGINA_LISTAR_JOGO;
	}
	
	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	public String editar(@RequestParam(value = "idParticipantes", required = false) List<String> idParticipantes, @RequestParam("anexos") List<MultipartFile> anexos,
			@Valid Jogo jogo, BindingResult result, Model model, HttpSession session,
			RedirectAttributes redirect) {
		
		//model.addAttribute("participantes", pessoaService.getParticipantes(getUsuarioLogado(session)));
		model.addAttribute("action", "editar");
		if (result.hasErrors()) {
			return PAGINA_CADASTRAR_JOGO;
		}
		
		Usuario usuario = getUsuarioLogado(session);
		Professor professor = professorService.find(Professor.class, usuario.getId());
		professor.setUsuario(usuario);
		jogo.setProfessor(professor);
		
		/*if(idParticipantes != null && !idParticipantes.isEmpty()) {
			List<Pessoa> participantes = new ArrayList<Pessoa>();
			for(String idParticipante : idParticipantes) {
				participantes.add(pessoaService.getPessoaById(new Long(idParticipante)));
			}
			jogo.setParticipantes(participantes);
		}*/
		
		List<Documento> documentos = new ArrayList<Documento>();	
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
		}
		
		jogoService.update(jogo);
		
		if(!documentos.isEmpty()) {
			documentoService.salvar(documentos);
		}
							
		redirect.addFlashAttribute("info", MENSAGEM_JOGO_ATUALIZADO);
		return REDIRECT_PAGINA_LISTAR_JOGO;
	}
	
	@RequestMapping(value = "/{id}/detalhes")
	public String verDetalhes(@PathVariable("id") Integer id, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, id);
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Usuario usuario = getUsuarioLogado(session);
		if (usuario.getId() == jogo.getProfessor().getId_professor() && usuarioService.isProfessor(usuario)) {			
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
		if (usuario.getId() == jogo.getProfessor().getId_professor()) {
			jogoService.delete(jogo);
			redirectAttributes.addFlashAttribute("info", MENSAGEM_JOGO_REMOVIDO);
		} else {
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
		}
		return REDIRECT_PAGINA_LISTAR_JOGO;

	}

	private Usuario getUsuarioLogado(HttpSession session) {
		if (session.getAttribute(USUARIO_LOGADO) == null) {
			Usuario usuario = usuarioService
					.getUsuarioByLogin(SecurityContextHolder.getContext()
							.getAuthentication().getName());
			session.setAttribute(USUARIO_LOGADO, usuario);
		}
		return (Usuario) session.getAttribute(USUARIO_LOGADO);
	}
}
