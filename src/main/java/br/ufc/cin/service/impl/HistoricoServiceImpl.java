package br.ufc.cin.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Historico;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Nota;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.repository.HistoricoRepository;
import br.ufc.cin.service.HistoricoService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class HistoricoServiceImpl extends GenericServiceImpl<Historico> implements HistoricoService{

	@Inject 
	private HistoricoRepository historicoRepository;
	
	@Override
	public Historico buscarPorJogoUsuario(Jogo jogo, Usuario usuario) {
		return historicoRepository.buscarPorJogoUsuario(jogo, usuario);
	}

	@Override
	public Float calculaMedia(Historico historico) {
		Float media = -0f;
		if(!historico.getNotas().isEmpty()){
			for (Nota nota : historico.getNotas()) {
				media += nota.getValor(); 
			}
			return media/historico.getNotas().size();
		}
		return -1f;
	}

}
