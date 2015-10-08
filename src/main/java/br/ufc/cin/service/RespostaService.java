package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Entrega;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Resposta;
import br.ufc.cin.model.Usuario;
import br.ufc.quixada.npi.service.GenericService;

public interface RespostaService extends GenericService<Resposta>{
	public abstract Resposta getRespostaByEntrega(Entrega entrega);

	public abstract List<Resposta> find(
			Usuario usuarioRequisitado);
	
	public abstract List<Resposta> find(
			Usuario usuarioRequisitado, Jogo jogo);
}
