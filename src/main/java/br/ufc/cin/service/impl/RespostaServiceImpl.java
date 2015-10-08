package br.ufc.cin.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Entrega;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Resposta;
import br.ufc.cin.model.Usuario;
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

	@Override
	public List<Resposta> find(Usuario usuarioRequisitado, Jogo jogo) {
		List<Resposta> respostas = find(usuarioRequisitado);
		List<Resposta> respostas2 = new ArrayList<Resposta>();
		if(respostas != null){
			for (Resposta resposta : respostas) {
				if(resposta.getEntrega() != null){
					Jogo temp = resposta.getEntrega().getEquipe().getJogo();
					if(jogo.equals(temp)){
						respostas2.add(resposta);
					}
				}
			}
		}
		return respostas2;
	}

	@Override
	public List<Resposta> find(Usuario usuarioRequisitado) {
		return respostaRepository.find(usuarioRequisitado);
	}
}
