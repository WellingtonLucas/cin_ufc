package br.ufc.cin.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Entrega;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Rodada;
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
	
	@Override
	public List<Equipe> equipesDesvinculadas(Jogo jogo, Rodada rodada) {
		jogo.getEquipes().removeAll(rodada.getJogo().getEquipes());
		return jogo.getEquipes();
	}

	@Override
	public Equipe equipePorAlunoNoJogo(Usuario aluno, Jogo jogo) {
		for (Equipe equipe : aluno.getEquipes()) {
			if(equipe.getJogo().equals(jogo))
				return equipe;
		}
		return null;
	}

	@Override
	public List<Entrega> getEntregasOrdenadasPorEquipe(Equipe equipe, Jogo jogo) {
		List<Entrega> entregas =  new ArrayList<Entrega>();
		for (Usuario aluno : equipe.getAlunos()) {
			for (Entrega entrega : aluno.getEntregas()) {
				if(entrega.getRodada().getJogo().equals(jogo)){
					entregas.add(entrega);		
				}
			}
		}
		if(entregas.size()==0)
			return null;
		return ordenaEntregas(entregas);
	}
	
	private List<Entrega> ordenaEntregas(List<Entrega> entregas){
		for (int i = 0; i < entregas.size(); i++) {
			for (int j = i+1; j < entregas.size(); j++) {
				if(entregas.get(i).getDia().getTime() < entregas.get(j).getDia().getTime()){
					Entrega aux = entregas.get(i);
					entregas.add(i, entregas.get(j));
					entregas.remove(i+1);
					entregas.add(j, aux);
					entregas.remove(j+1);
				}
			}
		}
		return entregas;
	}	
}
