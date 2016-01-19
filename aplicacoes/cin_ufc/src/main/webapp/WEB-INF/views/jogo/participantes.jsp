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
<title>Jogadores</title>

</head>
<body>
	<jsp:include page="../fragments/header.jsp" />
	
	<div class="section">
		<div class="container-fluid">
			<div class="row">
				<jsp:include page="../fragments/menu.jsp" />
				<div class="col-sm-8 col-md-10 main">
					<h2><strong>${jogo.nomeDoCurso }</strong> <small>${jogo.semestre }</small></h2>
					<div class="panel panel-primary">
						<div class="panel-heading">
							<i class="fa fa-users"></i>&nbsp;&nbsp;Participantes do Jogo
						</div>
						<div class="panel-body">
							<jsp:include page="../fragments/mensagens.jsp" />
							<div class="col-sm-12">
								<div class="table-responsive">
									<table id="participantes" class="table table-striped table-hover">
										<thead>
											<tr>
												<th>Nome</th>
												<th>Sobrenome</th>
												<th>Curso</th>
												<th>Email</th>
												<c:if test="${permissao eq 'professor' }">
													<th></th>
												</c:if>
											</tr>
										</thead>
										<tbody>
											<c:forEach var="usuario" items="${usuarios}">
												<tr>
													<td>
														<a href="<c:url value="/usuario/${usuario.id}/detalhes/${jogo.id }"></c:url>">${usuario.nome}</a>
													</td>
													<td>	
														${usuario.sobreNome}										
													</td>
													<td>${usuario.curso } </td>
													<td>${usuario.email } </td>
													<c:if test="${permissao eq 'professor' }">
														<td>
															<a id="submeter" data-toggle="modal" data-target="#confirm-submit" href="#"
																data-href="<c:url value="participantes/${usuario.id}/desvincular" ></c:url>" data-name="${usuario.nome }">
																<button class="btn btn-primary">Desvincular&nbsp;<i class="glyphicon glyphicon-remove"></i></button>
															</a>
													</td>
													</c:if>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<c:if test="${permissao eq 'professor' }">
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
	</c:if>
	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>