package br.ufc.cin.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Entrega;
import br.ufc.cin.model.Resposta;
import br.ufc.cin.repository.RespostaRepository;
import br.ufc.cin.service.RespostaService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class RespostaServiceImpl extends GenericServiceImpl<Resposta> implements RespostaService{

	@Inject
	private RespostaRepository respostaRepository;
	
	@Override
	public Resposta getRespostaByEntrega(Entrega entrega) {
		return respostaRepository.getRespostaByEntrega(entrega);
	}
}
