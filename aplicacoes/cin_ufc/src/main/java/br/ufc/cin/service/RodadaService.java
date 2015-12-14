package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Rodada;
import br.ufc.quixada.npi.service.GenericService;

public interface RodadaService extends GenericService<Rodada>{

	public abstract List<Rodada> ordenaPorInicio(List<Rodada> rodadas);
	
	public abstract List<Rodada> atualizaStatusRodadas(List<Rodada> rodadas);

	public abstract Rodada atualizaStatusRodada(Rodada rodada);

	public abstract Rodada atualizaStatusPrazoRodada(Rodada rodada);

	public abstract Rodada atualizaStatusAvaliacao(Rodada rodada);

	public abstract void verificaPeriodoAvaliacao(Rodada rodada);

}
