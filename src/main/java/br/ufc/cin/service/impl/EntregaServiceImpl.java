package br.ufc.cin.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import br.ufc.cin.model.Entrega;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Resposta;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.service.EntregaService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class EntregaServiceImpl extends GenericServiceImpl<Entrega> implements
		EntregaService {

	@Override
	public List<Entrega> getUltimasEntregasDaRodada(Rodada rodada) {
		List<Entrega> ultimasEntregas = new ArrayList<Entrega>();
	
		for (Equipe equipe : rodada.getEquipesAtivas()) {
			ultimasEntregas.add(getUltimaEntrega(rodada, equipe));
		}
		return ultimasEntregas;
	}

	private Entrega getUltimaEntrega(Rodada rodada, Equipe equipe) {
			Long ultima = (long) 0;
			Entrega ultimaEntrega = null;
			for(Usuario aluno : equipe.getAlunos()){
				for(Entrega entrega : aluno.getEntregas()){
					if(rodada.equals(entrega.getRodada())){
						if(ultima < entrega.getDia().getTime()){
							ultima = entrega.getDia().getTime();
							ultimaEntrega = entrega;
						}	
					}
				}
			}
			return ultimaEntrega;
	}

	@Override
	public List<Entrega> verificaSeRespondidas(List<Entrega> entregas, Usuario usuario) {
		List<Entrega> entregasComStatus = new ArrayList<Entrega>();
		for (Entrega entrega : entregas) {
			entrega.setRespondida(false);
			for (Resposta resposta : entrega.getRespostas()) {
				if(resposta.getUsuario().equals(usuario)){
					entrega.setRespondida(true);
					break;
				}
			}
			entregasComStatus.add(entrega);
		}
		return entregasComStatus;
	}

}
