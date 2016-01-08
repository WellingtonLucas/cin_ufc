package br.ufc.cin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Nota {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne
	@JoinColumn(name = "RODADA_ID")
	private Rodada rodada;
	
	@ManyToOne
	@JoinColumn(name = "HISTORICO_ID")
	private Historico historico;;
	
	@Column
	private Float valor;
	
	@Column
	private boolean satus;
	
	public boolean isSatus() {
		return satus;
	}

	public void setSatus(boolean satus) {
		this.satus = satus;
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

	public Historico getHistorico() {
		return historico;
	}

	public void setHistorico(Historico historico) {
		this.historico = historico;
	}

	public Float getValor() {
		return valor;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}
	
	@Override
	public String toString() {
		return "Nota id: "+getId()+"; valor: "+getValor()+";";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Nota)){
			return false;
		}
		Nota nota = (Nota) obj;
		
		return toString().equals(nota.toString());
	}
}
