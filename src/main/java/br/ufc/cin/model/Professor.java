package br.ufc.cin.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class Professor extends Usuario{
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "professor")
	private List<Jogo> jogos; 
	
	public List<Jogo> getJogos() {
		return jogos;
	}

	public void setJogos(List<Jogo> jogos) {
		this.jogos = jogos;
	}
}
