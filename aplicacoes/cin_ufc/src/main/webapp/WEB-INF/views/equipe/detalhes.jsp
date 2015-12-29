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
	<title>Informações da Empresa</title>
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
							Detalhes da Empresa
						</div>
						<div class="panel-body">
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
							<div class="row">
								<div class="col-md-3 col-lg-3 " align="center">
									<c:if test="${ equipe.logo != null}">
										<img class="img-circle"  width="200" height="200" 
										src="data:${equipe.logo.extensao };base64,${equipe.logo.encode }" 
										alt="Logo da equipe" />
									</c:if>
									<c:if test="${ equipe.logo == null}">
										<img class="img-circle"  width="200" height="200"  
										src="<c:url value="/resources/imagens/boxvazia.gif" />" 
										alt="Logo da equipe" />
									</c:if>
								</div>
								<div class=" col-md-9 col-lg-9 ">
									<table class="table table-user-information">
										<tbody>
											<tr>
												<td><h4><strong>Nome: ${equipe.nome }</strong></h4></td>
											</tr>
											<tr>
												<td>
													<h4><strong>
													Saldo (R$): &nbsp;&nbsp;<fmt:formatNumber currencyCode="BRL" maxFractionDigits="2" value="${equipe.saldo }" />
														</strong>
													</h4>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>	
							<h4><strong><label class="col-sm-2 field">Ideia de Negócio:</label></strong></h4>
							<div class="col-sm-10 field-value">
								<c:if test="${empty equipe.ideiaDeNegocio }">
									<label>-</label>
								</c:if>
								<c:if test="${not empty equipe.ideiaDeNegocio }">
									<article>
										<label>
											${equipe.ideiaDeNegocio }
										</label>
									</article>
								</c:if>
							</div>
						</div>
						<c:if test="${(permissao eq 'professor') || (permissao eq 'membro') }">
							<div class="panel-footer">
								<a id="editar" href="<c:url value="/jogo/${jogo.id}/equipe/${equipe.id }/editar" ></c:url>">
									<button class="btn btn-primary btn-lg">Editar&nbsp;<i class="fa fa-edit"></i></button>
								</a>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<c:if test="${permissao eq 'professor' }">
									<c:if test="${equipe.status == true}">									
										<a id="inativar" data-toggle="modal" data-target="#confirm-inativar-equipe" href="#" 
											data-href="<c:url value="/jogo/${jogo.id}/equipe/${equipe.id }/inativar">
											</c:url>" data-name="${equipe.nome }">
											<button class="btn btn-warning btn-lg">
												Inativar&nbsp;<i class="glyphicon glyphicon-ban-circle"></i>
											</button>
										</a>
									</c:if>
									<c:if test="${equipe.status == false }">
										<a id="ativar" data-toggle="modal" data-target="#confirm-ativar-equipe" href="#" 
											data-href="<c:url value="/jogo/${jogo.id}/equipe/${equipe.id }/ativar">
											</c:url>" data-name="${equipe.nome }">
											<button class="btn btn-success btn-lg">
												Ativar&nbsp;<i class="glyphicon glyphicon-ok-circle"></i>
											</button>
										</a>
									</c:if>
									<a id="excluir" data-toggle="modal" data-target="#confirm-delete2" href="#" 
									data-href="<c:url value="/jogo/${jogo.id}/equipe/${equipe.id }/excluir"></c:url>" data-name="${equipe.nome }">
										<button class="btn btn-danger btn-lg pull-right">Excluir&nbsp;<i class="fa fa-trash-o"></i></button>
									</a>
								</c:if>
							</div>
						</c:if>
					</div>
					<div class="panel panel-primary">
						<div class="panel-heading">
							Membros
						</div>
						<div class="panel-body">
							<div class="col-sm-12">
								<c:if test="${not empty equipe.alunos }">
									<table id="table_id" class="table table-striped table-hover">
										<thead>
											<tr>
												<th>Nome</th>
												<th>Sorenome</th>
												<th>Curso</th>
												<th>Email</th>
												<c:if test="${permissao eq 'professor' }">
													<th></th>
												</c:if>
											</tr>
										</thead>
										<tbody>
											<c:forEach var="usuario" items="${equipe.alunos}">
												<tr>
													<td>
														<a href="<c:url value="/usuario/${usuario.id}/detalhes/${jogo.id }"></c:url>">${usuario.nome}</a>
													</td>
													<td>${usuario.sobreNome}</td>
													<td>${usuario.curso } </td>
													<td>${usuario.email } </td>
													<c:if test="${permissao eq 'professor' }">
														<td>
															<a id="submeter" data-toggle="modal" data-target="#confirm-submit" href="#"
																data-href="<c:url value="${equipe.id }/usuario/${usuario.id}/desvincular" ></c:url>" data-name="${usuario.nome }">
																<button data-tooggle="tooltip"  data-placement="left" title="Desvincule ${usuario.nome } da empresa ${equipe.nome}" 
																 class="btn btn-primary">Desvincular&nbsp;<i class="glyphicon glyphicon-remove"></i></button>
															</a>
														</td>
													</c:if>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</c:if>	
							</div>
						</div>
					</div>
					
					<c:if test="${(not empty entregas) && (permissao == 'membro') || (permissao == 'professor')}">
						<jsp:include page="historicoSubmissoes.jsp" />
					</c:if>
				</div>
			</div>
		</div>
	</div>
	<c:if test="${permissao eq 'professor' }">			
		<!-- Modal Excluir EQUIPE -->
		<div class="modal fade" id="confirm-delete2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header btn-danger">
	        			<h4 class="modal-title" id="excluirModalLabel">Excluir</h4>
						<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					</div>
					<div class="modal-body"></div>
					<div class="modal-footer">
						<a href="#" class="btn btn-danger">Excluir</a>
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
					</div>
				</div>
			</div>
		</div>
		
		<!-- Modal Desvincular aluno -->
		<div class="modal fade" id="confirm-submit" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
	        			<h4 class="modal-title" id="submeterModalLabel">Desvincular</h4>
	        			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					</div>
					<div class="modal-body"></div>
					<div class="modal-footer">
						<a href="#" class="btn btn-primary">Desvincular</a>
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
					</div>
				</div>
			</div>
		</div>
		
		<!-- Modal Inativar Equipe -->
		<div class="modal fade" id="confirm-inativar-equipe" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header btn-warning">
	        			<h4 class="modal-title" id="inativarModalLabel">Inativar</h4>
	        			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					</div>
					<div class="modal-body"></div>
					<div class="modal-footer">
						<a href="#" class="btn btn-primary">Inativar</a>
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
					</div>
				</div>
			</div>
		</div>
		
		<!-- Modal ativar Equipe -->
		<div class="modal fade" id="confirm-ativar-equipe" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
	        			<h4 class="modal-title" id="ativarModalLabel">Ativar</h4>
	        			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					</div>
					<div class="modal-body"></div>
					<div class="modal-footer">
						<a href="#" class="btn btn-primary">Ativar</a>
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
					</div>
				</div>
			</div>
		</div>
	</c:if>	
	<jsp:include page="../fragments/footer.jsp" />	
</body>
</html>
