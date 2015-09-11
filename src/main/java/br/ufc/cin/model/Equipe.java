package br.ufc.cin.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Equipe {
	public Equipe() {
		alunos = new ArrayList<Usuario>();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column
	private String nome;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column
	private String ideiaDeNegocio;
	
	@Column
	private boolean status;
	
	@ManyToOne
	private Jogo jogo;
	
	@ManyToMany(mappedBy = "equipesAtivas")
	private List<Rodada> rodadas;
	
	@ManyToMany
	@JoinTable(name = "equipe_aluno", joinColumns = { @JoinColumn(name = "equipe_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "aluno_id", referencedColumnName = "id") })
	private List<Usuario> alunos;
	
	public void addAluno(Usuario usuario) {
		if (!getAlunos().contains(usuario)) {
			getAlunos().add(usuario);
		}
		if (!usuario.getEquipes().contains(this)) {
			usuario.getEquipes().add(this);
		}
	}

	public String getIdeiaDeNegocio() {
		return ideiaDeNegocio;
	}

	public void setIdeiaDeNegocio(String ideiaDeNegocio) {
		this.ideiaDeNegocio = ideiaDeNegocio;
	}
	
	public Jogo getJogo() {
		return jogo;
	}

	public void setJogo(Jogo jogo) {
		this.jogo = jogo;
	}

	public boolean isStatus() {
		return status;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public List<Usuario> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<Usuario> alunos) {
		this.alunos = alunos;
	}

	public Integer getId() {
		return id;
	}
	
	public List<Rodada> getRodadas() {
		return rodadas;
	}

	public void setRodadas(List<Rodada> rodadas) {
		this.rodadas = rodadas;
	}

	public String toString(){
		return "Equipe id: "+getId()+ " nome: " +getNome() ;
	}
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Equipe)){
			return false;
		}
		Equipe equipe = (Equipe) obj;
		
		return toString().equals(equipe.toString());
	}
}
