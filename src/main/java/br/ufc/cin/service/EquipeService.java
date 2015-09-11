package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.Usuario;
import br.ufc.quixada.npi.service.GenericService;

public interface EquipeService extends GenericService<Equipe>{
	
	public abstract List<Equipe> getEquipesByJogo(Jogo jogo);
	
	public abstract List<Usuario> alunosSemEquipe(Jogo jogo);

	public abstract List<Equipe> equipesDesvinculadas(Jogo jogo, Rodada rodada);
	
	public abstract Equipe equipePorAlunoNoJogo(Usuario aluno, Jogo jogo);

}
