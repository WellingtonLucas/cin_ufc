<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<html>
<head>
	<jsp:include page="../fragments/header-estrutura.jsp" />
	<title>Resposta -  ${usuarioRequisitado.nome }</title>
</head>

<body>
	<jsp:include page="../fragments/header.jsp" />
	
	<div class="section">
		<div class="container-fluid">
			<div class="row">
				<jsp:include page="../fragments/menu.jsp" />
				<div class="col-sm-8 col-sm-offset-3 col-md-10 col-md-offset-2 main">
					<div class="col-sm-12">
						<h2><strong>${formulario.titulo }</strong> - <small> Equipe: ${resposta.entrega.equipe.nome }. 
						Rodada: ${resposta.entrega.rodada.nome }. 
							</small>
						</h2>						
						<hr>				
						<c:if test="${not empty erro}">
							<div class="alert alert-warning alert-dismissible" role="alert">
								<button type="button" class="close" data-dismiss="alert">
									<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
								</button>
								<c:out value="${erro}"></c:out>
							</div>
						</c:if>
						<c:if test="${not empty info}">
							<div class="alert alert-success alert-dismissible" role="alert">
								<button type="button" class="close" data-dismiss="alert">
									<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
								</button>
								<c:out value="${info}"></c:out>
							</div>
						</c:if>
					</div>
					<div class="col-sm-12">
						<c:forEach var="pergunta" items="${formulario.perguntas}" varStatus="questId">
							<div class="panel panel-default">
								<div class="panel-heading">
									<label>Questão ${questId.count}:
										${pergunta.descricao }
									</label>											
								</div>
								<div class="radio panel-body">
									<label class="col-sm-10">
										<c:if test="${pergunta.opcoes[0].id == resposta.opcoes[questId.index].id }">
											<input type="radio" name="opcao${questId.index}" id="opcaoR1" disabled="disabled" checked="checked">										
										</c:if>
										<c:if test="${!(pergunta.opcoes[0].id == resposta.opcoes[questId.index].id) }">
											<input type="radio" name="opcao${questId.index}" id="opcaoR1" disabled="disabled">
										</c:if>
										<c:if test="${pergunta.opcoes[0].id == resposta.entrega.gabarito.opcoes[questId.index].id }">
											<c:if test="${liberaGaba }">
												<p class="bg-success">${pergunta.opcoes[0].descricao }</p>
											</c:if>
											<c:if test="${!liberaGaba }">
												<p>${pergunta.opcoes[0].descricao }</p>
											</c:if>
										</c:if>
										<c:if test="${pergunta.opcoes[0].id != resposta.entrega.gabarito.opcoes[questId.index].id }">
											<p>${pergunta.opcoes[0].descricao }</p>
										</c:if>
									</label>
								
									<label class="col-sm-10">
										<c:if test="${pergunta.opcoes[1].id == resposta.opcoes[questId.index].id }">
											<input type="radio" name="opcao${questId.index}" id="opcaoR2" disabled="disabled" checked="checked">										
										</c:if>
										<c:if test="${!(pergunta.opcoes[1].id == resposta.opcoes[questId.index].id) }">
											<input type="radio" name="opcao${questId.index}" id="opcaoR2" disabled="disabled">
										</c:if>
										<c:if test="${pergunta.opcoes[1].id == resposta.entrega.gabarito.opcoes[questId.index].id }">
											<c:if test="${liberaGaba }">
												<p class="bg-success">${pergunta.opcoes[1].descricao }</p>
											</c:if>
											<c:if test="${!liberaGaba }">
												<p>${pergunta.opcoes[3].descricao }</p>
											</c:if>
										</c:if>
										<c:if test="${pergunta.opcoes[1].id != resposta.entrega.gabarito.opcoes[questId.index].id }">
											<p>${pergunta.opcoes[1].descricao }</p>
										</c:if>
									</label>
									<label class="col-sm-10">
										<c:if test="${pergunta.opcoes[2].id == resposta.opcoes[questId.index].id }">
											<input type="radio" name="opcao${questId.index}" id="opcaoR3" disabled="disabled" checked="checked">										
										</c:if>
										<c:if test="${!(pergunta.opcoes[2].id == resposta.opcoes[questId.index].id) }">
											<input type="radio" name="opcao${questId.index}" id="opcaoR3" disabled="disabled">
										</c:if>
										<c:if test="${pergunta.opcoes[2].id == resposta.entrega.gabarito.opcoes[questId.index].id }">
											<c:if test="${liberaGaba }">
												<p class="bg-success">${pergunta.opcoes[2].descricao }</p>
											</c:if>
											<c:if test="${!liberaGaba }">
												<p>${pergunta.opcoes[3].descricao }</p>
											</c:if>
										</c:if>
										<c:if test="${pergunta.opcoes[2].id != resposta.entrega.gabarito.opcoes[questId.index].id }">
											<p>${pergunta.opcoes[2].descricao }</p>
										</c:if>
									</label>
									<label class="col-sm-10">
										<c:if test="${pergunta.opcoes[3].id == resposta.opcoes[questId.index].id }">
											<input type="radio" name="opcao${questId.index}" id="opcaoR4" disabled="disabled" checked="checked">										
										</c:if>
										<c:if test="${!(pergunta.opcoes[3].id == resposta.opcoes[questId.index].id) }">
											<input type="radio" name="opcao${questId.index}" id="opcaoR4" disabled="disabled">
										</c:if>
										<c:if test="${pergunta.opcoes[3].id == resposta.entrega.gabarito.opcoes[questId.index].id }">
											<c:if test="${liberaGaba }">
												<p class="bg-success">${pergunta.opcoes[3].descricao }</p>
											</c:if>
											<c:if test="${!liberaGaba }">
												<p>${pergunta.opcoes[3].descricao }</p>
											</c:if>
										</c:if>
										<c:if test="${pergunta.opcoes[3].id != resposta.entrega.gabarito.opcoes[questId.index].id }">
											<p>${pergunta.opcoes[3].descricao }</p>
										</c:if>
									</label>
									<label class="col-sm-10">
										<c:if test="${pergunta.opcoes[4].id == resposta.opcoes[questId.index].id }">
											<input type="radio" name="opcao${questId.index}" id="opcaoR5" disabled="disabled" checked="checked">										
										</c:if>
										<c:if test="${!(pergunta.opcoes[4].id == resposta.opcoes[questId.index].id) }">
											<input type="radio" name="opcao${questId.index}" id="opcaoR5" disabled="disabled">
										</c:if>
										<c:if test="${pergunta.opcoes[4].id == resposta.entrega.gabarito.opcoes[questId.index].id }">
											<c:if test="${liberaGaba }">
												<p class="bg-success">${pergunta.opcoes[4].descricao }</p>
											</c:if>
											<c:if test="${!liberaGaba }">
												<p>${pergunta.opcoes[4].descricao }</p>
											</c:if>
										</c:if>
										<c:if test="${pergunta.opcoes[4].id != resposta.entrega.gabarito.opcoes[questId.index].id }">
											<p>${pergunta.opcoes[4].descricao }</p>
										</c:if>
									</label>
								</div>
							</div>
						</c:forEach>
						
						<div class="col-sm-12"></div>
						<label class="col-sm-1 field">Nota:</label>
						<div class="col-sm-11 field-value">
							<c:if test="${empty formulario.nota }">
								<label>-</label>
							</c:if>
							<label>${formulario.nota }</label>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
				
	<jsp:include page="../fragments/footer.jsp" />	
</body>
</html>
