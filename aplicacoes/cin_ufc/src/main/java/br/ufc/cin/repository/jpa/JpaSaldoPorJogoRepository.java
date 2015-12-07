package br.ufc.cin.repository.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.SaldoPorJogo;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.repository.SaldoPorJogoRepository;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaSaldoPorJogoRepository extends JpaGenericRepositoryImpl<SaldoPorJogo> implements SaldoPorJogoRepository{

	@Override
	public SaldoPorJogo findByUsuarioJogo(Usuario apostador, Jogo jogo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idA", apostador.getId());
		params.put("idJ", jogo.getId());
		SaldoPorJogo result = findFirst(QueryType.JPQL, "from saldo_por_jogo where APOSTADOR_ID = :idA and JOGO_ID = :idJ " , params, 0);
		return result;
	}

	@Override
	public List<SaldoPorJogo> findByJogo(Jogo jogo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idJ", jogo.getId());
		List<SaldoPorJogo> result = find(QueryType.JPQL, "from saldo_por_jogo where JOGO_ID = :idJ " , params);
		if(!result.isEmpty() && !(result == null)){
			return result;
		}
		return null;
	}

}
