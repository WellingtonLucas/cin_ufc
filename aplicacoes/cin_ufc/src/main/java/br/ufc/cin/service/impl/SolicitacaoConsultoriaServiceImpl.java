package br.ufc.cin.service.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Consultoria;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.SaldoNaRodada;
import br.ufc.cin.model.SolicitacaoConsultoria;
import br.ufc.cin.repository.SolicitacaoConsultoriaRepository;
import br.ufc.cin.service.RegrasService;
import br.ufc.cin.service.SaldoNaRodadaService;
import br.ufc.cin.service.SolicitacaoConsultoriaService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class SolicitacaoConsultoriaServiceImpl extends GenericServiceImpl<SolicitacaoConsultoria> implements SolicitacaoConsultoriaService{
	
	@Inject
	private SolicitacaoConsultoriaRepository solicitacaoConsultoriaRepository;
	
	@Inject
	private SaldoNaRodadaService  saldoNaRodadaService;
	
	@Inject
	private RegrasService regrasService;
	
	@Override
	public List<SolicitacaoConsultoria> solicitacoesPorConsulta(
			Consultoria consultoria) {
		return solicitacaoConsultoriaRepository.findByConsultoria(consultoria);
	}

	@Override
	public void confirmarSolicitacao(
			SolicitacaoConsultoria solicitacaoConsultoria, Equipe equipe) {
		if(!solicitacaoConsultoria.isStatus()){
			solicitacaoConsultoria.setStatus(true);
			solicitacaoConsultoria.setDiaConfirmacao(new Date());
			Float saldo = solicitacaoConsultoria.getConsultoria().getValor();
			SaldoNaRodada saldoNaRodada = saldoNaRodadaService.findByEquipeRodada(equipe, solicitacaoConsultoria.getConsultoria().getRodada());
			if(saldoNaRodada==null){
				saldoNaRodada = new SaldoNaRodada();
				saldoNaRodada.setEquipe(equipe);
				saldoNaRodada.setSaldo(0F);
				saldoNaRodada.setRodada(solicitacaoConsultoria.getConsultoria().getRodada());
			}
			saldoNaRodada.setSaldo(saldoNaRodada.getSaldo() - saldo);
			saldoNaRodadaService.update(saldoNaRodada);
			update(solicitacaoConsultoria);
		}else{
			throw new IllegalArgumentException("Você não pode confirmar uma consultoria mais de uma vez por rodada.");
		}
	}

	@Override
	public void verificaConsistencia(
			SolicitacaoConsultoria solicitacaoConsultoria, Equipe equipe,
			Rodada rodada) {
		regrasService.verificaSolicitacao(solicitacaoConsultoria);
		regrasService.verificaEquipeSolicitacao(equipe, solicitacaoConsultoria);
		regrasService.verificaRodadaSolicitacao(rodada, solicitacaoConsultoria);
	}

	@Override
	public SolicitacaoConsultoria findByEquipeConsulta(Equipe equipe,
			Consultoria consultoria) {
		return solicitacaoConsultoriaRepository.findByEquipeConsulta(equipe, consultoria);
	}
}
