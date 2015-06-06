<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="page-header">
	<h1>
		CIN <small>Concurso de Ideias de Negócio</small>
	</h1>
</div>
<div class="navbar navbar-default navbar-static-top">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#navbar-ex-collapse">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			
		</div>
		<div class="collapse navbar-collapse" id="navbar-ex-collapse">
			<ul class="nav navbar-nav navbar-right">				
				<li><a  class="glyphicon glyphicon-home btn-lg" href="<c:url value="/jogo/listar" />"></a></li>				
				<li><a  class="glyphicon glyphicon-log-out btn-lg" href="<c:url value="/j_spring_security_logout" />"></a></li>
			</ul>
			<p class="navbar-right navbar-text">
				Bem Vindo ${usuario.nome}! <br>
			</p>
		</div>
	</div>
</div>
