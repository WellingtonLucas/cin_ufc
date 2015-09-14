<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
	<head>
		<c:if test="${action eq 'cadastrar' }">
			<c:set var="url" value="/jogo/${jogo.id }/nova-rodada"></c:set>
			<c:set var="titulo" value="Nova Rodada"></c:set>
		</c:if>
		<c:if test="${action eq 'editar' }">
			<c:set var="url" value="/${jogo.id }/rodada/editar"></c:set>
			<c:set var="titulo" value="Editar - ${rodada.nome } "></c:set>
		</c:if>
		
		<jsp:include page="../fragments/header-estrutura.jsp" />	
		<title>${titulo}</title>
	</head>
	<body>
	
		<jsp:include page="../fragments/header.jsp" />
		<c:if test="${not empty erro}">
			<div class="alert alert-danger alert-dismissible" role="alert">
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
		<br>
		<div class ="container">
			<div class="col-sm-12">
				<h2>${titulo}</h2>
				<hr>
			</div>
			<form:form id="adicionarRodadaForm" role="form" class="form-horizontal" commandName="rodada" servletRelativeAction="${url }" method="POST">
				<form:hidden path="id"/>
				<form:hidden path="status"/>
				<form:hidden path="statusPrazo"/>
				<div class="form-group">
					<div class="form-item">
						<label for="nome" class="col-sm-2 control-label" >Nome da Rodada:<span class="required">*</span></label>
						<div class="col-sm-6">
							<form:input type="text" class="form-control" id="nome" path="nome" placeholder="Rodada 1" />
							<div class="error-validation">
								<form:errors path="nome"></form:errors>
							</div>
						</div>
					</div>
				</div>
				
				<div class="form-group">
					<div class="form-item">
						<label for="inicio" class="col-sm-2 control-label">Data de Início:<span class="required">*</span></label>
						<div class="col-sm-2">
							<form:input id="inicio" type="text" path="inicio" cssClass="form-control data" placeholder="DD/MM/YYYY"/>
							<div class="error-validation">
								<form:errors path="inicio"></form:errors>
							</div>
							<c:if test="${not empty error_inicio}">
								<div class="error-validation">
									<span>${error_inicio}</span>
								</div>
							</c:if>
						</div>
					</div>

					<div class="form-item">
						<label for="termino" class="col-sm-2 control-label">Data de Término:<span class="required">*</span></label>
						<div class="col-sm-2">
							<form:input id="termino" type="text" path="termino" cssClass="form-control data" placeholder="DD/MM/YYYY"/>
							<div class="error-validation">
								<form:errors path="termino"></form:errors>
							</div>
							<c:if test="${not empty error_termino}">
								<div class="error-validation">
									<span>${error_termino}</span>
								</div>
							</c:if>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="form-item">
						<label for="prazoSubmissao" class="col-sm-2 control-label">Prazo de submissão:<span class="required">*</span></label>
						<div class="col-sm-2">
							<form:input type="text" path="prazoSubmissao" cssClass="form-control data" placeholder="DD/MM/YYYY"/>
							<div class="error-validation">
								<form:errors path="prazoSubmissao"></form:errors>
							</div>
							<c:if test="${not empty error_prazoSubmissao}">
								<div class="error-validation">
									<span>${error_prazoSubmissao}</span>
								</div>
							</c:if>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="form-item">
						<label for="descricao" class="col-sm-2 control-label">Descrição da Rodada:</label>
						<div class="col-sm-8">
							<form:textarea name="descricao" id="descricao" path="descricao" class="form-control"/>
							<div class="error-validation">
									<form:errors path="descricao"></form:errors>
							</div>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-2"></div>
					<div class="col-sm-2">
						<span class="campo-obrigatorio"><span class="required">*</span> Campos obrigatórios</span>
					</div>
				</div>
				
				<div class="form-group">
					<div class="col-sm-2"></div>
					<div class="col-sm-2">		
						<button type="submit" class="btn btn-primary btn-lg" >
							Salvar <i class="glyphicon glyphicon-floppy-disk"></i>
						</button>						
					</div>
					<div class="col-sm-2">
						<a href="<c:url value="/jogo/${jogo.id }/rodada/${rodada.id }/detalhes"></c:url>" class="btn btn-warning btn-lg">Cancelar</a>
					</div>
				</div>
			</form:form>
		</div>
		
		<jsp:include page="../fragments/footer.jsp" />
	</body>
</html>