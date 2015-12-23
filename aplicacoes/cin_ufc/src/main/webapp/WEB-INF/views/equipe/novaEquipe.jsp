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
			<c:set var="url" value="/${idJogo }/equipe/nova"></c:set>
			<c:set var="titulo" value="Nova Empresa"></c:set>
			<c:set var="txtBtn" value="Salvar"></c:set>
		</c:if>
		<c:if test="${action eq 'editar' }">
			<c:set var="url" value="/jogo/${idJogo }/equipe/${equipe.id }/editar"></c:set>
			<c:set var="titulo" value="Editar - ${equipe.nome } "></c:set>
			<c:set var="txtBtn" value="Atualizar"></c:set>
		</c:if>
		
		<jsp:include page="../fragments/header-estrutura.jsp" />	
		<title>${titulo}</title>
	</head>
	<body>
	
		<jsp:include page="../fragments/header.jsp" />
		<div class ="container">
			<div class="col-sm-12">
				<h2>${titulo}</h2>
				<hr>
				<c:if test="${not empty erro}">
					<div class="alert alert-danger alert-dismissible" role="alert">
						<button type="button" class="close" data-dismiss="alert">
							<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
						</button>
						<c:out value="${erro}"></c:out>
					</div>
				</c:if>
				<br>				
			</div>
			<form:form id="adicionarEquipeForm" role="form" class="form-horizontal" commandName="equipe" 
			enctype="multipart/form-data" servletRelativeAction="${url }" method="POST">
			
				<form:hidden path="id"/>
				<form:hidden path="status"/>
				<div class="form-group">
					<div class="form-item">
						<label for="nomeDoCurso" class="col-sm-2 control-label" >Nome da Empresa:<span class="required">*</span></label>
						<div class="col-sm-8">
							<form:input type="text" class="form-control" id="nome" path="nome" placeholder="Nome da Equipe" />
							<div class="error-validation">
								<form:errors path="nome"></form:errors>
							</div>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="form-item">
						<label for="ideiaDeNegocio" class="col-sm-2 control-label">Ideia de negócio:</label>
						<div class="col-sm-8">
							<form:textarea name="ideiaDeNegocio" id="ideiaDeNegocio" path="ideiaDeNegocio" class="form-control" />
						</div>
					</div>
				</div>
				
				<div class="form-group form-item">
					<label for="idFoto" class="col-sm-2 control-label">Logo:</label>
					<div class="col-sm-8">
						<input type="file" id="idfoto" name="anexo"></input>
					</div>
				</div>
				<c:if test="${action eq 'editar' }">
					<div class="form-group">
						<div class="col-sm-2"></div>
						<div class="col-sm-8">
							<p class="bg-info">Para mudar o logo da sua equipe escolha outra imagem.</p>
						</div>
					</div>
				</c:if>
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
							${txtBtn } <i class="glyphicon glyphicon-floppy-disk"></i>
						</button>						
					</div>
					<div class="col-sm-2">
						<a href="javascript:history.back();" class="btn btn-warning btn-lg">Cancelar</a>
					</div>
				</div>
			</form:form>
		</div>
		<jsp:include page="../fragments/footer.jsp" />
	</body>
</html>