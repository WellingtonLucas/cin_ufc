<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">Melhores avaliadores do jogo</h3>
	</div>
	<div class="panel-body">
		<c:forEach var="saldo" items="${saldos }" varStatus="idAlu">
			<ul class="media-list">
				<li class="media">
					<div class="col-md-3 col-lg-3 " align="center">
						<a class="pull-left" href="#"> 
							<c:if test="${saldo.apostador.foto == null }">
								<img class="img-circle"  width="200" height="200"
								src="<c:url value="/resources/imagens/empty_profile.gif" />"
								alt="Foto de ${saldo.apostador.nome }" />
							</c:if>
							<c:if test="${saldo.apostador.foto != null }">
								<img alt="Foto de ${saldo.apostador.nome} "
									src="data:${saldo.apostador.foto.extensao };base64,${saldo.apostador.foto.encode }"
									class="img-circle"  width="200" height="200" />
							</c:if>
						</a>
					</div>
					<div class="container media-body">
						<div class="well well-lg">
							<h1 class="media-heading text-uppercase reviews"><span class="label label-success">${idAlu.count }º</span>
								${saldo.apostador.nome} ${saldo.apostador.sobreNome}
							</h1>
							<h2>
								Saldo = R$ ${saldo.saldo}
							</h2>
							<p class="media-comment"><i class="glyphicon glyphicon-asterisk"></i>Parabéns pelo ótimo desempenho!</p>
						</div>
					</div>
				</li>
			</ul>
		</c:forEach>
	</div>
</div>
