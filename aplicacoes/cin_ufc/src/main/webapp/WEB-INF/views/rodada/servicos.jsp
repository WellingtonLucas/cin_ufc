<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
	<head>
		<jsp:include page="../fragments/header-estrutura.jsp" />
		<title>${rodada.nome } Serviços</title>
	</head>
	<body>
		<jsp:include page="../fragments/header.jsp" />
		<div class="section">
			<div class="container-fluid">
				<div class="row">
					<jsp:include page="../fragments/menu.jsp" />
					<div class="col-sm-8 col-md-10 main">
						<c:if test="${not empty info && (permissao == 'professor')}">
							<div class="alert alert-success alert-dismissible" role="alert">
								<button type="button" class="close" data-dismiss="alert">
									<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
								</button>
								<c:out value="${info}"></c:out>
							</div>
						</c:if>
						<c:if test="${permissao == 'professor'}">
							<div class="panel panel-primary">
								<div class="panel-heading">
									<i class="fa fa-shopping-basket"></i>&nbsp;&nbsp;Serviços Rodada
								</div>
								<div class="panel-body">
							        <div>
							        	<c:if test="${qtd > 0}">
								        	<a class="btn btn-lg btn-danger col-sm-2" data-toggle="tooltip" data-placement="right"
												title="Você possui ${qtd } <c:if test="${qtd > 1}">solicitações</c:if><c:if test="${qtd == 1}">solicitação</c:if> de consultoria, clique aqui para analisar." 
								        		href="<c:url value="/jogo/${jogo.id }/rodada/${rodada.id}/solicitacoes"></c:url>" role="button">
								        		Solicitações &nbsp;&nbsp;<span class="badge">${qtd}</span>
								        	</a>
						        		</c:if>
						        		<c:if test="${qtd == 0}">
								        	<a class="btn btn-lg btn-success col-sm-2" data-toggle="tooltip" data-placement="right"
												title="Você possui ${qtd } solicitações de consultoria." 
								        		href="<c:url value="/jogo/${jogo.id }/rodada/${rodada.id}/solicitacoes"></c:url>" role="button">
								        		Solicitações &nbsp;&nbsp;<span class="badge">${qtd}</span>
								        	</a>
						        		</c:if>
							        </div>
							    </div>
						    </div>
						    <c:if test="${not empty erro}">
								<div class="alert alert-danger alert-dismissible" role="alert">
									<button type="button" class="close" data-dismiss="alert">
										<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
									</button>
									<c:out value="${erro}"></c:out>
								</div>
							</c:if>
						    <c:if test="${(action == 'editar' || action=='cadastrar')}">
					        	<jsp:include page="novaConsultoria.jsp" />
					        </c:if>
				        </c:if>
				        <c:if test="${permissao == 'aluno' }">
				        	<jsp:include page="adquirirServico.jsp" />
				        </c:if>
				    </div>
				</div>
			</div>
		</div>
		<jsp:include page="../fragments/footer.jsp" />
	</body>
</html>