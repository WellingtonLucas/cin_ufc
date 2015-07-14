package br.ufc.cin.service.impl;

import java.util.List;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;

import br.ufc.cin.model.Documento;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.repository.DocumentoRepository;
import br.ufc.cin.service.DocumentoService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class DocumentoServiceImpl extends GenericServiceImpl<Documento> implements DocumentoService{
	
	@Autowired
	private DocumentoRepository documentoRepository;
	
	@Override
	public void salvar(List<Documento> documentos) {
		for(Documento documento : documentos) {
			save(documento);
		}		
	}

	@Override
	public List<Documento> getDocumentoByProjeto(Jogo jogo) {		
		return documentoRepository.getDocumentosByJogo(jogo);
	}

}
