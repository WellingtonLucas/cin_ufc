package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Consultoria;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.SolicitacaoConsultoria;
import br.ufc.quixada.npi.service.GenericService;

public interface SolicitacaoConsultoriaService extends GenericService<SolicitacaoConsultoria>{
	public abstract List<SolicitacaoConsultoria> solicitacoesPorConsulta(Consultoria consultoria);

	public abstract void confirmarSolicitacao(
			SolicitacaoConsultoria solicitacaoConsultoria, Equipe equipe);

	public abstract void verificaConsistencia(
			SolicitacaoConsultoria solicitacaoConsultoria, Equipe equipe,
			Rodada rodada);
}
