package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Rodada;
import br.ufc.quixada.npi.service.GenericService;

public interface RodadaService extends GenericService<Rodada>{

	void removerEquipeDasRodadas(List<Rodada> rodadas, Equipe equipe);
}
