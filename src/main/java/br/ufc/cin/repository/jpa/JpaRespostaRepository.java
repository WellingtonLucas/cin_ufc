package br.ufc.cin.repository.jpa;

import javax.inject.Named;

import br.ufc.cin.model.Resposta;
import br.ufc.cin.repository.RespostaRepository;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaRespostaRepository extends JpaGenericRepositoryImpl<Resposta>  implements RespostaRepository{

}
