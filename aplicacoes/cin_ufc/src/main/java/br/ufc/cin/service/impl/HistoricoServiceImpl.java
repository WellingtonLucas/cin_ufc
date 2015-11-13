package br.ufc.cin.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Entrega;
import br.ufc.cin.model.Historico;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Nota;
import br.ufc.cin.model.Resposta;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.repository.HistoricoRepository;
import br.ufc.cin.service.CalculoNotaService;
import br.ufc.cin.service.EntregaService;
import br.ufc.cin.service.HistoricoService;
import br.ufc.cin.service.RespostaService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class HistoricoServiceImpl extends GenericServiceImpl<Historico> implements HistoricoService{

	@Inject 
	private HistoricoRepository historicoRepository;
	
	@Inject 
	private CalculoNotaService calculoNotaService;
	
	@Inject
	private RespostaService respostaService;
	
	@Inject
	private EntregaService entregaService;
	
	@Override
	public Historico buscarPorJogoUsuario(Jogo jogo, Usuario usuario) {
		return historicoRepository.buscarPorJogoUsuario(jogo, usuario);
	}

	@Override
	public Float calculaMedia(Historico historico) {
		Float media = -0f;
		if(!historico.getNotas().isEmpty()){
			for (Nota nota : historico.getNotas()) {
				if(nota != null){
					media += nota.getValor();
				}
			}
			return media/historico.getNotas().size();
		}
		return -1f;
	}

	
	@Override
	public Historico atualizarHistorico(Historico historico,
			List<Rodada> rodadas, Usuario usuario) {
		if(historico.getNotas().size() < rodadas.size()){
			historico = criarNovasNotas(historico, rodadas);
		}
		for (Rodada rodada : rodadas) {
			if(!rodada.isStatusRaking()){//Alterar na história de liberar raking
				if(!historico.getNotas().isEmpty()){
					for(Nota nota: historico.getNotas()){
						if(nota.getRodada().equals(rodada)){
							List<Resposta> respostas = new ArrayList<Resposta>();
							for (Entrega entrega : entregaService.getUltimasEntregasDaRodada(rodada)) {
								Resposta resposta = respostaService.findUltimaRespostaPorEntrega(usuario, entrega);
								if(resposta != null){		
									respostas.add(resposta);
								}
							}
							if(!rodada.isStatusRaking() && !respostas.isEmpty()){
								nota.setValor(calculoNotaService.calculoMedia(respostas));
								historico.addNota(nota);
							}
						}
					}
				}
			}
		}
		update(historico);
		return historico;
	}

	@Override
	public Historico criarHistorico(Historico historico, List<Rodada> rodadas,
			Usuario usuario) {
		historico = new Historico();
		historico.setJogo(rodadas.get(0).getJogo());
		historico.setUsuario(usuario);
		List<Nota> notas = new ArrayList<Nota>();
		for (Rodada rodada : rodadas) {
			List<Resposta> respostas = new ArrayList<Resposta>();
			for (Entrega entrega : entregaService.getUltimasEntregasDaRodada(rodada)) {
				Resposta resposta = respostaService.findUltimaRespostaPorEntrega(usuario, entrega);
				if(resposta != null){		
					respostas.add(resposta);
				}
			}	
			Nota nota = new Nota();
			nota.setRodada(rodada);
			if(!rodada.isStatus() && !respostas.isEmpty()){
				nota.setValor(calculoNotaService.calculoMedia(respostas));
			}
			notas.add(nota);
		}
		historico.setNotas(notas);
		save(historico);
		return historico;
	}

	@Override
	public Historico criarNovasNotas(Historico historico, List<Rodada> rodadas) {
		for(int i=historico.getNotas().size(); i<rodadas.size();i++){
			Nota noaNota = new Nota();
			noaNota.setRodada(rodadas.get(i));
			noaNota.setValor(-1f);
			historico.addNota(noaNota);	
		}
		update(historico);
		return historico;
	}

}
