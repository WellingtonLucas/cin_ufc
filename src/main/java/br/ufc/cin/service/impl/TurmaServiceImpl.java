package br.ufc.cin.service.impl;

import java.util.List;

import javax.inject.Inject;

import br.ufc.cin.model.Turma;
import br.ufc.cin.repository.TurmaRepository;
import br.ufc.cin.service.TurmaService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

public class TurmaServiceImpl extends GenericServiceImpl<Turma> implements TurmaService{
	
	@Inject
	private TurmaRepository turmaRepository;
	
	@Override
	public List<Turma> getTurmaBySemestre(String semestre) {		
		return turmaRepository.getTurmaBySemestre(semestre);
	}

	@Override
	public Turma getTurmasBySemestreCurso(String semestre, String curso) { 
		return turmaRepository.getTurmasBySemestreCurso(semestre, curso);
	}

}
