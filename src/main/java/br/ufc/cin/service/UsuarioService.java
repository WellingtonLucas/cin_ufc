package br.ufc.cin.service;

import br.ufc.cin.model.Usuario;
import br.ufc.quixada.npi.service.GenericService;

public interface UsuarioService  extends GenericService<Usuario>{

	Usuario getUsuarioByLogin(String login);
	
	boolean isDiretor(Usuario usuario);

}
