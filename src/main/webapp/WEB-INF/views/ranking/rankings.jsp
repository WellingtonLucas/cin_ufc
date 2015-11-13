<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
	<head>
		<jsp:include page="../fragments/header-estrutura.jsp" />
		
		<title>${rodada.nome }</title>
	</head>
	<body>
		<jsp:include page="../fragments/header.jsp" />
		<c:if test="${not empty erro}">
			<div class="alert alert-danger alert-dismissible" role="alert">
				<button type="button" class="close" data-dismiss="alert">
					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>
				<c:out value="${erro}"></c:out>
			</div>
		</c:if>
		<div class="section">
			<div class="container-fluid">
				<div class="row">
					<jsp:include page="../fragments/menu.jsp" />
					<div class="col-sm-8 col-sm-offset-4 col-md-10 col-md-offset-2 main">
						<div class="jumbotron">
					        <h1>Rankings</h1>
					        <p class="lead">Escolha uma das opções abaixo para visualizar os respectivos Rankings.</p>
					        <div>
					        	<a class="btn btn-lg btn-success col-sm-3" href="<c:url value="/jogo/${jogo.id }/ranking/alunos"></c:url>" role="button">Alunos <i class="glyphicon glyphicon-list"></i></a>
					        </div>
					        <div class="col-sm-1"></div>
					        <div>
					        	<a class="btn btn-lg btn-primary col-sm-3" href="<c:url value="/jogo/${jogo.id }/ranking/equipes"></c:url>" role="button">Equipes <i class="glyphicon glyphicon-list"></i></a>
					        </div>
					    </div> <!-- criar mais dois botões -->
					    <c:if test="${jogo.alunos != null}">
					    	<jsp:include page="rankingAlunos.jsp" />
					    </c:if>
				    </div>
				</div>
			</div>
		</div>
	</body>
</html>