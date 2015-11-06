package br.ufc.cin.service;

import br.ufc.cin.model.Historico;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Usuario;
import br.ufc.quixada.npi.service.GenericService;

public interface HistoricoService extends GenericService<Historico>{

	public abstract Historico buscarPorJogoUsuario(Jogo jogo, Usuario usuario);

	public abstract Float calculaMedia(Historico historico);
}
