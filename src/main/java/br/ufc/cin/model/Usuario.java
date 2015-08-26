package br.ufc.cin.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Table(name="usuario")
public class Usuario {
	
	public Usuario() {
		jogoParticipa = new ArrayList<Jogo>();
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	@NotEmpty
	private String nome;
	
	@Column
	private String sobreNome;	
	
	@Column 
	private String curso;
	
	@Column(nullable = false)
	@NotEmpty
	private String senha;

	@Column(nullable = false, unique = true)
	@NotEmpty
	private String email;

	@Column
	private String matricula;
	
	@Column
	private String papel;
	
	@Column
	private boolean habilitado;	
	
	@ManyToOne
	@JoinColumn(name = "id_equipe")
	private Equipe equipe;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "professor")
	private List<Jogo> jogos; 
	
	@ManyToMany(mappedBy="alunos")
	private List<Jogo> jogoParticipa;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "professor")
	private List<Formulario> formulario;
	
	public List<Formulario> getFormulario() {
		return formulario;
	}

	public void setFormulario(List<Formulario> formulario) {
		this.formulario = formulario;
	}

	public List<Jogo> getJogoParticipa() {
		return jogoParticipa;
	}

	public void addJogoParticipa(Jogo jogo){
		if(!getJogoParticipa().contains(jogo)){
			getJogoParticipa().add(jogo);
		}
		if(!jogo.getAlunos().contains(this)){
			jogo.getAlunos().add(this);
		}
	}
	public void setJogoParticipa(List<Jogo> jogoParticipa) {
		this.jogoParticipa = jogoParticipa;
	}
	
	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}
	
	public List<Jogo> getJogos() {
		return jogos;
	}

	public void setJogos(List<Jogo> jogos) {
		this.jogos = jogos;
	}	
	public Equipe getEquipe() {
		return equipe;
	}

	public void setEquipe(Equipe equipe) {
		this.equipe = equipe;
	}
	
	public boolean isHabilitado() {
		return habilitado;
	}

	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}

	public String getPapel() {
		return papel;
	}

	public void setPapel(String papel) {
		this.papel = papel;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getSobreNome() {
		return sobreNome;
	}

	public void setSobreNome(String sobreNome) {
		this.sobreNome = sobreNome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
	
	public boolean isNew() {
		return (this.id == null);
	}
	
	 public String toString() {
        return "Usuario id: " + getId() + 
               ", nome: " + getNome();
	 }
}
