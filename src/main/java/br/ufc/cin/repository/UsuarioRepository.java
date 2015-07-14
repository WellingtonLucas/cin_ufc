package br.ufc.cin.repository;

import java.util.List;

import br.ufc.cin.model.Usuario;
import br.ufc.quixada.npi.repository.GenericRepository;

public interface UsuarioRepository extends GenericRepository<Usuario> {
	
	public abstract Usuario getUsuarioByLogin(String login);

	public abstract Usuario getUsuarioByEmail(String email);
	
	public abstract List<Usuario> getPossiveisParticipantes(Usuario usuario);

}
