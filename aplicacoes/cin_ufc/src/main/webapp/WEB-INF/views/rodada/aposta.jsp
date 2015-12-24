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
	<title>Apostas</title>

</head>
<body>
	<jsp:include page="../fragments/header.jsp" />
	
	<div class="container-fluid">
		<jsp:include page="../fragments/menu.jsp" />
		<div class="row">
			<div class="col-sm-8 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h2><strong>Rodada: ${rodada.nome }</strong> - Seu saldo é <strong>R$ ${aposta.saldo }</strong></h2>
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
				<div class="panel panel-primary">
					<div class="panel-heading">
						<h3 class="panel-title">Invista na equipe: ${equipe.nome }</h3>
					</div>
					<div class="panel-body">
						<form:form id="apostar" role="form" class="form-horizontal" commandName="deposito"
							servletRelativeAction="/jogo/${jogo.id }/rodada/${rodada.id }/apostar" method="POST">
							<form:hidden path="equipe.id" value="${equipe.id }" />
							
							<div class="form-group">
								<div class="form-item">
									<label for="quantia" class="col-sm-3 control-label">Valor de aposta (R$):</label>
									<div class="col-sm-2">
										<form:input type="number" step="0.1" path="quantia" min="0.1" max="${aposta.saldo }" class="form-control" />
										<div class="error-validation">
											<form:errors path="quantia"></form:errors>
										</div>
									</div>
								</div>
							</div>	
							<div class="form-group">
								<div class="col-sm-3"></div>
								<div class="col-sm-2">		
									<button type="submit" class="btn btn-primary btn-lg" >
										Apostar <i class="glyphicon glyphicon-floppy-disk"></i>
									</button>						
								</div>
								<div class="col-sm-2">
									<a href="javascript:history.back();" class="btn btn-warning btn-lg">Cancelar</a>
								</div>
							</div>
						</form:form>
					</div>
			    </div>
			</div>
		</div>
	</div>
	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>