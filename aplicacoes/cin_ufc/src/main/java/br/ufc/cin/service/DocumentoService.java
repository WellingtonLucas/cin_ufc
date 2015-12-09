package br.ufc.cin.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.ufc.cin.model.Documento;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.Usuario;
import br.ufc.quixada.npi.service.GenericService;

public interface DocumentoService extends GenericService<Documento> {
	
	public abstract void salvar(List<Documento> documentos);

	List<Documento> getDocumentoByProjeto(Jogo jogo);

	public abstract boolean verificaExtensao(String extensao);

	public abstract boolean verificaSeImagem(String extensao);

	/**
	 * Verifica o arquivo em anexo e cria uma nova instancia de documento.
	 * @author Wellington
	 * @throws IOException 
	 * */
	public abstract Documento verificaAnexoEntrega(MultipartFile anexo, Usuario usuario, Rodada rodada, Jogo jogo, Equipe equipe) throws IOException;

	/**
	 * Verifica a imagem em anexo e cria uma nova instancia de documento.
	 * @author Wellington
	 * @throws IOException 
	 * */
	public abstract Documento verificaAnexoImagem(MultipartFile anexo,
			Usuario usuario) throws IOException;

	public abstract void verificaArquivos(List<MultipartFile> anexos);

	public abstract List<Documento> criaDocumentos(List<MultipartFile> anexos,
			Jogo jogo) throws IOException;
}
