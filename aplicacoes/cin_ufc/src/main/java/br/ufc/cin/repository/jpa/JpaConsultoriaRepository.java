package br.ufc.cin.repository.jpa;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import br.ufc.cin.model.Consultoria;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.repository.ConsultoriaRepository;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaConsultoriaRepository extends JpaGenericRepositoryImpl<Consultoria> implements ConsultoriaRepository{

	@Override
	public Consultoria findByRodada(Rodada rodada) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idR", rodada.getId());
		Consultoria result = findFirst(QueryType.JPQL, "from Consultoria where RODADA_ID = :idR " , params, 0);
		return result;
	}
}
