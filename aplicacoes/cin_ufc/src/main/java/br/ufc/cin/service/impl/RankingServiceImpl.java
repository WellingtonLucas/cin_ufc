package br.ufc.cin.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Nota;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.service.NotaService;
import br.ufc.cin.service.RankingService;

@Named
public class RankingServiceImpl implements RankingService{

	@Inject
	private NotaService notaService;
	
	@Override
	public List<Nota> ordenaNotas(Rodada rodada) {
		List<Nota> notas = notaService.findByRodada(rodada);
		if(notas != null){
			for (int i=0;i<notas.size();i++) {
				for (int j = i+1; j < notas.size(); j++) {
					if(notas.get(i).getValor() < notas.get(j).getValor()){
						Nota aux = notas.get(i);
						notas.add(i, notas.get(j));
						notas.remove(i+1);
						notas.add(j, aux);
						notas.remove(j+1);
					}
				}
			}
			return notas;
		}
		return null;
	}

	
}
