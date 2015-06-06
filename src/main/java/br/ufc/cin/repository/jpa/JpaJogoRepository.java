package br.ufc.cin.repository.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import br.ufc.cin.model.Jogo;
import br.ufc.cin.repository.JogoRepository;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaJogoRepository extends JpaGenericRepositoryImpl<Jogo> implements JogoRepository{
	
	@Override
	public List<Jogo> getJogoBySemestre(String semestre) {
		Map<String, Object> params = new HashMap<String, Object>();	
		params.put("semestre", semestre);
		List<Jogo> jogos = find(QueryType.JPQL, "from Jogo where semestre = :semestre", params);
		if(!jogos.isEmpty() && jogos != null){
			return jogos;
		}		
		return null;
	}

	@Override
	public Jogo getJogosBySemestreCurso(String semestre, String nomeCurso) {
		Map<String, Object> params = new HashMap<String, Object>();	
		params.put("semestre", semestre);
		params.put("nome", nomeCurso);
		List<Jogo> jogos = find(QueryType.JPQL, "from Jogo where semestre = :semestre and nome_curso = :nome", params);
		if(!jogos.isEmpty() && jogos != null){
			return jogos.get(0);
		}
		return null;
	}

	@Override
	public List<Jogo> getJogosByProfessor(Integer idProfessor) {
		Map<String, Object> params = new HashMap<String, Object>();	
		params.put("idProf", idProfessor);		
		List<Jogo> jogos = find(QueryType.JPQL, "from Jogo where id_professor = :idProf", params);
		if(!jogos.isEmpty() && jogos != null){
			return jogos;
		}
		return null;
	}

}
