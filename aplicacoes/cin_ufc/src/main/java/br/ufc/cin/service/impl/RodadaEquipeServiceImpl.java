package br.ufc.cin.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.ReaberturaSubmissao;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.StatusRodadaEquipe;
import br.ufc.cin.repository.RodadaEquipeRepository;
import br.ufc.cin.service.RodadaEquipeService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class RodadaEquipeServiceImpl extends GenericServiceImpl<StatusRodadaEquipe> implements RodadaEquipeService{

	@Inject
	private RodadaEquipeRepository rodadaEquipeRepository;
	
	@Override
	public StatusRodadaEquipe find(Equipe equipe, Rodada rodada) {
		return rodadaEquipeRepository.find(equipe, rodada);
	}
	
	@Override
	public void setStatusEquipeRodada(Rodada rodada, List<Equipe> equipes, boolean status) {
		for (Equipe equipe : equipes) {
			StatusRodadaEquipe rodadaEquipe = new StatusRodadaEquipe();
			rodadaEquipe.setEquipe(equipe);
			rodadaEquipe.setRodada(rodada);
			rodadaEquipe.setAtiva(status);
			rodadaEquipeRepository.save(rodadaEquipe);
		}
	}

	@Override
	public List<Equipe> atualizaStatusEquipesNaRodada(List<Equipe> equipes, Rodada rodada) {
		List<Equipe> equipesAtualizadas = new ArrayList<Equipe>();
		for (Equipe equipe : equipes) {
			StatusRodadaEquipe temp = find(equipe, rodada);
			if(temp == null){
				equipe.setStatusNaRodada(false);
			}else{
				equipe.setStatusNaRodada(temp.isAtiva());
			}
			equipesAtualizadas.add(equipe);
		}
		return equipesAtualizadas;		
	}

	@Override
	public StatusRodadaEquipe atualizaStatusRodadaEquipe(ReaberturaSubmissao reabertura) {
		StatusRodadaEquipe status = find(reabertura.getEquipe(), reabertura.getRodada());
		if(status != null){
			if(!status.isAtiva())
				return status;
			status.setAtiva(verificaPrazo(reabertura));
		}else{
			status = new StatusRodadaEquipe();
			status.setEquipe(reabertura.getEquipe());
			status.setRodada(reabertura.getRodada());
			status.setAtiva(false);
		}
		update(status);
		return status;
	}
	
	private Long umDiaTimeInMillis(){
		Calendar cal1 = Calendar.getInstance();
		cal1.set(2015, 10, 7);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.set(2015, 10, 8);
		return cal2.getTimeInMillis() - cal1.getTimeInMillis();
	}
	
	private boolean verificaPrazo(ReaberturaSubmissao reabertura){
		Calendar hoje = Calendar.getInstance();
		Long hojeMillis = hoje.getTimeInMillis();
		
		int qtdDias = Integer.parseInt(reabertura.getQuantidadeDia());
		Long prazoSubmissao =reabertura.getRodada().getPrazoSubmissao().getTime(); 
		Long prazoFinal = prazoSubmissao + (umDiaTimeInMillis() * qtdDias);
		
		if((prazoFinal < hojeMillis || reabertura.getRodada().isStatusPrazo())){
			return false;
		}
		return true;
	}

	@Override
	public void deletePor(Rodada rodada) {
		List<StatusRodadaEquipe> rodadaEquipe = rodadaEquipeRepository.find(rodada);
		if(rodadaEquipe != null){
			for (StatusRodadaEquipe statusRodadaEquipe : rodadaEquipe) {
				delete(statusRodadaEquipe);
			}
		}
	}

	@Override
	public boolean verificaSeTemSolicitacao(List<Equipe> equipes, Rodada rodada) {
		for (Equipe equipe : equipes) {
			StatusRodadaEquipe rodadaEquipe = find(equipe, rodada);
			if(rodadaEquipe != null){
				if(rodadaEquipe.isAtiva()){
					throw new IllegalArgumentException("Aguarte até o prazo final de prorrogação dos prazos.");
				}
			}
		}
		return false;
	}

	@Override
	public void verificaStatusEquipeRodada(Equipe equipe, Rodada rodada) {
		if(equipe !=null){
			StatusRodadaEquipe statusRodadaEquipe = find(equipe, rodada);
			if(!statusRodadaEquipe.isAtiva()){
				throw new IllegalArgumentException("A sua equipe não possui permissão de submissão.");
			}
		}
	}

}
