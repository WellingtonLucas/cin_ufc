package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Nota;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.SaldoNaRodada;
import br.ufc.cin.model.SaldoPorJogo;

public interface RankingService {

	public abstract List<Nota> ordenaNotas(Rodada rodada);

	public abstract List<SaldoNaRodada> ordenaSaldos(Rodada rodada);

	public abstract List<Equipe> ordenaEquipes(Jogo jogo);

	public abstract List<SaldoPorJogo> ordenaSaldosPorJogo(Jogo jogo);
}
