package br.ufc.cin.web;

import static br.ufc.cin.util.Constants.MENSAGEM_EQUIPE_CADASTRADA;
import static br.ufc.cin.util.Constants.MENSAGEM_ERRO_AO_CADASTRAR_EQUIPE;
import static br.ufc.cin.util.Constants.PAGINA_CADASTRAR_EQUIPE;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.service.EquipeService;
import br.ufc.cin.service.JogoService;
import br.ufc.cin.service.UsuarioService;

@Controller
public class EquipeController {
	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private EquipeService equipeService;
	
	@Inject
	private JogoService jogoService;
	
	@RequestMapping(value ="/jogo/{id}/equipe/nova", method = RequestMethod.GET)
	public String cadastrarFrom(@PathVariable("id") Integer id, Model model, HttpSession session){
		Jogo jogo = jogoService.find(Jogo.class, id);
		
		model.addAttribute("action", "cadastrar");
		model.addAttribute("idJogo", id);
		model.addAttribute("equipe", new Equipe());
		model.addAttribute("participantes", jogo.getAlunos());
		
		return PAGINA_CADASTRAR_EQUIPE;
	}
	
	@RequestMapping(value = "/{id}/equipe/nova", method = RequestMethod.POST)
	public  String cadastrar(@RequestParam(value = "idParticipantes", required = false) List<String> idAlunos,
			@PathVariable("id") Integer id,	@ModelAttribute("equipe") Equipe equipe, BindingResult result, HttpSession session, 
			RedirectAttributes redirect, Model model){
		Jogo jogo = jogoService.find(Jogo.class, id);
		equipe.setJogo(jogo);
		model.addAttribute("action", "cadastrar");
		
		if (result.hasErrors()) {
			model.addAttribute("erro", MENSAGEM_ERRO_AO_CADASTRAR_EQUIPE);
			return "redirect:/jogo/"+ id +"/equipe/nova";
		}
		
		if(idAlunos != null && !idAlunos.isEmpty()) {
			List<Usuario> alunos = new ArrayList<Usuario>();
			for(String idaluno : idAlunos) {
				alunos.add(usuarioService.find(Usuario.class, new Integer(idaluno)));
			}
			equipe.setAlunos(alunos);
		}
		
		equipeService.save(equipe);	
		
		redirect.addFlashAttribute("info", MENSAGEM_EQUIPE_CADASTRADA);
		return "redirect:/jogo/"+ id +"/equipes";
	}
	
}
