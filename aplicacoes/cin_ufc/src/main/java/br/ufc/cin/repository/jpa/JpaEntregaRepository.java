package br.ufc.cin.repository.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import br.ufc.cin.model.Entrega;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.repository.EntregaRepository;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaEntregaRepository extends JpaGenericRepositoryImpl<Entrega> implements EntregaRepository{

	@Override
	public List<Entrega> findByRodada(Rodada rodada) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idRodada", rodada.getId());
		List<Entrega> result = find(QueryType.JPQL, "from Entrega where id_rodada = :idRodada", params);
		if(result != null && !result.isEmpty()) {
			return result;
		}
		return null;
	}

}
