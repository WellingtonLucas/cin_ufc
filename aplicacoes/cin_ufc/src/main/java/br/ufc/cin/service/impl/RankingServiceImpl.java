package br.ufc.cin.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Nota;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.SaldoNaRodada;
import br.ufc.cin.model.SaldoPorJogo;
import br.ufc.cin.service.NotaService;
import br.ufc.cin.service.RankingService;
import br.ufc.cin.service.SaldoNaRodadaService;
import br.ufc.cin.service.SaldoPorJogoService;

@Named
public class RankingServiceImpl implements RankingService{

	@Inject
	private NotaService notaService;
	
	@Inject
	private SaldoNaRodadaService saldoNaRodadaService;
	
	@Inject
	private SaldoPorJogoService saldoPorJogoService; 
	
	@Override
	public List<Nota> ordenaNotas(Rodada rodada) {
		List<Nota> notas = notaService.findByRodada(rodada);
		if(notas != null){
			for (int i=0;i<notas.size();i++) {
				for (int j = i+1; j < notas.size(); j++) {
					if(notas.get(i).getValor() < notas.get(j).getValor()){
						Nota aux = notas.get(i);
						notas.add(i, notas.get(j));
						notas.remove(i+1);
						notas.add(j, aux);
						notas.remove(j+1);
					}
				}
			}
			return notas;
		}
		return null;
	}

	@Override
	public List<SaldoNaRodada> ordenaSaldos(Rodada rodada) {
		List<SaldoNaRodada> saldos = saldoNaRodadaService.findByRodada(rodada);
		if(saldos != null){
			for (int i=0;i<saldos.size();i++) {
				for (int j = i+1; j < saldos.size(); j++) {
					if(saldos.get(i).getSaldoComFator() < saldos.get(j).getSaldoComFator()){
						SaldoNaRodada aux = saldos.get(i);
						saldos.add(i, saldos.get(j));
						saldos.remove(i+1);
						saldos.add(j, aux);
						saldos.remove(j+1);
					}
				}
			}
			return saldos;
		}
		return null;
	}

	@Override
	public List<Equipe> ordenaEquipes(Jogo jogo) {
		List<Equipe> equipes = jogo.getEquipes();
		if(equipes != null){
			for (int i=0;i<equipes.size();i++) {
				for (int j = i+1; j < equipes.size(); j++) {
					if(equipes.get(i).getSaldo() < equipes.get(j).getSaldo()){
						Equipe aux = equipes.get(i);
						equipes.add(i, equipes.get(j));
						equipes.remove(i+1);
						equipes.add(j, aux);
						equipes.remove(j+1);
					}
				}
			}
			return equipes;
		}
		throw new IllegalArgumentException(
				"O jogo ainda não possui equipes.");
	}

	@Override
	public List<SaldoPorJogo> ordenaSaldosPorJogo(Jogo jogo) {
		List<SaldoPorJogo> saldosPorJogo = saldoPorJogoService.findByJogo(jogo);
		if(saldosPorJogo != null){
			for (int i=0;i<saldosPorJogo.size();i++) {
				for (int j = i+1; j < saldosPorJogo.size(); j++) {
					if(saldosPorJogo.get(i).getSaldo() < saldosPorJogo.get(j).getSaldo()){
						SaldoPorJogo aux = saldosPorJogo.get(i);
						saldosPorJogo.add(i, saldosPorJogo.get(j));
						saldosPorJogo.remove(i+1);
						saldosPorJogo.add(j, aux);
						saldosPorJogo.remove(j+1);
					}
				}
			}
			return saldosPorJogo;
		}
		throw new IllegalArgumentException(
				"Nenhum períogo de apostas finalizado até o momento.");
	}

	
}
