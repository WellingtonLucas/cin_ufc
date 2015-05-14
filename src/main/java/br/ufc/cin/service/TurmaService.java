package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Turma;
import br.ufc.quixada.npi.service.GenericService;

public interface TurmaService extends GenericService<Turma> {
	
	public abstract Turma getTurmaBySemestre(String semestre);

	public abstract List<Turma> getTurmasBySemestreCurso(String semestre, String curso);
}
