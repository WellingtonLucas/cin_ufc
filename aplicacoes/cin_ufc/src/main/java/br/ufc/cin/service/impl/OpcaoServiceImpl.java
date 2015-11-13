package br.ufc.cin.service.impl;

import java.util.List;

import javax.inject.Named;

import br.ufc.cin.model.Opcao;
import br.ufc.cin.service.OpcaoService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class OpcaoServiceImpl extends GenericServiceImpl<Opcao> implements OpcaoService{

	@Override
	public void salvar(List<Opcao> opcoes) {
		for (Opcao opcao : opcoes) {
			save(opcao);
		}
		
	}

	@Override
	public void atualizar(List<Opcao> opcoes) {
		for (Opcao opcao : opcoes) {
			if(opcao.getId() == null){
				save(opcao);
			}else{
				update(opcao);
			}
		}
		
	}

}
