package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.SaldoNaRodada;
import br.ufc.quixada.npi.service.GenericService;

public interface SaldoNaRodadaService extends GenericService<SaldoNaRodada>{
	
	public abstract SaldoNaRodada findByEquipeRodada(Equipe equipe, Rodada rodada);
	
	public abstract List<SaldoNaRodada> findByRodada(Rodada rodada);
}
