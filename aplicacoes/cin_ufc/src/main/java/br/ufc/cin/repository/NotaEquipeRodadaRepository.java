package br.ufc.cin.repository;

import java.util.List;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.NotaEquipeRodada;
import br.ufc.cin.model.Rodada;
import br.ufc.quixada.npi.repository.GenericRepository;

public interface NotaEquipeRodadaRepository extends GenericRepository<NotaEquipeRodada>{

	public abstract List<NotaEquipeRodada> buscarPorEquipe(Equipe equipe);

	public abstract NotaEquipeRodada findByEquipeRodada(Equipe equipe,
			Rodada rodada);

}
