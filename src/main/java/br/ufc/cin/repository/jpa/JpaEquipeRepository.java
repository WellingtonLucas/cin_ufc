package br.ufc.cin.repository.jpa;

import javax.inject.Named;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.repository.EquipeRepository;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaEquipeRepository extends JpaGenericRepositoryImpl<Equipe> implements EquipeRepository{

}
