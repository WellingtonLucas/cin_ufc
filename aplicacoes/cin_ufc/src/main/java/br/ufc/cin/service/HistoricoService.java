package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Historico;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.Usuario;
import br.ufc.quixada.npi.service.GenericService;

public interface HistoricoService extends GenericService<Historico>{

	public abstract Historico buscarPorJogoUsuario(Jogo jogo, Usuario usuario);

	public abstract Float calculaMedia(Historico historico);
	
	public abstract Historico criarHistorico(Historico historico, List<Rodada> rodadas, Usuario usuario);
	
	public abstract Historico atualizarHistorico(Historico historico, List<Rodada> rodadas, Usuario usuario);
	
	public abstract Historico criarNovasNotas(Historico historico, List<Rodada> rodadas);
}
