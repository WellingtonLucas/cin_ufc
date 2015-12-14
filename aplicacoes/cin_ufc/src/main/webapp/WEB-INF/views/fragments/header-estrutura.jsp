<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="icon" href="<c:url value="/resources/imagens/logo_ufc.png" />" type="image/png" sizes="16x16" />
<link href="<c:url value="/resources/css/datepicker3.css"/>" rel="stylesheet" />
<link href="<c:url value="/resources/css/tabs.css"/>" rel="stylesheet" />
<link href="<c:url value="/webjars/bootstrap/3.3.5/css/bootstrap.min.css" />" rel="stylesheet" />
<link href="<c:url value="/webjars/datatables/1.10.9/css/jquery.dataTables.min.css" />" rel="stylesheet" />
<link href="<c:url value="/resources/css/dashboard.css"/>" rel="stylesheet" />
<link href="<c:url value="/resources/css/bootstrapValidator.css" />" rel="stylesheet" />
<link href="<c:url value="/resources/css/bootstrap-select.min.css" />" rel="stylesheet" />
<link href="<c:url value="/resources/css/select2.css"/>" rel="stylesheet"/>
<link href="<c:url value="/resources/css/select2-bootstrap.css"/>" rel="stylesheet"/>
<link href="<c:url value="/resources/css/fileinput.min.css"/>" rel="stylesheet"/>
<link href="<c:url value="/resources/css/font-awesome.min.css"/>" rel="stylesheet"/>
<link href="<c:url value="/resources/css/estilo.css"/>" rel="stylesheet" />
<link href="<c:url value="/resources/css/timeline-dotted.css"/>" rel="stylesheet" />
<link href="<c:url value="/resources/css/dynamic-avatar-blur.css"/>" rel="stylesheet" />
<link href="<c:url value="/resources/css/simple-user-profile.css"/>" rel="stylesheet" />
<link href="<c:url value="/resources/css/ranking.css"/>" rel="stylesheet" />
<c:if test="${action == 'rodadas' }">
	<link href="<c:url value="/resources/css/smooth-animated-thumbnails.css"/>" rel="stylesheet" />
</c:if>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

