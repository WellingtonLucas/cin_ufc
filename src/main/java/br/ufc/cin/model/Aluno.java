package br.ufc.cin.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Aluno extends Usuario{
	
	@ManyToOne
	@JoinColumn(nullable = true, name = "id_equipe")
	private Equipe equipe;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_jogo", nullable = false, unique = true)
	private Jogo jogo;		
	
	public Equipe getEquipe() {
		return equipe;
	}

	public void setEquipe(Equipe equipe) {
		this.equipe = equipe;
	}
	
}
