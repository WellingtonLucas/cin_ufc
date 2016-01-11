package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.Usuario;
import br.ufc.quixada.npi.service.GenericService;

public interface RodadaService extends GenericService<Rodada>{

	/**
	 * Ordena as rodadas pela data de início.
	 * */
	public abstract List<Rodada> ordenaPorInicio(List<Rodada> rodadas);
	
	public abstract List<Rodada> atualizaStatusRodadas(List<Rodada> rodadas);

	public abstract Rodada atualizaStatusRodada(Rodada rodada);

	/**
	 * Atualiza o prazo de submissão da rodada.
	 * */
	public abstract Rodada atualizaStatusPrazoRodada(Rodada rodada);

	/**
	 * Atualiza o status de avaliação da rodada com base no prazo de submissão mais o maior tempo 
	 * dos pedidos de reabertura confirmados, além do período de término de avaliação. 
	 * */
	public abstract Rodada atualizaStatusAvaliacao(Rodada rodada);

	/**
	 * Verifica se a rodada não está no prazo de submissões. Caso esteja é lançado uma exception.
	 * */
	public abstract void verificaSeNaoPrazoSubmissao(Rodada rodada);
	
	/**
	 * Verifica o status de submissão da rodada, se ainda for verdadeiro retorna uma IllegalArgumentException
	 * */
	public abstract void verificaStatusPrazoSubmissao(Rodada rodada);

	public abstract void verificaStatusRodada(Rodada rodada);

	public abstract void verificaStatusAvaliacao(Rodada rodada);

	public abstract void salvar(Rodada rodada, String allIn);

	public abstract void verificarDatas(Rodada rodada);

	public abstract void atualizar(Rodada rodada, String allIn);

	public abstract void verificaStatusRaking(Rodada rodada);

	/**
	 * Verifica se a rodada está no prazo de submissões. Caso não esteja é lançado uma exception.
	 * */
	public abstract void verificaSePrazoSubmissao(Rodada rodada);
	
	/**
	 * Verifica a visibilidade da ativação reaberturas de submissões para uma rodada.
	 * */
	public abstract boolean isPosPrazoSubmissoesEReabertura(Rodada rodada);

	/**
	 * Lista somente as rodadas que estão ativas ou que se encerraram, eliminando as futuras da visão dos alunos.
	 * Retorna todas as rodadas caso seja professor.
	 * **/
	public abstract List<Rodada> organizarPorPerfil(List<Rodada> rodadas, Usuario usuario);

	public abstract void atualizaStatusRanking(Rodada rodada);

	public abstract boolean defineStatusBtnRankings(Rodada rodada);

	public abstract void verificaGabaritos(Rodada rodada);

	public abstract void remover(Rodada rodada);
}
