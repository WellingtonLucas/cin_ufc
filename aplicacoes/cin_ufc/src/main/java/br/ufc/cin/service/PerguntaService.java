package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Pergunta;
import br.ufc.quixada.npi.service.GenericService;

public interface PerguntaService extends GenericService<Pergunta>{
	public abstract void salvar(List<Pergunta> perguntas);

	public abstract void atualizar(List<Pergunta> perguntas);
}
