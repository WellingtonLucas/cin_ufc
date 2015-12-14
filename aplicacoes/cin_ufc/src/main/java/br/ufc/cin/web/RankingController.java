package br.ufc.cin.web;

import static br.ufc.cin.util.Constants.MENSAGEM_JOGO_INEXISTENTE;
import static br.ufc.cin.util.Constants.MENSAGEM_PERMISSAO_NEGADA;
import static br.ufc.cin.util.Constants.REDIRECT_PAGINA_LISTAR_JOGO;
import static br.ufc.cin.util.Constants.USUARIO_LOGADO;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Nota;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.SaldoNaRodada;
import br.ufc.cin.model.SaldoPorJogo;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.service.ApostaService;
import br.ufc.cin.service.JogoService;
import br.ufc.cin.service.RankingService;
import br.ufc.cin.service.RegrasService;
import br.ufc.cin.service.RodadaService;
import br.ufc.cin.service.UsuarioService;

@Controller
public class RankingController {
	
	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private JogoService jogoService;
	
	@Inject
	private RodadaService rodadaService;
	
	@Inject
	private RankingService rankingService;
	
	@Inject
	private ApostaService apostaService;
	
	@Inject
	private RegrasService regrasService;
	
	@RequestMapping(value = "/jogo/{idJogo}/rodada/{idRodada}/publicarRankings", method = RequestMethod.GET)
	public String publicarRanking(@PathVariable("idJogo") Integer idJogo,
			@PathVariable("idRodada") Integer idRodada, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		if (jogo == null) {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_JOGO_INEXISTENTE);
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		
		Usuario usuario = getUsuarioLogado(session);
		if(!jogo.getProfessor().equals(usuario)){
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_PERMISSAO_NEGADA);
			return "redirect:/jogo/listar";
		}
		Rodada rodada = rodadaService.find(Rodada.class, idRodada);
		if (rodada == null || !jogo.getRodadas().contains(rodada)) {
			redirectAttributes.addFlashAttribute("erro",
					"Rodada solicitada não existe.");
			return "redirect:/jogo/" + idJogo + "/rodadas";
		}
		if(!rodada.isStatusRaking()){
			try{
				apostaService.atualizaSaldoEquipes(jogo,rodada);
				apostaService.atualizaSaldoAlunos(jogo,rodada);
				rodada.setStatusRaking(true);
				rodadaService.update(rodada);
			}catch(Exception e){
				redirectAttributes.addFlashAttribute("erro",
						"Erro ao tentar publicar o ranking.");
				return "redirect:/jogo/" + idJogo + "/rodada/"+idRodada+"/detalhes";
			}
		}
		redirectAttributes.addFlashAttribute("info",
				"Os rankings e notas dessa rodada já podem ser consultados.");
		return "redirect:/jogo/" + idJogo + "/rodada/"+idRodada+"/detalhes";
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/rodada/{idRodada}/rankings", method = RequestMethod.GET)
	public String rankingsRodada(@PathVariable("idJogo") Integer idJogo, 
			@PathVariable("idRodada") Integer idRodada,HttpSession session,
			RedirectAttributes redirectAttributes, Model model) {

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
		if(!rodada.isStatusRaking()){
			redirectAttributes.addFlashAttribute("erro",
					"O ranking da rodada ainda não está disponível, aguarde.");
			return "redirect:/jogo/" + idJogo + "/rodada/"+idRodada+"/detalhes";
		}
		
		Usuario usuario = getUsuarioLogado(session);
		if (usuario.equals(jogo.getProfessor())) {
			model.addAttribute("permissao", "professor");
		} else if(jogo.getAlunos().contains(usuario)){
			model.addAttribute("permissao", "aluno");
		}else {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_PERMISSAO_NEGADA);
		}
		model.addAttribute("jogo", jogo);
		model.addAttribute("rodada", rodada);
		model.addAttribute("action", "detalhesRodada");
		return "ranking/rankingsRodada";
	}

	@RequestMapping(value = "/jogo/{idJogo}/rodada/{idRodada}/alunos", method = RequestMethod.GET)
	public String alunos(@PathVariable("idJogo") Integer idJogo,@PathVariable("idRodada") Integer idRodada,
			HttpSession session,
			RedirectAttributes redirectAttributes, Model model) {

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
		if(!rodada.isStatusRaking()){
			redirectAttributes.addFlashAttribute("erro",
					"O ranking da rodada ainda não está disponível, aguarde.");
			return "redirect:/jogo/" + idJogo + "/rodada/"+idRodada+"/detalhes";
		}
		
		Usuario usuario = getUsuarioLogado(session);
		if (usuario.equals(jogo.getProfessor())) {
			model.addAttribute("permissao", "professor");
		} else if(jogo.getAlunos().contains(usuario)){
			model.addAttribute("permissao", "aluno");
		}else {
			redirectAttributes.addFlashAttribute("erro",
					MENSAGEM_PERMISSAO_NEGADA);
		}
		List<Nota> notas = rankingService.ordenaNotas(rodada);
		model.addAttribute("usuario", usuario);
		model.addAttribute("jogo", jogo);
		model.addAttribute("notas", notas);
		model.addAttribute("rankingAlunos", true);
		model.addAttribute("action", "detalhesRodada");
		model.addAttribute("rodada", rodada);
		return "ranking/rankingsRodada";
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/rodada/{idRodada}/equipes", method = RequestMethod.GET)
	public String equipes(@PathVariable("idJogo") Integer idJogo,@PathVariable("idRodada") Integer idRodada,
			HttpSession session,
			RedirectAttributes redirectAttributes, Model model) {

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
		if(!rodada.isStatusRaking()){
			redirectAttributes.addFlashAttribute("erro",
					"O ranking da rodada ainda não está disponível, aguarde.");
			return "redirect:/jogo/" + idJogo + "/rodada/"+idRodada+"/detalhes";
		}
		
		Usuario usuario = getUsuarioLogado(session);
		String permissao  = "";
		try {
			permissao = usuarioService.definePermissao(jogo,usuario);	
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro",e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		
		List<SaldoNaRodada> saldos = rankingService.ordenaSaldos(rodada);
		model.addAttribute("jogo", jogo);
		model.addAttribute("permissao", permissao);
		model.addAttribute("saldos", saldos);
		model.addAttribute("rankingEquipes", true);
		model.addAttribute("rodada", rodada);
		model.addAttribute("action", "detalhesRodada");
		return "ranking/rankingsRodada";
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/rankings", method = RequestMethod.GET)
	public String rankingGeral(@PathVariable("idJogo") Integer idJogo,
			HttpSession session, RedirectAttributes redirectAttributes, Model model) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Usuario usuario = getUsuarioLogado(session);
		try{
			regrasService.verificaJogo(jogo);
			regrasService.verificaParticipacao(usuario, jogo);
		}catch(IllegalArgumentException e){
			redirectAttributes.addFlashAttribute("erro",e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		String permissao  = "";
		try {
			permissao = usuarioService.definePermissao(jogo,usuario);	
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro",e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		model.addAttribute("permissao", permissao);
		model.addAttribute("jogo", jogo);
		return "ranking/rankingsJogo";
	}
	@RequestMapping(value = "/jogo/{idJogo}/rankings-equipes", method = RequestMethod.GET)
	public String rankingGeralEquipes(@PathVariable("idJogo") Integer idJogo,
			HttpSession session, RedirectAttributes redirectAttributes, Model model) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Usuario usuario = getUsuarioLogado(session);
		String permissao  = "";
		List<Equipe> equipes = new ArrayList<Equipe>();
		try{
			regrasService.verificaJogo(jogo);
			regrasService.verificaParticipacao(usuario, jogo);
			regrasService.verificaStatusRanking(jogo);
			permissao = usuarioService.definePermissao(jogo,usuario);
			equipes = rankingService.ordenaEquipes(jogo);
		}catch(IllegalArgumentException e){
			redirectAttributes.addFlashAttribute("erro",e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		
		model.addAttribute("rankingEquipes", true);
		model.addAttribute("permissao", permissao);
		model.addAttribute("jogo", jogo);
		model.addAttribute("equipes", equipes);
		return "ranking/rankingsJogo";
	}
	
	@RequestMapping(value = "/jogo/{idJogo}/rankings-alunos", method = RequestMethod.GET)
	public String rankingGeralAlunos(@PathVariable("idJogo") Integer idJogo,
			HttpSession session, RedirectAttributes redirectAttributes, Model model) {
		Jogo jogo = jogoService.find(Jogo.class, idJogo);
		Usuario usuario = getUsuarioLogado(session);
		String permissao  = "";
		List<SaldoPorJogo> saldosPorJogo = new ArrayList<SaldoPorJogo>();
		try{
			regrasService.verificaJogo(jogo);
			regrasService.verificaParticipacao(usuario, jogo);
			regrasService.verificaStatusRanking(jogo);
			permissao = usuarioService.definePermissao(jogo,usuario);
			saldosPorJogo = rankingService.ordenaSaldosPorJogo(jogo);
		}catch(IllegalArgumentException e){
			redirectAttributes.addFlashAttribute("erro",e.getMessage());
			return REDIRECT_PAGINA_LISTAR_JOGO;
		}
		
		model.addAttribute("rankingAlunos", true);
		model.addAttribute("permissao", permissao);
		model.addAttribute("jogo", jogo);
		model.addAttribute("saldos", saldosPorJogo);
		return "ranking/rankingsJogo";
	}
	private Usuario getUsuarioLogado(HttpSession session) {
		if (session.getAttribute(USUARIO_LOGADO) == null) {
			Usuario usuario = usuarioService
					.getUsuarioByEmail(SecurityContextHolder.getContext()
							.getAuthentication().getName());
			session.setAttribute(USUARIO_LOGADO, usuario);
		};
		return (Usuario) session.getAttribute(USUARIO_LOGADO);
	}
}
