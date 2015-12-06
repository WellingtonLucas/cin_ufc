package br.ufc.cin.repository.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import br.ufc.cin.model.Nota;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.repository.NotaRepository;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaNotaRepository extends JpaGenericRepositoryImpl<Nota> implements NotaRepository{

	@Override
	public List<Nota> findByRodada(Rodada rodada) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idRodada", rodada.getId());
		List<Nota> result = find(QueryType.JPQL, "from Nota where RODADA_ID = :idRodada", params);
		if(result != null && !result.isEmpty()) {
			return result;
		}
		return null;
	}

}
