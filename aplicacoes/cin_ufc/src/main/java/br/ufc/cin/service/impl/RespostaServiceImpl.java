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

	@Override
	public List<Resposta> find(Usuario usuario, Entrega entrega) {
		return respostaRepository.find(usuario, entrega);
	}

	@Override
	public Resposta findUltimaRespostaPorEntrega(Usuario usuario, Entrega entrega) {
		List<Resposta> respostas = find(usuario, entrega);
		if(respostas != null){
			Long maior = respostas.get(0).getDia().getTime();
			int temp = 0;
			for (int i = 1; i < respostas.size(); i++) {
				if(respostas.get(i).getDia().getTime() > maior){
					maior = respostas.get(i).getDia().getTime();
					temp = i;
				}
			}
			return respostas.get(temp);
		}
		return null;
	}
}
