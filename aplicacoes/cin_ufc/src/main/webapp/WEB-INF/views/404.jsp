<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>
<jsp:include page="fragments/header-estrutura.jsp" />
<title>404 - Página não encontrada</title>
</head>
<body>
	<div class="container">
		<jsp:include page="fragments/header.jsp" />
		
		<div class = "error">
		    <div class="col-lg-8 col-lg-offset-2 text-center">
				<div class="logo">
					<h1>404</h1>
				</div>
				<p class="lead text-muted">${message}</p>
				<br>
				<div class="col-lg-6 col-lg-offset-3">
					<a class="btn btn-warning btn-group-justified" href="javascript:history.back();">Voltar</a>
				</div>
			</div>
		</div>
	</div>
	
	<jsp:include page="fragments/footer.jsp" />
</body>
</html>

