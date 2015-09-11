package br.ufc.cin.service.impl;

import java.util.List;

import javax.inject.Named;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.service.RodadaService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class RodadaServiceImpl extends GenericServiceImpl<Rodada> implements RodadaService{

	@Override
	public void removerEquipeDasRodadas(List<Rodada> rodadas, Equipe equipe) {
		for (Rodada rodada : rodadas) {
			rodada.getEquipesAtivas().remove(equipe);
			update(rodada);
		}
		
	}

}
