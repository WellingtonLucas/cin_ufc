package br.ufc.cin.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Deposito {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne
	private Equipe equipe;
	
	@NotNull(message ="A quantia é obrigatória.")
	@Column
	private Float quantia;
	
	@Column
	private Float retorno;

	@DateTimeFormat(pattern = "dd/MM/yyyy'T'HH:mm:ss")
	private Date dia;
	
	public Date getDia() {
		return dia;
	}

	public void setDia(Date dia) {
		this.dia = dia;
	}

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

	public Float getRetorno() {
		return retorno;
	}

	public void setRetorno(Float retorno) {
		this.retorno = retorno;
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
