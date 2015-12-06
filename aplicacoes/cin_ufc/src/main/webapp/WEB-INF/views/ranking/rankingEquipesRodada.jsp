<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">Melhores equipes da rodada</h3>
	</div>
	<div class="panel-body">
		<c:forEach var="saldo" items="${saldos }" varStatus="idEqui">
			<ul class="media-list">
				<li class="media">
					<div class="col-md-3 col-lg-3 " align="center">
						<a class="pull-left" href="#"> 
							<c:if test="${saldo.equipe.logo == null }">
								<img class="media-object img-circle"
								src="<c:url value="/resources/imagens/empty_profile.gif" />"
								alt="Foto de ${saldo.equipe.nome }">
							</c:if>
							<c:if test="${saldo.equipe.logo != null }">
								<img alt="Foto de ${saldo.equipe.nome} "
									src="data:${saldo.equipe.logo.extensao };base64,${saldo.equipe.logo.encode }"
									class="img-thumbnail img-responsive" />
							</c:if>
						</a>
					</div>
					<div class="container media-body">
						<div class="well well-lg">
							<h1 class="media-heading text-uppercase reviews"><span class="label label-success">${idEqui.count }º</span>
								${saldo.equipe.nome} 
							</h1>
							<h2>
								Saldo = ${saldo.saldoComFator} R$
							</h2>
							<p class="media-comment"><i class="glyphicon glyphicon-asterisk"></i>Parabéns pelo ótimo desempenho!</p>
						</div>
					</div>
				</li>
			</ul>
		</c:forEach>
	</div>
</div>
