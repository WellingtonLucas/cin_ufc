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
	<c:if test="${action eq 'vincularFormularioRodada' }">
		<c:set var="url" value="/jogo/rodada/formulario/vincular"></c:set>
	</c:if>
	<title>Vincular Formulario</title>

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
						<h2><strong>Rodada: ${rodada.nome }</strong> - <small>Adicione um formulário a uma rodada</small></h2>
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
					<c:if test="${not empty formularios }">
						<div class="col-lg-4">
							<div class="input-group">
					            <input type="search" id="search" value="" class="form-control" placeholder="Pesquisar">
					            <span class="input-group-addon">
					            	<span class="glyphicon glyphicon-search"></span>
					            </span>
					        </div>
				        </div>
				        <form:form id="addEquipe" role="form" commandName="rodada" class="form-horizontal" method="POST"
							servletRelativeAction="${url }">
							<form:hidden path="id" />
							<div class="col-sm-12">
								<table id="table" class="table table-striped table-hover">
									<thead>
										<tr>
											<th>Titulo</th>
											<th>Marque aqui</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="formulario" items="${formularios}" varStatus="formId">
											<tr>
												<td>
													<a href="<c:url value="/jogo/${jogo.id}/formulario/${formulario.id }"></c:url>">${formulario.titulo}</a>
												</td>
												<td>
													<div class="btn-group" data-toggle="buttons">
														<label class="btn btn-primary">
															<input name="formulario.id" type="radio" autocomplete="off" value="${formulario.id }">
															<span class="glyphicon glyphicon-ok"></span>
														</label>
													</div>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
							<div class="form-group">
								<div class="col-sm-2">				
									<button type="submit" class="btn btn-primary btn-lg">Vincular&nbsp;<i class="glyphicon glyphicon-floppy-disk"></i></button>
								</div>
							</div>
						</form:form>
					</c:if>	
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>