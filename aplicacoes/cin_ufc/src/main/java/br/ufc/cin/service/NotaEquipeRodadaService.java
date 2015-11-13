package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.NotaEquipeRodada;
import br.ufc.quixada.npi.service.GenericService;

public interface NotaEquipeRodadaService extends GenericService<NotaEquipeRodada>{

	public abstract List<NotaEquipeRodada> buscarPorEquipe(Equipe equipe);

}
