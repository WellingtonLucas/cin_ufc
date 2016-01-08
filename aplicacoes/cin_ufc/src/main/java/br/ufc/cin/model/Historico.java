package br.ufc.cin.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Historico {
	
	public Historico() {
		notas = new ArrayList<Nota>();
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "historico", cascade = {
			CascadeType.ALL })
	private List<Nota> notas;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;

	@OneToOne
	@JoinColumn(name = "JOGO_ID")
	private Jogo jogo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Nota> getNotas() {
		return notas;
	}

	public void setNotas(List<Nota> notas) {
		this.notas = notas;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Jogo getJogo() {
		return jogo;
	}

	public void setJogo(Jogo jogo) {
		this.jogo = jogo;
	}

	public void addNota(Nota nota) {
		if (!getNotas().contains(nota)) {
			getNotas().add(nota);
			if (nota.getHistorico() != null) {
				nota.getHistorico().getNotas().remove(nota);
			}
			nota.setHistorico(this);
		}
	}

	@Override
	public String toString() {
		return "Id historico:" + getId() + ";";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Historico)) {
			return false;
		}
		Historico historico = (Historico) obj;

		return toString().equals(historico.toString());
	}
}
