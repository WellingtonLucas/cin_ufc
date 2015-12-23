package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Equipe;
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

	/**
	 * Define uma permissão para o usuário logado e verifica o status do jogo.
	 * @author Wellington
	 * @exception IllegalArgumentException
	 * */
	String definePermissao(Jogo jogo, Usuario usuario);

	void verificaDados(Usuario usuario);

	/**
	 * Verifica se o usuário não é nulo
	 * @author Wellington
	 * @exception IllegalArgumentException
	 * */
	void verificaUsuario(Usuario usuario);
	/**
	 * Define uma permissão para o usuário logado em relação a uma equipe. 
	 * Aborta a operação caso não seja uma das permissões esperada.
	 * @author Wellington
	 * 
	 * */
	String definePermissao(Equipe equipe, Usuario usuario);
	
}
