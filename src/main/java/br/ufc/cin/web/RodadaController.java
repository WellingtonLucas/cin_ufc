package br.ufc.cin.web;

import static br.ufc.cin.util.Constants.MENSAGEM_JOGO_INEXISTENTE;
import static br.ufc.cin.util.Constants.MENSAGEM_PERMISSAO_NEGADA;
import static br.ufc.cin.util.Constants.REDIRECT_PAGINA_LISTAR_JOGO;
import static br.ufc.cin.util.Constants.USUARIO_LOGADO;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.service.JogoService;
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
	
	@RequestMapping(value ="/jogo/{id}/rodadas", method = RequestMethod.GET)
	public String rodada(@PathVariable("id") Integer id, Model model, HttpSession session,
			RedirectAttributes redirectAttributes){
		Usuario usuario = getUsuarioLogado(session);
		Jogo jogo = jogoService.find(Jogo.class, id);
		if(jogo == null){
			redirectAttributes.addFlashAttribute("erro", "Jogo inexistente.");
			return "redirect:/jogo/listar";
		}
		if(jogo.getProfessor().equals(usuario)){
			model.addAttribute("action", "rodadas");
			model.addAttribute("permissao", "professor");
			if((jogo.getRodadas() == null) || (jogo.getRodadas().isEmpty())){
				model.addAttribute("info", "Nenhuma rodada cadastrada no momento.");
			}else{
				model.addAttribute("rodadas", jogo.getRodadas());
			}
			model.addAttribute("jogo", jogo);
			return "rodada/listar";
		}
		model.addAttribute("action", "rodadas");
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
		if(jogo.getProfessor().equals(usuario)){
			model.addAttribute("action", "cadastrar");
			model.addAttribute("editor", "rodada");
			model.addAttribute("jogo", jogo);
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
			rodadaService.save(rodada);
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
		Rodada rodada = rodadaService.find(Rodada.class, id);
		model.addAttribute("action", "detalhesRodada");
		if (rodada == null || !jogo.getRodadas().contains(rodada)) {
			redirectAttributes.addFlashAttribute("erro",
					"Rodada solicitada não existe.");
			return "redirect:/jogo/" + idJogo + "/rodadas";
		}
		Usuario usuario = getUsuarioLogado(session);
		model.addAttribute("editor", "rodada");
		if (usuario.getId() == jogo.getProfessor().getId() && jogo.getRodadas().contains(rodada)) {
			model.addAttribute("rodada", rodada);
			model.addAttribute("jogo", jogo);
			model.addAttribute("permissao", "professor");
			return "rodada/detalhes";
		}else if(jogo.getAlunos().contains(usuario)){
			model.addAttribute("rodada", rodada);
			model.addAttribute("jogo", jogo);
			model.addAttribute("permissao", "participante");
			return "rodada/detalhes";
		}else {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_PERMISSAO_NEGADA);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
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
		
		if (usuario.getId() == jogo.getProfessor().getId()) {
			model.addAttribute("jogo", jogo);
			model.addAttribute("rodada", rodada);
			model.addAttribute("editor", "rodada");
			model.addAttribute("action", "editar");
			return "rodada/novaRodada";
		}
		redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
		return REDIRECT_PAGINA_LISTAR_JOGO;
	}

	@RequestMapping(value = "/{id}/rodada/editar", method = RequestMethod.POST)
	public String editar(@PathVariable("id") Integer id, @Valid Rodada rodada, BindingResult result, HttpSession session,
			RedirectAttributes redirect, Model model) {
		Jogo jogo = jogoService.find(Jogo.class, id);
		model.addAttribute("editor", "rodada");
		model.addAttribute("action", "editar");

		if (result.hasErrors()) {
			redirect.addFlashAttribute("erro", "Erro ao editar rodada.");
			return "redirect:/jogo/" + id + "/rodada/" + rodada.getId()
					+ "/editar";
		}
		if (jogo == null) {
			redirect.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		rodada.setJogo(jogo);
		try{
			rodadaService.update(rodada);
			redirect.addFlashAttribute("info", "Rodada atualizada com sucesso.");
		}catch(Exception e){
			redirect.addFlashAttribute("erro", "Não foi possível atualizar a rodada.");
		}
		return "redirect:/jogo/" + id + "/rodada/"+rodada.getId()+"/detalhes";
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
