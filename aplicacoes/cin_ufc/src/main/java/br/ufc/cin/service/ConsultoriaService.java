package br.ufc.cin.service;

import br.ufc.cin.model.Consultoria;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Rodada;
import br.ufc.quixada.npi.service.GenericService;

public interface ConsultoriaService extends GenericService<Consultoria>{

	public abstract Consultoria findByRodada(Rodada rodada);

	public abstract String defineAcao(Consultoria consultoria);

	public abstract void salvar(Consultoria consultoria, Rodada rodada);

	/**
	 * Verifica se o serviço é da rodada, retornando uma instância consistente com dependências do banco.
	 * Caso não seja o serviço da rodada então será lançada uma IllegalArgument.
	 * **/
	public abstract Consultoria verificaRodadaContains(Rodada rodada,
			Consultoria consultoria);

	public abstract void verificaCampoDescricao(Consultoria consultoria);

	public abstract void atualizar(Consultoria consultoria, Equipe equipe);

	/**
	 * Retorna a quantidade de solicitações não atendidas para a consultoria da rodada.
	 * **/
	public abstract Integer quantidadeSolicitacoes(Consultoria consultoria);

}
