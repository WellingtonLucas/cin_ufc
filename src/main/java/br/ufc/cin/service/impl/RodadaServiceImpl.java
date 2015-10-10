package br.ufc.cin.service.impl;

import java.util.List;

import javax.inject.Named;

import br.ufc.cin.model.Rodada;
import br.ufc.cin.service.RodadaService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class RodadaServiceImpl extends GenericServiceImpl<Rodada> implements RodadaService{

	@Override
	public List<Rodada> ordenaPorInicio(List<Rodada> rodadas) {
		for (int i=0;i<rodadas.size();i++) {
			for (int j = i+1; j < rodadas.size(); j++) {
				if(rodadas.get(i).getInicio().getTime() > rodadas.get(j).getInicio().getTime()){
					Rodada aux = rodadas.get(i);
					rodadas.add(i, rodadas.get(j));
					rodadas.remove(i+1);
					rodadas.add(j, aux);
					rodadas.remove(j+1);
				}
			}
		}
		return rodadas;
	}
}
