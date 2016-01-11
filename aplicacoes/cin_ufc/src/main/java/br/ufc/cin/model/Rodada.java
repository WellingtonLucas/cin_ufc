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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Rodada {
	
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column
	@NotEmpty(message = "O nome é obrigatório.")
	private String nome;
	
	@NotNull(message = "A data de início é obrigatória.")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date inicio;
	
	@NotNull(message = "A data de término é obrigatória.")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date termino;
	
	@NotNull(message = "O prazo de submissões é obrigatório.")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date prazoSubmissao;
	
	@NotNull(message = "O prazo para avaliações é obrigatório.")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date terminoAvaliacao;
	
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
	
	@Column
	private boolean statusAvaliacao;
	
	@Column
	private boolean statusRaking;
	
	@Column
	private boolean statusNota;
	
	@OneToOne(cascade= CascadeType.ALL ,fetch = FetchType.LAZY)
	private Documento modelo;
	
	@OneToOne
	@JoinColumn(name = "FORM_ID")
	private Formulario formulario;
	
	@OneToMany(mappedBy = "rodada", cascade={CascadeType.ALL})
	private List<Entrega> entregas;
	
	@Column(name = "valor_liberado")
	private Float valorLiberado;
	
	@Column(name="all_in")
	private boolean allIn;
	
	@Column(name = "valor_reabertura")
	@NotNull(message = "O valor de reabertura de uma rodada é obrigatório.")
	private Float valorReabertura;
	
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

	public Date getTerminoAvaliacao() {
		return terminoAvaliacao;
	}

	public void setTerminoAvaliacao(Date terminoAvaliacao) {
		this.terminoAvaliacao = terminoAvaliacao;
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

	public Date getPrazoSubmissao() {
		return prazoSubmissao;
	}

	public void setPrazoSubmissao(Date prazoSubmissao) {
		this.prazoSubmissao = prazoSubmissao;
	}
	
	public boolean isStatusRaking() {
		return statusRaking;
	}

	public void setStatusRaking(boolean statusRaking) {
		this.statusRaking = statusRaking;
	}

	public boolean isStatusPrazo() {
		return statusPrazo;
	}

	public void setStatusPrazo(boolean statusPrazo) {
		this.statusPrazo = statusPrazo;
	}

	public boolean isStatusAvaliacao() {
		return statusAvaliacao;
	}

	public void setStatusAvaliacao(boolean statusAvaliacao) {
		this.statusAvaliacao = statusAvaliacao;
	}

	public Float getValorLiberado() {
		return valorLiberado;
	}

	public void setValorLiberado(Float valorLiberado) {
		this.valorLiberado = valorLiberado;
	}

	public boolean isStatusNota() {
		return statusNota;
	}

	public void setStatusNota(boolean statusNota) {
		this.statusNota = statusNota;
	}

	public boolean isAllIn() {
		return allIn;
	}

	public void setAllIn(boolean allIn) {
		this.allIn = allIn;
	}
	
	public Float getValorReabertura() {
		return valorReabertura;
	}

	public void setValorReabertura(Float valorReabertura) {
		this.valorReabertura = valorReabertura;
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
		return "Nome: "+getNome()+" Id: "+getId()+";";
	}	
}
