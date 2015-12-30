<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
	<head>
		<jsp:include page="../fragments/header-estrutura.jsp" />
		<title>${jogo.nomeDoCurso }</title>
	</head>
	<body>
		<jsp:include page="../fragments/header.jsp" />
		<div class="section">
			<div class="container-fluid">
				<div class="row">
					<jsp:include page="../fragments/menu.jsp" />
					<div class="col-sm-8 col-md-10 main">
						<h2><strong>${jogo.nomeDoCurso }</strong> <small>${jogo.semestre }</small></h2>
						<div class="panel panel-primary">
							<div class="panel-heading">
								<i class="fa fa-line-chart"></i>&nbsp;&nbsp;Rankings Gerais
							</div>
							<div class="panel-body">
								<jsp:include page="../fragments/mensagens.jsp" />
						        <p class="lead">Escolha uma das opções abaixo para visualizar os respectivos rankings gerais.</p>
						        <div>
						        	<a data-toggle="tooltip" data-placement="top" title="Ranking com os alunos com maior retorno de investimento no jogo."
						        		class="btn btn-lg btn-success col-sm-3" href="<c:url value="/jogo/${jogo.id }/rankings-alunos"></c:url>" 
						        		role="button"><i class="fa fa-money"></i>&nbsp;&nbsp;Investidores
						        	</a>
						        </div>
						        <div class="col-sm-1"></div>
						        <div>
						        	<a data-toggle="tooltip" data-placement="top" title="Ranking com as equipes com maiores investimentos recebidos no jogo."
						        		class="btn btn-lg btn-primary col-sm-3" href="<c:url value="/jogo/${jogo.id }/rankings-equipes"></c:url>" 
						        		role="button"><i class="fa fa-industry"></i>&nbsp;&nbsp;Empresas
						        	</a>
						        </div>
					        </div>
					    </div> 
					    <c:if test="${rankingAlunos}">
					    	<jsp:include page="rankingAlunosJogo.jsp" />
					    </c:if>
					    <c:if test="${rankingEquipes}">
					    	<jsp:include page="rankingEquipesJogo.jsp" />
					    </c:if>
				    </div>
				</div>
			</div>
		</div>
		<jsp:include page="../fragments/footer.jsp" />
	</body>
</html>