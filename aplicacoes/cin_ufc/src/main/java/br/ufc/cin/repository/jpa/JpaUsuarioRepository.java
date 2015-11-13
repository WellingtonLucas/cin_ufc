package br.ufc.cin.repository.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import br.ufc.cin.model.Usuario;
import br.ufc.cin.repository.UsuarioRepository;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaUsuarioRepository extends JpaGenericRepositoryImpl<Usuario> implements  UsuarioRepository {

	@Override
	public Usuario getUsuarioByLogin(String login) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("login", login);
		List<Usuario> result = find(QueryType.JPQL, "from Usuario where login = :login", params);
		if(result != null && !result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public Usuario getUsuarioByEmail(String email) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("email", email);
		List<Usuario> result = find(QueryType.JPQL, "from Usuario where email = :email", params);
		if(result != null && !result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public List<Usuario> getPossiveisParticipantes(Usuario usuario) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", usuario.getId());
		List<Usuario> result = find(QueryType.JPQL, "from Usuario where id != :id", params);
		if(result != null && !result.isEmpty()) {
			return result;
		}
		return null;
	}

}
