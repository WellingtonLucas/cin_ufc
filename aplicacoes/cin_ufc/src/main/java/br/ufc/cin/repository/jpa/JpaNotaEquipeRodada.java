package br.ufc.cin.repository.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.NotaEquipeRodada;
import br.ufc.cin.repository.NotaEquipeRodadaRepository;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaNotaEquipeRodada extends JpaGenericRepositoryImpl<NotaEquipeRodada> implements NotaEquipeRodadaRepository{

	@Override
	public List<NotaEquipeRodada> buscarPorEquipe(Equipe equipe) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idEquipe", equipe.getId());
		List<NotaEquipeRodada> result = find(QueryType.JPQL, "from nota_equipe_rodada where EQUIPE_ID = :idEquipe", params);
		if(result != null && !result.isEmpty()) {
			return result;
		}
		return null;
	}

}
