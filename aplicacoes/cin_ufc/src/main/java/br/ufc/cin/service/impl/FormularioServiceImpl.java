package br.ufc.cin.service.impl;

import java.util.regex.Pattern;

import javax.inject.Named;

import br.ufc.cin.model.Formulario;
import br.ufc.cin.model.Opcao;
import br.ufc.cin.model.Pergunta;
import br.ufc.cin.service.FormularioService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class FormularioServiceImpl extends GenericServiceImpl<Formulario> implements FormularioService{

	@Override
	public void verificaCamposObrigatorios(Formulario formulario) {
		boolean soEspacos = Pattern.matches("\\s+", formulario.getTitulo());
		if(formulario.getTitulo()==null || formulario.getTitulo().isEmpty() || soEspacos){
			throw new IllegalArgumentException("O título do formulário é obrigatório.");
		}
		for (Pergunta pergunta : formulario.getPerguntas()) {
			boolean espacosP = Pattern.matches("\\s+", pergunta.getDescricao());
			if(pergunta.getDescricao()==null || pergunta.getDescricao().isEmpty() || espacosP)
				throw new IllegalArgumentException("Os enunciados de todas as questões são obrigatórios");
			else{
				for (Opcao opcao : pergunta.getOpcoes()) {
					boolean soEspacosO = Pattern.matches("\\s+", opcao.getDescricao());
					if(opcao.getDescricao()==null || opcao.getDescricao().isEmpty() || soEspacosO)
						throw new IllegalArgumentException("Os enunciados de todas as opções são obrigatórios. "
								+ "Verifique a questão \""+pergunta.getDescricao()+"\".");
				}
			}
		}
		
	}

}
