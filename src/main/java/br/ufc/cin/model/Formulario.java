package br.ufc.cin.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Formulario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String titulo;

	@OneToMany(mappedBy = "formulario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Pergunta> perguntas;

	@ManyToOne
	@JoinColumn(name = "id_professor")
	private Usuario professor;

	@OneToMany(mappedBy = "formulario", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<Resposta> respostas;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date prazo;

	private String nota;

	public void addPergunta(Pergunta pergunta){
		if(!getPerguntas().contains(pergunta)){
			getPerguntas().add(pergunta);
			if(pergunta.getFormulario()!=null){
				pergunta.getFormulario().getPerguntas().remove(pergunta);
			}
			pergunta.setFormulario(this);
		}
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public Usuario getProfessor() {
		return professor;
	}

	public void setProfessor(Usuario professor) {
		this.professor = professor;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public List<Pergunta> getPerguntas() {
		return perguntas;
	}

	public void setPerguntas(List<Pergunta> perguntas) {
		this.perguntas = perguntas;
	}

	public List<Resposta> getRespostas() {
		return respostas;
	}

	public void setRespostas(List<Resposta> respostas) {
		this.respostas = respostas;
	}

	public Date getPrazo() {
		return prazo;
	}

	public void setPrazo(Date disponivelAte) {
		this.prazo = disponivelAte;
	}
	
	public String toString() {
		return "Formulario id: " + getId() + ", titulo: " + getTitulo();
	}
}
