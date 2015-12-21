package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Rodada;
import br.ufc.quixada.npi.service.GenericService;

public interface RodadaService extends GenericService<Rodada>{

	/**
	 * Ordena as rodadas pela data de início.
	 * */
	public abstract List<Rodada> ordenaPorInicio(List<Rodada> rodadas);
	
	public abstract List<Rodada> atualizaStatusRodadas(List<Rodada> rodadas);

	public abstract Rodada atualizaStatusRodada(Rodada rodada);

	public abstract Rodada atualizaStatusPrazoRodada(Rodada rodada);

	public abstract Rodada atualizaStatusAvaliacao(Rodada rodada);

	/**
	 * Verifica se o período de avaliação é menor que o tempo atual
	 * */
	public abstract void verificaPeriodoAvaliacao(Rodada rodada);
	
	/**
	 * Verifica o status de submissão da rodada, se ainda for verdadeiro retorna uma IllegalArgumentException
	 * */
	public abstract void verificaStatusPrazoSubmissao(Rodada rodada);

	public abstract void verificaStatusRodada(Rodada rodada);

	public abstract void verificaStatusAvaliacao(Rodada rodada);

}
