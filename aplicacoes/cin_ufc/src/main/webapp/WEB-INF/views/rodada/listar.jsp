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
<title>Rodadas</title>

</head>
<body>
	<jsp:include page="../fragments/header.jsp" />

	<div class="container-fluid">
		<div class="row">
			<jsp:include page="../fragments/menu.jsp" />
			<div class="col-sm-8 col-md-10 main">
				<h2>
					<strong>${jogo.nomeDoCurso }</strong> <small>${jogo.semestre }</small>
				</h2>
				<c:if test="${not empty erro}">
					<div class="alert alert-warning alert-dismissible" role="alert">
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
				<div class="panel panel-primary">
					<div class="panel-heading">
						<i class="fa fa-youtube-play"></i>&nbsp;&nbsp;Rodadas
					</div>
					<div class="panel-body">
						<div class="row placeholders">
							<c:if test="${not empty rodadas }">
								<c:forEach var="rodada" items="${rodadas}">
									<div class="col-lg-4">
										<article><h2>${rodada.nome }</h2></article>
										<p>
											Início: <fmt:formatDate pattern="dd/MM/yyyy" value="${rodada.inicio }" /> - 
											Término: <fmt:formatDate pattern="dd/MM/yyyy" value="${rodada.termino }" />
										</p>
										<c:if test="${!rodada.status}">
											<p>
												<a class="btn btn-primary" role="button" href="<c:url value="rodada/${rodada.id }/detalhes" />">Detalhes »</a>	
											</p>
										</c:if>
										<c:if test="${rodada.status}">
											<p>
												<a class="btn btn-success" role="button" href="<c:url value="rodada/${rodada.id }/detalhes" />">Detalhes »</a>	
											</p>
										</c:if>
									</div>
								</c:forEach>
							</c:if>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>