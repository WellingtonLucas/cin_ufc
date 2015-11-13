package br.ufc.cin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity(name="rodada_equipe")
public class StatusRodadaEquipe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	private Equipe equipe;
	
	@OneToOne
	@JoinColumn(name = "RODADA_ID")
	private Rodada Rodada;
	
	@Column
	private boolean ativa;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Equipe getEquipe() {
		return equipe;
	}

	public void setEquipe(Equipe equipe) {
		this.equipe = equipe;
	}

	public Rodada getRodada() {
		return Rodada;
	}

	public void setRodada(Rodada rodada) {
		Rodada = rodada;
	}

	public boolean isAtiva() {
		return ativa;
	}

	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}
	
	@Override
	public String toString() {
		return "[Id: "+getId()+"; id_equipe: "+ getEquipe().getId()+ "; id_rodada: "+getRodada().getId()+";]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof StatusRodadaEquipe)){
			return false;
		}
		StatusRodadaEquipe statusRodadaEquipe = (StatusRodadaEquipe) obj;
		
		return toString().equals(statusRodadaEquipe.toString());
	}
	
}
