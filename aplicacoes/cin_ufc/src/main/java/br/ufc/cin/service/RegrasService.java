package br.ufc.cin.service;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.Usuario;

public interface RegrasService {
	
	/**
	 * Verifica se o usuário participa do jogo, como aluno, e o status do jogo.
	 * @author Wellington
	 * */
	public abstract void verificaParticipacao(Usuario usuario, Jogo jogo);
	
	/**
	 * Verifica se a rodada e a equipe existem no jogo.
	 * @author Wellington
	 * */
	public abstract void verificaExistencia(Rodada rodada, Equipe equipe, Jogo jogo);

	/**
	 * Verifica se o jogo existe.
	 * @author Wellington
	 * */
	public abstract void verificaJogo(Jogo jogo);
	
	/**
	 * Verifica se a rodada existe.
	 * @author Wellington
	 * */
	public abstract void verificaRodada(Rodada rodada);
	
	/**
	 * Verifica se a equipe existe.
	 * @author Wellington
	 * */
	public abstract void verificaEquipe(Equipe equipe);

	/**
	 * Verifica se alguma rodada está com o status do ranking verdadeiro.
	 * @author Wellington
	 * */
	public abstract void verificaStatusRanking(Jogo jogo);

	/**
	 * Verifica se a equipe pertence ao jogo.
	 * @author Wellington
	 * */
	public abstract void verificaEquipeJogo(Equipe equipe, Jogo jogo);

	/**
	 * Verifica se a rodada pertence ao jogo
	 * @author Wellington
	 * */
	public abstract void verificaRodadaInJogo(Jogo jogo);

	/**
	 * Verifica se o usuário é o professor do jogo. Caso contrário será lançado uma exception.
	 * @author Wellington
	 * */
	public abstract void verificaSeProfessor(Usuario usuario, Jogo jogo);

	/**
	 * Verifica se a rodada faz parte do jogo.
	 * @author Wellington
	 * */
	public abstract void verificaRodadaJogo(Rodada rodada, Jogo jogo);

	/**
	 * Verifica se o jogo contem rodadas.
	 * @author Wellington
	 * */
	public abstract void verificaJogoComRodada(Jogo jogo);

	/**
	 * Verifica se o status do jogo é válido.
	 * @author Wellington
	 * */
	public abstract void verificaStatusJogo(Jogo jogo);

	/**
	 * Verifica se o usuário participa de uma equipe.
	 * @author Wellington
	 * */
	public abstract void verificaAlunoEquipe(Usuario usuario, Equipe equipe);
}
