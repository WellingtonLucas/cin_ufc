package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Entrega;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.NotaEquipeRodada;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.StatusRodadaEquipe;
import br.ufc.cin.model.Usuario;
import br.ufc.quixada.npi.service.GenericService;

public interface EquipeService extends GenericService<Equipe>{
	
	public abstract List<Equipe> getEquipesByJogo(Jogo jogo);
	
	public abstract List<Usuario> alunosSemEquipe(Jogo jogo);

	public abstract List<Equipe> equipesDesvinculadas(Jogo jogo, Rodada rodada);
	
	public abstract Equipe equipePorAlunoNoJogo(Usuario aluno, Jogo jogo);

	public abstract List<Entrega> getEntregasOrdenadasPorEquipe(Equipe equipe, Jogo jogo);

	public abstract List<NotaEquipeRodada> criarNotasEquipeRodadas(
			List<NotaEquipeRodada> notasEquipeRodadas, Equipe equipe, String permissao);

	public abstract List<NotaEquipeRodada> atualizarNotasEquipeRodadas(
			List<NotaEquipeRodada> notasEquipeRodadas, Equipe equipe, String permissao);

	/**
	 * Remove a equipe do jogo e dos usuários vinculados a ela.
	 * @author Wellington
	 * */
	public abstract void removeEquipe(Jogo jogo, Equipe equipe);

	public abstract void vincularParticipantes(Equipe equipeCompleta, List<Usuario> alunos);

	public abstract void ativarSubmissaoEquipeRodada(Equipe equipe, Rodada rodada, StatusRodadaEquipe rodadaEquipe);
	
	public abstract void desativarSubmissaoEquipeRodada(Equipe equipe, Rodada rodada, StatusRodadaEquipe rodadaEquipe);
}
