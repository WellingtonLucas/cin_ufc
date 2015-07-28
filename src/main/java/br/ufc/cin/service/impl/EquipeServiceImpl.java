package br.ufc.cin.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.repository.EquipeRepository;
import br.ufc.cin.service.EquipeService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class EquipeServiceImpl extends GenericServiceImpl<Equipe> implements
		EquipeService {

	@Inject
	private EquipeRepository equipeRepository;

	@Override
	public List<Usuario> alunosSemEquipe(Jogo jogo) {
		List<Usuario> alunosSemEquipe = new ArrayList<Usuario>();
		List<Usuario> alunosDeEquipes = new ArrayList<Usuario>();
		if (getEquipesByJogo(jogo) == null) {
			return jogo.getAlunos();
		} else {
			for (Equipe equipe : getEquipesByJogo(jogo)) {
				alunosDeEquipes.addAll(equipe.getAlunos());
			}
			if(alunosDeEquipes.isEmpty()){
				return jogo.getAlunos();
			}else{
				for (Usuario aluno : jogo.getAlunos()) {
					if (!alunosDeEquipes.contains(aluno) && !alunosSemEquipe.contains(aluno)) {
						alunosSemEquipe.add(aluno);
					}
				}
				return alunosSemEquipe;
			}
		}
	}

	@Override
	public List<Equipe> getEquipesByJogo(Jogo jogo) {
		return equipeRepository.getEquipeByJogo(jogo);
	}

}
