package br.ufc.cin.service;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.Usuario;

public interface RegrasService {
	
	/**
	 * Verifica se o usuário participa do jogo e o status do jogo.
	 * */
	public abstract void verificaParticipacao(Usuario usuario, Jogo jogo);
	
	/**
	 * Verifica se a rodada e a equipe existem no jogo.
	 * */
	public abstract void verificaExistencia(Rodada rodada, Equipe equipe, Jogo jogo);

	/**
	 * Verifica se o jogo existe.
	 * */
	public abstract void verificaJogo(Jogo jogo);
	
	/**
	 * Verifica se a rodada existe.
	 * */
	public abstract void verificaRodada(Rodada rodada);
	
	/**
	 * Verifica se a equipe existe.
	 * */
	public abstract void verificaEquipe(Equipe equipe);

	/**
	 * Verifica se alguma rodada está com o status do ranking verdadeiro.
	 * */
	public abstract void verificaStatusRanking(Jogo jogo);

	/**
	 * Verifica se a equipe pertence ao jogo.
	 * */
	public abstract void verificaEquipeJogo(Equipe equipe, Jogo jogo);

	/**
	 * Verifica se a rodada pertence ao jogo
	 * */
	public abstract void verificaRodadaInJogo(Jogo jogo);

	/**
	 * Verifica se o usuário é o professor do jogo.
	 * */
	public abstract void verificaSeProfessor(Usuario usuario, Jogo jogo);

	/**
	 * Verifica se a rodada faz parte do jogo.
	 * */
	public abstract void verificaRodadaJogo(Rodada rodada, Jogo jogo);
}