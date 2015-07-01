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
	<title>Informações do Projeto</title>
</head>

<body>
	<jsp:include page="../fragments/header.jsp" />
	
	<div class="container-fluid">
	<jsp:include page="../fragments/menu.jsp" />		
		<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">	
			<input id="jogoId" type="hidden" value="${jogo.id }"/>
			<h2>${jogo.nomeDoCurso } ${jogo.semestre }</h2>			
			<h3>Informações</h3><hr>						
			<div class="form-group">
				<label class="col-sm-2 control-label field">Início:</label>
				<div class="col-sm-4 field-value">
					<c:if test="${empty jogo.inicio }">
						<label>-</label>
					</c:if>
					<label><fmt:formatDate pattern="dd/MM/yyyy" value="${jogo.inicio }" /></label>
				</div>
				<label class="col-sm-2 control-label field">Término:</label>
				<div class="col-sm-4 field-value">
					<c:if test="${empty jogo.termino }">
						<label>-</label>
					</c:if>
					<label><fmt:formatDate pattern="dd/MM/yyyy" value="${jogo.termino }" /></label>
				</div>
			</div>				
			<br>			
			<div class="form-group">
				<label class="col-sm-2 control-label field">Descrição:</label>
				<div class="col-sm-10 field-value">
					<article><label>${jogo.descricao }</label></article>
				</div>						
			</div>
			<br>
			<div class="form-group">
				<label class="col-sm-2 control-label field">Regras:</label>
				<div class="col-sm-10 field-value">
					<c:if test="${empty jogo.regras }">
						<label>-</label>
					</c:if>
					<c:if test="${not empty jogo.regras }">
						<article>
							<label>							
							${jogo.regras }
							</label>
						</article>
					</c:if>
					
				</div>		
						
			</div>
			<br>			
			
			<div class="form-group">
				<label class="col-sm-2 control-label field">Anexos:</label>
				<div class="col-sm-10 field-value">
					<c:if test="${empty jogo.documentos }">
						<label>-</label>						
					</c:if>				
					<c:if test="${not empty jogo.documentos }">
						<c:forEach items="${jogo.documentos }" var="documento">
							<label><a href="<c:url value="/documento/downloadDocumento/${documento.id }" ></c:url>">${documento.nomeOriginal }</a></label><br>							
						</c:forEach>
					</c:if>
				</div>
			</div>				
			
			<div class="col-sm-12">
				<div class="row placeholders">
					<ul class="list-group">							
						<li class="media"><hr></li>							
					</ul>
					<div class="form-group">					
						<div class="col-sm-2">				
							<a id="editar" href="<c:url value="/jogo/${jogo.id}/editar" ></c:url>">
								<button class="btn btn-primary btn-lg">Editar&nbsp;<i class="fa fa-edit"></i></button>
							</a>
						</div>
						<div class="col-sm-2">
							<a id="excluir" data-toggle="modal" data-target="#confirm-delete" href="#" 
							data-href="<c:url value="/jogo/${jogo.id}/excluir"></c:url>" data-name="${jogo.nomeDoCurso }">
								<button class="btn btn-danger btn-lg">Excluir&nbsp;<i class="fa fa-trash-o"></i></button>
							</a>					
						</div>
					</div>
				</div>
			</div>
						
		</div>
	</div>
	<!-- Modal Excluir JOGO -->
	<div class="modal fade" id="confirm-delete" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
        			<h4 class="modal-title" id="excluirModalLabel">Excluir</h4>
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				</div>
				<div class="modal-body"></div>
				<div class="modal-footer">
					<a href="#" class="btn btn-danger">Excluir</a>
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../fragments/footer.jsp" />	
</body>
</html>
