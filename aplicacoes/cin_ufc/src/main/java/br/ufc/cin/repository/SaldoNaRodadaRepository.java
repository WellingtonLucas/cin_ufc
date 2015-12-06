package br.ufc.cin.repository;

import java.util.List;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.SaldoNaRodada;
import br.ufc.quixada.npi.repository.GenericRepository;

public interface SaldoNaRodadaRepository extends GenericRepository<SaldoNaRodada>{
	
	public abstract SaldoNaRodada findByEquipeRodada(Equipe equipe, Rodada rodada);
	
	public abstract List<SaldoNaRodada> findByRodada(Rodada rodada);
}
