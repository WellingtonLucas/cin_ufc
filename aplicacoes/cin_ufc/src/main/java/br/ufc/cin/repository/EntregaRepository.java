package br.ufc.cin.repository;

import java.util.List;

import br.ufc.cin.model.Entrega;
import br.ufc.cin.model.Rodada;
import br.ufc.quixada.npi.repository.GenericRepository;

public interface EntregaRepository extends GenericRepository<Entrega>{

	List<Entrega> findByRodada(Rodada rodada);

}
