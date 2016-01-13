package br.ufc.cin.service.impl;

import java.util.List;

import javax.inject.Named;

import br.ufc.cin.model.Entrega;
import br.ufc.cin.model.Opcao;
import br.ufc.cin.model.Resposta;
import br.ufc.cin.service.CalculoNotaService;

@Named
public class CalculoNotaServiceImpl implements CalculoNotaService{

	@Override
	public Float calculoNota(Resposta resposta) {
		List<Opcao> opcoes = resposta.getOpcoes();
		List<Opcao> gabarito = resposta.getEntrega().getGabarito().getOpcoes();
		float resultado = 0f;
		float somatorio = 0f;
		for (int i = 0; i < opcoes.size(); i++) {
			Integer diferenca = opcoes.get(i).getId() - gabarito.get(i).getId();
			Float valor = 0f;
			if( diferenca < 0){
				diferenca *= -1;
			}
			if(diferenca == 0)
				valor = 10f;
			if(diferenca == 1)
				valor = 3f;
			if(diferenca >= 2)
				valor = 0f;
			somatorio += valor;
		}
		resultado =  somatorio/opcoes.size();
		return resultado;
	}

	@Override
	public Float calculoMedia(List<Resposta> respostas, List<Entrega> ultimas) {
		Float soma =0f;
		if(respostas != null && !respostas.isEmpty()){
			for (Resposta resposta : respostas) {
				soma+= calculoNota(resposta);
			}
			return soma/ultimas.size();
		}
		return null;
	}

	@Override
	public Float calculoNotaEquipe(Entrega entrega) {
		List<Opcao> gabarito = entrega.getGabarito().getOpcoes();
		float somatorio = 0f;
		for (int i = 0; i < gabarito.size(); i++) {
			List<Opcao> opcoes = gabarito.get(i).getPergunta().getOpcoes();
			opcoes = ordenaOpcoesPorId(opcoes);
			float inicial = 10f;	
			for (Opcao opcao : opcoes) {
				if(opcao.equals(gabarito.get(i))){
					break;
				}
				inicial -= 2.5;
			}
			somatorio += inicial;
		}
		return somatorio/gabarito.size();
	}
	
	private List<Opcao> ordenaOpcoesPorId(List<Opcao>opcoes){
		for (int j=0;j<opcoes.size();j++) {
			for (int k = j+1; k < opcoes.size(); k++) {
				if(opcoes.get(j).getId() > opcoes.get(k).getId()){
					Opcao aux = opcoes.get(j);
					opcoes.add(j, opcoes.get(k));
					opcoes.remove(j+1);
					opcoes.add(k, aux);
					opcoes.remove(k+1);
				}
			}
		}
		return opcoes;
	}

}
