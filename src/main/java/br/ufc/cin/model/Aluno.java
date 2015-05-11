package br.ufc.cin.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Aluno extends Usuario{
	
	@ManyToOne
	@JoinColumn(nullable = true, name = "id_equipe")
	private Equipe equipe;

	public Equipe getEquipe() {
		return equipe;
	}

	public void setEquipe(Equipe equipe) {
		this.equipe = equipe;
	}
}
