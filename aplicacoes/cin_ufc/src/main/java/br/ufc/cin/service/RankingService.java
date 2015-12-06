package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Nota;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.SaldoNaRodada;

public interface RankingService {

	public abstract List<Nota> ordenaNotas(Rodada rodada);

	public abstract List<SaldoNaRodada> ordenaSaldos(Rodada rodada);
}
