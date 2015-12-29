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
		<title>Informações de ${usuarioParticipante.nome }</title>
	</head>
<body>
	<jsp:include page="../fragments/header.jsp" />
	
	<div class="container-fluid">
		<div class="row">
		<jsp:include page="../fragments/menu.jsp" />		
			<div class="col-sm-8 col-md-10 main">
				<div class="panel panel-primary">
					<div class="panel-heading">
						<i class="fa fa-user"></i>&nbsp;&nbsp;${usuarioParticipante.nome } ${usuarioParticipante.sobreNome }
					</div>
					<div class="panel-body">
						<c:if test="${not empty erro}">
							<div class="alert alert-warning alert-dismissible" role="alert">
								<button type="button" class="close" data-dismiss="alert">
									<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
								</button>
								<c:out value="${erro}"></c:out>
							</div>
						</c:if>
						<div class="row">
							<div class="col-md-3 col-lg-3 " align="center">
								<c:if test="${usuarioParticipante.foto == null }">
									<img alt="Foto do usuário"
										src="<c:url value="/resources/imagens/empty_profile.gif" />"
										class="img-circle"  width="200" height="200" />
								</c:if>
								<c:if test="${usuarioParticipante.foto != null }">
									<img alt="Foto do usuário"
										src="data:${usuarioParticipante.foto.extensao };base64,${usuarioParticipante.foto.encode }"
										class="img-circle"  width="200" height="200" />
								</c:if>
							</div>
							<div class=" col-md-9 col-lg-9 ">
								<table class="table table-user-information">
									<tbody>
										<tr>
											<td>Curso:</td>
											<td>${usuarioParticipante.curso }</td>
										</tr>
										<c:if test="${permissao == 'professor' || permissao == 'alunoLogado'}">
											<tr>
												<td>Matricula:</td>
												<td>${usuarioParticipante.matricula }</td>
											</tr>
										</c:if>
										<tr>
											<td>Email:</td>
											<td>${usuarioParticipante.email }</td>
										</tr>
										<tr>
											<td>Equipe:</td>
											<td>${equipe.nome }</td>
										</tr>
									</tbody>
								</table>
								<c:if test="${permissao == 'professor' || permissao == 'alunoLogado' }">
									<a href="<c:url value="/usuario/${usuarioParticipante.id }/jogo/${jogo.id }/avaliacoes" />" class="btn btn-primary">Avaliações Realizadas</a>
								</c:if> 
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../fragments/footer.jsp" />	
</body>
</html>
