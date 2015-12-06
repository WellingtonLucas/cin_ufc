package br.ufc.cin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity(name = "saldo_na_rodada")
public class SaldoNaRodada {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="EQUIPE_ID")
	private Equipe equipe;
	
	@OneToOne
	@JoinColumn(name = "RODADA_ID")
	private Rodada Rodada;
	
	@Column
	private Float saldo;

	@Column(name="saldo_com_fator")
	private Float saldoComFator;

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

	public Float getSaldoComFator() {
		return saldoComFator;
	}

	public void setSaldoComFator(Float saldoComFator) {
		this.saldoComFator = saldoComFator;
	}

	public Rodada getRodada() {
		return Rodada;
	}

	public void setRodada(Rodada rodada) {
		Rodada = rodada;
	}

	public Float getSaldo() {
		return saldo;
	}

	public void setSaldo(Float saldo) {
		this.saldo = saldo;
	}
	
	@Override
	public String toString(){
		return "Saldo id: "+getId()+ " valor: " +getSaldo()+";" ;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof SaldoNaRodada)){
			return false;
		}
		SaldoNaRodada saldoNaRodada = (SaldoNaRodada) obj;
		
		return toString().equals(saldoNaRodada.toString());
	}
}
