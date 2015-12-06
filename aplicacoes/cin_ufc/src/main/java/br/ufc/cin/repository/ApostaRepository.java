package br.ufc.cin.repository;

import java.util.List;

import br.ufc.cin.model.Aposta;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.Usuario;
import br.ufc.quixada.npi.repository.GenericRepository;

public interface ApostaRepository extends GenericRepository<Aposta>{
	public abstract Aposta findByUsuarioRodada(Usuario apostador, Rodada rodada);

	public abstract List<Aposta> findByRodada(Rodada rodada);
}
