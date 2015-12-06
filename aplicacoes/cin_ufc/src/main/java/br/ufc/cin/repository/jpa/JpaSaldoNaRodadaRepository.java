package br.ufc.cin.repository.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.SaldoNaRodada;
import br.ufc.cin.repository.SaldoNaRodadaRepository;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaSaldoNaRodadaRepository extends JpaGenericRepositoryImpl<SaldoNaRodada> implements SaldoNaRodadaRepository{

	@Override
	public SaldoNaRodada findByEquipeRodada(Equipe equipe, Rodada rodada) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idE", equipe.getId());
		params.put("idR", rodada.getId());
		SaldoNaRodada result = findFirst(QueryType.JPQL, "from saldo_na_rodada where EQUIPE_ID = :idE and RODADA_ID = :idR " , params,0);
		return result;
	}

	@Override
	public List<SaldoNaRodada> findByRodada(Rodada rodada) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idR", rodada.getId());
		List<SaldoNaRodada> result = find(QueryType.JPQL, "from saldo_na_rodada where RODADA_ID = :idR " , params);
		if(!result.isEmpty() && !(result == null)){
			return result;
		}
		return null;
	}

}
