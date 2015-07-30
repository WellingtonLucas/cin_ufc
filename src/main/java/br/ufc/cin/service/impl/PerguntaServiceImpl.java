package br.ufc.cin.service.impl;

import java.util.List;

import javax.inject.Named;

import br.ufc.cin.model.Pergunta;
import br.ufc.cin.service.PerguntaService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class PerguntaServiceImpl extends  GenericServiceImpl<Pergunta> implements PerguntaService{

	@Override
	public void salvar(List<Pergunta> perguntas) {
		for (Pergunta pergunta : perguntas) {
			save(pergunta);
		}
		
	}

}
