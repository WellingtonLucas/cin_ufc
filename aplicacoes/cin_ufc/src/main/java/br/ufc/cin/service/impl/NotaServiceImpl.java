package br.ufc.cin.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Historico;
import br.ufc.cin.model.Nota;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.repository.NotaRepository;
import br.ufc.cin.service.NotaService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class NotaServiceImpl extends GenericServiceImpl<Nota> implements NotaService{

	@Inject
	private NotaRepository notaRepository;
	
	@Override
	public List<Nota> findByRodada(Rodada rodada) {
		return notaRepository.findByRodada(rodada);
	}

	@Override
	public void deletePor(Rodada rodada) {
		List<Nota> notas = findByRodada(rodada);
		if(notas!=null){
			for (Nota nota : notas) {
				delete(nota);
			}
		}
	}

	@Override
	public Nota findByHistoricoRodada(Historico historico, Rodada rodada) {
		return notaRepository.findByHistoricoRodada(historico,rodada);
	}

}
