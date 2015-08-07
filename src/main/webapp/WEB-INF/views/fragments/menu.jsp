<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="col-sm-3 col-md-2 sidebar">
	<ul class="nav nav-sidebar">
		<c:if test="${not empty action }">
			<li><a href="<c:url value ="/jogo/novo-jogo"></c:url>">Novo Jogo</a></li>
			<c:if test="${action eq 'equipes'}">
				<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/equipe/nova"></c:url>">Nova Equipe</a></li>
			</c:if>
			<hr>
			<c:if test="${action != 'home'}">
				<c:if test="${action != 'detalhesUsuario'}">
					<c:if test="${action eq 'participantes'}">
						<li class="active"><a href="<c:url value ="/jogo/${jogo.id}/detalhes"></c:url>">${jogo.nomeDoCurso }</a></li>
					</c:if>
					<c:if test="${action != 'participantes'}">
						<li><a href="<c:url value ="/jogo/${jogo.id}/participantes"></c:url>">Participantes</a></li>
					</c:if>
					<c:if test="${action != 'equipes'}">
						<li><a href="<c:url value ="/jogo/${jogo.id}/equipes"></c:url>">Equipes&nbsp;</a></li>
					</c:if>
					<c:if test="${action eq 'equipes'}">
						<li><a href="<c:url value ="/jogo/${jogo.id}/detalhes"></c:url>">${jogo.nomeDoCurso }</a></li>
					</c:if>
					<c:if test="${action != 'formularios'}">
						<li><a href="<c:url value ="/jogo/${jogo.id}/formularios"></c:url>">Formulários&nbsp;</a></li>
					</c:if>
					<c:if test="${action eq 'formularios'}">
						<li><a href="<c:url value ="/jogo/${jogo.id}/detalhes"></c:url>">${jogo.nomeDoCurso }</a></li>
					</c:if>
					<hr>
					<li><a href="<c:url value ="/jogo/${jogo.id}/formulario"></c:url>">Criar questionário</a></li>
				</c:if>
				<c:if test="${action == 'detalhesUsuario'}">
					<li><a href="<c:url value ="/jogo/${jogo.id}/participantes"></c:url>">Participantes</a></li>
					<li><a href="<c:url value ="/jogo/${jogo.id}/equipes"></c:url>">Equipes&nbsp;</a></li>
					<li><a href="<c:url value ="/jogo/${jogo.id}/detalhes"></c:url>">${jogo.nomeDoCurso }</a></li>
					<hr>
					<li><a href="<c:url value ="/jogo/${jogo.id}/formulario"></c:url>">Criar questionário</a></li>
				</c:if>
				<li><a href="<c:url value ="#"></c:url>">Rankings</a></li>
				<li><a href="<c:url value ="#"></c:url>">Avaliações</a></li>
				<li><a href="<c:url value ="#"></c:url>">Rodadas</a></li>
				<li><a href="<c:url value ="#"></c:url>">Apostas</a></li>
			</c:if>
		</c:if>
	</ul>
</div>