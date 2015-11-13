<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<html>
<head>
	<title>403 - Acesso negado</title>
	<link href="<c:url value="/webjars/bootstrap/3.1.1/css/bootstrap.min.css" />" rel="stylesheet" />
	<script src="<c:url value="/webjars/jquery/2.1.0/jquery.js" />"></script>
	<script src="<c:url value="/webjars/bootstrap/3.1.1/js/bootstrap.min.js" />"></script>

</head>
<body>
	<div class="container">
		<div class="col-lg-8 col-lg-offset-2 text-center">
			<div class="logo">
				<h1>403</h1>
				<p class="lead text-muted">${message}</p>
			</div>
			<br>
		</div>
	</div>
</body>
</html>