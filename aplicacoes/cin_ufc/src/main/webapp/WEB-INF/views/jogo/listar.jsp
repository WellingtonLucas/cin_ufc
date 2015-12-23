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
	
	<div class="section">
		<div class="container-fluid">
			<div class="row">
				<jsp:include page="../fragments/menu.jsp" />
				<div class="col-sm-8 col-sm-offset-4 col-md-10 col-md-offset-2 main">
					<c:if test="${not empty erro}">
						<div class="alert alert-danger alert-dismissible" role="alert">
							<button type="button" class="close" data-dismiss="alert">
								<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
							</button>
							<c:out value="${erro}"></c:out>
						</div>
					</c:if>
					<div class="panel with-nav-tabs panel-primary">
						<div class="panel-heading">
							<ul class="nav nav-tabs">
								<li class="active"><a href="#tab1primary" data-toggle="tab">Jogos Participante</a></li>
								<li><a href="#tab2primary" data-toggle="tab">Jogos Criados</a></li>
							</ul>
						</div>
						<div class="panel-body">
							<div class="tab-content">
								<div class="tab-pane fade in active" id="tab1primary">
									<c:if test="${not empty infoParticipa}">
										<div class="alert alert-success alert-dismissible" role="alert">
											<button type="button" class="close" data-dismiss="alert">
												<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
											</button>
											<c:out value="${infoParticipa}"></c:out>
										</div>
									</c:if>
									<div class="row placeholders">
										<c:forEach var="jogo" items="${jogosParticipa}" varStatus="id">
											<div class="col-sm-3">
												<c:if test="${jogo.imagem == null }">		
													<a href="<c:url value="/jogo/${jogo.id}/detalhes"></c:url>">
														<img class="img-circle"
														src="<c:url value="/resources/imagens/boxvazia.gif" />"
														alt="Imágem vazia" width="200" height="200" >
													</a>
												</c:if>
												<c:if test="${jogo.imagem != null }">
													<a href="<c:url value="/jogo/${jogo.id}/detalhes"></c:url>">
														<img alt="Imagem do jogo" width="200" height="200"
														src="data:${jogo.imagem.extensao };base64,${jogo.imagem.encode }"
														class="img-circle" />
													</a>
												</c:if>
												<div class="caption">
													<h4>${jogo.nomeDoCurso}</h4>
													<p>${jogo.semestre}</p>
													<a class="btn btn-primary" role="button"
														href="<c:url value="/jogo/${jogo.id}/detalhes"></c:url>">
														Detalhes »</a>
												</div>
											</div>
										</c:forEach>
									</div>
								</div>
								<div class="tab-pane fade" id="tab2primary">
									<c:if test="${not empty info}">
										<div class="alert alert-success alert-dismissible" role="alert">
											<button type="button" class="close" data-dismiss="alert">
												<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
											</button>
											<c:out value="${info}"></c:out>
										</div>
									</c:if>
									<div class="row placeholders">
										<c:forEach var="jogo" items="${jogos}" varStatus="id">
											<div class="col-sm-3">		
												<c:if test="${jogo.imagem == null }">
													<a href="<c:url value="/jogo/${jogo.id}/detalhes"></c:url>">
														<img class="img-circle"
														src="<c:url value="/resources/imagens/boxvazia.gif" />"
														alt="" width="200" height="200" >
													</a>
												</c:if>
												<c:if test="${jogo.imagem != null }">
													<a href="<c:url value="/jogo/${jogo.id}/detalhes"></c:url>">
														<img alt="Imagem do jogo" width="200" height="200"
														src="data:${jogo.imagem.extensao };base64,${jogo.imagem.encode }"
														class="img-circle" />
													</a>
												</c:if>
												<div class="caption">
													<h4>${jogo.nomeDoCurso}</h4>
													<p>${jogo.semestre}</p>
													<a class="btn btn-primary" role="button"
														href="<c:url value="/jogo/${jogo.id}/detalhes"></c:url>">
														<c:if test="${!jogo.status}">
															<i class="glyphicon glyphicon-lock"></i>
														</c:if>
														<c:if test="${jogo.status}">
															<i class="fa fa-unlock"></i>
														</c:if>
														Detalhes »</a>
												</div>
											</div>
										</c:forEach>
									</div>										
								</div>
							</div>
						</div>
					</div>

				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>