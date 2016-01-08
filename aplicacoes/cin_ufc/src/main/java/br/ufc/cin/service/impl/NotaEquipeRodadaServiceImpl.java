package br.ufc.cin.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.ufc.cin.model.Aposta;
import br.ufc.cin.model.Consultoria;
import br.ufc.cin.model.Deposito;
import br.ufc.cin.model.Equipe;
import br.ufc.cin.model.Jogo;
import br.ufc.cin.model.NotaEquipeRodada;
import br.ufc.cin.model.ReaberturaSubmissao;
import br.ufc.cin.model.Rodada;
import br.ufc.cin.model.SolicitacaoConsultoria;
import br.ufc.cin.repository.NotaEquipeRodadaRepository;
import br.ufc.cin.service.ApostaService;
import br.ufc.cin.service.ConsultoriaService;
import br.ufc.cin.service.EquipeService;
import br.ufc.cin.service.NotaEquipeRodadaService;
import br.ufc.cin.service.ReaberturaSubmissaoService;
import br.ufc.cin.service.SolicitacaoConsultoriaService;
import br.ufc.quixada.npi.service.impl.GenericServiceImpl;

@Named
public class NotaEquipeRodadaServiceImpl extends GenericServiceImpl<NotaEquipeRodada> implements NotaEquipeRodadaService{

	@Inject
	private NotaEquipeRodadaRepository notaEquipeRodadaRepository;
	
	@Inject
	private EquipeService equipeService;

	@Inject
	private ApostaService apostaService;
	
	@Inject
	private ConsultoriaService ConsultoriaService;
	
	@Inject
	private SolicitacaoConsultoriaService solicitacaoConsultoriaService;
	
	@Inject
	private ReaberturaSubmissaoService reaberturaSubmissaoService;
	
	@Override
	public List<NotaEquipeRodada> buscarPorEquipe(Equipe equipe) {
		return notaEquipeRodadaRepository.buscarPorEquipe(equipe);
	}

	@Override
	public NotaEquipeRodada findByEquipeRodada(Equipe equipe, Rodada rodada) {
		return notaEquipeRodadaRepository.findByEquipeRodada(equipe,rodada);
	}

	@Override
	public void atualizaNotasEquipesRodada(Jogo jogo, String permissao) {
		for (Equipe equipe : jogo.getEquipes()) {
			List<NotaEquipeRodada> notasEquipeRodadas = buscarPorEquipe(equipe);
			if(notasEquipeRodadas == null){
				notasEquipeRodadas = equipeService.criarNotasEquipeRodadas(notasEquipeRodadas, equipe, permissao);
			}
			if(notasEquipeRodadas != null){
				notasEquipeRodadas = equipeService.atualizarNotasEquipeRodadas(notasEquipeRodadas, equipe, permissao);
			}	
		}		
	}

	@Override
	public Float calculaMedia(List<NotaEquipeRodada> notasEquipeRodadas) {
		Float media = 0f;
		int cont = 0;
		for (NotaEquipeRodada notaEquipeRodada : notasEquipeRodadas) {
			if(notaEquipeRodada.getRodada().isStatusRaking()){
				media += notaEquipeRodada.getValor();
				cont++;
			}
		}
		if(cont==0)
			return 0F;
		
		return (Float) media/cont;
	}

	@Override
	public List<NotaEquipeRodada> somaInvestimentos(
			List<NotaEquipeRodada> notasEquipeRodadas) {
		for (NotaEquipeRodada notaEquipeRodada : notasEquipeRodadas) {
			Consultoria consultoria = ConsultoriaService.findByRodada(notaEquipeRodada.getRodada());
			SolicitacaoConsultoria solicitacao;
			Float valConsulta = 0F;
			Float valReabertura = 0F;
			if(consultoria.getId()!=null){
				solicitacao = solicitacaoConsultoriaService.findByEquipeConsulta(notaEquipeRodada.getEquipe(), consultoria);
				if(solicitacao!=null && solicitacao.isStatus()){
					valConsulta = consultoria.getValor();
				}
			}
			Float valor = 0F;
			List<Aposta> apostas = apostaService.findByRodada(notaEquipeRodada.getRodada());
			if(apostas!=null){
				for (Aposta aposta : apostas) {
					for (Deposito deposito : aposta.getDepositos()) {
						if(deposito.getEquipe().equals(notaEquipeRodada.getEquipe()))
							valor += deposito.getQuantia();
					}
				}
			}
			ReaberturaSubmissao reaberturaSubmissao = reaberturaSubmissaoService.find(notaEquipeRodada.getEquipe(), notaEquipeRodada.getRodada());
			if(reaberturaSubmissao!=null && reaberturaSubmissao.isStatus()){
				Integer qtdDias =Integer.parseInt(reaberturaSubmissao.getQuantidadeDia());
				valReabertura =  qtdDias * notaEquipeRodada.getRodada().getValorReabertura();
			}
			notaEquipeRodada.setRetorno(valor * notaEquipeRodada.getFatorDeAposta() - valConsulta - valReabertura);
			update(notaEquipeRodada);
		}
		return notasEquipeRodadas;
	}

}
