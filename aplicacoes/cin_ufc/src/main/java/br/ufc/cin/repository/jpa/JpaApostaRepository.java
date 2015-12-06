package br.ufc.cin.repository.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import br.ufc.cin.model.Aposta;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.repository.ApostaRepository;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaApostaRepository extends JpaGenericRepositoryImpl<Aposta> implements ApostaRepository{

	@Override
	public Aposta findByUsuarioRodada(Usuario apostador, Rodada rodada) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idA", apostador.getId());
		params.put("idR", rodada.getId());
		Aposta result = findFirst(QueryType.JPQL, "from aposta where APOSTADOR_ID = :idA and RODADA_ID = :idR " , params, 0);
		return result;
	}

	@Override
	public List<Aposta> findByRodada(Rodada rodada) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idR", rodada.getId());
		List<Aposta> result = find(QueryType.JPQL, "from aposta where RODADA_ID = :idR " , params);
		if(!result.isEmpty() && !(result == null)){
			return result;
		}
		return null;
	}

}
