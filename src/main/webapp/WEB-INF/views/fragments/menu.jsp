<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="col-sm-3 col-md-2 sidebar">
	<ul class="nav nav-sidebar">
		<c:if test="${action == 'home'}">
			<li class="active"><a href="<c:url value ="/jogo/novo-jogo"></c:url>">Novo Jogo</a></li>
		</c:if>
		<c:if test="${(action == 'detalhesJogo')}">
			<li><a href="<c:url value ="/jogo/${jogo.id}/participantes"></c:url>">Participantes</a></li>
			<li><a href="<c:url value ="/jogo/${jogo.id}/equipes"></c:url>">Equipes&nbsp;</a></li>
		</c:if>
		<c:if test="${(action == 'detalhesJogo') && (permissao == 'professor')}">
			<li><a href="<c:url value ="/jogo/${jogo.id}/formularios"></c:url>">Formulários&nbsp;</a></li>
		</c:if>
		<c:if test="${(action == 'participantesJogo')}">
			<li><a href="<c:url value ="/jogo/${jogo.id}/detalhes"></c:url>">${jogo.nomeDoCurso }</a></li>
			<c:if test="${ (permissao == 'professor') }">
				<li><a href="<c:url value ="/jogo/${jogo.id}/vincular"></c:url>">Vincular Participantes</a></li>
			</c:if>
		</c:if>
		<c:if test="${(action == 'equipes')}">
			<li><a href="<c:url value ="/jogo/${jogo.id}/detalhes"></c:url>">${jogo.nomeDoCurso }</a></li>
			<c:if test="${ (permissao == 'professor') }">
				<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/equipe/nova"></c:url>">Nova Equipe</a></li>		
			</c:if>	
		</c:if>
		<c:if test="${(action == 'vincularAoJogo')}">
			<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/detalhes"></c:url>">${jogo.nomeDoCurso }</a></li>
		</c:if>
		<c:if test="${(action == 'detalhesEquipe')}">
			<li><a href="<c:url value ="/jogo/${jogo.id}/detalhes"></c:url>">${jogo.nomeDoCurso }</a></li>
			<li><a href="<c:url value ="/jogo/${jogo.id}/equipes"></c:url>">Equipes&nbsp;</a></li>	
			<c:if test="${ (permissao == 'professor') }">
				<li><a href="<c:url value ="/jogo/${jogo.id}/equipe/${equipe.id }/vincular"></c:url>">Vincular Membros</a></li>
			</c:if>
		</c:if>
		<c:if test="${(action == 'vincularEquipe')}">
			<li><a href="<c:url value ="/jogo/${jogo.id}/detalhes"></c:url>">${jogo.nomeDoCurso }</a></li>
			<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/equipe/${equipe.id }"></c:url>">${equipe.nome }</a></li>
		</c:if>
		<c:if test="${(action == 'formularios') && (permissao == 'professor')}">
			<li><a href="<c:url value ="/jogo/${jogo.id}/detalhes"></c:url>">${jogo.nomeDoCurso }</a></li>
			<li><a href="<c:url value ="/jogo/${jogo.id}/formulario"></c:url>">Criar questionário</a></li>
		</c:if>
		<c:if test="${(action == 'detalhesFormulario') || (action == 'responder')}">
			<li><a href="<c:url value ="/jogo/${jogo.id}/detalhes"></c:url>">${jogo.nomeDoCurso }</a></li>
			<li><a href="<c:url value ="/jogo/${jogo.id}/formularios"></c:url>">Formulários&nbsp;</a></li>
			<li><a href="<c:url value ="/jogo/${jogo.id}/formulario"></c:url>">Criar questionário</a></li>
		</c:if>
		<li><a href="<c:url value ="/usuario/${usuario.id}/submissoes"></c:url>">Submissões</a></li>
	</ul>
</div>