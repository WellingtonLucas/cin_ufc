package br.ufc.cin.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "jogo")
public class Jogo implements Serializable {

	private static final long serialVersionUID = 1L;

	public Jogo() {
		alunos = new ArrayList<Usuario>();
		equipes = new ArrayList<Equipe>();
		rodadas = new ArrayList<Rodada>();
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column
	private String descricao;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column
	private String regras;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date inicio;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date termino;

	@NotEmpty
	@Column
	private String semestre;

	@NotEmpty
	@Column(name = "nome_curso")
	private String nomeDoCurso;

	@Column
	private boolean status;

	@ManyToOne
	/* (fetch = FetchType.LAZY) */
	@JoinColumn(name = "id_professor")
	private Usuario professor;

	@ManyToMany
	@JoinTable(name = "jogo_aluno", joinColumns = { @JoinColumn(name = "jogo_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "aluno_id", referencedColumnName = "id") })
	private List<Usuario> alunos;

	@OneToMany(mappedBy = "jogo", cascade = { CascadeType.REMOVE,
			CascadeType.PERSIST })
	private List<Documento> documentos;

	@OneToMany(mappedBy = "jogo", cascade = {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE})
	private List<Equipe> equipes;

	@OneToMany(mappedBy = "jogo", cascade = {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE})
	private List<Rodada> rodadas;

	public List<Rodada> getRodadas() {
		return rodadas;
	}

	public void setRodadas(List<Rodada> rodadas) {
		this.rodadas = rodadas;
	}

	public void addEquipe(Equipe equipe){
		if(!getEquipes().contains(equipe)){
			getEquipes().add(equipe);
			if(equipe.getJogo()!=null){
				equipe.getJogo().getEquipes().remove(equipe);
			}
			equipe.setJogo(this);
		}
	}
	
	public void addRodada(Rodada rodada){
		if(!getRodadas().contains(rodada)){
			getRodadas().add(rodada);
			if(rodada.getJogo()!=null){
				rodada.getJogo().getRodadas().remove(rodada);
			}
			rodada.setJogo(this);
		}
	}
	
	public List<Equipe> getEquipes() {
		return equipes;
	}

	public void setEquipes(List<Equipe> equpes) {
		this.equipes = equpes;
	}

	public List<Usuario> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<Usuario> alunos) {
		this.alunos = alunos;
	}

	public void addAluno(Usuario usuario) {
		if (!getAlunos().contains(usuario)) {
			getAlunos().add(usuario);
		}
		if (!usuario.getJogoParticipa().contains(this)) {
			usuario.getJogoParticipa().add(this);
		}
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getRegras() {
		return regras;
	}

	public void setRegras(String regras) {
		this.regras = regras;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getTermino() {
		return termino;
	}

	public void setTermino(Date termino) {
		this.termino = termino;
	}

	public String getSemestre() {
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public String getNomeDoCurso() {
		return nomeDoCurso;
	}

	public void setNomeDoCurso(String nomeDoCurso) {
		this.nomeDoCurso = nomeDoCurso;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Usuario getProfessor() {
		return professor;
	}

	public void setProfessor(Usuario usuario) {
		this.professor = usuario;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public List<Documento> getDocumentos() {
		return documentos;
	}

	public void setDocumentos(List<Documento> documentos) {
		this.documentos = documentos;
	}

	public String toString() {
		return "Jogo id: " + getId() + ", nome: " + getNomeDoCurso();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Jogo)){
			return false;
		}
		Jogo jogo = (Jogo) obj;
		
		return toString().equals(jogo.toString());
	}
}
