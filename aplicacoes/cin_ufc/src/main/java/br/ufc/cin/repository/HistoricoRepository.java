package br.ufc.cin.repository;

import java.util.List;

import br.ufc.cin.model.Historico;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Usuario;
import br.ufc.quixada.npi.repository.GenericRepository;

public interface HistoricoRepository extends GenericRepository<Historico>{

	public abstract Historico buscarPorJogoUsuario(Jogo jogo, Usuario usuario);

	public abstract List<Historico> findByJogo(Jogo jogo);
}
