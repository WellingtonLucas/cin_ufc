package br.ufc.cin.service.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Consultoria;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.SolicitacaoConsultoria;
import br.ufc.cin.repository.ConsultoriaRepository;
import br.ufc.cin.service.ConsultoriaService;
import br.ufc.cin.service.EquipeService;
import br.ufc.cin.service.SolicitacaoConsultoriaService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class ConcultoriaServiceImpl extends GenericServiceImpl<Consultoria> implements ConsultoriaService{

	@Inject
	private ConsultoriaRepository consultoriaRepository;
	
	@Inject
	private EquipeService equipeService;
	
	@Inject
	private SolicitacaoConsultoriaService solicitacaoConsultoriaService;
	
	@Override
	public Consultoria findByRodada(Rodada rodada) {
		Consultoria consultoria = new Consultoria();
		Consultoria consultoriaB = consultoriaRepository.findByRodada(rodada);
		if(consultoriaB != null){
			consultoria = consultoriaB; 
		}
		return consultoria;
	}

	@Override
	public String defineAcao(Consultoria consultoria) {
		if(consultoria.getId() == null){
			return "cadastrar";
		}
		return "editar";
	}

	@Override
	public void salvar(Consultoria consultoria, Rodada rodada) {
		consultoria.setRodada(rodada);
		save(consultoria);
	}

	@Override
	public Consultoria verificaRodadaContains(Rodada rodada,
			Consultoria consultoria) {
		Consultoria consultoriaRodada = consultoriaRepository.findByRodada(rodada);
		if(consultoriaRodada == null || !consultoria.equals(consultoriaRodada)){
			throw new IllegalArgumentException("A consultoria especificada não faz parte da rodada \""+rodada.getNome()+"\".");
		}
		consultoriaRodada.setDescricao(consultoria.getDescricao());
		consultoriaRodada.setValor(consultoria.getValor());
		return consultoriaRodada;
	}

	@Override
	public void verificaCampoDescricao(Consultoria consultoria) {
		if(consultoria.getDescricao() == null || consultoria.getDescricao().isEmpty()){
			throw new IllegalAccessError("O campo Descrição é obrigatório. Tente novamente.");
		}
		
	}

	@Override
	public void atualizar(Consultoria consultoria, Equipe equipe) {
		SolicitacaoConsultoria statusConsultoria = new SolicitacaoConsultoria();
		statusConsultoria.setConsultoria(consultoria);
		statusConsultoria.setEquipe(equipe);
		statusConsultoria.setStatus(false);
		statusConsultoria.setDia(new Date());
		if(!equipe.addStatusConsultoria(statusConsultoria)){
			throw new IllegalAccessError("Sua equipe já possui solicitação de consultoria.");
		}
		equipeService.update(equipe);
	}

	@Override
	public Integer quantidadeSolicitacoes(Consultoria consultoria) {
		Integer temp = 0;
		if(consultoria.getId()!=null){
			List<SolicitacaoConsultoria> solicitacoesConsultoria = solicitacaoConsultoriaService.solicitacoesPorConsulta(consultoria);
			if(solicitacoesConsultoria != null){
				for (SolicitacaoConsultoria solicitacaoConsultoria : solicitacoesConsultoria) {
					if(!solicitacaoConsultoria.isStatus()){
						temp++;
					}
				}
			}
		}
		return temp;
	}

	@Override
	public void verificaConsultoria(Rodada rodada) {
		Consultoria consultoria = findByRodada(rodada);
		if(consultoria == null || consultoria.getId()==null){
			throw new IllegalAccessError("Esta rodada não possui serviço de consultoria. Entre em contado com o docente.");
		}
	}


}
