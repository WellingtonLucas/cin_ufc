package br.ufc.cin.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Aluno {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Integer id_aluno;
	
	@ManyToOne
	@JoinColumn(nullable = true, name = "id_equipe")
	private Equipe equipe;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_jogo", nullable = false, unique = true)
	private Jogo jogo;
	
	@OneToOne(cascade = CascadeType.REFRESH)
	private Usuario usuario;
	
	public Equipe getEquipe() {
		return equipe;
	}

	public void setEquipe(Equipe equipe) {
		this.equipe = equipe;
	}
	
}
