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
		<c:if test="${action eq 'vincularAoJogo' }">
			<c:set var="url" value="/jogo/participantes/vincular"></c:set>
		</c:if>
		<title>Vincular jogadores</title>
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
							Vincular Participantes
						</div>
						<div class="panel-body">
							<jsp:include page="../fragments/mensagens.jsp" />
					        <form:form id="responderForm" role="form" commandName="jogo" class="form-horizontal" method="POST"
								servletRelativeAction="${url }">
								<form:hidden path="id" value="${jogo.id }"/>
								<form:hidden path="semestre" value="${jogo.semestre }"/>
								<form:hidden path="nomeDoCurso" value="${jogo.nomeDoCurso }"/>
								<div class="col-sm-12">
									<div class="table-responsive">
										<table id="add-participante-jogo" class="table table-striped table-hover">
											<thead>
												<tr>
													<th>Nome</th>
													<th>Sobrenome</th>
													<th>Curso</th>
													<th>Email</th>
													<th>Marque aqui</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach var="usuario" items="${usuarios}" varStatus="userId">
													<tr>
														<td>
															<a href="<c:url value="/usuario/${usuario.id}/detalhes/${jogo.id }"></c:url>">${usuario.nome}</a>
														</td>
														<td>	
															${usuario.sobreNome}										
														</td>
														<td>${usuario.curso } </td>
														<td>${usuario.email } </td>
														<td>
															<div class="btn-group" data-toggle="buttons">
																<label class="btn btn-primary">
																	<input name="alunos[${userId.index }].id" type="checkbox" autocomplete="off" value="${usuario.id }">
																	<span class="glyphicon glyphicon-ok"></span>
																</label>
															</div>
														</td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</div>
								</div>
								<c:if test="${not empty usuarios }">
									<div class="form-group">
										<div class="col-sm-2">				
											<button type="submit" class="btn btn-primary btn-lg">Vincular&nbsp;<i class="glyphicon glyphicon-floppy-disk"></i></button>
										</div>
									</div>
								</c:if>
							</form:form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>