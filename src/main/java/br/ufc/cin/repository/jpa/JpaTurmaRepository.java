package br.ufc.cin.repository.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import br.ufc.cin.model.Turma;
import br.ufc.cin.repository.TurmaRepository;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaTurmaRepository extends JpaGenericRepositoryImpl<Turma> implements TurmaRepository{
	
	@Override
	public List<Turma> getTurmaBySemestre(String semestre) {
		Map<String, Object> params = new HashMap<String, Object>();	
		params.put("semestre", semestre);
		List<Turma> turmas = find(QueryType.JPQL, "from Turma where semestre = :semestre", params);
		if(!turmas.isEmpty() && turmas != null){
			return turmas;
		}		
		return null;
	}

	@Override
	public Turma getTurmasBySemestreCurso(String semestre, String sigla) {
		Map<String, Object> params = new HashMap<String, Object>();	
		params.put("semestre", semestre);
		params.put("sigla", sigla);
		List<Turma> turmas = find(QueryType.JPQL, "from Turma where semestre = :semestre and sigla_curso = :sigla", params);
		if(!turmas.isEmpty() && turmas != null){
			return turmas.get(0);
		}
		return null;
	}

}
