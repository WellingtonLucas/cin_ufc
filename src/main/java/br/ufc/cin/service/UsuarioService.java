package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Usuario;
import br.ufc.quixada.npi.service.GenericService;

public interface UsuarioService  extends GenericService<Usuario>{

	Usuario getUsuarioByLogin(String login);
	
	Usuario getUsuarioByEmail(String email);
	
	boolean isProfessor(Usuario usuario);	
	
	List<Usuario> getPossiveisParticipantes(Usuario usuario);
	
}
