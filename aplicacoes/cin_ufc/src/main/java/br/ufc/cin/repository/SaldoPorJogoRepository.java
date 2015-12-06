package br.ufc.cin.repository;

import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.SaldoPorJogo;
import br.ufc.cin.model.Usuario;
import br.ufc.quixada.npi.repository.GenericRepository;

public interface SaldoPorJogoRepository extends GenericRepository<SaldoPorJogo>{
	public abstract SaldoPorJogo findByUsuarioJogo(Usuario apostador, Jogo jogo);
}
