package br.ufc.cin.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Aposta;
import br.ufc.cin.model.Deposito;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.SaldoPorJogo;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.repository.ApostaRepository;
import br.ufc.cin.repository.DepositoRepository;
import br.ufc.cin.service.ApostaService;
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
		apostaRepository.update(aposta);
	}

	@Override
	public void realizarDeposito(Aposta aposta, Deposito deposito) {
		if(!verificaSaldo(aposta, deposito.getQuantia())){
			throw new IllegalArgumentException("Saldo insuficiente!");
		}
		depositoRepository.save(deposito);
		aposta.addDeposito(deposito);
		atualizaSaldoDisponivel(aposta, deposito.getQuantia());
	}


	@Override
	public List<Aposta> findByRodada(Rodada rodada) {
		return apostaRepository.findByRodada(rodada);
	}

}
