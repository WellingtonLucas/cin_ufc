package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Opcao;
import br.ufc.quixada.npi.service.GenericService;

public interface OpcaoService extends GenericService<Opcao>{
	public abstract void salvar(List<Opcao> opcoes);

	public abstract void atualizar(List<Opcao> opcoes);
}
