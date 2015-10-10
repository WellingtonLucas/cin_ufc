package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Rodada;
import br.ufc.quixada.npi.service.GenericService;

public interface RodadaService extends GenericService<Rodada>{

	List<Rodada> ordenaPorInicio(List<Rodada> rodadas);

}
