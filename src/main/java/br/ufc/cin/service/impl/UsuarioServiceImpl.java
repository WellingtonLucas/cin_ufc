package br.ufc.cin.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Usuario;
import br.ufc.cin.repository.UsuarioRepository;
import br.ufc.cin.service.UsuarioService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class UsuarioServiceImpl extends GenericServiceImpl<Usuario> implements UsuarioService{
	
	@Inject
	private UsuarioRepository usuarioRepository;
	
	@Override
	public Usuario getUsuarioByLogin(String login) {		
		return usuarioRepository.getUsuarioByLogin(login);
	}

	@Override
	public boolean isProfessor(Usuario usuario) {
		if(usuario.getPapel().equals("ROLE_PROFESSOR")){
			return true;
		}
		return false;
	}

}
