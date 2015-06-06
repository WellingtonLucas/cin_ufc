<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
	<head>
		<jsp:include page="../fragments/header-estrutura.jsp" />	
		<title>Novo Jogo</title>
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
		<br>
		<div class ="container">
			<form:form class="form-horizontal" action="/cin_ufc/jogo/novo-jogo" commandName="jogo" servletRelativeAction="/novo-jogo" method="POST">
				<input type="hidden" name="idUsuario" value="${idUsuario}"/>
				<div class="form-group">
					<label for="inputNome" class="col-sm-2 control-labe">Nome do Cusro*</label>
					<div class="col-sm-4">
						<form:input type="text" class="form-control" id="inputNome" path="nomeDoCurso"/>
					</div>
					<label for="inputSemestre" class="col-sm-1 control-labe">Semestre*</label>
					<div class="col-sm-2">
						<form:input type="text" class="form-control" id="inputSemestre" path="semestre"/><br>
					</div>
				</div>
				<div class="form-group">
					<label for="descricao" class="col-sm-2 control-labe">Descrição do Jogo*</label>
					<div class="col-sm-8">
						<form:textarea name="descricao" id="descricao" path="descricao"/>
					</div>
				</div>
				<div class="form-group">
					<label for="regras" class="col-sm-2 control-labe">Regras do Jogo</label>
					<div class="col-sm-8">
						<form:textarea name="regras" id="regras" path="regras" />
					</div>
				</div>
				
				<button type="submit" class="btn btn-primary btn-lg" >
					Cadastrar
				</button>
				
				<script>
					CKEDITOR.replace('descricao');
					CKEDITOR.replace('regras');
				</script>
			</form:form>
		</div>
		<jsp:include page="../fragments/footer.jsp" />
	</body>
</html>