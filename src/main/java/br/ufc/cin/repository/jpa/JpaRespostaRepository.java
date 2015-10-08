package br.ufc.cin.repository.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import br.ufc.cin.model.Entrega;
import br.ufc.cin.model.Resposta;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.repository.RespostaRepository;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaRespostaRepository extends JpaGenericRepositoryImpl<Resposta>  implements RespostaRepository{

	@Override
	public Resposta getRespostaByEntrega(Entrega entrega) {
		Map<String, Object> params = new HashMap<String, Object>();	
		params.put("idEntrega", entrega.getId());
		List<Resposta> respostas = find(QueryType.JPQL, "from Resposta where entregagabarito_id = :idEntrega", params);
		if(!respostas.isEmpty() && respostas != null){
			return respostas.get(respostas.size()-1);
		}		
		return null;
	}

	@Override
	public List<Resposta> find(Usuario usuarioRequisitado) {
		Map<String, Object> params = new HashMap<String, Object>();	
		params.put("idU", usuarioRequisitado.getId());
		List<Resposta> respostas = find(QueryType.JPQL, "from Resposta where usuario_id = :idU", params);
		if(!respostas.isEmpty() && respostas != null){
			return respostas;
		}		
		return null;
	}

}
