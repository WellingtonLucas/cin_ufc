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
<title>Formulários</title>

</head>
<body>
	<jsp:include page="../fragments/header.jsp" />

	<div class="container-fluid">
		<div class="row">
			<jsp:include page="../fragments/menu.jsp" />
			<div class="col-sm-8 col-md-10 main">
				<h2>
					<strong>Seus Formulários</strong> - <small>${usuario.nome } estes são os formulários criados por você.</small>
				</h2>
				<div class="panel panel-primary">
					<div class="panel-heading">
						<i class="glyphicon glyphicon-list-alt"></i>&nbsp;&nbsp;Formulários
					</div>
					<div class="panel-body">
						<jsp:include page="../fragments/mensagens.jsp" />
						<div class="row placeholders">
							<br>
							<c:if test="${not empty formularios }">
								<c:forEach var="formulario" items="${formularios}">
									<div class="col-lg-4">
										<article><h4>${formulario.titulo }</h4></article>
										<br>
										<p>
											<a class="btn btn-primary" href="<c:url value="formulario/${formulario.id }/detalhes" />">Detalhes »</a>
										</p>
									</div>
								</c:forEach>
							</c:if>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>