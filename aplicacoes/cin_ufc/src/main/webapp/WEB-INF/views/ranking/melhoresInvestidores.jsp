<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">Melhores investidores da rodada</h3>
	</div>
	<div class="panel-body">
		<c:forEach var="aposta" items="${apostas }" varStatus="idAlu">
			<ul class="media-list">
				<li class="media">
					<div class="col-md-3 col-lg-3 " align="center">
						<a class="pull-left" href="#"> 
							<c:if test="${aposta.apostador.foto == null }">
								<img class="img-circle"  width="200" height="200"
								src="<c:url value="/resources/imagens/empty_profile.gif" />"
								alt="Foto de ${aposta.apostador.nome }" />
							</c:if>
							<c:if test="${aposta.apostador.foto != null }">
								<img alt="Foto de ${aposta.apostador.nome} "
									src="data:${aposta.apostador.foto.extensao };base64,${aposta.apostador.foto.encode }"
									class="img-circle"  width="200" height="200" />
							</c:if>
						</a>
					</div>
					<div class="container media-body">
						<div class="well well-lg">
							<h1 class="media-heading text-uppercase reviews"><span class="label label-success">${idAlu.count }º</span>
								${aposta.apostador.nome} ${aposta.apostador.sobreNome}
							</h1>
							<h2>
								Saldo = R$ <fmt:formatNumber currencyCode="BRL" maxFractionDigits="2" value="${aposta.retorno}" />
							</h2>
							<p class="media-comment"><i class="glyphicon glyphicon-asterisk"></i>Parabéns pelas escolhas!</p>
						</div>
					</div>
				</li>
			</ul>
		</c:forEach>
	</div>
</div>
