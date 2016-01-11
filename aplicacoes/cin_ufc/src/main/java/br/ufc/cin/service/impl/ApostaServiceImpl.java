package br.ufc.cin.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Aposta;
import br.ufc.cin.model.Deposito;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.NotaEquipeRodada;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.SaldoNaRodada;
import br.ufc.cin.model.SaldoPorJogo;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.repository.ApostaRepository;
import br.ufc.cin.repository.DepositoRepository;
import br.ufc.cin.service.ApostaService;
import br.ufc.cin.service.EquipeService;
import br.ufc.cin.service.NotaEquipeRodadaService;
import br.ufc.cin.service.SaldoNaRodadaService;
import br.ufc.cin.service.SaldoPorJogoService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class ApostaServiceImpl extends GenericServiceImpl<Aposta> implements ApostaService{

	@Inject
	private ApostaRepository apostaRepository;
	
	@Inject
	private DepositoRepository depositoRepository; 
	
	@Inject
	private SaldoPorJogoService saldoPorJogoService;
	
	@Inject
	private SaldoNaRodadaService saldoNaRodadaService;
	
	@Inject
	private EquipeService equipeService;
	
	@Inject
	private NotaEquipeRodadaService notaEquipeRodadaService;

	@Override
	public Aposta findByUsuarioRodada(Usuario apostador, Rodada rodada) {
		return apostaRepository.findByUsuarioRodada(apostador, rodada);
	}


	@Override
	public boolean verificaSaldo(Aposta aposta, Float valor) {
		if(aposta.getSaldo() < valor){
			return false;
		}
		return true;
	}


	@Override
	public Aposta criarAposta(Usuario apostador, Rodada rodada) {
		Aposta aposta = new Aposta(); 
		aposta.setApostador(apostador);
		aposta.setRodada(rodada);
		if(rodada.isAllIn()){
			SaldoPorJogo saldo = saldoPorJogoService.findByUsuarioJogo(apostador, rodada.getJogo());
			aposta.setSaldo(saldo.getSaldo());
		}else{
			aposta.setSaldo(rodada.getValorLiberado());
		}
		apostaRepository.save(aposta);
		return aposta;
	}

	@Override
	public void atualizaSaldoDisponivel(Aposta aposta,Float deposito) {
		if(verificaSaldo(aposta, deposito)){
			aposta.setSaldo(aposta.getSaldo() - deposito);
		}
		update(aposta);
	}

	@Override
	public void realizarDeposito(Aposta aposta, Deposito deposito) {
		if(!verificaSaldo(aposta, deposito.getQuantia())){
			throw new IllegalArgumentException("Saldo insuficiente!");
		}
		SaldoNaRodada saldoNaRodada = saldoNaRodadaService.findByEquipeRodada(deposito.getEquipe(), aposta.getRodada());
		if(saldoNaRodada == null){
			deposito.setDia(new Date());
			depositoRepository.save(deposito);
			saldoNaRodada = new SaldoNaRodada();
			saldoNaRodada.setEquipe(deposito.getEquipe());
			saldoNaRodada.setRodada(aposta.getRodada());
			saldoNaRodada.setSaldo(deposito.getQuantia());
			saldoNaRodadaService.save(saldoNaRodada);
			aposta.addDeposito(deposito);
			atualizaSaldoDisponivel(aposta, deposito.getQuantia());
		}else{
			deposito.setDia(new Date());
			depositoRepository.save(deposito);
			saldoNaRodada.setSaldo(saldoNaRodada.getSaldo() + deposito.getQuantia());
			saldoNaRodadaService.update(saldoNaRodada);
			aposta.addDeposito(deposito);
			atualizaSaldoDisponivel(aposta, deposito.getQuantia());
		}
		
	}


	@Override
	public List<Aposta> findByRodada(Rodada rodada) {
		return apostaRepository.findByRodada(rodada);
	}


	@Override
	public void atualizaSaldoEquipes(Jogo jogo, Rodada rodada) {
		for (Equipe equipe : jogo.getEquipes()) {
			SaldoNaRodada saldoNaRodada = saldoNaRodadaService.findByEquipeRodada(equipe, rodada);
			if(saldoNaRodada != null){
				NotaEquipeRodada notaEquipeRodada = notaEquipeRodadaService.findByEquipeRodada(equipe, rodada);
				if(notaEquipeRodada != null){
					saldoNaRodada.setSaldoComFator((saldoNaRodada.getSaldo() * notaEquipeRodada.getFatorDeAposta()) - saldoNaRodada.getDebito());
					saldoNaRodadaService.update(saldoNaRodada);
					
					equipe.setSaldo(equipe.getSaldo() + saldoNaRodada.getSaldoComFator());
					equipeService.update(equipe);
				}
			}
		}
	}


	@Override
	public void atualizaSaldoAlunos(Jogo jogo, Rodada rodada) {
		List<Usuario> alunos = jogo.getAlunos();
		for (Usuario usuario : alunos) {
			Float saldo = 0f;
			SaldoPorJogo saldoPorJogo = saldoPorJogoService.findByUsuarioJogo(usuario, jogo);
			if(saldoPorJogo == null){
				saldoPorJogo = new SaldoPorJogo();
				saldoPorJogo.setApostador(usuario);
				saldoPorJogo.setJogo(jogo);
				saldoPorJogo.setSaldo(saldo);
			}
			Aposta aposta = findByUsuarioRodada(usuario, rodada);
			saldo = saldoPorJogo.getSaldo();
			if(aposta!=null){
				List<Deposito> depositos = aposta.getDepositos();
				for (Deposito deposito : depositos) {
					NotaEquipeRodada notaEquipeRodada =
							notaEquipeRodadaService.findByEquipeRodada(deposito.getEquipe(), rodada);
					if(notaEquipeRodada != null){
						saldo += (deposito.getQuantia() * notaEquipeRodada.getFatorDeAposta());
					}
				}
			}
			saldoPorJogo.setSaldo(saldo);
			saldoPorJogoService.update(saldoPorJogo);
		}
	}

	@Override
	public void realizarDeposito(Equipe equipe, String quantidadeDia) {
		Deposito deposito = new Deposito();
		deposito.setEquipe(equipe);
		Float fator = converte(quantidadeDia);
		deposito.setQuantia((-fator)*1000);
		depositoRepository.save(deposito);
	}
	
	private Float converte(String quantidade){
		if(quantidade.equals("1") || quantidade.equals("2") || quantidade.equals("3")){
			return Float.parseFloat(quantidade);
		}else{
			throw new IllegalArgumentException("Quantidade de dias inválida para uma reabertura.");
		}
	}

	@Override
	public List<Aposta> findByUsuarioRodada(Usuario requisitado, Jogo jogo) {
		List<Aposta> apostas = new ArrayList<Aposta>();
		Long now = new Date().getTime();
		for (Rodada rodada : jogo.getRodadas()) {
			if(now > rodada.getTermino().getTime() || rodada.isStatus()){
				Aposta aposta = findByUsuarioRodada(requisitado, rodada);
				if(aposta!=null){
					atualizaDepositos(aposta.getDepositos(), rodada);
					update(aposta);
					apostas.add(aposta);
				}
			}
		}
		if(apostas.isEmpty()){
			throw new IllegalArgumentException("Não existem investimentos desse aluno no jogo.");
		}
		return apostas;
	}

	private void atualizaDepositos(List<Deposito> depositos, Rodada rodada){
		for (Deposito deposito : depositos) {
			NotaEquipeRodada nota = notaEquipeRodadaService.findByEquipeRodada(deposito.getEquipe(), rodada);
			if(nota!=null && nota.getFatorDeAposta()!=null){
				deposito.setRetorno(nota.getFatorDeAposta() * deposito.getQuantia());
			}
		}
	}

	@Override
	public List<Aposta> atualizaTotalRetorno(List<Aposta> apostas) {
		if(apostas == null ||apostas.isEmpty()){
			throw new IllegalArgumentException("A rodada não possui investimentos.");
		}
		List<Aposta> atuaisApostas = new ArrayList<Aposta>();
		for (Aposta aposta : apostas) {
			atualizaDepositos(aposta.getDepositos(), aposta.getRodada());
			Float temp = 0f;
			for (Deposito deposito : aposta.getDepositos()) {
				temp += deposito.getRetorno();
			}
			aposta.setRetorno(temp);
			update(aposta);
			atuaisApostas.add(aposta);
		}
		return atuaisApostas;
	}

	@Override
	public List<Aposta> ordenaPorRetorno(List<Aposta> apostas) {
		if(apostas != null){
			for (int i=0;i<apostas.size();i++) {
				for (int j = i+1; j < apostas.size(); j++) {
					if(apostas.get(i).getRetorno() < apostas.get(j).getRetorno()){
						Aposta aux = apostas.get(i);
						apostas.add(i, apostas.get(j));
						apostas.remove(i+1);
						apostas.add(j, aux);
						apostas.remove(j+1);
					}
				}
			}
			return apostas;
		}
		return null;
	}


	@Override
	public void deletePor(Rodada rodada) {
		List<Aposta> apostas = findByRodada(rodada);
		if(apostas!=null){
			for (Aposta aposta : apostas) {
				delete(aposta);
			}
		}
	}

}
