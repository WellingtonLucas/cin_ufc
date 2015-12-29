package br.ufc.cin.repository;

import java.util.List;

import br.ufc.cin.model.Consultoria;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.SolicitacaoConsultoria;
import br.ufc.quixada.npi.repository.GenericRepository;

public interface SolicitacaoConsultoriaRepository extends GenericRepository<SolicitacaoConsultoria> {
	public abstract List<SolicitacaoConsultoria> findByConsultoria(Consultoria consultoria);

	public abstract SolicitacaoConsultoria findByEquipeConsulta(Equipe equipe,
			Consultoria consultoria);
}
