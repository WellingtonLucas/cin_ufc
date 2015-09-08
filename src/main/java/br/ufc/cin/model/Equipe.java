package br.ufc.cin.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
	
	@OneToMany(mappedBy = "equipe", cascade={CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE})
	private List<Usuario> alunos;
	
	@ManyToOne
	private Jogo jogo;

	public void addAluno(Usuario aluno){
		if(!getAlunos().contains(aluno)){
			getAlunos().add(aluno);
			if(aluno.getEquipe() != null){
				aluno.getEquipe().getAlunos().remove(aluno);
			}
			aluno.setEquipe(this);
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

	public boolean isAtivo() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public List<Usuario> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<Usuario> usuarios) {
		this.alunos.addAll(usuarios);
	}

	public Integer getId() {
		return id;
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
