package br.ufc.cin.service;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.Usuario;

public interface RegrasService {

	public abstract void verificaParticipacao(Usuario usuario, Jogo jogo);
	
	public abstract void verificaExistencia(Rodada rodada, Equipe equipe, Jogo jogo);
}
