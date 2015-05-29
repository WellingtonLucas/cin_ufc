package br.ufc.cin.repository;

import java.util.List;

import br.ufc.cin.model.Jogo;
import br.ufc.quixada.npi.repository.GenericRepository;

public interface JogoRepository extends GenericRepository<Jogo>{
			
	public abstract List<Jogo> getJogoBySemestre(String semestre);
	
	public abstract Jogo getJogosBySemestreCurso(String semestre, String curso);
}
