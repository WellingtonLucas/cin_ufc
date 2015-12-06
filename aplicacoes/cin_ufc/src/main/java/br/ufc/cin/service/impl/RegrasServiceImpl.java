package br.ufc.cin.service.impl;

import javax.inject.Named;

import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.service.RegrasService;
import br.ufc.cin.util.Constants;

@Named
public class RegrasServiceImpl implements RegrasService {

	@Override
	public void verificaParticipacao(Usuario usuario, Jogo jogo) {
		if (jogo == null) {
			throw new IllegalArgumentException(
					Constants.MENSAGEM_JOGO_INEXISTENTE);
		} else if (!jogo.isStatus() && jogo.getAlunos().contains(usuario)) {
			throw new IllegalArgumentException(
					"Jogo inativado no momento. Para mais informações "
							+ jogo.getProfessor().getEmail());
		} else if (!jogo.getAlunos().contains(usuario)
				&& !jogo.getProfessor().equals(usuario)) {
			throw new IllegalArgumentException(
					Constants.MENSAGEM_PERMISSAO_NEGADA);
		}
	}

	@Override
	public void verificaExistencia(Rodada rodada, Equipe equipe, Jogo jogo) {
		if (rodada == null || !jogo.getRodadas().contains(rodada)) {
			throw new IllegalArgumentException(
					"Rodada solicitada não existe.");
		}
		if(equipe == null || !jogo.getEquipes().contains(equipe)){
			throw new IllegalArgumentException(
					"Equipe solicitada não existe.");
		}		
	}

}
