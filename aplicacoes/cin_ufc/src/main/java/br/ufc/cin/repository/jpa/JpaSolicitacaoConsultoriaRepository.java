package br.ufc.cin.repository.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import br.ufc.cin.model.Consultoria;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.SolicitacaoConsultoria;
import br.ufc.cin.repository.SolicitacaoConsultoriaRepository;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaSolicitacaoConsultoriaRepository extends
		JpaGenericRepositoryImpl<SolicitacaoConsultoria> implements
		SolicitacaoConsultoriaRepository {

	@Override
	public List<SolicitacaoConsultoria> findByConsultoria(
			Consultoria consultoria) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idC", consultoria.getId());
		List<SolicitacaoConsultoria> result = find(QueryType.JPQL, "from solicitacao_consultoria where CONSULTORIA_ID = :idC " , params);
		if(!result.isEmpty() && !(result == null)){
			return result;
		}
		return null;
	}

	@Override
	public SolicitacaoConsultoria findByEquipeConsulta(Equipe equipe,
			Consultoria consultoria) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idE", equipe.getId());
		params.put("idC", consultoria.getId());
		SolicitacaoConsultoria result = findFirst(QueryType.JPQL, "from solicitacao_consultoria where CONSULTORIA_ID = :idC and EQUIPE_ID = :idE " , params, 0);
		return result;
	}

}
