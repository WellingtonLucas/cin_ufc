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
	<title>Formulario</title>
</head>

<body>
	<jsp:include page="../fragments/header.jsp" />
	
	<div class="section">
		<div class="container-fluid">
			<div class="row">
				<jsp:include page="../fragments/menu.jsp" />
				<div class="col-sm-8 col-sm-offset-3 col-md-10 col-md-offset-2 main">
					<div class="col-sm-12">
						<h2><strong>${formulario.titulo }</strong></h2>						
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
					</div>
					<div class="col-sm-12">
						<c:forEach var="pergunta" items="${formulario.perguntas}" varStatus="questId">
							<div class="panel panel-default">
								<div class="panel-heading">
									<label>Questão ${questId.count}:
										${pergunta.descricao }
									</label>											
								</div>
								<div class="radio panel-body">
									<label class="col-sm-10">
										<input type="radio" name="opcaoR" id="opcaoR1" disabled="disabled">
										${pergunta.opcoes[0].descricao }
									</label>
								
									<label class="col-sm-10">
										<input type="radio" name="opcaoR" id="opcaoR2" disabled="disabled">
										${pergunta.opcoes[1].descricao }
									</label>
									<label class="col-sm-10">
										<input type="radio" name="opcaoR" id="opcaoR3" disabled="disabled">
										${pergunta.opcoes[2].descricao }
									</label>
									<label class="col-sm-10">
										<input type="radio" name="opcaoR" id="opcaoR4" disabled="disabled">
										${pergunta.opcoes[3].descricao }
									</label>
									<label class="col-sm-10">
										<input type="radio" name="opcaoR" id="opcaoR5" disabled="disabled">
										${pergunta.opcoes[4].descricao }
									</label>
								</div>
							</div>
						</c:forEach>
						<label class="col-sm-1 field">Prazo:</label>
						<div class="col-sm-4 field-value">
							<c:if test="${empty formulario.prazo }">
								<label>-</label>
							</c:if>
							<label><fmt:formatDate pattern="dd/MM/yyyy" value="${formulario.prazo }" /></label>
						</div>
						<div class="col-sm-12"></div>
						<label class="col-sm-1 field">Nota:</label>
						<div class="col-sm-11 field-value">
							<c:if test="${empty formulario.nota }">
								<label>-</label>
							</c:if>
							<label>${formulario.nota }</label>
						</div>
					</div>
					<div class="col-sm-12">
						<div class="row placeholders">
							<ul class="list-group">							
								<li class="media"><hr></li>							
							</ul>
							<div class="form-group">
								<div class="col-sm-2">				
									<a id="editar" href="<c:url value="/jogo/${jogo.id}/formulario/${formulario.id }" ></c:url>">
										<button class="btn btn-primary btn-lg">Responder&nbsp;<i class="glyphicon glyphicon-floppy-disk"></i></button>
									</a>
								</div>
								<div class="col-sm-2">				
									<a id="editar" href="<c:url value="/jogo/${jogo.id}/formulario/${formulario.id }/editar" ></c:url>">
										<button class="btn btn-primary btn-lg">Editar&nbsp;<i class="glyphicon glyphicon-edit"></i></button>
									</a>
								</div>
								<div class="col-sm-2">				
									<a id="copiar" href="<c:url value="/jogo/${jogo.id}/formulario/${formulario.id }/copiar" ></c:url>">
										<button class="btn btn-success btn-lg">Copiar&nbsp;<i class="glyphicon glyphicon-duplicate"></i></button>
									</a>
								</div>
								<div class="col-sm-2">
									<a id="excluir" data-toggle="modal" data-target="#confirm-delete3" href="#" 
									data-href="<c:url value="/jogo/${jogo.id}/formulario/${formulario.id }/excluir"></c:url>" data-name="${formulario.titulo }">
										<button class="btn btn-danger btn-lg">Excluir&nbsp;<i class="glyphicon glyphicon-trash"></i></button>
									</a>					
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
				
	<!-- Modal Excluir FORMULARIO -->
	<div class="modal fade" id="confirm-delete3" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
