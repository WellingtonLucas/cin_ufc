package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Entrega;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.Usuario;
import br.ufc.quixada.npi.service.GenericService;

public interface EntregaService extends GenericService<Entrega>{

	List<Entrega> getUltimasEntregasDaRodada(Rodada rodada);

	List<Entrega> verificaSeRespondidas(List<Entrega> entregas, Usuario usuario);
	
	List<Entrega> getUltimasEntregasDaEquipe(Equipe equipe);

	List<Entrega> getUltimasEntregasDaEquipeComGabarito(Equipe equipe);

}
