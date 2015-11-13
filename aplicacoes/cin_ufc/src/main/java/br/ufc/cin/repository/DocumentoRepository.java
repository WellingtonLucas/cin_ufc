package br.ufc.cin.repository;

import java.util.List;

import br.ufc.cin.model.Documento;
import br.ufc.cin.model.Jogo;
import br.ufc.quixada.npi.repository.GenericRepository;

public interface DocumentoRepository extends GenericRepository<Documento>{
	public abstract List<Documento> getDocumentosByJogo(Jogo jogo);
}
