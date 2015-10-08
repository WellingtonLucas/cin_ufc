package br.ufc.cin.repository.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.StatusRodadaEquipe;
import br.ufc.cin.repository.RodadaEquipeRepository;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaRodadaEquipeRepository extends JpaGenericRepositoryImpl<StatusRodadaEquipe> implements RodadaEquipeRepository{

	@Override
	public StatusRodadaEquipe find(Equipe equipe, Rodada rodada) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idE", equipe.getId());
		params.put("idR", rodada.getId());
		List<StatusRodadaEquipe> result = find(QueryType.JPQL, "from rodada_equipe where EQUIPE_ID = :idE and RODADA_ID = :idR " , params);
		if(!result.isEmpty() && result != null){
			return result.get(0);
		}
		return null;
	}
}
