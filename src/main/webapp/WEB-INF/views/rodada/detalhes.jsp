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
	<title>Rodada</title>
</head>

<body>
	<jsp:include page="../fragments/header.jsp" />
	
	<div class="section">
		<div class="container-fluid">
			<div class="row">
				<jsp:include page="../fragments/menu.jsp" />
				<div class="col-sm-8 col-sm-offset-3 col-md-10 col-md-offset-2 main">
					<div class="col-sm-12">
						<h2><strong>${jogo.nomeDoCurso }</strong> <small>${jogo.semestre }</small></h2>						
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
					
					<div class="form-group">
						<label class="col-sm-2 control-label field">Nome:</label>
						<div class="col-sm-10 field-value">
							<c:if test="${empty rodada.nome }">
								<label>-</label>
							</c:if>
							<c:if test="${not empty rodada.nome }">
								<label>							
									${rodada.nome }
								</label>
							</c:if>
						</div>		
					</div>
					<br>
					<div class="form-group">
						<label class="col-sm-2 control-label field">Início:</label>
						<div class="col-sm-4 field-value">
							<c:if test="${empty rodada.inicio }">
								<label>-</label>
							</c:if>
							<label><fmt:formatDate pattern="dd/MM/yyyy" value="${rodada.inicio }" /></label>
						</div>
						<label class="col-sm-2 control-label field">Término:</label>
						<div class="col-sm-4 field-value">
							<c:if test="${empty rodada.termino }">
								<label>-</label>
							</c:if>
							<label><fmt:formatDate pattern="dd/MM/yyyy" value="${rodada.termino }" /></label>
						</div>
					</div>
					<br>
					<div class="form-group">
						<label class="col-sm-2 control-label field">Descrição:</label>
						<c:if test="${empty rodada.descricao }">
							<label>-</label>
						</c:if>
						<div class="col-sm-10 field-value">
							<label>${rodada.descricao }</label>
						</div>						
					</div>
					<div class="col-sm-12">
						<hr>
						<div class="row placeholders">
							<div class="form-group">	
								<c:if test="${(permissao eq 'professor') || (permissao eq 'membro') }">			
									<div class="col-sm-2">				
										<a id="editar" href="<c:url value="/jogo/${jogo.id}/rodada/${rodada.id }/editar" ></c:url>">
											<button class="btn btn-primary btn-lg">Editar&nbsp;<i class="fa fa-edit"></i></button>
										</a>
									</div>
								</c:if>
								<c:if test="${permissao eq 'professor' }">
									<div class="col-sm-2">
										<c:if test="${rodada.status == true}">									
											<a id="inativar" data-toggle="modal" data-target="#confirm-inativar-rodada" href="#" 
												data-href="<c:url value="/jogo/${jogo.id}/rodada/${rodada.id }/inativar">
												</c:url>" data-name="${rodada.nome }">
												<button class="btn btn-warning btn-lg">
													Inativar&nbsp;<i class="glyphicon glyphicon-ban-circle"></i>
												</button>
											</a>
										</c:if>
										<c:if test="${rodada.status == false }">
											<a id="ativar" data-toggle="modal" data-target="#confirm-ativar-rodada" href="#" 
												data-href="<c:url value="/jogo/${jogo.id}/rodada/${rodada.id }/ativar">
												</c:url>" data-name="${rodada.nome }">
												<button class="btn btn-success btn-lg">
													Ativar&nbsp;<i class="glyphicon glyphicon-ok-circle"></i>
												</button>
											</a>
										</c:if>
									</div>
									<div class="col-sm-2">
										<a id="excluir" data-toggle="modal" data-target="#confirm-delete-rodada" href="#" 
										data-href="<c:url value="/jogo/${jogo.id}/rodada/${rodada.id }/excluir"></c:url>" data-name="${rodada.nome }">
											<button class="btn btn-danger btn-lg">Excluir&nbsp;<i class="fa fa-trash-o"></i></button>
										</a>					
									</div>
								</c:if>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<c:if test="${permissao eq 'professor' }">			
		<!-- Modal Excluir Rodada -->
		<div class="modal fade" id="confirm-delete-rodada" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
		
		<!-- Modal Inativar Rodada -->
		<div class="modal fade" id="confirm-inativar-rodada" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
		
		<!-- Modal ativar Rodada -->
		<div class="modal fade" id="confirm-ativar-rodada" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
