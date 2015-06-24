package br.ufc.cin.service.impl;

import java.util.List;

import javax.inject.Named;

import br.ufc.cin.model.Documento;
import br.ufc.cin.service.DocumentoService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class DocumentoServiceImpl extends GenericServiceImpl<Documento> implements DocumentoService{
	
	@Override
	public void salvar(List<Documento> documentos) {
		for(Documento documento : documentos) {
			save(documento);
		}		
	}

}
