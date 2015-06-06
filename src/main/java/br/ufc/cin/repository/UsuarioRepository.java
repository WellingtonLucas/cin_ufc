package br.ufc.cin.repository;

import br.ufc.cin.model.Usuario;
import br.ufc.quixada.npi.repository.GenericRepository;

public interface UsuarioRepository extends GenericRepository<Usuario> {
	public abstract Usuario getUsuarioByLogin(String login);
}
