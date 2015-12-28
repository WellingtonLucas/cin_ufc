<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<html>
	<head>
		<jsp:include page="../fragments/header-estrutura.jsp" />
		<title>Histórico ${equipe.nome }</title>
	</head>
<body>
	<jsp:include page="../fragments/header.jsp" />
	
	<div class="container-fluid">
		<div class="row">
			<jsp:include page="../fragments/menu.jsp" />
			<div class="col-sm-8 col-md-10 main">
				<div class="col-sm-12">
					<h2>
						<strong>Histórico da empresa </strong><small>${equipe.nome } </small>
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
					<div class="panel-group" id="accordion" role="tablist"
						aria-multiselectable="true">
						<div class="panel panel-primary">
							<div class="panel-heading" role="tab" id="headingOne">
								<h4 class="panel-title">
									<a role="button" data-toggle="collapse" data-parent="#accordion"
										href="#collapseOne" aria-expanded="true"
										aria-controls="collapseOne"> <strong>Notas da Empresa</strong> </a>
								</h4>
							</div>
							<div id="collapseOne" class="panel-collapse collapse in"
								role="tabpanel" aria-labelledby="headingOne">
								<div class="panel-body">
									<c:if test="${notasEquipeRodadas != null }">
										<table id="tabela-notas-equipe" class="table table-striped table-hover">
											<thead>
												<tr>
													<td><strong>Rodada </strong></td>
													<td><strong>Fator de aposta </strong></td>
													<td><strong>Valor Recebido</strong></td>
													<td><strong>Nota </strong></td>
												</tr>
											</thead>
											<tbody>
												<c:forEach var="nota" items="${notasEquipeRodadas }">
													<tr>
														<td>${nota.rodada.nome }</td>
														<c:if test="${nota.fatorDeAposta == null }">
															<td>-</td>
														</c:if>
														<c:if test="${nota.fatorDeAposta != null }">
															<td>
																<c:if test="${permissao == 'professor' }">
																	<form id="fatorForm" role="form"
																		class="form-horizontal" 
																		action="<c:url value="/jogo/${jogo.id}/equipe/${nota.equipe.id }/fator/${nota.id }"></c:url>"
																		method="POST">
																		<div class="form-group col-sm-3">
																			<input class="form-control" type="text" name="fatorDeAposta"  
																			 value='<fmt:formatNumber type="number"
																			 maxFractionDigits="2">${nota.fatorDeAposta }
																			 </fmt:formatNumber>' />
																		</div>
																		<div class="col-sm-1">
																			<button type="submit" class="btn btn-primary">
																				<i class="glyphicon glyphicon-floppy-disk"></i>
																			</button>
																		</div>	 
																	</form>
																</c:if>
																<c:if test="${permissao == 'aluno' }">
																	<fmt:formatNumber type="number" maxFractionDigits="2">
																		 ${nota.fatorDeAposta }
																	 </fmt:formatNumber>
																</c:if>
															</td>
														</c:if>
														<c:if test="${nota.retorno != null }">
															<td><fmt:formatNumber type="number" maxFractionDigits="2" 
																value= "${nota.retorno }" />
															</td>
														</c:if>
														<c:if test="${nota.retorno == null }">
															<td>-</td>
														</c:if>
														<c:if test="${nota.valor != null }">
															<td><fmt:formatNumber type="number" maxFractionDigits="2" 
																value= "${nota.valor }" />
															</td>
														</c:if>
														<c:if test="${nota.valor == null }">
															<td>-</td>
														</c:if>
													</tr>
												</c:forEach>
											</tbody>
										</table>
										<strong>Média:&nbsp;&nbsp;
											<c:if test="${mediaEquipe >= 0 }">
												<fmt:formatNumber type="number" maxFractionDigits="2" 
														value= "${mediaEquipe }" />
											</c:if>
											<c:if test="${mediaEquipe < 0 }">
												0.0
											</c:if>
										</strong>
									</c:if>
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
			