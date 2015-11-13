package br.ufc.cin.repository.jpa;

import javax.inject.Named;

import br.ufc.cin.model.Rodada;
import br.ufc.cin.repository.RodadaRepository;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaRodadaRepository extends JpaGenericRepositoryImpl<Rodada> implements RodadaRepository{

}
