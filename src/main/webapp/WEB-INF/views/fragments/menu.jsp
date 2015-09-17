<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="col-sm-3 col-md-2 sidebar">
	<ul class="nav nav-sidebar">
		<c:if test="${action == 'home'}">
			<li class="active"><a href="<c:url value ="/jogo/novo-jogo"></c:url>">Novo Jogo</a></li>
		</c:if>
		<c:if test="${(permissao == 'professor')||(permissao == 'aluno') || (permissao == 'jogador')}">
			<li><a href="<c:url value ="/jogo/${jogo.id}/detalhes"></c:url>">${jogo.nomeDoCurso }</a></li>
			<li><a href="<c:url value ="/jogo/${jogo.id}/equipes"></c:url>">Equipes&nbsp;</a></li>
			<li><a href="<c:url value ="/jogo/${jogo.id}/rodadas"></c:url>">Rodadas</a></li>
			<hr>
		</c:if>
		<c:if test="${permissao == 'professor'}">
			<li><a href="<c:url value ="/jogo/${jogo.id}/participantes"></c:url>">Participantes do Jogo</a></li>
			<li><a href="<c:url value ="/jogo/${jogo.id}/formularios"></c:url>">Formulários&nbsp;</a></li>
		</c:if>
		
		<c:if test="${(action == 'participantesJogo')}">
			<c:if test="${ (permissao == 'professor') }">
				<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/vincular"></c:url>">Vincular Participantes</a></li>
			</c:if>
		</c:if>
		<c:if test="${(action == 'equipes') && (permissao == 'professor')}">
			<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/equipe/nova"></c:url>">Nova Equipe</a></li>		
		</c:if>
		<c:if test="${(action == 'vincularAoJogo') && (permissao == 'professor')}">
			<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/participantes"></c:url>">Participantes</a></li>
		</c:if>
		<c:if test="${(action == 'detalhesEquipe')}">
			<c:if test="${ (permissao == 'professor') || (permissao == 'aluno') }">
				<li><a href="<c:url value ="/jogo/${jogo.id}/equipe/${equipe.id }/avaliacoes"></c:url>">Avaliações</a></li>
			</c:if>
			<c:if test="${ (permissao == 'professor') }">
				<li><a href="<c:url value ="/jogo/${jogo.id}/equipe/${equipe.id }/vincular"></c:url>">Vincular Membros</a></li>
			</c:if>
		</c:if>
		<c:if test="${(action == 'vincularEquipe')}">
			<li><a href="<c:url value ="/jogo/${jogo.id}/detalhes"></c:url>">${jogo.nomeDoCurso }</a></li>
			<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/equipe/${equipe.id }"></c:url>">${equipe.nome }</a></li>
		</c:if>
		<c:if test="${(action == 'formularios') && (permissao == 'professor')}">
			<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/formulario"></c:url>">Criar questionário</a></li>
		</c:if>
		<c:if test="${permissao== 'professor' && ((action == 'detalhesFormulario') || (action == 'responder'))}">
			<li><a href="<c:url value ="/jogo/${jogo.id}/formulario"></c:url>">Criar questionário</a></li>
		</c:if>
		<c:if test="${(action == 'detalhesUsuario')}">
			<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/equipe/${equipe.id }"></c:url>">${equipe.nome }</a></li>
		</c:if>
		<c:if test="${(action == 'rodadas')}">
			<c:if test="${ (permissao == 'professor') }">
				<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/rodada/nova"></c:url>">Nova Rodada</a></li>		
			</c:if>	
		</c:if>
		<c:if test="${(action == 'detalhesRodada')}">
			<c:if test="${ (permissao == 'professor') }">
				<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/rodada/${rodada.id}/vincular"></c:url>">Vincular Equipes</a></li>		
				<li><a href="<c:url value ="/jogo/${jogo.id}/rodada/${rodada.id}/vincularFormulario"></c:url>">Vincular Formulario</a></li>
				<hr>
			</c:if>	
			<c:if test="${(permissao == 'professor')||(permissao == 'aluno') }">
				<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/rodada/${rodada.id}/submissoes"></c:url>">Submissões da rodada</a></li>
			</c:if>
		</c:if>
		
		<c:if test="${(action =='vincularEquipeRodada') && (permissao == 'professor')}">
			<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/rodada/${rodada.id}/detalhes"></c:url>">${rodada.nome }</a></li>
			<li><a href="<c:url value ="/jogo/${jogo.id}/rodadas"></c:url>">Rodadas</a></li>
		</c:if>
		
		<c:if test="${(action =='vincularFormularioRodada') && (permissao == 'professor')}">
			<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/rodada/${rodada.id}/detalhes"></c:url>">${rodada.nome }</a></li>
			<li><a href="<c:url value ="/jogo/${jogo.id}/rodadas"></c:url>">Rodadas</a></li>
			<hr>
			<li><a href="<c:url value ="/jogo/${jogo.id}/formularios"></c:url>">Formulários&nbsp;</a></li>
		</c:if>
		<c:if test="${(action =='submissoes') && ((permissao == 'professor')||(permissao == 'aluno'))}">
			<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/rodada/${rodada.id}/detalhes"></c:url>">${rodada.nome }</a></li>
		</c:if>
		<c:if test="${(action =='avaliacoes') && ((permissao == 'professor')||(permissao == 'aluno'))}">
			<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/equipe/${equipe.id}"></c:url>">${equipe.nome}</a></li>
		</c:if>
		<c:if test="${(action =='avaliacao') && ((permissao == 'professor')||(permissao == 'aluno'))}">
			<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/equipe/${equipe.id}/avaliacoes"></c:url>">Avaliações</a></li>
		</c:if>
	</ul>
</div>