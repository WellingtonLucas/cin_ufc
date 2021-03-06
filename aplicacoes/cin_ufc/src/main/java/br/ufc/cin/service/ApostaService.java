package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Aposta;
import br.ufc.cin.model.Deposito;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.Usuario;
import br.ufc.quixada.npi.service.GenericService;

public interface ApostaService extends GenericService<Aposta>{
	
	public abstract Aposta findByUsuarioRodada(Usuario apostador, Rodada rodada);
	
	public abstract boolean verificaSaldo(Aposta aposta, Float valor);

	public abstract Aposta criarAposta(Usuario apostador, Rodada rodada);
	
	public abstract void atualizaSaldoDisponivel(Aposta aposta, Float deposito);
	
	public abstract void realizarDeposito(Aposta aposta, Deposito deposito);

	public abstract List<Aposta> findByRodada(Rodada rodada);

	public abstract void atualizaSaldoEquipes(Jogo jogo, Rodada rodada);

	public abstract void atualizaSaldoAlunos(Jogo jogo, Rodada rodada);

	public abstract void realizarDeposito(Equipe equipe, String quantidadeDia);

	/**
	 * Buscar as apostas de um aluno no jogo
	 * @author Wellington
	 * */
	public abstract List<Aposta> findByUsuarioRodada(Usuario requisitado, Jogo jogo);

	public abstract List<Aposta> atualizaTotalRetorno(List<Aposta> apostas);

	public abstract List<Aposta> ordenaPorRetorno(List<Aposta> apostas);

	public abstract void deletePor(Rodada rodada);
	
}
