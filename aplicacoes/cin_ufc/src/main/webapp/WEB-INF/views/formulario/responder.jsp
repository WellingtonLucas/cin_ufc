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
	<title>${entrega.rodada.nome }</title>
	<c:if test="${action eq 'responder' }">
		<c:set var="url" value="/${jogo.id}/entrega/${entrega.id }/formulario/${formulario.id }/responder"></c:set>
	</c:if>
</head>

<body>
	<jsp:include page="../fragments/header.jsp" />
	
	<div class="section">
		<div class="container-fluid">
			<div class="row">
				<jsp:include page="../fragments/menu.jsp" />
				<div class="col-sm-8 col-md-10 main">
					<h2><strong>${formulario.titulo }</strong></h2>						
					<div class="panel panel-primary">
						<div class="panel-heading">
							<strong>Equipe avaliada: ${entrega.equipe.nome }</strong>
						</div>
						<div class="panel-body">
							<jsp:include page="../fragments/mensagens.jsp" />
							<form:form id="responderForm" role="form" commandName="resposta" class="form-horizontal" method="POST"
							servletRelativeAction="${url }" >
								<div class="col-sm-12">
									<form:hidden path="id" value="${resposta.id }"/>
									<c:if test="${formulario != null }">
										
										<c:forEach var="pergunta" items="${formulario.perguntas}" varStatus="questId">
											<div class="panel panel-default">
												<div class="panel-heading">
													<label>Questão ${questId.count}:
														${pergunta.descricao }
													</label>											
												</div>
													<div class="radio panel-body">
														<label class="col-sm-10">
															<input type="radio" name="opcoes[${questId.index }].id" value="${pergunta.opcoes[0].id }"/>
															${pergunta.opcoes[0].descricao }
														</label>
													
														<label class="col-sm-10">
															<input type="radio" name="opcoes[${questId.index }].id" value="${pergunta.opcoes[1].id }" />
															${pergunta.opcoes[1].descricao }
														</label>
														<label class="col-sm-10">
															<input type="radio" name="opcoes[${questId.index }].id" value="${pergunta.opcoes[2].id }" />
															${pergunta.opcoes[2].descricao }
														</label>
														<label class="col-sm-10">
															<input type="radio" name="opcoes[${questId.index }].id" value="${pergunta.opcoes[3].id }" />
															${pergunta.opcoes[3].descricao }
														</label>
														<label class="col-sm-10">
															<input type="radio" name="opcoes[${questId.index }].id" value="${pergunta.opcoes[4].id }" />
															${pergunta.opcoes[4].descricao }
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
									</c:if>	
								</div>
								<div class="col-sm-12">
									<div class="row placeholders">
										<ul class="list-group">							
											<li class="media"><hr></li>							
										</ul>
										<div class="form-group">
											<div class="col-sm-2">				
												<button type="submit" class="btn btn-primary btn-lg">Responder&nbsp;<i class="glyphicon glyphicon-floppy-disk"></i></button>
											</div>
										</div>
									</div>
								</div>
							</form:form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
				
	<jsp:include page="../fragments/footer.jsp" />	
</body>
</html>
