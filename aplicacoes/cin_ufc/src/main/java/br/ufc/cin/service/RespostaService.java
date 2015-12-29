package br.ufc.cin.service;

import java.util.List;

import br.ufc.cin.model.Entrega;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Formulario;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Resposta;
import br.ufc.cin.model.Usuario;
import br.ufc.quixada.npi.service.GenericService;

public interface RespostaService extends GenericService<Resposta>{
	public abstract Resposta getRespostaByEntrega(Entrega entrega);

	public abstract List<Resposta> find(
			Usuario usuarioRequisitado);
	
	public abstract List<Resposta> find(
			Usuario usuarioRequisitado, Jogo jogo);
	
	public abstract List<Resposta> find(
			Usuario usuario, Entrega entrega);
	
	public abstract Resposta findUltimaRespostaPorEntrega(
			Usuario usuario, Entrega entrega);

	public abstract void salvar(Resposta resposta,Jogo jogo, Formulario formulario, Usuario usuario, Entrega entrega);

	public abstract Resposta findUltimaRespostaPorEquipe(Equipe equipe, Entrega entrega);

}
