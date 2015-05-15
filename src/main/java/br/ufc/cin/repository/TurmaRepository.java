package br.ufc.cin.repository;

import java.util.List;

import br.ufc.cin.model.Turma;
import br.ufc.quixada.npi.repository.GenericRepository;

public interface TurmaRepository extends GenericRepository<Turma>{
			
	public abstract List<Turma> getTurmaBySemestre(String semestre);
	
	public abstract Turma getTurmasBySemestreCurso(String semestre, String curso);
}
