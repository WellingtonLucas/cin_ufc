package br.ufc.cin.repository.jpa;

import javax.inject.Named;

import br.ufc.cin.model.Professor;
import br.ufc.cin.repository.ProfessorReposiroty;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaProfessorRepository extends JpaGenericRepositoryImpl<Professor> implements ProfessorReposiroty{

}
