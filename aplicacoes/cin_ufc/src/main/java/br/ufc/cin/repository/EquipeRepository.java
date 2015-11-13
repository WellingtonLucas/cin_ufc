package br.ufc.cin.repository;

import java.util.List;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.quixada.npi.repository.GenericRepository;

public interface EquipeRepository extends GenericRepository<Equipe>{
	public abstract List<Equipe> getEquipeByJogo(Jogo jogo);
}
