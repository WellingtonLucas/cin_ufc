<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="col-sm-3 col-md-2 sidebar">
	<ul class="nav nav-sidebar">
		<c:if test="${action == 'home' || action == 'perfil' || action == 'profile'}">
			<li class="active"><a href="<c:url value ="/jogo/novo-jogo"></c:url>"><i class="glyphicon glyphicon-plus"></i>&nbsp;&nbsp;Novo Jogo</a></li>
			<li><a href="<c:url value ="/formularios"></c:url>"><i class="glyphicon glyphicon-list-alt"></i>&nbsp;&nbsp;Formulários</a></li>
		</c:if>
		<!-- OPCOES DO JOGO -->
		<c:if test="${(permissao == 'professor')||(permissao == 'aluno') ||(permissao == 'alunoLogado') || (permissao == 'jogador')}">
			<li><a href="<c:url value ="/jogo/${jogo.id}/detalhes"></c:url>"><i class="glyphicon glyphicon-education"></i>&nbsp;&nbsp;Home Jogo</a></li>
			<li><a href="<c:url value ="/jogo/${jogo.id}/equipes"></c:url>"><i class="fa fa-industry"></i>&nbsp;&nbsp;Empresas</a></li>
			<li><a href="<c:url value ="/jogo/${jogo.id}/rodadas"></c:url>"><i class="fa fa-youtube-play"></i>&nbsp;&nbsp;Rodadas</a></li>
			<li><a href="<c:url value ="/jogo/${jogo.id}/rankings"></c:url>"><i class="glyphicon glyphicon-list"></i>&nbsp;&nbsp;Rankings Gerais</a></li>
			<hr>
		</c:if>
		<!-- OPCOES DO JOGO PROFESSOR-->
		<c:if test="${permissao == 'professor'}">
			<li><a href="<c:url value ="/jogo/${jogo.id}/participantes"></c:url>"><i class="fa fa-users"></i>&nbsp;&nbsp;Participantes do Jogo</a></li>
			<li><a href="<c:url value ="/formularios"></c:url>"><i class="glyphicon glyphicon-list-alt"></i>&nbsp;&nbsp;Formulários</a></li>
			<hr>
		</c:if>
		<c:if test="${permissao == 'aluno'}">
			<li><a href="<c:url value ="/usuario/${usuario.id }/detalhes/${jogo.id}"></c:url>"><i class="fa fa-user"></i>&nbsp;&nbsp;${usuario.nome }</a></li>
		</c:if>
		<!-- FORMULÁRIOS -->
		<c:if test="${permissao == 'professorForm'}">
			<li><a href="<c:url value ="/formularios"></c:url>"><i class="glyphicon glyphicon-list-alt"></i>&nbsp;&nbsp;Formulários</a></li>
		</c:if>
		<c:if test="${(action == 'formularios') && (permissao == 'professorForm')}">
			<li class="active"><a href="<c:url value ="/formulario"></c:url>"><i class="glyphicon glyphicon-plus"></i>&nbsp;&nbsp;Criar Formulário</a></li>
			<li>
				<a href="javascript:history.back();"><i class="glyphicon glyphicon-arrow-left"></i>&nbsp;&nbsp;Voltar</a>
			</li>
				
		</c:if>
		<c:if test="${permissao== 'professorForm' && ((action == 'detalhesFormulario') || (action == 'responder'))}">
			<li class="active"><a href="<c:url value ="/formulario"></c:url>"><i class="glyphicon glyphicon-plus"></i>&nbsp;&nbsp;Criar Formulário</a></li>
		</c:if>
		<c:if test="${action == 'responder' }">
			<li class="active"><a href="javascript:history.back();"><i class="glyphicon glyphicon-arrow-left"></i>&nbsp;&nbsp;Voltar</a></li>
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
			<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/equipe/nova"></c:url>"><i class="glyphicon glyphicon-plus"></i>&nbsp;&nbsp;Nova Empresa</a></li>		
		</c:if>
		
		<c:if test="${(action == 'detalhesEquipe')}">
			<c:if test="${ (permissao == 'professor') || (permissao == 'aluno') }">
				<li><a href="<c:url value ="/jogo/${jogo.id}/equipe/${equipe.id }/avaliacoes"></c:url>"><i class="fa fa-archive"></i>&nbsp;&nbsp;Avaliações</a></li>
			</c:if>
			<c:if test="${ (permissao == 'professor') }">
				<li><a href="<c:url value ="/jogo/${jogo.id}/equipe/${equipe.id }/vincular"></c:url>"><i class="fa fa-user-plus"></i>&nbsp;&nbsp;Vincular Membros</a></li>
			</c:if>
			<c:if test="${ (permissao == 'professor' || permissao== 'alunoLogado') }">
				<li><a href="<c:url value ="/jogo/${jogo.id}/equipe/${equipe.id }/historico"></c:url>"><i class="fa fa-history"></i>&nbsp;&nbsp;Histórico de Notas</a></li>
			</c:if>
		</c:if>
		<c:if test="${(action == 'vincularEquipe')}">
			<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/equipe/${equipe.id }"></c:url>"><i class="fa fa-industry"></i>&nbsp;&nbsp;${equipe.nome }</a></li>
		</c:if>
		<!-- DETALHES USUÁRIO  -->
		<c:if test="${(action == 'detalhesUsuario')}">
			<c:if test="${ (permissao == 'professor' || permissao== 'alunoLogado') }">
				<li>
					<a href="<c:url value ="/usuario/${usuarioParticipante.id }/jogo/${jogo.id}/avaliacoes"></c:url>">
					<i class="fa fa-archive"></i>&nbsp;&nbsp;Avaliações Realizadas
					</a>
				</li>
				<li>
					<a href="<c:url value ="/usuario/${usuarioParticipante.id }/jogo/${jogo.id}/historico"></c:url>"><i class="fa fa-history"></i>&nbsp;&nbsp;Histórico de Notas</a>
				</li>
				<li>
					<a data-toggle="tooltip" data-placement="right"
					title="Histórico de investimentos." 
					href="<c:url value ="/usuario/${usuarioParticipante.id }/jogo/${jogo.id}/investimentos"></c:url>">
					<i class="fa fa-money"></i>&nbsp;&nbsp;Investimentos
					</a>
				</li>
			</c:if>
			<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/equipe/${equipe.id }"></c:url>"><i class="fa fa-industry"></i>&nbsp;&nbsp;${equipe.nome }</a></li>
		</c:if>
		<!-- RODADAS -->
		<c:if test="${(action == 'rodadas')}">
			<c:if test="${ (permissao == 'professor') }">
				<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/rodada/nova"></c:url>"><i class="glyphicon glyphicon-plus"></i>&nbsp;&nbsp;Nova Rodada</a></li>		
			</c:if>	
		</c:if>
		<c:if test="${(action == 'detalhesRodada')}">
			<c:if test="${ (permissao == 'aluno') && rodada.statusPrazo}">
				<li>
					<a data-toggle="modal" data-target="#squarespaceModal" data-toggle="tooltip" data-placement="right"
					title="Esta funcionalidade permite que você solicite a reabertura desta rodada para sua equipe." 
					href="<c:url value ="/#"></c:url>"><i class="fa fa-cart-plus"></i>&nbsp;&nbsp;Solicitar Reabertura</a>
				</li>
			</c:if>	
			<c:if test="${(permissao == 'professor')||(permissao == 'aluno') }">
				<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/rodada/${rodada.id}/submissoes"></c:url>"><i class="glyphicon glyphicon-open"></i>&nbsp;&nbsp;Submissões da rodada</a></li>
				<li><a href="<c:url value ="/jogo/${jogo.id}/rodada/${rodada.id}/rankings"></c:url>"><i class="glyphicon glyphicon-list"></i>&nbsp;&nbsp;Rankings</a></li>
				<c:if test="${permissao == 'professor'}">
					<li><a href="<c:url value ="/jogo/${jogo.id}/rodada/${rodada.id}/apostas"></c:url>"><i class="fa fa-money"></i>&nbsp;&nbsp;Investimentos</a></li>
				</c:if>
			</c:if>
		</c:if>
		<c:if test="${(action =='submissoes') && ((permissao == 'professor')||(permissao == 'aluno'))}">
			<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/rodada/${rodada.id}/detalhes"></c:url>"><i class="glyphicon glyphicon-arrow-left"></i>&nbsp;&nbsp;Voltar</a></li>
		</c:if>
		<!-- AVALIACOES -->
		<c:if test="${(action =='avaliacoes') && ((permissao == 'professor')||(permissao == 'aluno'))}">
			<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/equipe/${equipe.id}"></c:url>"><i class="fa fa-industry"></i>&nbsp;&nbsp;${equipe.nome}</a></li>
		</c:if>
		<c:if test="${(action =='avaliacao') && ((permissao == 'professor')||(permissao == 'aluno'))}">
			<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/equipe/${equipe.id}/avaliacoes"></c:url>">
				<i class="fa fa-archive"></i>&nbsp;&nbsp;Avaliações</a>
			</li>
			<li><a href="<c:url value ="/jogo/${jogo.id}/equipe/${equipe.id }"></c:url>"><i class="fa fa-industry"></i>&nbsp;&nbsp;${equipe.nome }</a></li>
		</c:if>
		
		<c:if test="${(action =='avaliacaoDoAluno') && (permissao == 'professor' || permissao== 'alunoLogado')}">
			<li><a href="<c:url value ="/usuario/${usuarioRequisitado.id }/detalhes/${jogo.id}"></c:url>"><i class="fa fa-user"></i>&nbsp;&nbsp;${usuarioRequisitado.nome }</a></li>
			<li class="active"><a href="<c:url value ="/usuario/${usuarioRequisitado.id }/jogo/${jogo.id}/avaliacoes"></c:url>">
				<i class="fa fa-archive"></i>&nbsp;&nbsp;Avaliações</a>
			</li>
		</c:if>
		<c:if test="${(action =='avaliacoesDoAluno') && (permissao == 'professor' || permissao== 'alunoLogado')}">
			<li class="active"><a href="<c:url value ="/usuario/${usuarioRequisitado.id }/detalhes/${jogo.id}"></c:url>"><i class="fa fa-user"></i>&nbsp;&nbsp;${usuarioRequisitado.nome }</a></li>
		</c:if>
		<!-- HISTORICO ALUNO -->
		<c:if test="${(action =='historico') && (permissao == 'professor' || permissao== 'alunoLogado')}">
			<li class="active"><a href="<c:url value ="/usuario/${requisitado.id }/detalhes/${jogo.id}"></c:url>"><i class="fa fa-user"></i>&nbsp;&nbsp;${requisitado.nome }</a></li>
		</c:if>
		<!-- RANKING -->
		<c:if test="${rankingJogo == true && (permissao == 'professor' || permissao == 'aluno')}">
			<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/rodada/${rodada.id}/detalhes"></c:url>">${rodada.nome }</a></li>
		</c:if>
	</ul>
</div>