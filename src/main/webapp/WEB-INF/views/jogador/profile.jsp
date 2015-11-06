<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html>
<head>
<jsp:include page="../fragments/header-estrutura.jsp" />
<title>Perfil</title>
</head>

<body>
	<jsp:include page="../fragments/header.jsp" />

	<div class="container-fluid">
		<jsp:include page="../fragments/menu.jsp" />
		<div class="row">
			<div class="col-sm-8 col-sm-offset-4 col-md-10 col-md-offset-2 toppad">
				<div class="panel panel-primary">
					<div class="panel-heading">
						<h3 class="panel-title">${usuario.nome } ${usuario.sobreNome }</h3>
					</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-md-3 col-lg-3 " align="center">
								<c:if test="${usuario.foto == null }">
									<img alt="Foto do usuário"
										src="<c:url value="/resources/imagens/empty_profile.gif" />"
										class="img-circle img-responsive" />
								</c:if>
								<c:if test="${usuario.foto != null }">
									<img alt="Foto do usuário"
										src="data:${usuario.foto.extensao };base64,${usuario.foto.encode }"
										class="img-thumbnail img-responsive" />
								</c:if>
							</div>

							<div class=" col-md-9 col-lg-9 ">
								<table class="table table-user-information">
									<tbody>
										<tr>
											<td>Curso:</td>
											<td>${usuario.curso }</td>
										</tr>
										<tr>
											<td>Matricula:</td>
											<td>${usuario.matricula }</td>
										</tr>
										<tr>
											<td>Email:</td>
											<td>${usuario.email }</td>
										</tr>
									</tbody>
								</table>

								<a href="<c:url value="/jogo/listar" />" class="btn btn-primary">Meus Jogos</a> 
								<a href="<c:url value="/formularios" />"  class="btn btn-primary">Meus Formulários</a>
							</div>
						</div>
					</div>
					<div class="panel-footer">
						<span> 
							<a href="<c:url value="/usuario/perfil" />"
								data-original-title="Edit this user" data-toggle="tooltip"
								type="button" class="btn btn-warning">
								Editar
								<i class="glyphicon glyphicon-edit"></i>
							</a>
						</span>
					</div>

				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>
