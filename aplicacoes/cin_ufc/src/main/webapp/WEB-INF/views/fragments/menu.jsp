<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="col-sm-4 col-md-2">
	<div class="well nav-sidebar">
		<h4><i class="fa fa-tachometer"></i><small><b> OPÇÕES GERAIS</b></small></h4>
		<ul class="nav nav-pills nav-stacked">
			<c:if test="${action == 'home' || action == 'perfil' || action == 'profile'}">
				<li class="active"><a href="<c:url value ="/jogo/novo-jogo"></c:url>"><i class="glyphicon glyphicon-plus"></i>&nbsp;&nbsp;Novo Jogo</a></li>
				<li><a href="<c:url value ="/formularios"></c:url>"><i class="glyphicon glyphicon-list-alt"></i>&nbsp;&nbsp;Formulários</a></li>
			</c:if>
			<!-- OPCOES DO JOGO -->
			<c:if test="${(permissao == 'professor')||(permissao == 'aluno') ||(permissao == 'alunoLogado') || (permissao == 'membro')}">
				<li data-toggle="tooltip" data-placement="top" title="Página de detalhes do jogo."><a 
					href="<c:url value ="/jogo/${jogo.id}/detalhes"></c:url>">
					<i class="glyphicon glyphicon-education"></i>&nbsp;&nbsp;Home Jogo
					</a>
				</li>
				<li><a data-toggle="tooltip" data-placement="top" title="Veja as empresas do jogo." 
					href="<c:url value ="/jogo/${jogo.id}/equipes"></c:url>">
					<i class="fa fa-industry"></i>&nbsp;&nbsp;Empresas
					</a>
				</li>
				<li><a data-toggle="tooltip" data-placement="top" title="Veja as rodadas do jogo."
					href="<c:url value ="/jogo/${jogo.id}/rodadas"></c:url>">
					<i class="fa fa-youtube-play"></i>&nbsp;&nbsp;Rodadas
					</a>
				</li>
				<li><a data-toggle="tooltip" data-placement="top" title="Rankings gerais do jogo." 
					href="<c:url value ="/jogo/${jogo.id}/rankings"></c:url>">
					<i class="fa fa-line-chart"></i>&nbsp;&nbsp;Rankings
					</a>
				</li>
			</c:if>
			<!-- OPCOES DO JOGO PROFESSOR-->
			<c:if test="${permissao == 'professor'}">
				<li><a data-toggle="tooltip" data-placement="top" title="Veja os participantes do jogo."
					href="<c:url value ="/jogo/${jogo.id}/participantes"></c:url>"><i class="fa fa-users"></i>&nbsp;&nbsp;Participantes</a></li>
				<li><a data-toggle="tooltip" data-placement="top" title="Veja seus formulários."
					href="<c:url value ="/formularios"></c:url>"><i class="glyphicon glyphicon-list-alt"></i>&nbsp;&nbsp;Formulários</a></li>
			</c:if>
			<c:if test="${permissao == 'aluno' || permissao == 'membro' || (permissao == 'alunoLogado')}">
				<li><a data-toggle="tooltip" data-placement="top" title="Veja seu perfil no jogo."
					 href="<c:url value ="/usuario/${usuario.id }/detalhes/${jogo.id}"></c:url>"><i class="fa fa-user"></i>&nbsp;&nbsp;${usuario.nome }</a></li>
			</c:if>
			<!-- FORMULÁRIOS -->
			<c:if test="${permissao == 'professorForm'}">
				<li><a href="<c:url value ="/formularios"></c:url>"><i class="glyphicon glyphicon-list-alt"></i>&nbsp;&nbsp;Formulários</a></li>
				<li class="active"><a href="<c:url value ="/formulario"></c:url>"><i class="glyphicon glyphicon-plus"></i>&nbsp;&nbsp;Criar Formulário</a></li>
			</c:if>
			<!-- VINCULAR PARTICIPANTES JOGO -->
			<c:if test="${(action == 'participantesJogo') &&  (permissao == 'professor')}">
				<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/vincular"></c:url>" data-toggle="tooltip" data-placement="right"
					title="Vincule participantes ao jogo." >
					<i class="fa fa-user-plus"></i>&nbsp;&nbsp;Vincular
					</a>
				</li>
			</c:if>
			<!-- EQUIPES -->
			<c:if test="${(action == 'equipes') && (permissao == 'professor')}">
				<li class="active"><a data-toggle="tooltip" data-placement="top" title="Crie uma nova empresa para o jogo."
					href="<c:url value ="/jogo/${jogo.id}/equipe/nova"></c:url>">
					<i class="glyphicon glyphicon-plus"></i>&nbsp;&nbsp;Nova Empresa</a>
				</li>		
			</c:if>
			<c:if test="${(action == 'detalhesEquipe')}">
				<h4><i class="fa fa-cogs"></i><small><b> OPÇÕES EMPRESA</b></small></h4>
				<c:if test="${ (permissao == 'professor') || (permissao == 'membro') }">
					<li><a data-toggle="tooltip" data-placement="top" title="Veja as avaliações da empresa." 
					href="<c:url value ="/jogo/${jogo.id}/equipe/${equipe.id }/avaliacoes"></c:url>"><i class="fa fa-archive"></i>&nbsp;&nbsp;Avaliações</a></li>
				</c:if>
				<c:if test="${ (permissao == 'professor' || permissao== 'membro') }">
					<li><a data-toggle="tooltip" data-placement="top" title="Veja o histórico de notas e fatores de aposta da empresa." 
					href="<c:url value ="/jogo/${jogo.id}/equipe/${equipe.id }/historico"></c:url>"><i class="fa fa-history"></i>&nbsp;&nbsp;Histórico de Notas</a></li>
				</c:if>
				<c:if test="${ (permissao == 'professor') }">
					<li><a data-toggle="tooltip" data-placement="top" title="Vincule participantes a empresa." 
						href="<c:url value ="/jogo/${jogo.id}/equipe/${equipe.id }/vincular"></c:url>"><i class="fa fa-user-plus"></i>&nbsp;&nbsp;Vincular Membros</a></li>
				</c:if>
			</c:if>
			<c:if test="${(action == 'vincularEquipe')}">
				<h4><i class="fa fa-cogs"></i><small><b>OPÇÕES EMPRESA</b></small></h4>
				<li><a href="<c:url value ="/jogo/${jogo.id}/equipe/${equipe.id }"></c:url>">
					<i class="fa fa-industry"></i>&nbsp;&nbsp;Empresa</a>
				</li>
			</c:if>
			<c:if test="${(action == 'historicoEquipe')}">
				<h4><i class="fa fa-cogs"></i><small><b> OPÇÕES EMPRESA</b></small></h4>
				<li><a href="<c:url value ="/jogo/${jogo.id}/equipe/${equipe.id }"></c:url>">
					<i class="fa fa-industry"></i>&nbsp;&nbsp;Empresa</a>
				</li>
			</c:if>
			<!-- DETALHES USUÁRIO  -->
			<c:if test="${(action == 'detalhesUsuario')}">
				<h4><i class="fa fa-cogs"></i><small><b> OPÇÕES PARTICIPANTES</b></small></h4>
				<c:if test="${ (permissao == 'professor' || permissao== 'alunoLogado') }">
					<li>
						<a data-toggle="tooltip" data-placement="top" title="Avaliações realizadas pelo participante."
							href="<c:url value ="/usuario/${usuarioParticipante.id }/jogo/${jogo.id}/avaliacoes"></c:url>">
							<i class="fa fa-archive"></i>&nbsp;&nbsp;Avaliações
						</a>
					</li>
					<li>
						<a data-toggle="tooltip" data-placement="top" title="Histórico de notas do participante."
							href="<c:url value ="/usuario/${usuarioParticipante.id }/jogo/${jogo.id}/historico"></c:url>">
							<i class="fa fa-history"></i>&nbsp;&nbsp;Histórico de Notas
						</a>
					</li>
					<li>
						<a data-toggle="tooltip" data-placement="right"
							title="Histórico de investimentos." 
							href="<c:url value ="/usuario/${usuarioParticipante.id }/jogo/${jogo.id}/investimentos"></c:url>">
							<i class="fa fa-money"></i>&nbsp;&nbsp;Investimentos
						</a>
					</li>
				</c:if>
				<li><a data-toggle="tooltip" data-placement="top" title="Empresa ${equipe.nome}." 
					href="<c:url value ="/jogo/${jogo.id}/equipe/${equipe.id }"></c:url>">
					<i class="fa fa-industry"></i>&nbsp;&nbsp;Empresa</a>
				</li>
			</c:if>
			<!-- RODADAS -->
			<c:if test="${(action == 'rodadas')}">
				<c:if test="${ (permissao == 'professor') }">
					<li class="active"><a data-toggle="tooltip" data-placement="top" title="Crie uma nova rodada para o jogo." 
						href="<c:url value ="/jogo/${jogo.id}/rodada/nova"></c:url>">
						<i class="glyphicon glyphicon-plus"></i>&nbsp;&nbsp;Nova Rodada</a>
					</li>		
				</c:if>	
			</c:if>
			<c:if test="${(menu == 'servicos')}">
				<h4><i class="fa fa-cogs"></i><small><b> OPÇÕES RODADA</b></small></h4>
				<li class="active"><a data-toggle="tooltip" data-placement="top" title="Rodada ${rodada.nome}." 
					href="<c:url value ="/jogo/${jogo.id}/rodada/${rodada.id}/detalhes"></c:url>">
					<i class="fa fa-youtube-play"></i>&nbsp;&nbsp;Rodada</a>
				</li>
			</c:if>
			<c:if test="${(menu == 'solicitacoes')}">
				<h4><i class="fa fa-cogs"></i><small><b> OPÇÕES RODADA</b></small></h4>
				<li><a data-toggle="tooltip" data-placement="top" title="Empresa ${rodada.nome}." 
					href="<c:url value ="/jogo/${jogo.id}/rodada/${rodada.id}/detalhes"></c:url>">
					<i class="fa fa-youtube-play"></i>&nbsp;&nbsp;Rodada</a>
				</li>
				<li><a data-toggle="tooltip" data-placement="top" title="Consultorias para ${rodada.nome}."
					href="<c:url value ="/jogo/${jogo.id}/rodada/${rodada.id}/servicos"></c:url>">
					<i class="fa fa-shopping-basket"></i>&nbsp;&nbsp;Consultoria</a>
				</li>
			</c:if>
			<c:if test="${(action == 'detalhesRodada')}">
				<h4><i class="fa fa-cogs"></i><small><b> OPÇÕES RODADA</b></small></h4>
				<c:if test="${(permissao == 'professor')||(permissao == 'aluno') }">
					<li><a data-toggle="tooltip" data-placement="top" title="Submissões da rodada ${rodada.nome}." 
						href="<c:url value ="/jogo/${jogo.id}/rodada/${rodada.id}/submissoes"></c:url>">
						<i class="glyphicon glyphicon-open"></i>&nbsp;&nbsp;Submissões</a>
					</li>
					<li><a data-toggle="tooltip" data-placement="top" title="Rankings para a rodada ${rodada.nome}." 
						href="<c:url value ="/jogo/${jogo.id}/rodada/${rodada.id}/rankings"></c:url>">
						<i class="fa fa-line-chart"></i>&nbsp;&nbsp;Rankings</a>
					</li>
					<li><a data-toggle="tooltip" data-placement="top" title="Consultorias para ${rodada.nome}."
						href="<c:url value ="/jogo/${jogo.id}/rodada/${rodada.id}/servicos"></c:url>">
						<i class="fa fa-shopping-basket"></i>&nbsp;&nbsp;Consultoria</a>
					</li>
					<c:if test="${permissao == 'professor'}">
						<li><a data-toggle="tooltip" data-placement="top" title="Investimentos realizados na rodada ${rodada.nome}." 
							href="<c:url value ="/jogo/${jogo.id}/rodada/${rodada.id}/apostas"></c:url>">
							<i class="fa fa-money"></i>&nbsp;&nbsp;Investimentos</a>
						</li>
					</c:if>
				</c:if>
				<c:if test="${ (permissao == 'aluno') && rodada.statusPrazo}">
					<li>
						<a data-toggle="modal" data-target="#squarespaceModal" data-toggle="tooltip" data-placement="top"
						title="Esta funcionalidade permite que você solicite a reabertura desta rodada para sua equipe." 
						href="<c:url value ="/#"></c:url>"><i class="fa fa-cart-plus"></i>&nbsp;&nbsp;Solicitar Reabertura</a>
					</li>
				</c:if>
			</c:if>
			<c:if test="${(action =='submissoes') && ((permissao == 'professor')||(permissao == 'aluno'))}">
				<h4><i class="fa fa-cogs"></i><small><b> OPÇÕES RODADA</b></small></h4>
				<li class="active"><a 
					href="<c:url value ="/jogo/${jogo.id}/rodada/${rodada.id}/detalhes"></c:url>">
					<i class="fa fa-youtube-play"></i>&nbsp;&nbsp;Rodada</a>
				</li>
			</c:if>
			<!-- AVALIACOES -->
			<c:if test="${(action =='avaliacoes') && ((permissao == 'professor')||(permissao == 'aluno'))}">
				<h4><i class="fa fa-cogs"></i><small><b> OPÇÕES EMPRESA</b></small></h4>
				<li><a href="<c:url value ="/jogo/${jogo.id}/equipe/${equipe.id}"></c:url>">
				<i class="fa fa-industry"></i>&nbsp;&nbsp;Empresa</a></li>
			</c:if>
			<c:if test="${(action =='avaliacao') && ((permissao == 'professor')||(permissao == 'aluno'))}">
				<h4><i class="fa fa-cogs"></i><small><b> OPÇÕES EMPRESA</b></small></h4>
				<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/equipe/${equipe.id}/avaliacoes"></c:url>">
					<i class="fa fa-archive"></i>&nbsp;&nbsp;Avaliações</a>
				</li>
				<li><a href="<c:url value ="/jogo/${jogo.id}/equipe/${equipe.id }"></c:url>">
					<i class="fa fa-industry"></i>&nbsp;&nbsp;Empresa</a>
				</li>
			</c:if>
			
			<c:if test="${(action =='avaliacaoDoAluno') && (permissao == 'professor' || permissao== 'alunoLogado')}">
				<h4><i class="fa fa-cogs"></i><small><b> OPÇÕES PARTICIPANTES</b></small></h4>
				<c:if test="${permissao == 'professor'}">	
					<li><a href="<c:url value ="/usuario/${usuarioRequisitado.id }/detalhes/${jogo.id}"></c:url>">
						<i class="fa fa-user"></i>&nbsp;&nbsp;${usuarioRequisitado.nome }</a>
					</li>
				</c:if>
				<li><a href="<c:url value ="/usuario/${usuarioRequisitado.id }/jogo/${jogo.id}/avaliacoes"></c:url>">
					<i class="fa fa-archive"></i>&nbsp;&nbsp;Avaliações</a>
				</li>
			</c:if>
			<!-- HISTORICO ALUNO -->
			<c:if test="${(action =='historico') && (permissao == 'professor')}">
				<h4><i class="fa fa-cogs"></i><small><b> OPÇÕES PARTICIPANTES</b></small></h4>
				<li class="active"><a href="<c:url value ="/usuario/${requisitado.id }/detalhes/${jogo.id}"></c:url>">
					<i class="fa fa-user"></i>&nbsp;&nbsp;${requisitado.nome }</a>
				</li>
			</c:if>
			<li>
				<a href="javascript:history.back();"><i class="glyphicon glyphicon-arrow-left"></i>&nbsp;&nbsp;Voltar</a>
			</li>
		</ul>
	</div>
</div>