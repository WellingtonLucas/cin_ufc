package br.ufc.cin.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Consultoria {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "RODADA_ID")
	private Rodada rodada;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@NotNull(message = "A descrição é obrigatória.")
	@Column
	private String descricao;
	
	@NotNull(message = "O valor é obrigatório.")
	@Column
	private Float valor;
	
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Float getValor() {
		return valor;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "Id: "+getId()+" id rodada: "+rodada.getId();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Consultoria)){
			return false;
		}
		Consultoria consultoria = (Consultoria) obj;
		
		return toString().equals(consultoria.toString());
	}
}
