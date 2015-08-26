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
	
	<div class="section">
		<div class="container-fluid">
			<div class="row">
				<jsp:include page="../fragments/menu.jsp" />
				<div class="col-sm-8 col-sm-offset-3 col-md-10 col-md-offset-2 main">
					<div class="col-sm-12">
						<h2><strong>${equipe.nome }</strong></h2>						
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
						<c:if test="${not empty equipe.alunos }">
							<table id="table_id" class="table table-striped table-hover">
								<thead>
									<tr>
										<th>Nome</th>
										<th>Sorenome</th>
										<th>Curso</th>
										<th>Email</th>
										<th></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="usuario" items="${equipe.alunos}">
										<tr>
											<td>
												<a href="<c:url value="${equipe.id }/usuario/${usuario.id}/detalhes"></c:url>">${usuario.nome}</a>
											</td>
											<td>${usuario.sobreNome}</td>
											<td>${usuario.curso } </td>
											<td>${usuario.email } </td>
											<td>
												<a id="submeter" data-toggle="modal" data-target="#confirm-submit" href="#"
													data-href="<c:url value="${equipe.id }/usuario/${usuario.id}/desvincular" ></c:url>" data-name="${usuario.nome }">
													<button class="btn btn-primary">Desvincular&nbsp;<i class="glyphicon glyphicon-remove"></i></button>
												</a>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</c:if>	
					</div>
					<div class="col-sm-12">
						<div class="row placeholders">
							<ul class="list-group">							
								<li class="media"><hr></li>							
							</ul>
							<div class="form-group">					
								<div class="col-sm-2">				
									<a id="editar" href="<c:url value="/jogo/${jogo.id}/equipe/${equipe.id }/editar" ></c:url>">
										<button class="btn btn-primary btn-lg">Editar&nbsp;<i class="fa fa-edit"></i></button>
									</a>
								</div>
								<div class="col-sm-2">
									<c:if test="${equipe.status == true}">									
										<a id="inativar" data-toggle="modal" data-target="#confirm-inativar" href="#" 
											data-href="<c:url value="/jogo/${jogo.id}/equipe/${equipe.id }/inativar">
											</c:url>" data-name="${equipe.nome }">
											<button class="btn btn-warning btn-lg">
												Inativar&nbsp;<i class="glyphicon glyphicon-ban-circle"></i>
											</button>
										</a>
									</c:if>
									<c:if test="${equipe.status == false }">
										<a id="ativar" data-toggle="modal" data-target="#confirm-ativar" href="#" 
											data-href="<c:url value="/jogo/${jogo.id}/equipe/${equipe.id }/ativar">
											</c:url>" data-name="${equipe.nome }">
											<button class="btn btn-success btn-lg">
												Ativar&nbsp;<i class="glyphicon glyphicon-ok-circle"></i>
											</button>
										</a>
									</c:if>
								</div>
								<div class="col-sm-2">
									<a id="excluir" data-toggle="modal" data-target="#confirm-delete2" href="#" 
									data-href="<c:url value="/jogo/${jogo.id}/equipe/${equipe.id }/excluir"></c:url>" data-name="${equipe.nome }">
										<button class="btn btn-danger btn-lg">Excluir&nbsp;<i class="fa fa-trash-o"></i></button>
									</a>					
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
				
	<!-- Modal Excluir JOGO -->
	<div class="modal fade" id="confirm-delete2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
	
	<!-- Modal Desvincular aluno -->
	<div class="modal fade" id="confirm-submit" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
        			<h4 class="modal-title" id="submeterModalLabel">Desvincular</h4>
        			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				</div>
				<div class="modal-body"></div>
				<div class="modal-footer">
					<a href="#" class="btn btn-primary">Desvincular</a>
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- Modal Inativar Equipe -->
	<div class="modal fade" id="confirm-inativar" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
	
	<!-- Modal ativar Equipe -->
	<div class="modal fade" id="confirm-ativar" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
	<jsp:include page="../fragments/footer.jsp" />	
</body>
</html>