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
	<title>Investimentos</title>
</head>
<body>
	<jsp:include page="../fragments/header.jsp" />
	
	<div class="section">
		<div class="container-fluid">
			<div class="row">
				<jsp:include page="../fragments/menu.jsp" />
				<div class="col-sm-8 col-md-10 main">
					<h2><strong>${rodada.nome }</strong> - Histórico de Investimentos</h2>
					<div class="panel panel-primary">
						<div class="panel-heading">
							<strong>Investimentos</strong> 
						</div>
						<div class="panel-body">
							<jsp:include page="../fragments/mensagens.jsp" />
							<div class="col-sm-12">
								<div class="table-responsive">
									<table id="tabela-apostas" class="table table-striped table-hover">
										<thead>
											<tr>
												<th>Nome</th>
												<th>Equipe</th>
												<th>Quantia</th>
												<th>Dia - Hora</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach var="aposta" items="${apostas}">
												<c:forEach var="deposito" items="${aposta.depositos}">
													<tr>
														<td>
															<a href="<c:url value="/usuario/${aposta.apostador.id}/detalhes/${jogo.id }"></c:url>">${aposta.apostador.nome} ${aposta.apostador.sobreNome}</a>
														</td>
														<td>
															<a href="<c:url value="/jogo/${jogo.id}/equipe/${deposito.equipe.id }"></c:url>">${deposito.equipe.nome }</a> 
														</td>
														<td>R$ <fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${deposito.quantia }" /></td>
														<td>
															<c:if test="${empty deposito.dia}">-</c:if>
															<fmt:formatDate pattern="dd/MM/yyyy' - 'HH:mm:ss" value="${deposito.dia }" />
														</td>
													</tr>
												</c:forEach>
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
	</div>
	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>