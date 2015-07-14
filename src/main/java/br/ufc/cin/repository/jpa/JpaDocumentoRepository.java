package br.ufc.cin.repository.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import br.ufc.cin.model.Documento;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.repository.DocumentoRepository;
import br.ufc.quixada.npi.enumeration.QueryType;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaDocumentoRepository extends JpaGenericRepositoryImpl<Documento> implements DocumentoRepository{
	
	@Override
	public List<Documento> getDocumentosByJogo(Jogo jogo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", jogo.getId());
		return find(QueryType.JPQL, "select d from Documento as d where d.jogo.id = :id" , params);
	}
	
}
