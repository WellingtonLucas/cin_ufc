package br.ufc.cin.service.impl;

import static br.ufc.cin.util.Constants.MENSAGEM_PERMISSAO_NEGADA;

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
		if (!jogo.isStatus() && jogo.getAlunos().contains(usuario)) {
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

	@Override
	public void verificaJogo(Jogo jogo) {
		if (jogo == null) {
			throw new IllegalArgumentException(
					"Jogo solicitado não existe.");
		}
		
	}

	@Override
	public void verificaRodada(Rodada rodada) {
		if (rodada == null) {
			throw new IllegalArgumentException(
					"Rodada solicitada não existe.");
		}
	}

	@Override
	public void verificaEquipe(Equipe equipe) {
		if (equipe == null) {
			throw new IllegalArgumentException(
					"Equipe solicitada não existe.");
		}
		
	}

	@Override
	public void verificaStatusRanking(Jogo jogo) {
		boolean flag = false;
		for (Rodada rodada : jogo.getRodadas()) {
			if(rodada.isStatusRaking()){
				flag = true;
			}
		}
		if(!flag){
			throw new IllegalArgumentException(
					"Não existem rodadas com rankings prontos até o momento.");
		}
	}

	@Override
	public void verificaEquipeJogo(Equipe equipe, Jogo jogo) {
		if(!jogo.getEquipes().contains(equipe)){
			throw new IllegalArgumentException(
					"Equipe não existe ou não pertence ao jogo.");
		}		
	}

	@Override
	public void verificaRodadaInJogo(Jogo jogo) {
		if(jogo.getRodadas() == null || jogo.getRodadas().isEmpty()){
			throw new IllegalArgumentException(
					"O jogo ainda não possui rodadas.");
		}
			
		
	}

	@Override
	public void verificaSeProfessor(Usuario usuario, Jogo jogo) {
		if(!jogo.getProfessor().equals(usuario)){
			throw new IllegalArgumentException(
					MENSAGEM_PERMISSAO_NEGADA);
		}
	}

	@Override
	public void verificaRodadaJogo(Rodada rodada, Jogo jogo) {
		if (rodada == null || !jogo.getRodadas().contains(rodada)) {
			throw new IllegalArgumentException(
					"Rodada solicitada não existe.");
		}
		
	}

	@Override
	public void verificaJogoComRodada(Jogo jogo) {
		if (jogo.getRodadas() == null || jogo.getRodadas().isEmpty()) {
			throw new IllegalArgumentException(
					"O jogo ainda não possui rodadas.");
		}	
	}

	@Override
	public void verificaStatusJogo(Jogo jogo) {
		if (!jogo.isStatus()) {
			throw new IllegalArgumentException(
					"Jogo inativo.");
		}	
		
	}

}
