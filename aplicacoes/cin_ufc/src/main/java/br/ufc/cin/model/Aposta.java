package br.ufc.cin.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity(name ="aposta")
public class Aposta {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	private Usuario apostador;
	
	@OneToMany(cascade = {CascadeType.ALL})
	private List<Deposito> depositos;

	@OneToOne
	@JoinColumn(name = "RODADA_ID")
	private Rodada rodada;

	@Column(name = "saldo")
	private Float saldo;
	
	@Column
	private Float retorno;
	
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

	public Float getRetorno() {
		return retorno;
	}

	public void setRetorno(Float retorno) {
		this.retorno = retorno;
	}

	public List<Deposito> getDepositos() {
		return depositos;
	}

	public void setDepositos(List<Deposito> depositos) {
		this.depositos = depositos;
	}

	public void addDeposito(Deposito deposito){
		if(!getDepositos().contains(deposito)){
			getDepositos().add(deposito);
		}
	}
	
	/**
	 * Saldo disponível da equipe para a rodada
	 * */
	public Float getSaldo() {
		return saldo;
	}

	public void setSaldo(Float saldo) {
		this.saldo = saldo;
	}

	public Usuario getApostador() {
		return apostador;
	}

	public void setApostador(Usuario apostador) {
		this.apostador = apostador;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Aposta)) {
			return false;
		}
		Aposta valor = (Aposta) obj;
		return toString().equals(valor.toString());
	}

	@Override
	public String toString() {
		return "Valor id: " + getId() + " [[Rodada: " + rodada.getId()
				+ ", nome: " + rodada.getNome() + "], [Apostador: "
				+ apostador.getId() + ", nome: " + apostador.getNome() + "]]";
	}

}
