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
	<title>Informações do Usuario</title>
</head>

<body>
	<jsp:include page="../fragments/header.jsp" />
	
	<div class="container-fluid">
	<jsp:include page="../fragments/menu.jsp" />		
		<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">	
			<input id="jogoId" type="hidden" value="${jogo.id }"/>
			<h2>${usuario.nome } ${usuario.sobreNome }</h2>	
			<c:if test="${not empty erro}">
				<div class="alert alert-warning alert-dismissible" role="alert">
					<button type="button" class="close" data-dismiss="alert">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<c:out value="${erro}"></c:out>
				</div>
			</c:if>		
			<h3>Informações</h3><hr>						
			<div class="form-group">
				<label class="col-sm-2 control-label field">Curso:</label>
				<div class="col-sm-4 field-value">
					<c:if test="${empty usuario.curso }">
						<label>-</label>
					</c:if>
					<c:if test="${not empty  usuario.curso }">
						<label>${usuario.curso }</label>
					</c:if>
				</div>
				<label class="col-sm-1 control-label field">Email:</label>
				<div class="col-sm-5 field-value">
					<c:if test="${empty usuario.email }">
						<label>-</label>
					</c:if>
					<c:if test="${not empty usuario.email }">
						<label>${usuario.email }</label>
					</c:if>
				</div>
			</div>
			<br>
			<div class="form-group">
				<label class="col-sm-2 control-label field" for="matricula">Matricula:</label>
				<div class="field-value col-sm-3" id="matricula">
					<c:if test="${empty usuario.matricula }">
						<label>-</label>
					</c:if>
					<c:if test="${not empty usuario.matricula }">
						${usuario.matricula }
					</c:if>
				</div>
			</div>		
			<br>				
			<div class="form-group">
				<label class="col-sm-2 control-label field">Equipe:</label>
				<div class="col-sm-10 field-value">
					<c:if test="${empty usuario.equipe.nome }">
						<label>-</label>
					</c:if>
					<c:if test="${not empty usuario.equipe.nome }">
						${usuario.equipe.nome }
					</c:if>
				</div>		
			</div>
		</div>
	</div>
	<jsp:include page="../fragments/footer.jsp" />	
</body>
</html>
