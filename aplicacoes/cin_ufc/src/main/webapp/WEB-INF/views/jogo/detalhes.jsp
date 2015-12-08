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
	<title>Informações do Jogo</title>
</head>

<body>
	<jsp:include page="../fragments/header.jsp" />
	<div class="section">
		<div class="container-fluid">
			<div class="row">
				<jsp:include page="../fragments/menu.jsp" />		
				<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">	
					<h2><strong>${jogo.nomeDoCurso }</strong> <small> ${jogo.semestre }</small></h2>	
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
							<h4>Informações do Jogo</h4>
						</div>
						<div class="panel-body">			
							<div class="form-horizontal">		
								<div class="form-group">
									<label for="inicio" class="col-sm-2 field">Início:</label>
									<div class="col-sm-4 field-value">
										<c:if test="${empty jogo.inicio }">
											<label>-</label>
										</c:if>
										<label id="inicio"><fmt:formatDate pattern="dd/MM/yyyy" value="${jogo.inicio }" /></label>
									</div>
									<label class="col-sm-2 field">Término:</label>
									<div class="col-sm-4 field-value">
										<c:if test="${empty jogo.termino }">
											<label>-</label>
										</c:if>
										<label><fmt:formatDate pattern="dd/MM/yyyy" value="${jogo.termino }" /></label>
									</div>
								</div>				
								<div class="form-group">
									<label class="col-sm-2 field">Descrição:</label>
									<div class="col-sm-10 field-value" id="desc">
										<article><label>${jogo.descricao }</label></article>
									</div>						
								</div>
								<div class="form-group">
									<label class="col-sm-2 field">Regras:</label>
									<div class="col-sm-10 field-value">
										<c:if test="${empty jogo.regras }">
											<label>-</label>
										</c:if>
										<c:if test="${not empty jogo.regras }">
											<article><p class="text-justify">${jogo.regras }</p></article>
										</c:if>
									</div>		
								</div>
								
								<div class="form-group">
									<label class="col-sm-2 field">Anexos:</label>
									<div class="col-sm-10 field-value">
										<c:if test="${empty jogo.documentos }">
											<label>-</label>						
										</c:if>				
										<c:if test="${not empty jogo.documentos }">
											<c:forEach items="${jogo.documentos }" var="documento">
												<label><a href="<c:url value="/documento/downloadDocumento/${documento.id }" ></c:url>">${documento.nomeOriginal }</a></label> - 							
											</c:forEach>
										</c:if>
									</div>
								</div>		
								<div class="form-group">
									<label class="col-sm-2 field">Criador:</label>
									<div class="col-sm-4 field-value">
										<label>${jogo.professor.nome} ${jogo.professor.sobreNome}</label>
									</div>
									<label class="col-sm-2 field">Email:</label>
									<div class="col-sm-4 field-value">
										<label>${jogo.professor.email}</label>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-sm-12">
						<div class="row placeholders">
							<c:if test="${permissao == 'professor' }">
								<div class="form-group">					
									<div class="col-sm-2">				
										<a id="editar" href="<c:url value="/jogo/${jogo.id}/editar" ></c:url>">
											<button class="btn btn-primary btn-lg">Editar&nbsp;<i class="glyphicon glyphicon-edit"></i></button>
										</a>
									</div>
									<div class="col-sm-2">
										<c:if test="${jogo.status == true}">									
											<a id="inativar" data-toggle="modal" data-target="#confirm-inativar-jogo" href="#" 
												data-href="<c:url value="/jogo/${jogo.id}/inativar">
												</c:url>" data-name="${jogo.nomeDoCurso }">
												<button class="btn btn-warning btn-lg">
													Inativar&nbsp;<i class="glyphicon glyphicon-ban-circle"></i>
												</button>
											</a>
										</c:if>
										<c:if test="${jogo.status == false }">
											<a id="ativar" data-toggle="modal" data-target="#confirm-ativar-jogo" href="#" 
												data-href="<c:url value="/jogo/${jogo.id}/ativar">
												</c:url>" data-name="${jogo.nomeDoCurso }">
												<button class="btn btn-success btn-lg">
													Ativar&nbsp;<i class="glyphicon glyphicon-ok-circle"></i>
												</button>
											</a>
										</c:if>
									</div>
									<div class="col-sm-2">
										<a id="excluir" data-toggle="modal" data-target="#confirm-delete" href="#" 
										data-href="<c:url value="/jogo/${jogo.id}/excluir"></c:url>" data-name="${jogo.nomeDoCurso }">
											<button class="btn btn-danger btn-lg">Excluir&nbsp;<i class="glyphicon glyphicon-trash"></i></button>
										</a>					
									</div>
								</div>
							</c:if>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<c:if test="${permissao == 'professor' }">
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
		<!-- Modal Inativar jogo -->
		<div class="modal fade" id="confirm-inativar-jogo" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
	        			<h4 class="modal-title" id="inativarModalLabel">Inativar</h4>
	        			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					</div>
					<div class="modal-body"></div>
					<div class="modal-footer">
						<a href="#" class="btn btn-primary">Inativar</a>
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
					</div>
				</div>
			</div>
		</div>
		
		<!-- Modal ativar jogo -->
		<div class="modal fade" id="confirm-ativar-jogo" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
	        			<h4 class="modal-title" id="ativarModalLabel">Ativar</h4>
	        			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					</div>
					<div class="modal-body"></div>
					<div class="modal-footer">
						<a href="#" class="btn btn-primary">Ativar</a>
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
					</div>
				</div>
			</div>
		</div>
	</c:if>	
	<jsp:include page="../fragments/footer.jsp" />	
</body>
</html>