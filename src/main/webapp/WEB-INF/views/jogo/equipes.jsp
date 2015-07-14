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
<title>Jogadores</title>

</head>
<body>
	<jsp:include page="../fragments/header.jsp" />

	<div class="container-fluid">
		<div class="row">
			<jsp:include page="../fragments/menu.jsp" />
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<div class="col-sm-12">
					<h2>
						<strong>${jogo.nomeDoCurso }</strong> <small>${jogo.semestre }</small>
					</h2>
					<hr>
					<c:if test="${not empty erro}">
						<div class="alert alert-warning alert-dismissible" role="alert">
							<button type="button" class="close" data-dismiss="alert">
								<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
							</button>
							<c:out value="${erro}"></c:out>
						</div>
					</c:if>
				</div>
				<div class="row placeholders">
					<c:if test="${not empty equipes }">
						<c:forEach var="equipe" items="${equipes}">
							<div class="col-xs-6 col-sm-3 placeholder">
								<%-- <img data-src="holder.js/200x200/auto/sky"
									class="img-responsive" alt="Logo da equipe ${equipe.nome }"> --%>
								<a class="btn btn-info" href="<c:url value="equipe/${equipe.id }" />">Detalhes</a>	
								<h4>${equipe.nome }</h4>
								<span class="text-muted">Alguma informação</span>
							</div>
						</c:forEach>
					</c:if>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>