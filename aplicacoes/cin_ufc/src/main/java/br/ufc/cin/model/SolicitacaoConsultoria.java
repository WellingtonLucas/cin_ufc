package br.ufc.cin.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.springframework.format.annotation.DateTimeFormat;

@Entity(name="solicitacao_consultoria")
public class SolicitacaoConsultoria {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column
	private boolean status;

	@ManyToOne
	private Equipe equipe;
	
	@OneToOne
	@JoinColumn(name = "CONSULTORIA_ID")
	private Consultoria consultoria;

	@DateTimeFormat(pattern = "dd/MM/yyyy'T'HH:mm:ss")
	private Date dia;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy'T'HH:mm:ss")
	private Date diaConfirmacao;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Equipe getEquipe() {
		return equipe;
	}

	public void setEquipe(Equipe equipe) {
		this.equipe = equipe;
	}

	public Consultoria getConsultoria() {
		return consultoria;
	}

	public void setConsultoria(Consultoria consultoria) {
		this.consultoria = consultoria;
	}
	
	public Date getDia() {
		return dia;
	}

	public void setDia(Date dia) {
		this.dia = dia;
	}

	public Date getDiaConfirmacao() {
		return diaConfirmacao;
	}

	public void setDiaConfirmacao(Date diaConfirmacao) {
		this.diaConfirmacao = diaConfirmacao;
	}

	@Override
	public String toString() {
		return "[Id equie: "+ equipe.getId()+", id consultoria: ]"+consultoria.getId();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof SolicitacaoConsultoria)){
			return false;
		}
		SolicitacaoConsultoria statusConsultoria = (SolicitacaoConsultoria) obj;
		return toString().equals(statusConsultoria.toString());
	}
}
