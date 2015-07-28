package br.ufc.cin.model;

import java.util.ArrayList;
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
	
	@ManyToMany (cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<Opcao> opcoes;
	
	@ManyToOne
	private Formulario formulario;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Opcao> getOpcao() {
		return opcoes;
	}

	public void setOpcao(List<Opcao> opcao) {
		this.opcoes = opcao;
	}
	
	public void adcionarOpcaoId(Integer opcaoId){
		if(this.opcoes == null)  
			this.opcoes = new ArrayList<Opcao>();
		Opcao opcao = new Opcao();
		opcao.setId(opcaoId);
		this.opcoes.add(opcao);
	}

	public Formulario getEnquete() {
		return formulario;
	}

	public void setEnquete(Formulario formulario) {
		this.formulario = formulario;
	}
	
	public void setEnqueteId(Integer enqueteId){
		if(this.formulario == null) 
			this.formulario = new Formulario();
		
		this.formulario.setId(enqueteId);
	}
}
