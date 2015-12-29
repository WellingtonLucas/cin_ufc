package br.ufc.cin.web;

import java.security.Principal;

import javax.servlet.http.HttpSession;



import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.ufc.cin.model.Usuario;

@Controller
public class LoginController {
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) {

		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error", "Usuário e/ou senha inválidos!");
		}

		if (logout != null) {
			model.addObject("msg", "You've been logged out successfully.");
		}
		
		model.addObject("usuario", new Usuario());
		model.setViewName("login");
		
		return model;

	}

	@RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
	public String loginerror(ModelMap model) {
		model.addAttribute("usuario", new Usuario());
		model.addAttribute("error", "Usuário e/ou senha inválidos!");
		return "login";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(ModelMap model, HttpSession session) {
		session.invalidate();
		return "login";
	}
	
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String acessoNegado(ModelMap model, Principal user) {
		if (user != null) {
			model.addAttribute("message", "Olá, " + user.getName() 
			+ ", você não tem permissão para acessar essa página!");
		} else {
			model.addAttribute("message", 
			"Você não tem permissão para acessar essa página!");
		}
		return "403";
	}
	
	@RequestMapping(value = "/400", method = RequestMethod.POST)
	public String paginaRequisicaoNaoPresente(ModelMap model, Principal user) {
		model.addAttribute("message", "Verifique se preencheu todos os campos requeridos.");
		return "400";
	}
	
	@RequestMapping(value = "/404", method = RequestMethod.GET)
	public String paginaInexistente(ModelMap model, Principal user) {
		model.addAttribute("message", "Oops, página não encontrada.");
		return "404";
	}
	
	@RequestMapping(value = "/500", method = RequestMethod.GET)
	public String paginaNaoEncontrada(ModelMap model, Principal user) {
		model.addAttribute("message", "Oops, página não encontrada.");
		return "500";
	}
		
}