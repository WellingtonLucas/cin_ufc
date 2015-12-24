package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Aposta;
import br.ufc.cin.model.Entrega;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Formulario;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.NotaEquipeRodada;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.Usuario;

/**
*Seriço destinado a verificar consistências de regras de negócios gerais, 
*eventualmente algumas regras são verificadas em outros serviços.
*@author Wellington
**/
public interface RegrasService {
	
	/**
	 * Verifica se o usuário participa do jogo, e o status do jogo. 
	 * Lançando uma exception caso não participe do jogo ou o status do jogo esteja falso e não seja professor.
	 * @author Wellington
	 * */
	public abstract void verificaParticipacao(Usuario usuario, Jogo jogo);
	
	/**
	 * Verifica se a rodada e a equipe existem no jogo.
	 * @author Wellington
	 * */
	public abstract void verificaExistencia(Rodada rodada, Equipe equipe, Jogo jogo);

	/**
	 * Verifica se o jogo existe.
	 * @author Wellington
	 * */
	public abstract void verificaJogo(Jogo jogo);
	
	/**
	 * Verifica se a rodada existe.
	 * @author Wellington
	 * */
	public abstract void verificaRodada(Rodada rodada);
	
	/**
	 * Verifica se a equipe existe.
	 * @author Wellington
	 * */
	public abstract void verificaEquipe(Equipe equipe);

	/**
	 * Verifica se alguma rodada está com o status do ranking verdadeiro.
	 * @author Wellington
	 * */
	public abstract void verificaStatusRanking(Jogo jogo);

	/**
	 * Verifica se a equipe pertence ao jogo.
	 * @author Wellington
	 * */
	public abstract void verificaEquipeJogo(Equipe equipe, Jogo jogo);

	/**
	 * Verifica se existem rodadas no jogo
	 * @author Wellington
	 * */
	public abstract void verificaRodadaInJogo(Jogo jogo);

	/**
	 * Verifica se o usuário é o professor do jogo. Caso contrário será lançado uma exception.
	 * @author Wellington
	 * */
	public abstract void verificaSeProfessor(Usuario usuario, Jogo jogo);

	/**
	 * Verifica se a rodada faz parte do jogo.
	 * @author Wellington
	 * */
	public abstract void verificaRodadaJogo(Rodada rodada, Jogo jogo);

	/**
	 * Verifica se o jogo contem rodadas.
	 * @author Wellington
	 * */
	public abstract void verificaJogoComRodada(Jogo jogo);

	/**
	 * Verifica se o status do jogo é válido.
	 * @author Wellington
	 * */
	public abstract void verificaStatusJogo(Jogo jogo);

	/**
	 * Verifica se o usuário participa de uma equipe.
	 * @author Wellington
	 * */
	public abstract void verificaAlunoEquipe(Usuario usuario, Equipe equipe);

	public abstract void verificaFormulario(Formulario formulario);

	public abstract void verificaEntrega(Entrega entrega);

	public abstract void verificaNoraEquipeRodada(
			NotaEquipeRodada notaEquipeRodada);

	public abstract void verificaApostas(List<Aposta> apostas);

	public abstract void verificaMembroOuProfessorEquipe(Usuario usuario,
			Equipe equipe);
}
