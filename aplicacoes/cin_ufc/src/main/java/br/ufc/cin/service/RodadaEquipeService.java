package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.ReaberturaSubmissao;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.StatusRodadaEquipe;
import br.ufc.quixada.npi.service.GenericService;

public interface RodadaEquipeService extends GenericService<StatusRodadaEquipe>{
	
	public abstract StatusRodadaEquipe find(Equipe equipe, Rodada rodada);
	
	public abstract void setStatusEquipeRodada(Rodada rodada, List<Equipe> equipes, boolean status);

	public abstract List<Equipe> atualizaStatusEquipesNaRodada(List<Equipe> equipes,
			Rodada rodada);

	public abstract StatusRodadaEquipe atualizaStatusRodadaEquipe(ReaberturaSubmissao find);

	public abstract void deletePor(Rodada rodada);

	public abstract boolean verificaSeTemSolicitacao(List<Equipe> equipes, Rodada rodada);
}
