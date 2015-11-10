package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Resposta;

public interface CalculoNotaService {
	
	public abstract Float calculoNota(Resposta resposta);
	
	public abstract Float calculoMedia(List<Resposta> respostas);

	public abstract Float calculoNotaEquipe(Resposta resposta);
}
