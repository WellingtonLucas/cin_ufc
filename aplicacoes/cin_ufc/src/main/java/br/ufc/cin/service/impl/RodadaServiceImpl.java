package br.ufc.cin.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Formulario;
import br.ufc.cin.model.ReaberturaSubmissao;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.service.FormularioService;
import br.ufc.cin.service.ReaberturaSubmissaoService;
import br.ufc.cin.service.RodadaService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class RodadaServiceImpl extends GenericServiceImpl<Rodada> implements RodadaService{

	@Inject
	private FormularioService formularioService;
	
	@Inject
	private ReaberturaSubmissaoService reaberturaSubmissaoService;
	
	@Override
	public List<Rodada> ordenaPorInicio(List<Rodada> rodadas) {
		for (int i=0;i<rodadas.size();i++) {
			for (int j = i+1; j < rodadas.size(); j++) {
				if(rodadas.get(i).getInicio().getTime() > rodadas.get(j).getInicio().getTime()){
					Rodada aux = rodadas.get(i);
					rodadas.add(i, rodadas.get(j));
					rodadas.remove(i+1);
					rodadas.add(j, aux);
					rodadas.remove(j+1);
				}
			}
		}
		return rodadas;
	}

	@Override
	public List<Rodada> atualizaStatusRodadas(List<Rodada> rodadas) {
		for (Rodada rodada : rodadas) {
			atualizaStatusRodada(rodada);
		}
		return rodadas;
	}

	@Override
	public Rodada atualizaStatusRodada(Rodada rodada) {
		Calendar calendario = Calendar.getInstance();
		long tempoAtual = calendario.getTimeInMillis();
		if(rodada.getTermino().getTime() < tempoAtual || rodada.getInicio().getTime() > tempoAtual){
			if(!rodada.isStatus())
				return rodada;
			rodada.setStatus(false);
		}else{
			if(rodada.isStatus())
				return rodada;
			rodada.setStatus(true);
		}
		update(rodada);
		return rodada;
	}

	@Override
	public Rodada atualizaStatusPrazoRodada(Rodada rodada) {
		Calendar calendario = Calendar.getInstance();
		long tempoAtual = calendario.getTimeInMillis();
		if( rodada.getPrazoSubmissao().getTime() < tempoAtual || rodada.getInicio().getTime() > tempoAtual){
			if(!rodada.isStatusPrazo())
				return rodada;
			rodada.setStatusPrazo(false);
		}else{
			if(rodada.isStatusPrazo())
				return rodada;
			rodada.setStatusPrazo(true);
		}
		update(rodada);
		return rodada;
	}

	@Override
	public Rodada atualizaStatusAvaliacao(Rodada rodada) {
		Calendar calendario = Calendar.getInstance();
		long tempoAtual = calendario.getTimeInMillis();
		Long prazoComReabertura = rodada.getPrazoSubmissao().getTime() + quantidadeDiasReaberturaMillis(rodada);
		if(prazoComReabertura < tempoAtual && (rodada.getTerminoAvaliacao().getTime() < tempoAtual || rodada.getInicio().getTime() > tempoAtual)){
			if(!rodada.isStatusAvaliacao())
				return rodada;
			rodada.setStatusAvaliacao(false);
		}else{
			if(rodada.isStatusAvaliacao())
				return rodada;
			rodada.setStatusAvaliacao(true);
		}
		update(rodada);
		return rodada;
	}

	@Override
	public void verificaSeNaoPrazoSubmissao(Rodada rodada) {
		Calendar calendario = Calendar.getInstance();
		long tempoAtual = calendario.getTimeInMillis();
		if(rodada.getPrazoSubmissao().getTime() > tempoAtual){
			throw new IllegalArgumentException("Aguarde o prazo de submissão se encerrar.");
		}
	}

	@Override
	public void verificaStatusPrazoSubmissao(Rodada rodada) {
		if(rodada.isStatusPrazo()){
			throw new IllegalArgumentException("Aguarde o término do período de Submissão.");
		}
		
	}

	@Override
	public void verificaStatusRodada(Rodada rodada) {
		if(!rodada.isStatus()){
			throw new IllegalArgumentException("A rodada se encerrou!");
		}
		
	}

	@Override
	public void verificaStatusAvaliacao(Rodada rodada) {
		if(!rodada.isStatusAvaliacao()){
			throw new IllegalArgumentException("Você não está no período de avaliação desta rodada!");
		}
		
	}

	@Override
	public void salvar(Rodada rodada, String allIn) {
		rodada.setStatus(false);
		rodada.setStatusPrazo(true);
		rodada.setStatusAvaliacao(false);
		rodada.setStatusRaking(false);
		rodada.setStatusNota(true);
		if(allIn.equals("sim")){
			rodada.setAllIn(true);
		}else{
			if(rodada.getValorLiberado() == null){
				throw new IllegalAccessError("Preencha o valor liberado para a rodada e tente novamente.");
			}
			rodada.setAllIn(false);
		}
		if( rodada.getFormulario() == null || rodada.getFormulario().getId() == null){
			throw new IllegalArgumentException("Primeiramente crie formulários para associar a uma rodada.");
		}
		Formulario formulario = formularioService.find(Formulario.class, rodada.getFormulario().getId());
		if(formulario == null){
			throw new IllegalArgumentException("Primeiramente crie formulários para associar a uma rodada.");
		}
		rodada.setFormulario(formulario);
		save(rodada);
	}

	@Override
	public void verificarDatas(Rodada rodada) {
		if(rodada.getInicio().getTime() > rodada.getTermino().getTime()){
			throw new IllegalArgumentException("A data de início deve ser anterior a data de término.");
		}
		if(rodada.getInicio().getTime() > rodada.getPrazoSubmissao().getTime()){
			throw new IllegalArgumentException("O prazo de submissão deve ser posterior a data de início.");
		}
		if(rodada.getInicio().getTime() > rodada.getTerminoAvaliacao().getTime()){
			throw new IllegalArgumentException("O prazo de avaliação deve ser posterior a data de início.");
		}
		if(rodada.getTermino().getTime() < rodada.getPrazoSubmissao().getTime()){
			throw new IllegalArgumentException("O prazo de submissão deve ser anterior a data de término.");
		}
		if(rodada.getTermino().getTime() < rodada.getTerminoAvaliacao().getTime()){
			throw new IllegalArgumentException("O prazo de Avaliação deve ser anterior a data de término.");
		}
		if(rodada.getTerminoAvaliacao().getTime() < rodada.getPrazoSubmissao().getTime()){
			throw new IllegalArgumentException("O prazo de submissão deve ser anterior ao prazo de avaliação.");
		}
	}

	@Override
	public void atualizar(Rodada rodada, String allIn) {
		Rodada oldRodada = find(Rodada.class, rodada.getId());
		Formulario formulario = formularioService.find(Formulario.class, rodada.getFormulario().getId());
		rodada.setFormulario(formulario);
		rodada.setJogo(oldRodada.getJogo());
		if(allIn.equals("sim")){
			rodada.setAllIn(true);
		}else{
			if(rodada.getValorLiberado() == null){
				throw new IllegalAccessError("Preencha o valor liberado para a rodada e tente novamente.");
			}
			rodada.setAllIn(false);
		}
		if(rodada.getModelo()!= null)
			rodada.setModelo(oldRodada.getModelo());
		update(rodada);
		
	}

	@Override
	public void verificaStatusRaking(Rodada rodada) {
		if(!rodada.isStatusRaking()){
			throw new IllegalAccessError("O ranking da rodada ainda não está disponível, aguarde.");
		}
		
	}
	
	@Override
	public void verificaSePrazoSubmissao(Rodada rodada) {
		if(!rodada.isStatusPrazo()){
			throw new IllegalArgumentException("Rodada fora do prazo de submissão.");
		}
	}
	
	private Long quantidadeDiasReaberturaMillis(Rodada rodada){
		List<ReaberturaSubmissao> reaberturaSubmissaos = reaberturaSubmissaoService.findByRodada(rodada);
		Integer qtdDias = 0;
		if(reaberturaSubmissaos!=null && !reaberturaSubmissaos.isEmpty()){
			for (ReaberturaSubmissao reaberturaSubmissao : reaberturaSubmissaos) {
				Integer temp = Integer.parseInt(reaberturaSubmissao.getQuantidadeDia());
				if(qtdDias < temp){
					qtdDias = temp;
				}
				if(qtdDias==3){
					break;
				}
			}
		}
		return qtdDias * umDiaTimeInMillis();
	}
	
	private Long umDiaTimeInMillis(){
		Calendar cal1 = Calendar.getInstance();
		cal1.set(2015, 10, 7);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.set(2015, 10, 8);
		return cal2.getTimeInMillis() - cal1.getTimeInMillis();
	}

	@Override
	public boolean isPosPrazoSubmissoesEReabertura(Rodada rodada) {
		Calendar calendario = Calendar.getInstance();
		long tempoAtual = calendario.getTimeInMillis();
		Long prazoComReabertura = rodada.getPrazoSubmissao().getTime() + quantidadeDiasReaberturaMillis(rodada);
		if(prazoComReabertura < tempoAtual || tempoAtual < rodada.getInicio().getTime()){
			return false;
		}
		return true;
	}

	@Override
	public List<Rodada> organizarPorPerfil(List<Rodada> rodadas, Usuario usuario) {
		List<Rodada> novaOrganizacao = new ArrayList<Rodada>();
		Long now = new Date().getTime();
		for (Rodada rodada : rodadas) {
			if(rodada.getJogo().getProfessor().equals(usuario)){
				return rodadas;
			}
			if(rodada.isStatus() || rodada.getTermino().getTime() < now){
				novaOrganizacao.add(rodada);
			}
		}
		return novaOrganizacao;
	}
}
