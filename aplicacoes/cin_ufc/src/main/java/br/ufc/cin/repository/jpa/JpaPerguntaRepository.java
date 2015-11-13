package br.ufc.cin.repository.jpa;

import javax.inject.Named;

import br.ufc.cin.model.Pergunta;
import br.ufc.cin.repository.PerguntaRepository;
import br.ufc.quixada.npi.repository.jpa.JpaGenericRepositoryImpl;

@Named
public class JpaPerguntaRepository extends JpaGenericRepositoryImpl<Pergunta>  implements PerguntaRepository{

}
