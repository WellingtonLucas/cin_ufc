package br.ufc.cin.repository;

import br.ufc.cin.model.Consultoria;
import br.ufc.cin.model.Rodada;
import br.ufc.quixada.npi.repository.GenericRepository;

public interface ConsultoriaRepository extends GenericRepository<Consultoria>{

	public abstract Consultoria findByRodada(Rodada rodada);
}
