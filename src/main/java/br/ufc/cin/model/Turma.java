package br.ufc.cin.model;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Table(name = "turma", uniqueConstraints = {@UniqueConstraint(columnNames = "curso")})
public class Turma implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_turma")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty
	@Column
	private String semestre;	
	
	@NotEmpty
	@Column(name = "sigla_curso")
	@Pattern(regexp = "[a-zA-Z]{0,}", message = "O campo não pode possuir caracteres especiais ou números.")
	@Size(min = 2, max = 3, message = "A sigla deve conter entre 2 e 3 caracteres")
	private String siglaDoCurso;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_professor")
	private Professor professor;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "turma")
	private List<Aluno> alunos;
	
	public String getSemestre() {
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public String getCurso() {
		return siglaDoCurso;
	}
	
	/**
	 * Recebe uma sigla de um curso, que deve conter entre 2 a 3 caracteres.
	 * Não pode receber caracteres especiais ou numeros
	 * */
	public void setCurso(String sigla) {
		this.siglaDoCurso = sigla;
	}

	public Integer getId() {
		return id;
	}
	
	public String getSiglaDoCurso() {
		return siglaDoCurso;
	}

	public void setSiglaDoCurso(String siglaDoCurso) {
		this.siglaDoCurso = siglaDoCurso;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public List<Aluno> getAlunos() {
		return alunos;
	}

	/*
	 * Falta corrigir
	 * */
	public void setAlunos(List<Aluno> alunos) {
		this.alunos = alunos;
	}

}
