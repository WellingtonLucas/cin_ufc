package br.ufc.cin.model;

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
import javax.persistence.OneToOne;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Rodada {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column
	@NotEmpty
	private String nome;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date inicio;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date termino;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date prazoSubmissao;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column
	private String descricao;
	
	@ManyToOne
	private Jogo jogo;
	
	@Column
	private boolean status;

	@Column
	private boolean statusPrazo;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Documento modelo;
	
	@OneToOne
	@JoinColumn(name = "FORM_ID")
	private Formulario formulario;

	@ManyToMany
	@JoinTable(name = "equipe_rodada", joinColumns = { @JoinColumn(name = "rodada_id", referencedColumnName = "id") }, 
	inverseJoinColumns = { @JoinColumn(name = "equipe_id", referencedColumnName = "id") })
	private List<Equipe> equipesAtivas;
	
	@OneToMany(mappedBy = "rodada", cascade={CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE})
	private List<Entrega> entregas;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Documento getModelo() {
		return modelo;
	}

	public void setModelo(Documento modelo) {
		this.modelo = modelo;
	}

	public Formulario getFormulario() {
		return formulario;
	}

	public void setFormulario(Formulario formulario) {
		this.formulario = formulario;
	}

	public Jogo getJogo() {
		return jogo;
	}

	public void setJogo(Jogo jogo) {
		this.jogo = jogo;
	}
	
	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getTermino() {
		return termino;
	}

	public void setTermino(Date termino) {
		this.termino = termino;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Entrega> getEntregas() {
		return entregas;
	}

	public void setEntregas(List<Entrega> entregas) {
		this.entregas = entregas;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public List<Equipe> getEquipesAtivas() {
		return equipesAtivas;
	}

	public void setEquipesAtivas(List<Equipe> equipesAtivas) {
		this.equipesAtivas = equipesAtivas;
	}

	public Date getPrazoSubmissao() {
		return prazoSubmissao;
	}

	public void setPrazoSubmissao(Date prazoSubmissao) {
		this.prazoSubmissao = prazoSubmissao;
	}

	
	public boolean isStatusPrazo() {
		return statusPrazo;
	}

	public void setStatusPrazo(boolean statusPrazo) {
		this.statusPrazo = statusPrazo;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Rodada)){
			return false;
		}
		Rodada rodada = (Rodada) obj;
		
		return toString().equals(rodada.toString());
	}	

	@Override
	public String toString() {
		return "Nome: "+getNome()+" Id: "+getId();
	}	
}
