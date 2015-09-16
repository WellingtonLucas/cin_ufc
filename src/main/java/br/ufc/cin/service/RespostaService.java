package br.ufc.cin.service;

import br.ufc.cin.model.Entrega;
import br.ufc.cin.model.Resposta;
import br.ufc.quixada.npi.service.GenericService;

public interface RespostaService extends GenericService<Resposta>{
	public abstract Resposta getRespostaByEntrega(Entrega entrega);
}
