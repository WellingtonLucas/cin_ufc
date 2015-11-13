package br.ufc.cin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.springframework.security.crypto.codec.Base64;

@Entity
public class Documento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "nome_original")
	private String nomeOriginal;

	private String nome;

	private String extensao;

	@Type(type = "org.hibernate.type.BinaryType")
	private byte[] arquivo;

	@ManyToOne
	private Jogo jogo;

	@Transient
	private String encode;

	public String getEncode() {
		byte[] encoded = Base64.encode(arquivo);
		String encodedString = new String(encoded);
		return encodedString;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNomeOriginal() {
		return nomeOriginal;
	}

	public void setNomeOriginal(String nomeOriginal) {
		this.nomeOriginal = nomeOriginal;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getExtensao() {
		return extensao;
	}

	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}

	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}

	public Jogo getJogo() {
		return jogo;
	}

	public void setJogo(Jogo jogo) {
		this.jogo = jogo;
	}

	public Integer getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Documento id: " + getId() + "; Nome: " + getNomeOriginal()
				+ "; Extensão: " + getExtensao()+";";
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Documento)){
			return false;
		}
		Documento documento = (Documento) obj;
		
		return toString().equals(documento.toString());
	}

}
