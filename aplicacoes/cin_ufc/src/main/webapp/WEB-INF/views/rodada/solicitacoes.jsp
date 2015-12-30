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
	<title>Solicitações de consultoria da rodada</title>

</head>
<body>
	<jsp:include page="../fragments/header.jsp" />
	<div class="section">
		<div class="container-fluid">
			<div class="row">
				<jsp:include page="../fragments/menu.jsp" />
				<div class="col-sm-8 col-md-10 main">
					<h2><strong>Rodada: ${rodada.nome }</strong></h2>	
					<div class="panel panel-primary">
						<div class="panel-heading">
							<strong>Solicitações de Consultoria</strong>
						</div>
						<div class="panel-body">
							<jsp:include page="../fragments/mensagens.jsp" />
					       	<div class="col-sm-12">
								<table id="tabela-solicitacoes" class="table table-striped table-hover">
									<thead>
										<tr>
											<th>Empresa</th>
											<th>Confirmar</th>
											<th>Solicitação</th>
											<th>Confirmação</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="solicitacao" items="${solicitacoes}" >
											<tr>
												<td>
													<a href="<c:url value="/jogo/${jogo.id}/equipe/${solicitacao.equipe.id }"></c:url>">${solicitacao.equipe.nome}</a>
												</td>
												<td>
													<c:if test="${!solicitacao.status }">
														<a href="<c:url value="/jogo/${jogo.id}/rodada/${rodada.id }/equipe/${solicitacao.equipe.id }/solicitacao/${solicitacao.id }" ></c:url>">
															<button class="btn btn-primary">Confirmar</button>
														</a>
													</c:if>
													<c:if test="${solicitacao.status }">
														<span class="label label-success">Confirmado</span>
													</c:if>
												</td>
												<td>
													<span><fmt:formatDate pattern="dd/MM/yyyy - HH:mm:ss" value="${solicitacao.dia }" /></span>	
												</td>
												<c:if test="${solicitacao.status }">
													<td>
														<span><fmt:formatDate pattern="dd/MM/yyyy - HH:mm:ss" value="${solicitacao.diaConfirmacao }" /></span>	
													</td>
												</c:if>
												<c:if test="${!solicitacao.status }">
													<td><span class="label label-danger">Pendente</span></td>
												</c:if>
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