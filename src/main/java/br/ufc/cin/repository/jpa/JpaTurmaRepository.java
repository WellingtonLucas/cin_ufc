package br.ufc.cin.repository.jpa;

import java.util.List;

import br.ufc.cin.model.Turma;
import br.ufc.cin.repository.TurmaRepository;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

public class JpaTurmaRepository extends JpaGenericRepositoryImpl<Turma> implements TurmaRepository{
	
	@Override
	public Turma getTurmaBySemestre(String semestre) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Turma> getTurmasBySemestreCurso(String semestre, String curso) {
		// TODO Auto-generated method stub
		return null;
	}

}
