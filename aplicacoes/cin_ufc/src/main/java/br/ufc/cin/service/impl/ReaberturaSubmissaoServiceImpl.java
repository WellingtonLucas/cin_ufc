package br.ufc.cin.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.ReaberturaSubmissao;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.repository.ReaberturaSubmissaoRepository;
import br.ufc.cin.service.EquipeService;
import br.ufc.cin.service.ReaberturaSubmissaoService;
import br.ufc.cin.service.RodadaService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class ReaberturaSubmissaoServiceImpl extends GenericServiceImpl<ReaberturaSubmissao> implements ReaberturaSubmissaoService{

	@Inject
	private ReaberturaSubmissaoRepository reaberturaSubmissaorRepository;
	
	@Inject
	private RodadaService rodadaService;
	
	@Inject
	private EquipeService equipeService;
	
	@Override
	public List<Equipe> atualizarSolitacoesDeReabertura(List<Equipe> equipes,
			Rodada rodada) {
		List<Equipe> equipesAtualizadas = new ArrayList<Equipe>();
		for (Equipe equipe : equipes) {
			ReaberturaSubmissao temp = find(equipe, rodada);
			if(temp == null){
				equipe.setStatusReabertura(false);
			}else{
				equipe.setStatusReabertura(temp.isStatus());
			}
			equipesAtualizadas.add(equipe);
		}
		return equipesAtualizadas;	
	}

	@Override
	public ReaberturaSubmissao find(Equipe equipe, Rodada rodada) {
		return reaberturaSubmissaorRepository.find(equipe, rodada);
	}

	@Override
	public void solicitarReabertura(Rodada rodada, Equipe equipe,ReaberturaSubmissao reaberturaSubmissao) {
		if( !rodada.isStatusPrazo()){
			throw new IllegalArgumentException("Uma solicitação de reabertura do prazo de submissão deve ser feita antes do encerramento deste.");
		}else{
			ReaberturaSubmissao oldReaberturaSubmissao = find(equipe, rodada);
			if(oldReaberturaSubmissao == null){
				if(equipe.getId() == reaberturaSubmissao.getEquipe().getId()){
					reaberturaSubmissao.setEquipe(equipe);
				}else{
					throw new IllegalArgumentException("Tente não burlar o sistema. :)");
				}
				if(rodada.getId() == reaberturaSubmissao.getRodada().getId()){
					reaberturaSubmissao.setRodada(rodadaService.find(Rodada.class, rodada.getId()));
				}else{
					throw new IllegalArgumentException("Tente não burlar o sistema. :)");
				}
				try {
					reaberturaSubmissao.setStatus(true);
					update(reaberturaSubmissao);
					equipe.addReaberturaSubmissao(reaberturaSubmissao);
					equipeService.update(equipe);
				}catch (IllegalArgumentException e) {
					throw new IllegalArgumentException("Erro ao efetuar pedido. Verifique seus valores e tente novamente.");
				}
			}else{
				throw new IllegalArgumentException("Já existe um pedido de prorrogação para esta equipe.");
			}
		}
	
		
	}

	@Override
	public List<ReaberturaSubmissao> findByRodada(Rodada rodada) {
		return reaberturaSubmissaorRepository.findByRodada(rodada);
	}

	@Override
	public void verificaSeTemPedido(Rodada rodada, Equipe equipe) {
		ReaberturaSubmissao reaberturaSubmissao = find(equipe, rodada);
		if(reaberturaSubmissao==null){
			throw new IllegalAccessError("Esta equipe não possui pedido de reabertura para a rodada.");
		}else if(reaberturaSubmissao.getQuantidadeDia() == null){
			throw new IllegalAccessError("Esta equipe não possui pedido de reabertura para a rodada."); 
		}
		Integer temp = Integer.parseInt(reaberturaSubmissao.getQuantidadeDia());
		if(temp <= 0 || temp > 3){
			throw new IllegalArgumentException("Esta equipe não possui pedido de reabertura para a rodada");
		}
	}

	@Override
	public void deletePor(Rodada rodada) {
		List<ReaberturaSubmissao> reaberturas = findByRodada(rodada);
		if(reaberturas!=null){
			for (ReaberturaSubmissao reaberturaSubmissao : reaberturas) {
				delete(reaberturaSubmissao);
			}
		}
		
	}

}
