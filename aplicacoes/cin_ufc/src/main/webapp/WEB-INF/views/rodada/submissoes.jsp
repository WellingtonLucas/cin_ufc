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
				<div class="col-sm-8 col-md-10 main">
					<h2><strong>Rodada: ${rodada.nome }</strong> - <small>Últimas submissões da rodada</small>
						<c:if test="${permissao != 'professor' }">- Seu saldo é <strong>R$ <fmt:formatNumber currencyCode="BRL" value="${aposta.saldo }" /></strong></c:if>.
					</h2>
					<div class="panel panel-primary">
						<div class="panel-heading">
							<h4><strong>Últimas Submissões</strong> </h4>
						</div>
						<div class="panel-body">
							<jsp:include page="../fragments/mensagens.jsp" />
							<c:if test="${not empty entregas }">
						       	<div class="col-sm-12">
									<table id="tabela-submissoes" class="table table-striped table-hover">
										<thead>
											<tr>
												<th>Empresa</th>
												<th>Documento</th>
												<th>Data de Submissão</th>
												<th>Avalie</th>
												<c:if test="${permissao != 'professor' }">
													<th>Aposte</th>
												</c:if>
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
														<fmt:formatDate pattern="dd/MM/yyyy' - 'HH:mm:ss" value="${entrega.dia }" />
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
													<c:if test="${permissao != 'professor' }">
														<td>
															<a href="<c:url value="/jogo/${jogo.id}/rodada/${rodada.id }/equipe/${entrega.equipe.id }/apostar" ></c:url>">
																<button class="btn btn-primary">Apostar</button>
															</a>
														</td>
													</c:if>
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
		</div>
	</div>
	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>