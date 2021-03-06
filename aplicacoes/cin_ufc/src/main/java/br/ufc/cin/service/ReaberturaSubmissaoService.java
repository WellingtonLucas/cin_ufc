package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.ReaberturaSubmissao;
import br.ufc.cin.model.Rodada;
import br.ufc.quixada.npi.service.GenericService;

public interface ReaberturaSubmissaoService extends GenericService<ReaberturaSubmissao>{

	public abstract List<Equipe> atualizarSolitacoesDeReabertura(List<Equipe> equipes,
			Rodada rodada);
	public abstract ReaberturaSubmissao find(Equipe equipe, Rodada rodada);
	
	public abstract void solicitarReabertura(Rodada rodada, Equipe equipe,
			ReaberturaSubmissao reaberturaSubmissao);
	
	public abstract List<ReaberturaSubmissao> findByRodada(Rodada rodada);
	
	/**
	 * Verifica se a equipe possui pedido de reabertura para a rodada.
	 * **/
	public abstract void verificaSeTemPedido(Rodada rodada, Equipe equipe);
	
	public abstract void deletePor(Rodada rodada);
}
