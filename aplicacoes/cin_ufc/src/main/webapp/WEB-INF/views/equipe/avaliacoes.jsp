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
	<title>Avaliações</title>

</head>
<body>
	<jsp:include page="../fragments/header.jsp" />
	
	<div class="section">
		<div class="container-fluid">
			<div class="row">
				<jsp:include page="../fragments/menu.jsp" />
				<div class="col-sm-8 col-md-10 main">
					<h2><strong>${jogo.nomeDoCurso }</strong> <small>${jogo.semestre }</small></h2>
					<h2><strong>Equipe: ${equipe.nome }</strong> - <small>Avaliações da Empresa</small></h2>
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
							Avaliações da Empresa
						</div>
						<div class="panel-body">
					       	<div class="col-sm-12">
								<table id="tabela-avaliacoes" class="table table-striped table-hover">
									<thead>
										<tr>
											<th>Rodada</th>
											<th>Entrega</th>
											<th>Avaliação</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="entrega" items="${entregas}" varStatus="entregaId">
											<tr>
												<td>
													<a href="<c:url value="/jogo/${jogo.id}/rodada/${entrega.rodada.id }/detalhes"></c:url>">${entrega.rodada.nome}</a>
												</td>
												<td>
													<a href="<c:url value="/documento/downloadDocumento/${entrega.documento.id }"></c:url>">${entrega.documento.nomeOriginal}</a>
												</td>
												<td>
													<a href="<c:url value="/jogo/${jogo.id}/equipe/${equipe.id }/entrega/${entrega.id }/formulario/${entrega.gabarito.formulario.id }/avaliacao" ></c:url>">
														<button class="btn btn-primary">Ver avaliação</button>
													</a>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
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