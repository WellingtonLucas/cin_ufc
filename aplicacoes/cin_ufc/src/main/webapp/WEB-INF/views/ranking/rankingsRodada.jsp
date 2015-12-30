<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
	<head>
		<jsp:include page="../fragments/header-estrutura.jsp" />
		<title>${rodada.nome } Rakings</title>
	</head>
	<body>
		<jsp:include page="../fragments/header.jsp" />
		<div class="section">
			<div class="container-fluid">
				<div class="row">
					<jsp:include page="../fragments/menu.jsp" />
					<div class="col-sm-8 col-md-10 main">
						<div class="panel panel-primary">
							<div class="panel-heading">
								<i class="fa fa-line-chart"></i>&nbsp;&nbsp;Rankings Rodada
							</div>
							<div class="panel-body">
								<jsp:include page="../fragments/mensagens.jsp" />
						        <p class="lead">Escolha uma das opções abaixo para visualizar os respectivos Rankings.</p>
						        <div>
						        	<a data-toggle="tooltip" data-placement="top" title="Ranking com os melhores avaliadores da rodada."
						        	 	class="btn btn-lg btn-info col-sm-3" 
						        	 	href="<c:url value="/jogo/${jogo.id }/rodada/${rodada.id}/alunos"></c:url>" 
						        	 	role="button"><i class="fa fa-users"></i>&nbsp;&nbsp;Avaliadores
						        	 </a>
						        </div>
						        <div class="col-sm-1"></div>
						        <div>
						        	<a class="btn btn-lg btn-primary col-sm-3" data-toggle="tooltip" data-placement="top" title="Ranking com as equipes com maiores investimentos da rodada."
						        		href="<c:url value="/jogo/${jogo.id }/rodada/${rodada.id}/equipes"></c:url>" 
						        		role="button"><i class="fa fa-industry"></i>&nbsp;&nbsp;Empresas
						        	</a>
						        </div>
						        <div class="col-sm-1"></div>
						        <div>
						        	<a class="btn btn-lg btn-success col-sm-3" data-toggle="tooltip" data-placement="top" title="Ranking com os alunos com maior retorno de investimento na rodada."
						        		href="<c:url value="/jogo/${jogo.id }/rodada/${rodada.id}/investidores"></c:url>" 
						        		role="button"><i class="fa fa-money"></i>&nbsp;&nbsp;Investidores
						        	</a>
						        </div>
						    </div>
					    </div>
					    <c:if test="${notas != null && rankingAlunos}">
					    	<jsp:include page="rankingAlunosRodada.jsp" />
					    </c:if>
					    <c:if test="${saldos != null && rankingEquipes}">
					    	<jsp:include page="rankingEquipesRodada.jsp" />
					    </c:if>
					    <c:if test="${apostas != null && melhoresInvestidores}">
					    	<jsp:include page="melhoresInvestidores.jsp" />
					    </c:if>
				    </div>
				</div>
			</div>
		</div>
		<jsp:include page="../fragments/footer.jsp" />
	</body>
</html>