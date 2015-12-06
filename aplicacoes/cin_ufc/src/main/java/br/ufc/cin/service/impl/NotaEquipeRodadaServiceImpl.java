package br.ufc.cin.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.NotaEquipeRodada;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.repository.NotaEquipeRodadaRepository;
import br.ufc.cin.service.NotaEquipeRodadaService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class NotaEquipeRodadaServiceImpl extends GenericServiceImpl<NotaEquipeRodada> implements NotaEquipeRodadaService{

	@Inject
	private NotaEquipeRodadaRepository notaEquipeRodadaRepository;
	
	@Override
	public List<NotaEquipeRodada> buscarPorEquipe(Equipe equipe) {
		return notaEquipeRodadaRepository.buscarPorEquipe(equipe);
	}

	@Override
	public NotaEquipeRodada findByEquipeRodada(Equipe equipe, Rodada rodada) {
		return notaEquipeRodadaRepository.findByEquipeRodada(equipe,rodada);
	}

}
