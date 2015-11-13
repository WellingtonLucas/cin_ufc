package br.ufc.cin.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.ReaberturaSubmissao;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.repository.ReaberturaSubmissaoRepository;
import br.ufc.cin.service.ReaberturaSubmissaoService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class ReaberturaSubmissaoServiceImpl extends GenericServiceImpl<ReaberturaSubmissao> implements ReaberturaSubmissaoService{

	@Inject
	private ReaberturaSubmissaoRepository reaberturaSubmissaorRepository;
	
	
	@Override
	public List<Equipe> atualizarSolitacoesDeReabertura(List<Equipe> equipes,
			Rodada rodada) {
		List<Equipe> equipesAtualizadas = new ArrayList<Equipe>();
		for (Equipe equipe : equipes) {
			ReaberturaSubmissao temp = find(equipe, rodada);
			if(temp == null){
				equipe.setStatusReabertura(false);
			}else{
				equipe.setStatusReabertura(temp.isStatus());
			}
			equipesAtualizadas.add(equipe);
		}
		return equipesAtualizadas;	
	}

	@Override
	public ReaberturaSubmissao find(Equipe equipe, Rodada rodada) {
		return reaberturaSubmissaorRepository.find(equipe, rodada);
	}

}
