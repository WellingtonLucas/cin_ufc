package br.ufc.cin.service;

import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.SaldoPorJogo;
import br.ufc.cin.model.Usuario;
import br.ufc.quixada.npi.service.GenericService;

public interface SaldoPorJogoService extends GenericService<SaldoPorJogo>{
	public abstract SaldoPorJogo findByUsuarioJogo(Usuario apostador, Jogo jogo);
}
