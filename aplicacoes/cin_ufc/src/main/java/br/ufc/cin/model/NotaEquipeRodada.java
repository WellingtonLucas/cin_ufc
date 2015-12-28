package br.ufc.cin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity(name="nota_equipe_rodada")
public class NotaEquipeRodada {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "RODADA_ID")
	private Rodada rodada;
	
	@OneToOne
	@JoinColumn(name = "EQUIPE_ID")
	private Equipe equipe;
	
	@Column
	private Float fatorDeAposta;
	
	@Column
	private Float retorno;
	
	@Column
	private Float valor;
	
	public Float getRetorno() {
		return retorno;
	}

	public void setRetorno(Float retorno) {
		this.retorno = retorno;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Rodada getRodada() {
		return rodada;
	}

	public void setRodada(Rodada rodada) {
		this.rodada = rodada;
	}

	public Equipe getEquipe() {
		return equipe;
	}

	public void setEquipe(Equipe equipe) {
		this.equipe = equipe;
	}

	public Float getFatorDeAposta() {
		return fatorDeAposta;
	}

	public void setFatorDeAposta(Float fatorDeAposta) {
		this.fatorDeAposta = fatorDeAposta;
	}

	public Float getValor() {
		return valor;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}
	
	@Override
	public String toString() {
		return "[ID: " +getId()+ ", id_Equipe: "+getEquipe().getId()+", id_Rodada: "+getRodada().getId()+"]" ;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof NotaEquipeRodada)) {
			return false;
		}
		NotaEquipeRodada noraEquipeRodada = (NotaEquipeRodada) obj;

		return toString().equals(noraEquipeRodada.toString());
	}
}
