package br.ufc.cin.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Jogo;
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

	@Override
	public Usuario getUsuarioByEmail(String email) {
		return usuarioRepository.getUsuarioByEmail(email);
	}

	@Override
	public List<Usuario> getPossiveisParticipantes(Usuario usuario) {
		return usuarioRepository.getPossiveisParticipantes(usuario);
	}
	
	@Override
	public List<Usuario> getPossiveisParticipantes(Usuario usuario, Jogo jogo) {
		if(jogo.getAlunos().isEmpty()){
			return usuarioRepository.getPossiveisParticipantes(usuario);
		}else{
			ArrayList<Usuario> usuarios = (ArrayList<Usuario>) usuarioRepository.getPossiveisParticipantes(usuario);
			usuarios.removeAll(jogo.getAlunos());
			return usuarios;
		}
	}
	
	@Override
	public void salvar(List<Usuario> alunos) {
		for(Usuario aluno : alunos) {
			save(aluno);
		}
	}

	@Override
	public void atualizar(List<Usuario> alunos) {
		for(Usuario aluno : alunos) {
			update(aluno);
		}
	}

	@Override
	public String definePermissao(Jogo jogo, Usuario usuario) {
		if (usuario.equals(jogo.getProfessor())) {
			return "professor";
		} else if(jogo.getAlunos().contains(usuario)){
			return "aluno";
		}else {
			throw new IllegalArgumentException("Permissão negada.");
		}
	}
}
