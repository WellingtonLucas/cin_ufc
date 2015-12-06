package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Nota;
import br.ufc.cin.model.Rodada;
import br.ufc.quixada.npi.service.GenericService;

public interface NotaService extends GenericService<Nota>{

	public abstract List<Nota> findByRodada(Rodada rodada);
}
