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
	<title>Avaliações do aluno</title>
</head>
<body>
	<jsp:include page="../fragments/header.jsp" />
	
	<div class="section">
		<div class="container-fluid">
			<div class="row">
				<jsp:include page="../fragments/menu.jsp" />
				<div class="col-sm-8 col-md-10 main">
					<h2><strong>${jogo.nomeDoCurso }</strong> <small>${jogo.semestre }</small></h2>
					<h2><strong>Aluno: ${usuarioRequisitado.nome }</strong> - <small>Avaliações efetuadas pelo aluno</small></h2>
					<div class="panel panel-primary">
						<div class="panel-heading">
							<i class="fa fa-archive"></i>&nbsp;&nbsp;Avaliações
						</div>
						<div class="panel-body">
							<jsp:include page="../fragments/mensagens.jsp" />
					       	<div class="col-sm-12">
					       		<div class="table-responsive">
									<table id="tabela-avaliacoes-user" class="table table-striped table-hover">
										<thead>
											<tr>
												<th>Rodada</th>
												<th>Empresa</th>
												<th>Entrega</th>
												<th>Avaliação</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach var="resposta" items="${respostas}" varStatus="respostaId">
												<tr>
													<td>
														<a href="<c:url value="/jogo/${jogo.id}/rodada/${resposta.entrega.rodada.id }/detalhes"></c:url>">${resposta.entrega.rodada.nome}</a>
													</td>
													<td>
														<a href="<c:url value="/jogo/${jogo.id}/equipe/${resposta.entrega.equipe.id }"></c:url>">${resposta.entrega.equipe.nome}</a>
													</td>
													<td>
														<a href="<c:url value="/documento/downloadDocumento/${resposta.entrega.documento.id }"></c:url>">${resposta.entrega.documento.nomeOriginal}</a>
													</td>
													<td>
														<a href="<c:url value="/usuario/${usuarioRequisitado.id }/jogo/${jogo.id}/resposta/${resposta.id }/avaliacao" ></c:url>">
															<button class="btn btn-primary">Ver avaliação</button>
														</a>
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>