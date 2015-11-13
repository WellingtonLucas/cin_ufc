package br.ufc.cin.repository.jpa;

import javax.inject.Named;

import br.ufc.cin.model.Opcao;
import br.ufc.cin.repository.OpcaoRepository;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaOpcaoRepository extends JpaGenericRepositoryImpl<Opcao> implements OpcaoRepository{

}
