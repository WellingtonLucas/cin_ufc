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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
public class Equipe {
	public Equipe() {
		alunos = new ArrayList<Usuario>();
		statusRodadaEquipes = new ArrayList<StatusRodadaEquipe>();
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
	
	@ManyToOne
	private Jogo jogo;
	
	@ManyToMany
	@JoinTable(name = "equipe_aluno", joinColumns = { @JoinColumn(name = "equipe_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "aluno_id", referencedColumnName = "id") })
	private List<Usuario> alunos;
	
	@OneToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "LOGO_ID")
	private Documento logo;
	
	@OneToMany(mappedBy = "equipe", cascade={CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE})
	private List<StatusRodadaEquipe> statusRodadaEquipes;
	
	@OneToMany(mappedBy = "equipe", cascade={CascadeType.REMOVE})
	private List<ReaberturaSubmissao> reaberturaSubmissao;

	@OneToMany(mappedBy = "equipe", cascade={CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE})
	private List<SaldoNaRodada> saldosNasRodadas;
	
	private Float saldo;
	
	@Transient
	private boolean statusNaRodada;
	
	@Transient
	private boolean statusReabertura;
	
	public boolean isStatusReabertura() {
		return statusReabertura;
	}

	public void setStatusReabertura(boolean statusReabertura) {
		this.statusReabertura = statusReabertura;
	}

	public boolean isStatusNaRodada() {
		return statusNaRodada;
	}

	public void setStatusNaRodada(boolean statusNaRodada) {
		this.statusNaRodada = statusNaRodada;
	}
	
	public List<ReaberturaSubmissao> getReaberturaSubmissao() {
		return reaberturaSubmissao;
	}

	public void setReaberturaSubmissao(List<ReaberturaSubmissao> reaberturaSubmissao) {
		this.reaberturaSubmissao = reaberturaSubmissao;
	}
	
	public void addAluno(Usuario usuario) {
		if (!getAlunos().contains(usuario)) {
			getAlunos().add(usuario);
		}
		if (!usuario.getEquipes().contains(this)) {
			usuario.getEquipes().add(this);
		}
	}
	
	public List<SaldoNaRodada> getSaldosNasRodadas() {
		return saldosNasRodadas;
	}

	public void setSaldosNasRodadas(List<SaldoNaRodada> saldosNasRodadas) {
		this.saldosNasRodadas = saldosNasRodadas;
	}

	public void addSaldo(SaldoNaRodada saldo) {
		if (!getSaldosNasRodadas().contains(saldo)) {
			getSaldosNasRodadas().add(saldo);
		}
	}
	
	public void addStatusRodadaEquipe(StatusRodadaEquipe statusRodadaEquipe){
		if(!getStatusRodadaEquipes().contains(statusRodadaEquipe)){
			getStatusRodadaEquipes().add(statusRodadaEquipe);
		}
	}
	
	public void addReaberturaSubmissao(ReaberturaSubmissao reaberturaSubmissao){
		if(!getReaberturaSubmissao().contains(reaberturaSubmissao))
			getReaberturaSubmissao().add(reaberturaSubmissao);
	}
	
	public String getIdeiaDeNegocio() {
		return ideiaDeNegocio;
	}

	public void setIdeiaDeNegocio(String ideiaDeNegocio) {
		this.ideiaDeNegocio = ideiaDeNegocio;
	}
	
	public Documento getLogo() {
		return logo;
	}

	public void setLogo(Documento logo) {
		this.logo = logo;
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

	public void setStatus(boolean status) {
		this.status = status;
	}

	public List<Usuario> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<Usuario> alunos) {
		this.alunos = alunos;
	}

	public Integer getId() {
		return id;
	}
	
	public List<StatusRodadaEquipe> getStatusRodadaEquipes() {
		return statusRodadaEquipes;
	}

	public void setStatusRodadaEquipes(List<StatusRodadaEquipe> statusRodadaEquipes) {
		this.statusRodadaEquipes = statusRodadaEquipes;
	}

	public Float getSaldo() {
		return saldo;
	}

	public void setSaldo(Float saldo) {
		this.saldo = saldo;
	}

	public String toString(){
		return "Equipe id: "+getId()+ " nome: " +getNome()+";" ;
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
