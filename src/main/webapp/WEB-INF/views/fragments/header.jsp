<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%-- <div class="page-header">
	<div class="row">
		<div class="col-md-6">
			<img width="370"
				src="<c:url value="/resources/imagens/brasao-qxd.png" />"
				alt="Brasão UFC Quixadá">
		</div>
		<div class="col-md-6"></div>
	</div>

</div> --%>
<div class="navbar navbar-default navbar-fixed-top">
	<div class="container-fluid">
		<div class="navbar-header">			
			<a class="navbar-brand" href="<c:url value="/jogo/listar" />">				
					<strong>CIN</strong> <small>Concurso de Ideias de Negócio</small>				
			</a>			
		</div>
		<div id="navbar" class="navbar-collapse collapse">
			<ul class="nav navbar-nav navbar-right">			
				<li><a class="glyphicon glyphicon-home btn-lg" data-tooggle="tooltip"  data-placement="bottom"
				title="Principal" href="<c:url value="/jogo/listar" />"></a></li>
				<li><a class="glyphicon glyphicon-log-out btn-lg" data-tooggle="tooltip"  data-placement="bottom"
				title="Sair" href="<c:url value="/j_spring_security_logout" />"></a></li>
			</ul>
			<p class="navbar-right navbar-text">
				Bem Vindo ${usuario.nome}!<br>
			</p>

		</div>
	</div>
</div>
