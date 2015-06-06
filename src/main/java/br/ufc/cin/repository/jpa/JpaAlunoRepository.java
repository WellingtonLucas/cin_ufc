package br.ufc.cin.repository.jpa;

import javax.inject.Named;

import br.ufc.cin.model.Aluno;
import br.ufc.cin.repository.AlunoRepository;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaAlunoRepository extends JpaGenericRepositoryImpl<Aluno> implements AlunoRepository{

}
