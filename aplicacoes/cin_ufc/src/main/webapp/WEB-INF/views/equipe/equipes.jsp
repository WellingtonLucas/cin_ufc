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
<title>Empresas</title>

</head>
<body>
	<jsp:include page="../fragments/header.jsp" />

	<div class="container-fluid">
		<div class="row">
			<jsp:include page="../fragments/menu.jsp" />
			<div class="col-sm-8 col-md-10 main">
				<h2>
					<strong>${jogo.nomeDoCurso }</strong> <small>${jogo.semestre }</small>
				</h2>
				<div class="panel panel-primary">
					<div class="panel-heading">
						<i class="fa fa-industry"></i>&nbsp;&nbsp;Empresas
					</div>
					<div class="panel-body">
						<jsp:include page="../fragments/mensagens.jsp" />
						<div class="row placeholders">
							<c:if test="${not empty equipes }">
								<c:forEach var="equipe" items="${equipes}">
									<div class="col-sm-3">
							            <div class="card">
							                <canvas class="header-bg" width="250" height="70" id="header-blur"></canvas>
							                <div class="avatar">
							                    <img src="" alt="Logo da Equipe" />
							                </div>
							                <div class="content">
							                    <h4>${equipe.nome }</h4> <br>
							                    
							                    <p> Veja mais detalhes</p>    
							                    <p>
							                    	<a data-toggle="tooltip" data-placement="top" title="Veja detalhes da empresa."
							                    		class="btn btn-success" href="<c:url value="equipe/${equipe.id }" />">Detalhes</a>
							                    </p>
							                </div>
							            </div>
							        </div>
							        <c:if test="${equipe.logo ==null }">
							       		<img class="src-image"  src="<c:url value="/resources/imagens/boxvazia.gif" />"/>
							       	</c:if>
							        <c:if test="${equipe.logo !=null }">
							        	<img class="src-image" src="data:${equipe.logo.extensao };base64,${equipe.logo.encode }"/>
							        </c:if>
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