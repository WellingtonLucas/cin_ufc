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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "usuario")
public class Usuario {

	public Usuario() {
		jogoParticipa = new ArrayList<Jogo>();
		equipes = new ArrayList<Equipe>();
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

	@Column(nullable=false)
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "professor")
	private List<Jogo> jogos;

	@ManyToMany(mappedBy = "alunos")
	private List<Jogo> jogoParticipa;

	@ManyToMany(mappedBy = "alunos")
	private List<Equipe> equipes;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "professor")
	private List<Formulario> formulario;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
	private List<Entrega> entregas;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
	private List<Historico> historicos;
	
	@OneToOne
	@JoinColumn(name = "FOTO_ID")
	private Documento foto;

	@OneToOne
	@JoinColumn(name = "HISTORICO_ID")
	private Historico historico;
	
	public Historico getHistorico() {
		return historico;
	}

	public void setHistorico(Historico historico) {
		this.historico = historico;
	}

	public List<Entrega> getEntregas() {
		return entregas;
	}

	public void setEntregas(List<Entrega> entregas) {
		this.entregas = entregas;
	}

	public Documento getFoto() {
		return foto;
	}

	public void setFoto(Documento foto) {
		this.foto = foto;
	}

	public List<Formulario> getFormulario() {
		return formulario;
	}

	public void setFormulario(List<Formulario> formulario) {
		this.formulario = formulario;
	}

	public List<Jogo> getJogoParticipa() {
		return jogoParticipa;
	}

	public void addJogoParticipa(Jogo jogo) {
		if (!getJogoParticipa().contains(jogo)) {
			getJogoParticipa().add(jogo);
		}
		if (!jogo.getAlunos().contains(this)) {
			jogo.getAlunos().add(this);
		}
	}

	public void addEquipe(Equipe equipe) {
		if (!getEquipes().contains(equipe)) {
			getEquipes().add(equipe);
		}
		if (!equipe.getAlunos().contains(this)) {
			equipe.getAlunos().add(this);
		}
	}

	public void addHistorico(Historico historico) {
		if (!getHistoricos().contains(historico)) {
			getHistoricos().add(historico);
			if (historico.getUsuario() != null) {
                historico.getUsuario().getHistoricos().remove(historico);
            }
            historico.setUsuario(this);
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

	public List<Equipe> getEquipes() {
		return equipes;
	}

	public void setEquipes(List<Equipe> equipes) {
		this.equipes = equipes;
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

	public List<Historico> getHistoricos() {
		return historicos;
	}

	public void setHistoricos(List<Historico> historicos) {
		this.historicos = historicos;
	}

	public String toString() {
		return "Usuario id: " + getId() + ", nome: " + getNome()+" email:"+ getEmail()+";";
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Usuario)){
			return false;
		}
		Usuario user = (Usuario) obj;
		
		return toString().equals(user.toString());
	}

}
