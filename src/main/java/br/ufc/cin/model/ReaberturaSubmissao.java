package br.ufc.cin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity(name="reabertura_submissao")
public class ReaberturaSubmissao {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "quantidada_dia")
	private String quantidadeDia;
	
	@ManyToOne
	private Equipe equipe;
	
	@OneToOne
	@JoinColumn(name = "RODADA_ID")
	private Rodada rodada;

	@Column
	private boolean status;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getQuantidadeDia() {
		return quantidadeDia;
	}

	public void setQuantidadeDia(String quantidadeDia) {
		this.quantidadeDia = quantidadeDia;
	}

	public Equipe getEquipe() {
		return equipe;
	}

	public void setEquipe(Equipe equipe) {
		this.equipe = equipe;
	}

	public Rodada getRodada() {
		return rodada;
	}

	public void setRodada(Rodada rodada) {
		this.rodada = rodada;
	}
	
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Id: "+getId()+" rodada: "+ getRodada().getNome()+" qtdDias "+ getQuantidadeDia();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ReaberturaSubmissao)){
			return false;
		}
		ReaberturaSubmissao solicitacao = (ReaberturaSubmissao) obj;
		
		return toString().equals(solicitacao.toString());
	}
	
}
