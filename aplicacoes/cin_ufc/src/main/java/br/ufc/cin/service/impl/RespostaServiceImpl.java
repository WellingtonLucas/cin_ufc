package br.ufc.cin.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Entrega;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Formulario;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.Opcao;
import br.ufc.cin.model.Resposta;
import br.ufc.cin.model.Usuario;
import br.ufc.cin.repository.RespostaRepository;
import br.ufc.cin.service.EntregaService;
import br.ufc.cin.service.OpcaoService;
import br.ufc.cin.service.RespostaService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class RespostaServiceImpl extends GenericServiceImpl<Resposta> implements RespostaService{

	@Inject
	private RespostaRepository respostaRepository;
	
	@Inject
	private OpcaoService opcaoService;
	
	@Inject
	private  EntregaService entregaService;
	
	@Override
	public Resposta getRespostaByEntrega(Entrega entrega) {
		return respostaRepository.getRespostaByEntrega(entrega);
	}

	@Override
	public List<Resposta> find(Usuario usuarioRequisitado, Jogo jogo) {
		List<Resposta> respostas = find(usuarioRequisitado);
		List<Resposta> respostas2 = new ArrayList<Resposta>();
		if(respostas != null){
			for (Resposta resposta : respostas) {
				if(resposta.getEntrega() != null){
					Jogo temp = resposta.getEntrega().getEquipe().getJogo();
					if(jogo.equals(temp)){
						respostas2.add(resposta);
					}
				}
			}
		}
		return respostas2;
	}

	@Override
	public List<Resposta> find(Usuario usuarioRequisitado) {
		return respostaRepository.find(usuarioRequisitado);
	}

	@Override
	public List<Resposta> find(Usuario usuario, Entrega entrega) {
		return respostaRepository.find(usuario, entrega);
	}

	@Override
	public Resposta findUltimaRespostaPorEntrega(Usuario usuario, Entrega entrega) {
		List<Resposta> respostas = find(usuario, entrega);
		if(respostas != null){
			Long maior = respostas.get(0).getDia().getTime();
			int temp = 0;
			for (int i = 1; i < respostas.size(); i++) {
				if(respostas.get(i).getDia().getTime() > maior){
					maior = respostas.get(i).getDia().getTime();
					temp = i;
				}
			}
			return respostas.get(temp);
		}
		return null;
	}
	
	@Override
	public void salvar(Resposta resposta, Jogo jogo, Formulario formulario, Usuario usuario, Entrega entrega) {
		List<Opcao> opcoes = new ArrayList<Opcao>();
		for (Opcao opcao : resposta.getOpcoes()) {
			if(!(opcao.getId()==null))
				opcoes.add(opcaoService.find(Opcao.class, opcao.getId()));
		}
		if(opcoes.size()==0 || (formulario.getPerguntas().size() != opcoes.size())){
			throw new IllegalAccessError("É necessário responder todas as perguntas para efetuar uma avaliação.");
			
		}
		Calendar calendario = Calendar.getInstance();
		Date data =  calendario.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm:ss");
		simpleDateFormat.format(data);
		resposta.setOpcoes(opcoes);
		resposta.setFormulario(formulario);
		resposta.setUsuario(usuario);
		if(usuario.equals(jogo.getProfessor())){
			resposta.setEntregaGabarito(entrega);
		}else{
			resposta.setEntrega(entrega);
		}
		resposta.setDia(data);
		save(resposta);
		
		if(usuario.equals(jogo.getProfessor())){
			entrega.setGabarito(getRespostaByEntrega(entrega));
			entregaService.update(entrega);
		}
		
	}

	@Override
	public Resposta findUltimaRespostaPorEquipe(Equipe equipe,
			Entrega entrega) {
		Resposta ultima = new Resposta();
		int i=0;
		for (Usuario usuario : equipe.getAlunos()) {
			Resposta resposta = findUltimaRespostaPorEntrega(usuario, entrega);
			if(resposta!=null){
				if(i==0){
					ultima = resposta;
					i++;
				}else{
					if(ultima.getDia().getTime()<resposta.getDia().getTime()){
						ultima = resposta;
					}
				}
			}
		}
		return ultima;
	}
}
