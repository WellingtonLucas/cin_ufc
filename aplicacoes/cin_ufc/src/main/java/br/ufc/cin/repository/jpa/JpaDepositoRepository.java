package br.ufc.cin.repository.jpa;

import javax.inject.Named;

import br.ufc.cin.model.Deposito;
import br.ufc.cin.repository.DepositoRepository;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaDepositoRepository extends JpaGenericRepositoryImpl<Deposito> implements DepositoRepository{

}
