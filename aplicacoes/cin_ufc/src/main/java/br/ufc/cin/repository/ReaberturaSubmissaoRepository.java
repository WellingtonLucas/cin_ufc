package br.ufc.cin.repository;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.ReaberturaSubmissao;
import br.ufc.cin.model.Rodada;
import br.ufc.quixada.npi.repository.GenericRepository;

public interface ReaberturaSubmissaoRepository extends GenericRepository<ReaberturaSubmissao>{
	public abstract ReaberturaSubmissao find(Equipe equipe, Rodada rodada);
}
