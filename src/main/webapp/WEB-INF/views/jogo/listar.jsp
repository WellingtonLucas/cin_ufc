<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<jsp:include page="../fragments/header-estrutura.jsp" />
<title>Jogos</title>

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
	<c:if test="${not empty info}">
		<div class="alert alert-success alert-dismissible" role="alert">
			<button type="button" class="close" data-dismiss="alert">
				<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
			</button>
			<c:out value="${info}"></c:out>
		</div>
	</c:if>
	<div class="section">
		<div class="container-fluid">
			<div class="row">
				<jsp:include page="../fragments/menu.jsp" />
				<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
					<div class="row placeholders">
						<ul class="list-group">
							<%-- <li class="media"><a class="btn btn-primary"
								href="<c:url value ="/jogo/novo-jogo"></c:url>">Novo Jogo</a></li>
							<li class="media"><hr></li> --%>
							<li class="media"><hr></li>
							<c:forEach var="jogo" items="${jogos}">
								<a class="list-group-item"
									href="<c:url value="/jogo/${jogo.id}/detalhes"></c:url>">
									<h4 class="list-group-item-heading">${jogo.nomeDoCurso}</h4>
									<p class="list-group-item-text">${jogo.semestre}</p>
								</a>
								<li class="media"><hr></li>
							</c:forEach>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>	
	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>