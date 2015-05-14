<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="page-header">
	<h1>
		CIN <small>Concurso de Ideias de Negócio</small>
	</h1>
</div>
<div align="right">Olá, docente!</div>
<nav class="navbar navbar-default" role="navigation">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#bs-example-navbar-collapse-1">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="<c:url value='/'/>">Início</a>
		</div>

		<div class="collapse navbar-collapse"
			id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">							
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown">Curso<b class="caret"></b></a>									
			</ul>


			<ul class="nav navbar-nav navbar-right">
				<li><a href="<c:url value="#" />">Sair<span class="glyphicon glyphicon-off"></span></a></li>
			</ul>
		</div>
	</div>
</nav>
