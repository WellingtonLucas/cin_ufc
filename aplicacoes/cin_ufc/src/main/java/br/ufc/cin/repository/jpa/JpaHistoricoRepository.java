package br.ufc.cin.repository.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import br.ufc.cin.model.Historico;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.repository.HistoricoRepository;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaHistoricoRepository extends JpaGenericRepositoryImpl<Historico> implements HistoricoRepository{

	@Override
	public Historico buscarPorJogoUsuario(Jogo jogo, Usuario usuario) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idJ", jogo.getId());
		params.put("idU", usuario.getId());
		List<Historico> result = find(QueryType.JPQL, "from Historico where USUARIO_ID = :idU and JOGO_ID = :idJ", params);
		if(result != null && !result.isEmpty()) {
			return result.get(0);
		}
		return null;

	}

}
