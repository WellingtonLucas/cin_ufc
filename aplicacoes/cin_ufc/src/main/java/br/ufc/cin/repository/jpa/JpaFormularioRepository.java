package br.ufc.cin.repository.jpa;

import javax.inject.Named;

import br.ufc.cin.model.Formulario;
import br.ufc.cin.repository.FormularioRepository;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaFormularioRepository extends JpaGenericRepositoryImpl<Formulario> implements FormularioRepository{

}
