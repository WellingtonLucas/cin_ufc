package br.ufc.cin.web;

import static br.ufc.cin.util.Constants.*;
import static br.ufc.cin.util.Constants.USUARIO_LOGADO;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.cin.model.Documento;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.service.DocumentoService;
import br.ufc.cin.service.JogoService;
import br.ufc.cin.service.RegrasService;
import br.ufc.cin.service.UsuarioService;

@Controller
@RequestMapping("documento")
public class DocumentoController {
	
	@Inject
	private DocumentoService documentoService;
	
	@Inject
	private JogoService jogoService;
	
	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private RegrasService regrasService;
	
	@RequestMapping(value = "/{idJogo}/{idArquivo}", method = RequestMethod.GET)
	public void getArquivo(@PathVariable("idJogo") Integer idJogo, @PathVariable("idArquivo") Integer idArquivo, HttpServletResponse response, 
			 HttpSession session) {
		try {
			Jogo jogo = jogoService.find(Jogo.class, idJogo);
			Usuario usuario = getUsuarioLogado(session);
			regrasService.verificaJogo(jogo);
			regrasService.verificaSeProfessor(usuario, jogo);
			Documento documento = documentoService.find(Documento.class,idArquivo);
			if(documento != null) {
				InputStream is = new ByteArrayInputStream(documento.getArquivo());
				response.setContentType(documento.getExtensao());
				response.setHeader("Content-Disposition", "attachment; filename=" + documento.getNomeOriginal());
				IOUtils.copy(is, response.getOutputStream());
				response.flushBuffer();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = {"/downloadDocumento/{id}"}, method = RequestMethod.GET)
	public HttpEntity<byte[]> downloadDocumento(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
		Documento documento = documentoService.find(Documento.class, id);
		byte[] arquivo = documento.getArquivo();
		String[] tipo = documento.getExtensao().split("/");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType(tipo[0], tipo[1]));
		headers.set("Content-Disposition", "attachment; filename=" + documento.getNomeOriginal().replace(" ", "_"));
		headers.setContentLength(arquivo.length);

		redirectAttributes.addFlashAttribute("infos", "Download do Documento realizado com sucesso");
		return new HttpEntity<byte[]>(arquivo, headers);
	}
	
	@RequestMapping(value = {"/{id}"}, method = RequestMethod.GET)
	public String documento(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
		Documento documento = documentoService.find(Documento.class, id);
		return "redirect:/documento/"+documento.getJogo().getId()+"/"+documento.getId();	
	}
	
	@RequestMapping(value = "/ajax/remover/{id}", method = RequestMethod.GET)
	private String excluirDocumento(@PathVariable("id") Integer id){
		return REDIRECT_PAGINA_LISTAR_JOGO;
	}
	
	@RequestMapping(value = "/ajax/remover/{id}", method = RequestMethod.POST)
	@ResponseBody 
	public  ModelMap excluirDocumento(@PathVariable("id") Integer id, HttpSession session) {
		ModelMap model = new ModelMap();
		Documento documento = documentoService.find(Documento.class,id);
		if(documento == null) {
			model.addAttribute("result", "erro");
			model.addAttribute("mensagem", MENSAGEM_DOCUMENTO_INEXISTENTE);
			return model;
		}
		
		documentoService.delete(documento);
		model.addAttribute("result", "ok");
		return model;
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
