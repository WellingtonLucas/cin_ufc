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
		<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2>${usuario.nome } ${usuario.sobreNome }</h2>
			<h3>Informações</h3>
			<hr>
			<c:if test="${not empty erro}">
				<div class="alert alert-warning alert-dismissible" role="alert">
					<button type="button" class="close" data-dismiss="alert">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<c:out value="${erro}"></c:out>
				</div>
			</c:if>
			
			<ul class="nav nav-tabs">
				<li class="active"><a href="#geral" data-toggle="tab">Profile</a></li>
				<li><a href="#profile" data-toggle="tab">Password</a></li>
			</ul>
			<form:form id="perfil" role="form" class="form-horizontal"
				commandName="usuario" servletRelativeAction="atualizar"
				method="POST">

				<div id="myTabContent" class="tab-content">
					<div class="tab-pane active in" id="geral">
						<form:hidden path="id" value="${usuario.id }" />
						<form:hidden path="papel" value="${usuario.papel }" />
						<form:hidden path="habilitado" value="${usuario.habilitado }" />
						<form:hidden path="email" value="${usuario.email }" />
						<br>
						<div class="form-group">
							<label for="nome" class="col-sm-1 control-label">Nome:</label>
							<div class="col-sm-4">
								<div class="error-validation">
									<form:errors path="nome"></form:errors>
								</div>
								<form:input id="nome" path="nome" type="text"
									class="form-control" name="nome" placeholder="Nome" />
							</div>
							<label for="snome" class="col-sm-2 control-label">Sobrenome:</label>
							<div class="col-sm-4">
								<div class="error-validation">
									<form:errors path="sobreNome"></form:errors>
								</div>
								<form:input id="snome" type="text" path="sobreNome"
									placeholder="Sobrenome" class="form-control" />
							</div>
						</div>
						<div class="form-group">
							<label for="curso" class="col-sm-1 control-label">Matricula:</label>
							<div class="col-sm-4">
								<div class="error-validation">
									<form:errors path="matricula"></form:errors>
								</div>
								<form:input id="matricula" path="matricula" type="text"
									cssClass="form-control" placeholder="Matricula" />
							</div>
						</div>
						<div class="form-group">
							<label for="curso" class="col-sm-1 control-label">Curso:</label>
							<div class="col-sm-8">
								<div class="error-validation">
									<form:errors path="curso"></form:errors>
								</div>
								<form:input id="curso" path="curso" type="text"
									cssClass="form-control" placeholder="Curso" />
							</div>
						</div>

						<div class="form-group">
							<label for="email" class="col-sm-1 control-label">Email:</label>
							<div class="col-sm-8">
								<div class="error-validation">
									<form:errors path="email"></form:errors>
								</div>
								<form:input id="email" type="email" path="email"
									placeholder="Email" class="form-control" disabled="true" />
							</div>
						</div>

					</div>
					<div class="tab-pane fade" id="profile">
						<br>
						<div class="form-group">
							<label for="senha" class="col-sm-1 control-label">Senha:</label>
							<div class="col-sm-4">
								<input type="password" id="senha" name="senha"
									placeholder="Preencha caso queira alterar" class="form-control" />
							</div>
						</div>
					</div>
					<div class="col-sm-2">
						<button type="submit" class="btn btn-primary btn-lg">
							Salvar</button>
					</div>
				</div>
			</form:form>
			<br>
		</div>
	</div>
	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>
