<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

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
				<li class="dropdown">
	              <a href="#" class="dropdown-toggle glyphicon glyphicon-menu-down btn-lg" data-toggle="dropdown" role="button" 
	              aria-haspopup="true" aria-expanded="false"></a>
	              <ul class="dropdown-menu">
	                <li><a href="<c:url value="/usuario/perfil" />">Perfil</a></li>
	                <li><a href="#">Another action</a></li>
	                <li><a href="#">Something else here</a></li>
	                <li role="separator" class="divider"></li>
	                <li class="dropdown-header">Nav header</li>
	                <li><a href="#">Separated link</a></li>
	                <li><a href="#">One more separated link</a></li>
	              </ul>
	            </li>
				<li><a class="glyphicon glyphicon-log-out btn-lg" data-tooggle="tooltip"  data-placement="bottom"
				title="Sair" href="<c:url value="/j_spring_security_logout" />"></a></li>
			</ul>
			<p class="navbar-right navbar-text">
				Bem Vindo ${usuario.nome}!<br>
			</p>

		</div>
	</div>
</div>
