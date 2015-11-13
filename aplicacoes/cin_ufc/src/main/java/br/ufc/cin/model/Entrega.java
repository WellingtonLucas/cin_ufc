package br.ufc.cin.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Entrega {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_rodada")
	private Rodada rodada;

	@DateTimeFormat(pattern = "dd/MM/yyyy'T'HH:mm:ss")
	private Date dia;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	@OneToOne
	@JoinColumn(name = "DOC_ID")
	private Documento documento;

	@OneToOne
	@JoinColumn(name = "EQUIPE_ID")
	private Equipe equipe;

	@OneToOne
	private Resposta gabarito;

	@OneToMany(mappedBy ="entrega")
	private List<Resposta> respostas;
	
	@Transient
	private boolean respondida;
	
	public boolean isRespondida() {
		return respondida;
	}

	public void setRespondida(boolean respondida) {
		this.respondida = respondida;
	}
	
	public Resposta getGabarito() {
		return gabarito;
	}

	public void setGabarito(Resposta gabarito) {
		this.gabarito = gabarito;
	}
	
	public List<Resposta> getRespostas() {
		return respostas;
	}

	public void setRespostas(List<Resposta> respostas) {
		this.respostas = respostas;
	}
	
	public void addResposta(Resposta resposta){
		if(!getRespostas().contains(resposta)){
			getRespostas().add(resposta);
		}
	}
	
	public Equipe getEquipe() {
		return equipe;
	}
	
	public void setEquipe(Equipe equipe) {
		this.equipe = equipe;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
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

	public Date getDia() {
		return dia;
	}

	public void setDia(Date dia) {
		this.dia = dia;
	}
	
	@Override
	public String toString() {
		return "Entrega id: "+getId()+" dia: "+getDia().getTime()+";";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Entrega)){
			return false;
		}
		Entrega entrega = (Entrega) obj;
		
		return toString().equals(entrega.toString());
	}	
}
