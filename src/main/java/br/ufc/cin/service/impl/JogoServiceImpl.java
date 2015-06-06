package br.ufc.cin.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Jogo;
import br.ufc.cin.repository.JogoRepository;
import br.ufc.cin.service.JogoService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class JogoServiceImpl extends GenericServiceImpl<Jogo> implements JogoService{
	
	@Inject
	private JogoRepository turmaRepository;
	
	@Override
	public List<Jogo> getJogoBySemestre(String semestre) {		
		return turmaRepository.getJogoBySemestre(semestre);
	}

	@Override
	public Jogo getJogosBySemestreCurso(String semestre, String nomeCurso) { 
		return turmaRepository.getJogosBySemestreCurso(semestre, nomeCurso);
	}

	@Override
	public List<Jogo> getJogoByProfessor(Integer idProfessor) {		
		return turmaRepository.getJogosByProfessor(idProfessor);
	}

}
