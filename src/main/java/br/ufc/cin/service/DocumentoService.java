package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Documento;
import br.ufc.cin.model.Jogo;
import br.ufc.quixada.npi.service.GenericService;

public interface DocumentoService extends GenericService<Documento> {
	
	public abstract void salvar(List<Documento> documentos);

	List<Documento> getDocumentoByProjeto(Jogo jogo);

	public abstract boolean verificaExtensao(String extensao);
}
