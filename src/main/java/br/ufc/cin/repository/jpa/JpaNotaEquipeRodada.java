package br.ufc.cin.repository.jpa;

import javax.inject.Named;

import br.ufc.cin.model.NotaEquipeRodada;
import br.ufc.cin.repository.NotaEquipeRodadaRepository;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaNotaEquipeRodada extends JpaGenericRepositoryImpl<NotaEquipeRodada> implements NotaEquipeRodadaRepository{

}
