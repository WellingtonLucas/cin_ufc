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
		<div class="col-sm-8 col-md-10 main">
			<jsp:include page="../fragments/mensagens.jsp" />
			<div class="panel with-nav-tabs panel-primary">
				<div class="panel-heading">
					<ul class="nav nav-tabs">
						<li class="active"><a href="#geral" data-toggle="tab">Profile</a></li>
						<li><a href="#profile" data-toggle="tab">Password</a></li>
					</ul>
				</div>
				<div class="panel-body">
					<form:form id="perfil" role="form" class="form-horizontal"
						commandName="usuario" enctype="multipart/form-data" servletRelativeAction="atualizar"
						method="POST">
						<div id="myTabContent" class="tab-content">
							<div class="tab-pane active in" id="geral">
								<form:hidden path="id"/>
								<form:hidden path="papel"/>
								<form:hidden path="habilitado"/>
								<form:hidden path="email"/>
								<div class="form-group">
									<label for="nome" class="col-sm-1 control-label">Nome:</label>
									<div class="col-sm-3">
										<form:input id="nome" path="nome" type="text"
											class="form-control" name="nome" placeholder="Nome" />
										<div class="error-validation">
											<form:errors path="nome"></form:errors>
										</div>
									</div>
									<label for="sobreNome" class="col-sm-2 control-label">Sobrenome:</label>
									<div class="col-sm-3">
										<form:input type="text" path="sobreNome"
											placeholder="Sobrenome" class="form-control" />
										<div class="error-validation">
											<form:errors path="sobreNome"></form:errors>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label for="matricula" class="col-sm-1 control-label">Matricula:</label>
									<div class="col-sm-3">
										<form:input path="matricula" type="text"
											cssClass="form-control" placeholder="Matricula" />
										<div class="error-validation">
											<form:errors path="matricula"></form:errors>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label for="curso" class="col-sm-1 control-label">Curso:</label>
									<div class="col-sm-8">
										<form:input path="curso" type="text"
											cssClass="form-control" placeholder="Curso" />
										<div class="error-validation">
											<form:errors path="curso"></form:errors>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label for="email" class="col-sm-1 control-label">Email:</label>
									<div class="col-sm-8">
										<form:input id="email" type="email" path="email"
											placeholder="Email" class="form-control" disabled="true" />
										<div class="error-validation">
											<form:errors path="email"></form:errors>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label for="idFoto" class="col-sm-1 control-label">Foto:</label>
									<div class="col-sm-8">
										<input type="file" id="idfoto" name="anexo"></input>
									</div>
								</div>
								<c:if test="${action eq 'perfil' }">
									<div class="form-group">
										<div class="col-sm-1"></div>
										<div class="col-sm-8">
											<p class="bg-info">Para mudar sua foto escolha outra imagem.</p>
										</div>
									</div>
								</c:if>
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
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>
