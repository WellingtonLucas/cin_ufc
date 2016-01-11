package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.NotaEquipeRodada;
import br.ufc.cin.model.Rodada;
import br.ufc.quixada.npi.service.GenericService;

public interface NotaEquipeRodadaService extends GenericService<NotaEquipeRodada>{

	public abstract List<NotaEquipeRodada> buscarPorEquipe(Equipe equipe);

	public abstract NotaEquipeRodada findByEquipeRodada(Equipe equipe,
			Rodada rodada);
	
	public abstract void atualizaNotasEquipesRodada(Jogo jogo, String permissao);

	public abstract Float calculaMedia(List<NotaEquipeRodada> notasEquipeRodadas);

	public abstract List<NotaEquipeRodada> somaInvestimentos(List<NotaEquipeRodada> notasEquipeRodadas);

	public abstract void findByRodada(Rodada rodada);

}
