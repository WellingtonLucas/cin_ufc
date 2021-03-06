package br.ufc.cin.service.impl;

import static br.ufc.cin.util.Constants.MENSAGEM_PERMISSAO_NEGADA;

import java.util.Date;
import java.util.List;

import javax.inject.Named;

import br.ufc.cin.model.Aposta;
import br.ufc.cin.model.Consultoria;
import br.ufc.cin.model.Entrega;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Formulario;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.NotaEquipeRodada;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.SolicitacaoConsultoria;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.service.RegrasService;
import br.ufc.cin.util.Constants;

@Named
public class RegrasServiceImpl implements RegrasService {

	@Override
	public void verificaParticipacao(Usuario usuario, Jogo jogo) {
		if (!jogo.isStatus() && jogo.getAlunos().contains(usuario)) {
			throw new IllegalArgumentException(
					"Jogo inativo no momento. Para mais informações: "
							+ jogo.getProfessor().getEmail()+".");
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

	@Override
	public void verificaAlunoEquipe(Usuario usuario, Equipe equipe) {
		if(!equipe.getAlunos().contains(usuario)){
			throw new IllegalArgumentException(MENSAGEM_PERMISSAO_NEGADA);
		}
		
	}

	@Override
	public void verificaFormulario(Formulario formulario) {
		if(formulario==null){
			throw new IllegalArgumentException("Formulário inexistente.");
		}
		
	}

	@Override
	public void verificaEntrega(Entrega entrega) {
		if(entrega == null){
			throw new IllegalArgumentException("Entrega inexistente.");
		}
	}

	@Override
	public void verificaNoraEquipeRodada(NotaEquipeRodada notaEquipeRodada) {
		if(notaEquipeRodada==null){
			throw new IllegalArgumentException("Não existe nota para esta equipe, nesta rodada.");
		}
		
	}

	@Override
	public void verificaApostas(List<Aposta> apostas) {
		if (apostas == null || apostas.isEmpty()) {
			throw new IllegalArgumentException("Não existem apostas até o momento.");
		}		
	}

	@Override
	public void verificaMembroOuProfessorEquipe(Usuario usuario, Equipe equipe) {
		if((!equipe.getAlunos().contains(usuario)) && (!equipe.getJogo().getProfessor().equals(usuario))){
			throw new IllegalArgumentException(MENSAGEM_PERMISSAO_NEGADA);
		}
		
	}

	@Override
	public void verificaSeProfessorPeriodoRodada(Usuario usuario, Rodada rodada) {
		Long now = new Date().getTime();
		if(!rodada.getJogo().getProfessor().equals(usuario) && (!rodada.isStatus() && (rodada.getTermino().getTime() > now))){
			throw new IllegalAccessError("Rodada não está ativa no momento.");
		}
		
	}

	@Override
	public void verificaUsuario(Usuario requisitado) {
		if(requisitado==null){
			throw new IllegalArgumentException("Usuário não existe.");
		}
		
	}

	@Override
	public void verificaSolicitacao(
			SolicitacaoConsultoria solicitacaoConsultoria) {
		if(solicitacaoConsultoria==null){
			throw new IllegalArgumentException("Solicitação de consultoria não existe.");
		}
	}

	@Override
	public void verificaEquipeSolicitacao(Equipe equipe,
			SolicitacaoConsultoria solicitacaoConsultoria) {
		if(!solicitacaoConsultoria.getEquipe().equals(equipe)){
			throw new IllegalArgumentException("Solicitação de consultoria não é da equipe informada.");
		}
	}

	@Override
	public void verificaRodadaSolicitacao(Rodada rodada,
			SolicitacaoConsultoria solicitacaoConsultoria) {
		if(!solicitacaoConsultoria.getConsultoria().getRodada().equals(rodada)){
			throw new IllegalArgumentException("A consultoria não pertence a rodada especificada.");
		}
		
	}

	@Override
	public void verificaSeAluno(Usuario usuario, Jogo jogo) {
		if(!jogo.getAlunos().contains(usuario)){
			throw new IllegalArgumentException("Somente alunos podem efetuar apostas.");
		}
		
	}

	@Override
	public void verificaConsultoria(Consultoria consultoria) {
		if(consultoria.getId()==null){
			throw new IllegalArgumentException("Primeiramente crie a consultoria para esta rodada.");
		}
	}

	@Override
	public void verificaStatusRanking(Rodada rodada) {
		if(rodada.isStatusRaking()){
			throw new IllegalArgumentException("O ranking da rodada já está publicado.");
		}
		
	}

}
