package br.ufc.cin.repository;

import java.util.List;

import br.ufc.cin.model.Entrega;
import br.ufc.cin.model.Resposta;
import br.ufc.cin.model.Usuario;
import br.ufc.quixada.npi.repository.GenericRepository;

public interface RespostaRepository extends GenericRepository<Resposta>{
	
	public abstract Resposta getRespostaByEntrega(Entrega entrega);

	public abstract List<Resposta> find(Usuario usuarioRequisitado);

	public abstract List<Resposta> find(Usuario usuario, Entrega entrega);
}
