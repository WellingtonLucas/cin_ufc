package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Usuario;
import br.ufc.quixada.npi.service.GenericService;

public interface UsuarioService  extends GenericService<Usuario>{

	Usuario getUsuarioByLogin(String login);
	
	Usuario getUsuarioByEmail(String email);
	
	boolean isProfessor(Usuario usuario);	
	
	List<Usuario> getPossiveisParticipantes(Usuario usuario);
	
	List<Usuario> getPossiveisParticipantes(Usuario usuario, Jogo jogo);

	void salvar(List<Usuario> alunos);

	void atualizar(List<Usuario> alunos);

	String definePermissao(Jogo jogo, Usuario usuario);

	void verificaDados(Usuario usuario);
	
}
