package br.ufc.cin.service.impl;

import static br.ufc.cin.util.Constants.MENSAGEM_ERRO_UPLOAD;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import br.ufc.cin.model.Documento;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.repository.DocumentoRepository;
import br.ufc.cin.service.DocumentoService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class DocumentoServiceImpl extends GenericServiceImpl<Documento> implements DocumentoService{
	
	@Autowired
	private DocumentoRepository documentoRepository;
	
	@Override
	public void salvar(List<Documento> documentos) {
		if(!documentos.isEmpty()){
			for(Documento documento : documentos) {
				save(documento);
			}		
		}else{
			throw new IllegalArgumentException("Erro ao persistir os documentos.");
		}
	}

	@Override
	public List<Documento> getDocumentoByProjeto(Jogo jogo) {		
		return documentoRepository.getDocumentosByJogo(jogo);
	}

	@Override
	public boolean verificaExtensao(String extensao) {
		if(extensao.equals("application/vnd.oasis.opendocument.text") || extensao.equals("application/pdf") 
				|| extensao.equals("application/msword") 
				 || extensao.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")){
			return true;
		}
		return false;
	}

	@Override
	public boolean verificaSeImagem(String extensao) {
		if(extensao.equals("image/jpeg") || extensao.equals("image/png")){
			return true;
		}
		return false;
	}

	@Override
	public Documento verificaAnexoEntrega(MultipartFile anexo, Usuario usuario, Rodada rodada, Jogo jogo, Equipe equipe) throws IOException {
		Documento documento = new Documento();
		if (anexo != null && anexo.getBytes().length > 0) {
			if (anexo.getBytes() != null && anexo.getBytes().length != 0) {
				documento.setArquivo(anexo.getBytes());
				documento.setNomeOriginal(anexo.getOriginalFilename());
				if (!usuario.equals(jogo.getProfessor())) {
					documento
							.setNome(equipe.getNome() + "-" + rodada.getNome());
				} else {
					documento.setNome("MODELO-" + rodada.getNome());
				}
				documento.setExtensao(anexo.getContentType());
				if (!verificaExtensao(documento.getExtensao())) {
					throw new IllegalArgumentException(
							"O arquivo deve está com algum desses formatos: "
									+ "doc, docx, pdf, odt ou fodt!");
				}
			}			

		} else if(anexo.getSize() == 0){
			throw new IllegalArgumentException(
					"Selecione um arquivo para a entrega!");
		} else if(anexo.getSize() > 10000000){
			throw new IllegalArgumentException(
					"O arquivo deve ser menor que 1Mb!");
		}
		return documento;
	}

	@Override
	public Documento verificaAnexoImagem(MultipartFile anexo, Usuario usuario) throws IOException {
		Documento documento = new Documento();
		if (anexo != null) {
			if (anexo.getBytes() != null && anexo.getBytes().length != 0) {
				if(anexo.getSize() <= 100000){
					documento.setArquivo(anexo.getBytes());
					String data = new Date().getTime() + "";
					documento.setNomeOriginal(data + "-"
							+ anexo.getOriginalFilename());
					documento.setNome(usuario.getNome() + "-" + "foto");
					documento.setExtensao(anexo.getContentType());
					if (!verificaSeImagem(documento.getExtensao())) {
						throw new IllegalArgumentException(
								"O arquivo deve ter um destes formatos: PNG ou JPEG ");
					}
				}else{
					throw new IllegalArgumentException(
							"A imagem precisa ter no máximo 100KB.");
				}
			}
		}
		return documento;
	}

	@Override
	public void verificaArquivos(List<MultipartFile> anexos) {
		if(anexos != null && !anexos.isEmpty()) {
			for(MultipartFile anexo : anexos) {
				if(anexo.getSize() > 10000000){
					throw new IllegalArgumentException("Os arquivos devem possuir menos de 10Mb.");
				}
			}
		}
	}

	@Override
	public List<Documento> criaDocumentos(List<MultipartFile> anexos, Jogo jogo) throws IOException {
		List<Documento> documentos = new ArrayList<Documento>();
		if(anexos != null && !anexos.isEmpty()) {
			for(MultipartFile anexo : anexos) {
				if(anexo.getBytes() != null && anexo.getBytes().length != 0) {
					Documento documento = new Documento();
					documento.setArquivo(anexo.getBytes());
					documento.setNomeOriginal(anexo.getOriginalFilename());
					documento.setNome(anexo.getName());
					documento.setExtensao(anexo.getContentType());
					documento.setJogo(jogo);
					documentos.add(documento);
				}
			}
			return documentos;
		}else{
			throw new IllegalArgumentException(MENSAGEM_ERRO_UPLOAD);
		}
	}

}
