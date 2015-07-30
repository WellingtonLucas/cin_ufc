package br.ufc.cin.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Pergunta {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(columnDefinition="TEXT")
	private String descricao;
	
	@OneToMany(mappedBy="pergunta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Opcao> opcoes;
	
	@ManyToOne
	private Formulario formulario;
	
	@NotNull @Column(columnDefinition = "boolean default false")
	private boolean obrigatoria;

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

	public List<Opcao> getOpcoes() {
		return opcoes;
	}

	public void setOpcoes(List<Opcao> opcoes) {
		this.opcoes = opcoes;
	}
	public void addOpcao(Opcao opcao){
		if(!getOpcoes().contains(opcao)){
			getOpcoes().add(opcao);
			if(opcao.getPergunta()!=null){
				opcao.getPergunta().getOpcoes().remove(opcao);
			}
			opcao.setPergunta(this);
		}
	}
	public Formulario getFormulario() {
		return formulario;
	}

	public void setFormulario(Formulario enquete) {
		this.formulario = enquete;
	}

	public boolean isObrigatoria() {
		return obrigatoria;
	}

	public void setObrigatoria(boolean obrigatoria) {
		this.obrigatoria = obrigatoria;
	}
	
	public String toString() {
		return "Pergunta id: " + getId() + ", descricao: " + getDescricao();
	}
}
