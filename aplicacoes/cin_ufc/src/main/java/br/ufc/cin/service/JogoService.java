package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Jogo;
import br.ufc.quixada.npi.service.GenericService;

public interface JogoService extends GenericService<Jogo> {
	
	public abstract List<Jogo> getJogoBySemestre(String semestre);

	public abstract Jogo getJogosBySemestreCurso(String semestre, String nomeCurso);
	
	public abstract List<Jogo> getJogoByProfessor(Integer idProfessor);

	public abstract void verificaDatas(Jogo jogo);

	public abstract void verificaNomeSemestre(Jogo jogo);
}
