package br.ufc.cin.repository;

import java.util.List;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.StatusRodadaEquipe;
import br.ufc.quixada.npi.repository.GenericRepository;

public interface RodadaEquipeRepository extends GenericRepository<StatusRodadaEquipe>{
	public abstract StatusRodadaEquipe find(Equipe equipe, Rodada rodada);

	public abstract List<StatusRodadaEquipe> find(Rodada rodada);
}
