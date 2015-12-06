package br.ufc.cin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Deposito {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne
	private Equipe equipe;
	
	@Column
	private Float quantia;

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

	public Float getQuantia() {
		return quantia;
	}

	public void setQuantia(Float quantia) {
		this.quantia = quantia;
	}
	
	@Override
	public String toString() {
		return "ID: "+getId()+" quantia: " +getQuantia();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Deposito)){
			return false;
		}
		Deposito deposito = (Deposito) obj;
		return toString().equals(deposito.toString());
	}
}
