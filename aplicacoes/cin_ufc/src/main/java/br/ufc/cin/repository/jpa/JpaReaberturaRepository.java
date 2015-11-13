package br.ufc.cin.repository.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.ReaberturaSubmissao;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.repository.ReaberturaSubmissaoRepository;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaReaberturaRepository extends JpaGenericRepositoryImpl<ReaberturaSubmissao> implements ReaberturaSubmissaoRepository{

	@Override
	public ReaberturaSubmissao find(Equipe equipe, Rodada rodada) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idE", equipe.getId());
		params.put("idR", rodada.getId());
		List<ReaberturaSubmissao> result = find(QueryType.JPQL, "from reabertura_submissao where EQUIPE_ID = :idE and RODADA_ID = :idR " , params);
		if(!result.isEmpty() && result != null){
			return result.get(0);
		}
		return null;
	}

}
