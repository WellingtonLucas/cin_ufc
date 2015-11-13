package br.ufc.cin.repository.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.repository.EquipeRepository;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaEquipeRepository extends JpaGenericRepositoryImpl<Equipe> implements EquipeRepository{

	@Override
	public List<Equipe> getEquipeByJogo(Jogo jogo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", jogo.getId());
		List<Equipe> result = find(QueryType.JPQL, "from Equipe where jogo_id = :id", params);
		if(result != null && !result.isEmpty()) {
			return result;
		}
		return null;

	}

}
