package br.ufc.cin.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Resposta {
	
	public Resposta() {
		this.opcoes = new ArrayList<Opcao>();
	}

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne
	private Usuario usuario;
	
	@OneToOne
	private Entrega entregaGabarito;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy'T'HH:mm:ss")
	private Date dia;
	
	@ManyToMany (cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<Opcao> opcoes;
	
	@ManyToOne
	private Formulario formulario;
	
	@ManyToOne
	private Entrega entrega;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Entrega getEntregaGabarito() {
		return entregaGabarito;
	}

	public void setEntregaGabarito(Entrega entregaGabarito) {
		this.entregaGabarito = entregaGabarito;
	}

	public Entrega getEntrega() {
		return entrega;
	}

	public void setEntrega(Entrega entrega) {
		this.entrega = entrega;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Opcao> getOpcoes() {
		return opcoes;
	}

	public void setOpcoes(List<Opcao> opcoes) {
		this.opcoes = opcoes;
	}
	
	public Formulario getFormulario() {
		return formulario;
	}

	public void setFormulario(Formulario formulario) {
		this.formulario = formulario;
	}
	
	public Date getDia() {
		return dia;
	}

	public void setDia(Date dia) {
		this.dia = dia;
	}
	
	public String toString() {
		return "Resposta id: " + getId();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Resposta)){
			return false;
		}
		Resposta resposta = (Resposta) obj;
		
		return toString().equals(resposta.toString());
	}
	
}
