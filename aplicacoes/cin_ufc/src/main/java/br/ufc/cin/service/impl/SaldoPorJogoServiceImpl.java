package br.ufc.cin.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.SaldoPorJogo;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.repository.SaldoPorJogoRepository;
import br.ufc.cin.service.SaldoPorJogoService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class SaldoPorJogoServiceImpl extends GenericServiceImpl<SaldoPorJogo> implements SaldoPorJogoService{

	@Inject
	private SaldoPorJogoRepository saldoPorJogoRepository;
	
	@Override
	public SaldoPorJogo findByUsuarioJogo(Usuario apostador, Jogo jogo) {
		return saldoPorJogoRepository.findByUsuarioJogo(apostador, jogo);
	}

}
