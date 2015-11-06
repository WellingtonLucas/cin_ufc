package br.ufc.cin.service.impl;

import java.util.List;

import javax.inject.Named;

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
				valor = 5f;
			if(diferenca >= 2)
				valor = 0f;
			somatorio += valor;
		}
		resultado =  somatorio/opcoes.size();
		return resultado;
	}

	@Override
	public Float calculoMedia(List<Resposta> respostas) {
		Float soma =0f;
		if(respostas != null && !respostas.isEmpty()){
			for (Resposta resposta : respostas) {
				soma+= calculoNota(resposta);
			}
			return soma/respostas.size();
		}
		return null;
	}

}
