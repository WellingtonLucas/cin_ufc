package br.ufc.cin.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Opcao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull(message = "O enunciado é obrigatório.")
	private String descricao;

	@ManyToOne
	private Pergunta pergunta;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Pergunta getPergunta() {
		return pergunta;
	}

	public void setPergunta(Pergunta pergunta) {
		this.pergunta = pergunta;
	}
	
	public String toString() {
		return "Opcao id: " + getId() + ", descricao: " + getDescricao()+";";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Opcao)){
			return false;
		}
		Opcao opcao = (Opcao) obj;
		
		return toString().equals(opcao.toString());
	}
}
