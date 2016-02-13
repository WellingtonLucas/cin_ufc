package br.ufc.cin.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.repository.JogoRepository;
import br.ufc.cin.service.JogoService;
import br.ufc.cin.service.RodadaService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class JogoServiceImpl extends GenericServiceImpl<Jogo> implements JogoService{
	
	@Inject
	private JogoRepository turmaRepository;

	@Inject 
	private RodadaService rodadaService;
	
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

	@Override
	public void verificaDatas(Jogo jogo) {
		if(jogo.getInicio().getTime() > jogo.getTermino().getTime()){
			throw new IllegalArgumentException("A data de início deve ser posterior a de término.");
		}
		
	}

	@Override
	public void verificaNomeSemestre(Jogo jogo) {
		if(jogo.getNomeDoCurso()==null || jogo.getSemestre() == null){
			throw new IllegalArgumentException("Verifique os campos com asterísco e preencha-os para criar um novo jogo.");
		}
		
	}

	@Override
	public void excluir(Jogo jogo) {
		if(jogo.getRodadas()!=null){
			for (Rodada rodada : jogo.getRodadas()) {
				rodadaService.remover(rodada);
			}
		}
		delete(jogo);
	}

}
