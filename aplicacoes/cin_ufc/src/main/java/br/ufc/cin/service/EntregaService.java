package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Documento;
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

	void salvar(Entrega entrega, Rodada rodada, Equipe equipe, Usuario usuario,
			Documento documento);

	void verificaExistenciaEntregas(List<Entrega> entregas);

	List<Entrega> findByRodada(Rodada rodada);
	
	void deletePor(Rodada rodada);

}
