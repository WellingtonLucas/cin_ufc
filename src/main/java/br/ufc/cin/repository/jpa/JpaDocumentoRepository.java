package br.ufc.cin.repository.jpa;

import javax.inject.Named;

import br.ufc.cin.model.Documento;
import br.ufc.cin.repository.DocumentoRepository;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaDocumentoRepository extends JpaGenericRepositoryImpl<Documento> implements DocumentoRepository{
	
}
