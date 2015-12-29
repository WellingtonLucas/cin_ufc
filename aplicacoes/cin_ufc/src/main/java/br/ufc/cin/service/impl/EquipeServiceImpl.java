package br.ufc.cin.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Entrega;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.NotaEquipeRodada;
import br.ufc.cin.model.Resposta;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.SaldoNaRodada;
import br.ufc.cin.model.StatusRodadaEquipe;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.repository.EquipeRepository;
import br.ufc.cin.service.CalculoNotaService;
import br.ufc.cin.service.EntregaService;
import br.ufc.cin.service.EquipeService;
import br.ufc.cin.service.JogoService;
import br.ufc.cin.service.NotaEquipeRodadaService;
import br.ufc.cin.service.RespostaService;
import br.ufc.cin.service.SaldoNaRodadaService;
import br.ufc.cin.service.UsuarioService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class EquipeServiceImpl extends GenericServiceImpl<Equipe> implements
		EquipeService {

	@Inject
	private EquipeRepository equipeRepository;
	
	@Inject
	private EntregaService entregaService;
	
	@Inject
	private RespostaService respostaService;
	
	@Inject
	private CalculoNotaService calculoNotaService;

	@Inject
	private NotaEquipeRodadaService notaEquipeRodadaService;
	
	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private JogoService jogoService;
	
	@Inject
	private SaldoNaRodadaService saldoNaRodadaService;
	
	
	@Override
	public List<Usuario> alunosSemEquipe(Jogo jogo) {
		List<Usuario> alunosSemEquipe = new ArrayList<Usuario>();
		List<Usuario> alunosDeEquipes = new ArrayList<Usuario>();
		if (getEquipesByJogo(jogo) == null) {
			return jogo.getAlunos();
		} else {
			for (Equipe equipe : getEquipesByJogo(jogo)) {
				alunosDeEquipes.addAll(equipe.getAlunos());
			}
			if(alunosDeEquipes.isEmpty()){
				return jogo.getAlunos();
			}else{
				for (Usuario aluno : jogo.getAlunos()) {
					if (!alunosDeEquipes.contains(aluno) && !alunosSemEquipe.contains(aluno)) {
						alunosSemEquipe.add(aluno);
					}
				}
				return alunosSemEquipe;
			}
		}
	}

	@Override
	public List<Equipe> getEquipesByJogo(Jogo jogo) {
		return equipeRepository.getEquipeByJogo(jogo);
	}
	
	@Override
	public List<Equipe> equipesDesvinculadas(Jogo jogo, Rodada rodada) {
		jogo.getEquipes().removeAll(rodada.getJogo().getEquipes());
		return jogo.getEquipes();
	}

	@Override
	public Equipe equipePorAlunoNoJogo(Usuario aluno, Jogo jogo) {
		if(!jogo.getProfessor().equals(aluno) && jogo.getAlunos().contains(aluno)){
			for (Equipe equipe : aluno.getEquipes()) {
				if(equipe.getJogo().equals(jogo))
					return find(Equipe.class, equipe.getId());
			}
		}
		return null;
	}

	@Override
	public List<Entrega> getEntregasOrdenadasPorEquipe(Equipe equipe, Jogo jogo) {
		List<Entrega> entregas =  new ArrayList<Entrega>();
		for (Usuario aluno : equipe.getAlunos()) {
			for (Entrega entrega : aluno.getEntregas()) {
				if(entrega.getRodada().getJogo().equals(jogo)){
					entregas.add(entrega);		
				}
			}
		}
		if(entregas.size()==0)
			return null;
		return ordenaEntregas(entregas);
	}
	
	private List<Entrega> ordenaEntregas(List<Entrega> entregas){
		for (int i = 0; i < entregas.size(); i++) {
			for (int j = i+1; j < entregas.size(); j++) {
				if(entregas.get(i).getDia().getTime() < entregas.get(j).getDia().getTime()){
					Entrega aux = entregas.get(i);
					entregas.add(i, entregas.get(j));
					entregas.remove(i+1);
					entregas.add(j, aux);
					entregas.remove(j+1);
				}
			}
		}
		return entregas;
	}

	@Override
	public List<NotaEquipeRodada> criarNotasEquipeRodadas(
			List<NotaEquipeRodada> notasEquipeRodadas, Equipe equipe, String permissao) {
		notasEquipeRodadas = new ArrayList<NotaEquipeRodada>();
		List<Entrega> entregas = entregaService.getUltimasEntregasDaEquipe(equipe);
		List<Resposta> respostas = new ArrayList<Resposta>();
		for (Entrega entrega : entregas) {
			Resposta resposta = respostaService.findUltimaRespostaPorEntrega(entrega.getUsuario(), entrega);
			if(resposta!= null){
				respostas.add(resposta);
			}
			if(((entrega.getRodada().isStatusNota() && permissao.equals("professor")) || entrega.getRodada().isStatusRaking()) && !respostas.isEmpty()){
				NotaEquipeRodada notaEquipeRodada = new NotaEquipeRodada();
				notaEquipeRodada.setEquipe(equipe);
				notaEquipeRodada.setRodada(entrega.getRodada());
				Float nota = calculoNotaService.calculoNotaEquipe(resposta);
				notaEquipeRodada.setValor(nota);
				Float fator = nota/10 + 1;
				notaEquipeRodada.setFatorDeAposta(fator);
				notaEquipeRodadaService.save(notaEquipeRodada);
				notasEquipeRodadas.add(notaEquipeRodada);
			}
		}	
		return notasEquipeRodadas;
	}

	@Override
	public List<NotaEquipeRodada> atualizarNotasEquipeRodadas(
			List<NotaEquipeRodada> notasEquipeRodadas, Equipe equipe, String permissao) {
		List<Entrega> entregas = entregaService.getUltimasEntregasDaEquipe(equipe);
		List<Resposta> respostas = new ArrayList<Resposta>();
		for (int i= notasEquipeRodadas.size(); i<entregas.size();i++) {
			Resposta resposta = respostaService.findUltimaRespostaPorEquipe(equipe, entregas.get(i));
			if(resposta!= null){
				respostas.add(resposta);
			}
			if(((entregas.get(i).getRodada().isStatusNota() && permissao.equals("professor")) || entregas.get(i).getRodada().isStatusRaking()) && !respostas.isEmpty()){
				NotaEquipeRodada notaEquipeRodada = new NotaEquipeRodada();
				notaEquipeRodada.setEquipe(equipe);
				notaEquipeRodada.setRodada(entregas.get(i).getRodada());
				Float nota = calculoNotaService.calculoNotaEquipe(resposta);
				notaEquipeRodada.setValor(nota);
				Float fator = nota/10 + 1;
				notaEquipeRodada.setFatorDeAposta(fator);
				notaEquipeRodadaService.save(notaEquipeRodada);
				notasEquipeRodadas.add(notaEquipeRodada);
			}
		}
		return notasEquipeRodadas;
	}

	@Override
	public void removeEquipe(Jogo jogo, Equipe equipe) {
		for (Usuario aluno : equipe.getAlunos()) {
			aluno.getEquipes().remove(equipe);
			usuarioService.update(aluno);
		}
		jogo.getEquipes().remove(equipe);
		jogoService.update(jogo);
		delete(equipe);
	}

	@Override
	public void vincularParticipantes(Equipe equipeCompleta,
			List<Usuario> alunos) {
		boolean flag = false;
		for (Usuario aluno : alunos) {
			if(aluno.getId() != null){
				aluno = usuarioService.find(Usuario.class, aluno.getId());
				aluno.addEquipe(equipeCompleta);
				flag = true;
			}
		}
		if(flag){
			update(equipeCompleta);
		}else{
			throw new IllegalStateException("Selecione usuários para associar à empresa.");
		}
	}

	@Override
	public void ativarSubmissaoEquipeRodada(Equipe equipe, Rodada rodada, StatusRodadaEquipe rodadaEquipe) {
		if(rodadaEquipe == null){
			rodadaEquipe = new StatusRodadaEquipe();
			rodadaEquipe.setEquipe(equipe);
			rodadaEquipe.setRodada(rodada);
		}
		SaldoNaRodada saldoNaRodada = saldoNaRodadaService.findByEquipeRodada(equipe, rodada);
		saldoNaRodada.setSaldo(-rodada.getValorReabertura());
		rodadaEquipe.setAtiva(true);
		equipe.addStatusRodadaEquipe(rodadaEquipe);
		update(equipe);
	}

	@Override
	public void desativarSubmissaoEquipeRodada(Equipe equipe, Rodada rodada,
			StatusRodadaEquipe rodadaEquipe) {
		if(rodadaEquipe == null){
			rodadaEquipe = new StatusRodadaEquipe();
			rodadaEquipe.setEquipe(equipe);
			rodadaEquipe.setRodada(rodada);
		}
		rodadaEquipe.setAtiva(false);
		equipe.addStatusRodadaEquipe(rodadaEquipe);
		update(equipe);
		
	}

	@Override
	public void verificaNome(Equipe equipe) {
		boolean soEspacos = Pattern.matches("\\s+", equipe.getNome());
		if(soEspacos || equipe.getNome().isEmpty()){
			throw new IllegalArgumentException("O nome da empresa é obrigatório.");
		}
	}

}
