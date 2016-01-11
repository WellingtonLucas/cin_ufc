package br.ufc.cin.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.SaldoNaRodada;
import br.ufc.cin.repository.SaldoNaRodadaRepository;
import br.ufc.cin.service.SaldoNaRodadaService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class SaldoNaRodadaServiceImpl extends GenericServiceImpl<SaldoNaRodada> implements SaldoNaRodadaService{

	@Inject
	private SaldoNaRodadaRepository saldoNaRodadaRespository;
	
	@Override
	public SaldoNaRodada findByEquipeRodada(Equipe equipe, Rodada rodada) {
		return saldoNaRodadaRespository.findByEquipeRodada(equipe, rodada);
	}

	@Override
	public List<SaldoNaRodada> findByRodada(Rodada rodada) {
		return saldoNaRodadaRespository.findByRodada(rodada);
	}

	@Override
	public void criarSaldoEquipesRodada(Jogo jogo, Rodada rodada) {
		for (Equipe equipe : jogo.getEquipes()) {
			SaldoNaRodada saldoNaRodada = new SaldoNaRodada();
			saldoNaRodada.setEquipe(equipe);
			saldoNaRodada.setRodada(rodada);
			saldoNaRodada.setSaldo(0F);
			saldoNaRodada.setDebito(0F);
			saldoNaRodada.setSaldoComFator(0F);
			save(saldoNaRodada);
		}
		
	}

	@Override
	public void deletePor(Rodada rodada) {
		List<SaldoNaRodada> saldos = findByRodada(rodada);
		if(saldos!=null){
			for (SaldoNaRodada saldoNaRodada : saldos) {
				delete(saldoNaRodada);
			}		
		}
	}

}
