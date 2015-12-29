<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="navbar navbar-default navbar-fixed-top">
	<div class="container-fluid">
		<div class="navbar-header" data-tooggle="tooltip"  data-placement="bottom" title="Página Inicial">			
			<a  
				class="navbar-brand" href="<c:url value="/jogo/listar" />">
				<strong>CIN</strong> <small>Concurso de Idéias de Negócio</small>
			</a>			
		</div>
		<div id="navbar" class="navbar-collapse collapse">
			<ul class="nav navbar-nav navbar-right">			
				<li><a class="glyphicon glyphicon-home btn-lg" data-toggle="tooltip" data-placement="bottom"
				title="Página Inicial" href="<c:url value="/jogo/listar" />"></a></li>
				<li class="dropdown" data-tooggle="tooltip"  data-placement="bottom" title="Suas opções">
	              <a href="#" class="dropdown-toggle glyphicon glyphicon-menu-down btn-lg" data-toggle="dropdown" role="button" 
	              aria-haspopup="true" aria-expanded="false"></a>
	              <ul class="dropdown-menu">
	              	<li><a class="glyphicon glyphicon-user" href="<c:url value="/usuario/profile" />">&nbsp;Perfil</a></li>
	                <li><a class="glyphicon glyphicon-edit" href="<c:url value="/usuario/perfil" />">&nbsp;Editar&nbsp;Perfil</a></li>
	                <li role="separator" class="divider"></li>
	                <!-- <li class="dropdown-header">Nav header</li> -->
	                <li>
	                	<a class="glyphicon glyphicon-log-out btn-lg" data-tooggle="tooltip"  data-placement="bottom"
							title="Sair" href="<c:url value="/j_spring_security_logout" />">
							Sair
						</a>
					</li>
					<li></li>
	              </ul>
	            </li>
			</ul>
			<p class="navbar-right navbar-text">
				Olá ${sessionScope.usuario.nome}!<br>
			</p>

		</div>
	</div>
</div>
