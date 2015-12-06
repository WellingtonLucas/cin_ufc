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
	<c:if test="${action eq 'vincularEquipeRodada' }">
		<c:set var="url" value="/jogo/rodada/equipes/vincular"></c:set>
	</c:if>
	<title>Submissões da rodada</title>

</head>
<body>
	<jsp:include page="../fragments/header.jsp" />
	
	<div class="section">
		<div class="container-fluid">
			<div class="row">
				<jsp:include page="../fragments/menu.jsp" />
				<div class="col-sm-8 col-sm-offset-3 col-md-10 col-md-offset-2 main">
					<div class="col-sm-12">
						<h2><strong>${jogo.nomeDoCurso }</strong> <small>${jogo.semestre }</small></h2>
						<h2><strong>Rodada: ${rodada.nome }</strong> - <small>Ultimas submissões da rodada</small>
							- Seu saldo é <strong>${aposta.saldo } R$</strong>
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
						<c:if test="${not empty info}">
							<div class="alert alert-success alert-dismissible" role="alert">
								<button type="button" class="close" data-dismiss="alert">
									<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
								</button>
								<c:out value="${info}"></c:out>
							</div>
						</c:if>
					</div>
					<c:if test="${not empty entregas }">
						<div class="col-lg-4">
							<div class="input-group">
					            <input type="search" id="search" value="" class="form-control" placeholder="Pesquisar">
					            <span class="input-group-addon">
					            	<span class="glyphicon glyphicon-search"></span>
					            </span>
					        </div>
				        </div>
				       	<div class="col-sm-12">
							<table id="table" class="table table-striped table-hover">
								<thead>
									<tr>
										<th>Nome da Equipe</th>
										<th>Nome do Documento</th>
										<th>Avalie</th>
										<th>Aposte</th>
										<th>Data de Submissão</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="entrega" items="${entregas}" varStatus="entregaId">
										<tr>
											<td>
												<a href="<c:url value="/jogo/${jogo.id}/equipe/${entrega.equipe.id }"></c:url>">${entrega.equipe.nome}</a>
											</td>
											<td>
												<a href="<c:url value="/documento/downloadDocumento/${entrega.documento.id }"></c:url>">${entrega.documento.nomeOriginal}</a>
											</td>
											<td>
												<c:if test="${!entrega.respondida }">
													<a href="<c:url value="/jogo/${jogo.id}/entrega/${entrega.id }/formulario/${rodada.formulario.id }" ></c:url>">
														<button class="btn btn-primary">Avaliar</button>
													</a>
												</c:if>
												<c:if test="${entrega.respondida }">
													<a href="<c:url value="/jogo/${jogo.id}/entrega/${entrega.id }/formulario/${rodada.formulario.id }" ></c:url>">
														<button class="btn btn-success">Avaliar</button>
													</a>
												</c:if>
											</td>
											<td>
												<a href="<c:url value="/jogo/${jogo.id}/rodada/${rodada.id }/equipe/${entrega.equipe.id }/apostar" ></c:url>">
													<button class="btn btn-primary">Apostar</button>
												</a>
											</td>
											<td>
												<fmt:formatDate pattern="dd/MM/yyyy' - 'HH:mm:ss" value="${entrega.dia }" />
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</c:if>	
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>