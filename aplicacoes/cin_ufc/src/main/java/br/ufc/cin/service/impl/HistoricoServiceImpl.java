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
import br.ufc.cin.service.NotaService;
import br.ufc.cin.service.RespostaService;
import br.ufc.cin.service.RodadaService;
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
	
	@Inject
	private RodadaService rodadaService;
	
	@Inject
	private NotaService notaService;
	
	@Override
	public Historico buscarPorJogoUsuario(Jogo jogo, Usuario usuario) {
		return historicoRepository.buscarPorJogoUsuario(jogo, usuario);
	}

	@Override
	public Float calculaMedia(Historico historico) {
		Float media = 0f;
		if(historico.getNotas()!= null && !historico.getNotas().isEmpty()){
			int cont =0 ;
			for (Nota nota : historico.getNotas()) {
				if(nota != null && nota.getRodada().isStatusRaking() && nota.getValor()!= null){
					media += nota.getValor();
					cont++;
				}
			}
			if(cont>0)
				media = (Float) media/cont;
		}
		return media;
	}

	@Override
	public Historico atualizarHistorico(Historico historico,
			List<Rodada> rodadas, Usuario usuario) {
		if(historico.getNotas().size() < rodadas.size()){
			historico = criarNovasNotas(historico, rodadas);
		}
		for (Rodada rodada : rodadas) {
			for(Nota nota: historico.getNotas()){
				if(nota.getRodada().equals(rodada)){
					List<Resposta> respostas = new ArrayList<Resposta>();
					for (Entrega entrega : entregaService.getUltimasEntregasDaRodada(rodada)) {
						Resposta resposta = respostaService.findUltimaRespostaPorEntrega(usuario, entrega);
						if(resposta != null){		
							respostas.add(resposta);
						}
					}
					nota.setSatus(false);
					nota.setValor(0F);
					if(rodada.isStatusRaking() && !respostas.isEmpty()){
						nota.setSatus(true);
						nota.setValor(calculoNotaService.calculoMedia(respostas));
					}
					historico.addNota(nota);
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
		save(historico);
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
			nota.setSatus(false);
			nota.setValor(0f);
			if(rodada.isStatusRaking() && !respostas.isEmpty()){
				nota.setSatus(true);
				nota.setValor(calculoNotaService.calculoMedia(respostas));
			}
			historico.addNota(nota);
			notaService.save(nota);
		}
		update(historico);
		return historico;
	}

	@Override
	public Historico criarNovasNotas(Historico historico, List<Rodada> rodadas) {
		for(int i=historico.getNotas().size(); i<rodadas.size();i++){
			Nota novaNota = new Nota();
			novaNota.setRodada(rodadas.get(i));
			novaNota.setValor(0f);
			novaNota.setSatus(false);
			historico.addNota(novaNota);	
		}
		update(historico);
		return historico;
	}

	@Override
	public void atualizaNotas(List<Historico> historicos, Jogo jogo) {
		List<Rodada> rodadas;
		rodadas = rodadaService.ordenaPorInicio(jogo.getRodadas());
		rodadas = rodadaService.atualizaStatusRodadas(rodadas);
		for (Usuario aluno : jogo.getAlunos()) {
			Historico historico;
			historico = buscarPorJogoUsuario(jogo, aluno);
			if(historico == null){
				historico = criarHistorico(historico, rodadas, aluno);
			}else{
				historico = atualizarHistorico(historico, rodadas, aluno);
			}			
		}
	}

	@Override
	public List<Historico> findByJogo(Jogo jogo) {
		return historicoRepository.findByJogo(jogo);
	}

}
