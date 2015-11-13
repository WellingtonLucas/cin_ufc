<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
	<title>CIN - Concurso de Ideias de Negocio</title>
	<link rel="icon"
		href="<c:url value="/resources/imagens/logo_ufc.png" />"
		type="image/png" sizes="16x16" />
	<link href="<c:url value="/webjars/bootstrap/3.3.5/css/bootstrap.min.css" />"	rel="stylesheet" />
	<link href="<c:url value="/resources/css/dashboard.css"/>"	rel="stylesheet" />
	<link href="<c:url value="/resources/css/bootstrapValidator.css" />" rel="stylesheet" />
	<link href="<c:url value="/resources/css/font-awesome.min.css"/>" rel="stylesheet"/>
	<link href="<c:url value="/resources/css/estilo.css"/>" rel="stylesheet" />
	
</head>

<body onload='document.f.j_username.focus();'>
	<div class="navbar navbar-default navbar-fixed-top">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar" aria-expanded="false"
					aria-controls="navbar">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<div class="navbar-brand"> 
					<strong>CIN</strong>
					<small>Concurso de Ideias de Negócio</small>
				</div>
			</div>
			<div id="navbar" class="navbar-collapse collapse">
				<form name="f" class="navbar-form navbar-right"
					action="<c:url value='j_spring_security_check' />" method='POST'>
					<div class="form-group">
						<c:if test="${not empty error}">
							<div class="error">${error}</div>
						</c:if>
						<c:if test="${not empty msg}">
							<div class="msg">${msg}</div>
						</c:if>
					</div>
					<div class="form-group">
						<input type="text" placeholder="Email" class="form-control"
							name='j_username'>
					</div>
					<div class="form-group">
						<input type="password" placeholder="Password" class="form-control"
							name='j_password'>
					</div>
					<button type="submit" class="btn btn-success" name="submit"
						value="Login">Entrar</button>
					<input class="btn btn-default" name="reset" type="reset"
						value="Limpar" />
				</form>
			</div>
		</div>
	</div>
	<div class="col-lg-6"></div>
	<div class="col-lg-6">
		
		<form:form id="cadastro" class="form-horizontal" role="form" commandName="usuario" 
		modelAttribute="usuario" method="POST" servletRelativeAction="/usuario/cadastre-se" >			
			<div></div>
			<!-- Form Name -->
			<h1>Crie uma conta</h1>
			<hr>
			<c:if test="${not empty erro}">
			<div class="alert alert-danger alert-dismissible" role="alert">
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
			
			<div class="form-group">					
				<div class="col-sm-4">
					<div class="error-validation"><form:errors path="nome"></form:errors></div>
					<form:input id="nome" path="nome" type="text" class="form-control" name="nome" placeholder="Nome" />
				</div>
				<div class="col-sm-4">
					<div class="error-validation"><form:errors path="sobreNome"></form:errors></div>
					<form:input id="snome" type="text" path="sobreNome"
						placeholder="Sobrenome" class="form-control" />
				</div>
			</div>	
			
			<div class="form-group">					
				<div class="col-sm-4">
					<div class="error-validation"><form:errors path="matricula"></form:errors></div>
					<form:input id="matricula" path="matricula" type="text" cssClass="form-control" placeholder="Matricula" />
				</div>
			</div>
				
			<div class="form-group">					
				<div class="col-sm-8">
					<div class="error-validation"><form:errors path="curso"></form:errors></div>
					<form:input id="curso" path="curso" type="text" cssClass="form-control" placeholder="Curso" />
				</div>
			</div>		
			
			<div class="form-group">					
				<div class="col-sm-8">
					<div class="error-validation"><form:errors path="email"></form:errors></div>
					<form:input id="email" type="email" path="email"
						placeholder="Email" class="form-control" />
				</div>
			</div>
			
			<div class="form-group">					
				<div class="col-sm-8">
					<div class="error-validation"><form:errors path="senha"></form:errors></div>			
					<form:input id="senha" type="password" path="senha" placeholder="Senha"
					class="form-control" />
				</div>
			</div>
			
			<div class="form-group">
				<div class="col-sm-8">															
					<button type="submit" id="cadastrar" name="cadastrar" class="btn btn-success btn-lg">Abrir
						uma conta</button>
				</div>
			</div>	
			<hr>		
		</form:form>

	</div>
	<script src="<c:url value="/webjars/jquery/2.1.0/jquery.js" />"></script>
	<script	src="<c:url value="/webjars/bootstrap/3.3.5/js/bootstrap.min.js" />"></script>
	<script src="<c:url value="/resources/js/funcoes2.js" />"></script>
	<script src="<c:url value="/resources/js/bootstrapValidator.min.js" />"></script>
	

</body>
</html>