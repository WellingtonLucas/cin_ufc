package br.ufc.cin.repository.jpa;

import javax.inject.Named;

import br.ufc.cin.model.Entrega;
import br.ufc.cin.repository.EntregaRepository;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaEntregaRepository extends JpaGenericRepositoryImpl<Entrega> implements EntregaRepository{

}
