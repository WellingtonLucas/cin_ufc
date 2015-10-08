package br.ufc.cin.web;

import static br.ufc.cin.util.Constants.MENSAGEM_JOGO_INEXISTENTE;
import static br.ufc.cin.util.Constants.MENSAGEM_PERMISSAO_NEGADA;
import static br.ufc.cin.util.Constants.REDIRECT_PAGINA_LISTAR_JOGO;
import static br.ufc.cin.util.Constants.REDIRECT_PAGINA_LOGIN;
import static br.ufc.cin.util.Constants.USUARIO_LOGADO;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Resposta;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.service.EquipeService;
import br.ufc.cin.service.JogoService;
import br.ufc.cin.service.RespostaService;
import br.ufc.cin.service.UsuarioService;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private JogoService jogoService;

	@Inject
	private EquipeService equipeService;

	@Inject
	private RespostaService respostaService;
	
	@RequestMapping(value = "/cadastre-se", method = RequestMethod.POST)
	public String cadastrarPessoa(HttpSession session, Model model,
			@Valid @ModelAttribute("usuario") Usuario usuario,
			BindingResult result, RedirectAttributes redirect) {
		
		if (result.hasErrors()) {
			model.addAttribute("cadastro", true);
			return "login";
		}
		if(usuarioService.getUsuarioByEmail(usuario.getEmail()) != null){
			redirect.addFlashAttribute("erro",
					"Erro, já existe um usuário cadastrado com esse email, por favor escolha outro.");
			return REDIRECT_PAGINA_LOGIN;
		}
			
		usuario.setHabilitado(true);
		ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
		usuario.setSenha(encoder.encodePassword(usuario.getSenha(), ""));
		usuario.setPapel("ROLE_USUARIO");
		usuarioService.save(usuario);

		redirect.addFlashAttribute("info",
				"Parabéns, seu cadastro esta realizado.");
		return REDIRECT_PAGINA_LOGIN;
	}

	@RequestMapping(value = "/{id}/detalhes/{idJogo}", method = RequestMethod.GET)
	public String verDetalhes(@PathVariable("id") Integer id,
			@PathVariable("idJogo") Integer idJogo, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		Usuario logado = getUsuarioLogado(session);
		logado = usuarioService.find(Usuario.class, logado.getId());
		
		if(!jogo.isStatus() && jogo.getAlunos().contains(logado)){
			redirectAttributes.addFlashAttribute("erro",
					"Jogo inativado no momento. Para mais informações "+jogo.getProfessor().getEmail());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}else if(!jogo.getAlunos().contains(logado) && !jogo.getProfessor().equals(logado)){
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_PERMISSAO_NEGADA);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		
		Usuario usuario = usuarioService.find(Usuario.class, id);
		Equipe equipe = equipeService.equipePorAlunoNoJogo(usuario, jogo);
		if (usuario == null) {
			redirectAttributes.addFlashAttribute("erro", "Usuário inexistente");
			return "redirect:/jogo/" + idJogo + "/participantes";
		}
		if(!jogo.getAlunos().contains(usuario)) {
			redirectAttributes.addFlashAttribute("erro", "Usuário não faz parte do jogo.");
			return "redirect:/jogo/" + idJogo + "/participantes";
		}
		if(!jogo.getEquipes().contains(equipe)) {
			redirectAttributes.addFlashAttribute("erro", "Usuário não faz parte de equipe no jogo.");
			return "redirect:/jogo/" + idJogo + "/participantes";
		}
		
		if(logado.equals(jogo.getProfessor())){
			model.addAttribute("permissao", "professor");
		}else  if(logado.getEquipes().contains(equipe)){
			model.addAttribute("permissao", "aluno");
		}else{
			redirectAttributes.addFlashAttribute("erro", "Você não possui permissão de acesso.");
			return "redirect:/jogo/" + idJogo + "/equipe/"+equipe.getId();
		}
		model.addAttribute("usuario", logado);
		model.addAttribute("jogo", jogo);
		model.addAttribute("action", "detalhesUsuario");
		model.addAttribute("usuarioParticipante", usuario);
		model.addAttribute("equipe", equipe);
		return "jogador/usuario";
		
	}

	@RequestMapping(value = "/perfil", method = RequestMethod.GET)
	public String perfil(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		Usuario usuario = getUsuarioLogado(session);
		model.addAttribute("usuario", usuario);
		return "jogador/perfil";
	}
	
	@RequestMapping(value = "/atualizar", method = RequestMethod.POST)
	public String atualizar(Model model, HttpSession session, RedirectAttributes redirectAttributes, 
			@Valid Usuario usuario, BindingResult result) {
		Usuario perfilAnterior = usuarioService.find(Usuario.class,usuario.getId());
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar seus dados.");
			return "redirect:/usuario/perfil";
		}
		if(!(usuario.getSenha().isEmpty())){
			ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
			perfilAnterior.setSenha(encoder.encodePassword(usuario.getSenha(), ""));
		}
		perfilAnterior.setNome(usuario.getNome());
		perfilAnterior.setSobreNome(usuario.getSobreNome());
		perfilAnterior.setMatricula(usuario.getMatricula());
		perfilAnterior.setCurso(usuario.getCurso());
		perfilAnterior.setEmail(usuario.getEmail());
		usuarioService.update(perfilAnterior);
		return "redirect:/usuario/perfil";
	}
	
	@RequestMapping(value = "/{id}/jogo/{idJogo}/avaliacoes", method = RequestMethod.GET)
	public String avaliacoes(@PathVariable("idJogo") Integer idJogo,
			@PathVariable("id") Integer id, Model model,
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

		Usuario usuarioRequisitado = usuarioService.find(Usuario.class, id);
		List<Resposta> respostas = respostaService.find(usuarioRequisitado, jogo);
		
		if(respostas.isEmpty() || respostas == null){
			redirectAttributes.addFlashAttribute("erro",
					"Ainda não existem avaliações efetuadas pelo aluno neste jogo.");
			return "redirect:/usuario/"+usuarioRequisitado.getId()+"/detalhes/"+jogo.getId();
		}
		if (usuario.equals(jogo.getProfessor())) {
			model.addAttribute("permissao", "professor");
		}else {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_PERMISSAO_NEGADA);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		model.addAttribute("action", "avaliacoesDoAluno");
		model.addAttribute("jogo", jogo);
		model.addAttribute("respostas", respostas);
		model.addAttribute("usuario", usuario);
		model.addAttribute("usuarioRequisitado", usuarioRequisitado);
		return "jogador/avaliacoes";
	}

	@RequestMapping(value = "/{id}/jogo/{idJogo}/resposta/{idRes}/avaliacao", method = RequestMethod.GET)
	public String avaliacao(@PathVariable("idJogo") Integer idJogo, @PathVariable("idRes") Integer idRes,
			@PathVariable("id") Integer id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {

		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		if(jogo == null){
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		
		Resposta resposta = respostaService.find(Resposta.class, idRes);
		if (resposta == null) {
			redirectAttributes.addFlashAttribute("erro", "Avaliação inexistente.");
			return "redirect:/jogo/"+ idJogo +"/rodadas";
		}
		Usuario requisitado = usuarioService.find(Usuario.class, id);
		if(!jogo.getAlunos().contains(requisitado)){
			redirectAttributes.addFlashAttribute("erro",
					"Aluno não existe ou não pertence ao jogo.");
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
		}else{			
			redirectAttributes.addFlashAttribute("erro", MENSAGEM_PERMISSAO_NEGADA);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		model.addAttribute("usuario", usuario);
		model.addAttribute("usuarioRequisitado", requisitado);
		model.addAttribute("action", "avaliacaoDoAluno");
		model.addAttribute("formulario", resposta.getFormulario());
		model.addAttribute("jogo", jogo);
		model.addAttribute("entrega", resposta.getEntrega());
		return "jogador/avaliacao";
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
