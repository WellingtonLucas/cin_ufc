package br.ufc.cin.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Professor{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Integer id_professor;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "professor")
	private List<Jogo> jogos; 
	
	@OneToOne(cascade = CascadeType.REFRESH)
	private Usuario usuario;

	public List<Jogo> getJogos() {
		return jogos;
	}

	public void setJogos(List<Jogo> jogos) {
		this.jogos = jogos;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
