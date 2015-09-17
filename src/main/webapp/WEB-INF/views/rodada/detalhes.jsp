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
	<c:if test="${action eq 'cadastrar' }">
		<c:set var="url" value="/rodada/${rodada.id}/entrega"></c:set>
	</c:if>
	<title>${rodada.nome }</title>
</head>

<body>
	<jsp:include page="../fragments/header.jsp" />
	
	<div class="section">
		<div class="container-fluid">
			<div class="row">
				<jsp:include page="../fragments/menu.jsp" />
				<div class="col-sm-8 col-sm-offset-3 col-md-10 col-md-offset-2 main">
					<div class="col-sm-12">
						<h2><strong>${jogo.nomeDoCurso }</strong> <small>${jogo.semestre }</small></h2>
						<h3><strong>${rodada.nome }</strong> </h3>							
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
					<div class="form-horizontal">
						<div class="form-group">
							<label class="col-sm-2 control-label field">Nome:</label>
							<div class="col-sm-10 field-value">
								<c:if test="${empty rodada.nome }">
									<label>-</label>
								</c:if>
								<c:if test="${not empty rodada.nome }">
									<label>							
										${rodada.nome }
									</label>
								</c:if>
							</div>		
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label field">Início:</label>
							<div class="col-sm-3 field-value">
								<c:if test="${empty rodada.inicio }">
									<label>-</label>
								</c:if>
								<label><fmt:formatDate pattern="dd/MM/yyyy" value="${rodada.inicio }" /></label>
							</div>
							<label class="col-sm-3 control-label field">Prazo de submissão:</label>
							<div class="col-sm-4 field-value">
								<c:if test="${empty rodada.prazoSubmissao }">
									<label>-</label>
								</c:if>
								<label><fmt:formatDate pattern="dd/MM/yyyy" value="${rodada.prazoSubmissao }" /></label>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label field">Término:</label>
							<div class="col-sm-4 field-value">
								<c:if test="${empty rodada.termino }">
									<label>-</label>
								</c:if>
								<label><fmt:formatDate pattern="dd/MM/yyyy" value="${rodada.termino }" /></label>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label field">Descrição:</label>
							<c:if test="${empty rodada.descricao }">
								<label>-</label>
							</c:if>
							<div class="col-sm-10 field-value">
								<label>${rodada.descricao }</label>
							</div>						
						</div>
						<c:if test="${(rodada.statusPrazo && rodada.status) || (permissao == 'professor')}">
							<form:form id="adicionarEntregaForm" role="form" class="form-horizontal" commandName="rodada"
							 	enctype="multipart/form-data" servletRelativeAction="/jogo/${jogo.id }/rodada/entrega" method="POST">
								<form:hidden path="id" value="${rodada.id }"/>
								<div class="form-group form-item">
									<label for="fileupload" class="col-sm-2 control-label field">Entrega:</label>
									<div class="col-sm-7">
										<input type="file" id="fileupload" class="file" name="anexos" multiple></input>	
									</div>
								</div>
							</form:form>
						</c:if>
						<c:if test="${(!rodada.statusPrazo || !rodada.status) && permissao != 'professor'}">
							<div class="form-group form-item">
								<label for="fileupload" class="col-sm-2 control-label field">Entrega:</label>
								<div class="col-sm-7">
									<input type="file" id="fileupload" class="file" name="anexos" multiple disabled="disabled"></input>	
								</div>
							</div>
						</c:if>
						<div class="form-group">
							<label class="col-sm-2 control-label field">Formulário:</label>
							<div class="col-sm-10 field-value">
								<c:if test="${empty rodada.formulario.titulo }">
									<label>
										-
									</label>
								</c:if>
								<c:if test="${not empty rodada.formulario.titulo }">
									<label>							
										<a href="<c:url value="/jogo/${jogo.id }/formulario/${rodada.formulario.id }/detalhes" />">
											${rodada.formulario.titulo }
										</a>
									</label>
								</c:if>
							</div>		
						</div>	
					</div>	
					<div class="col-sm-12">
						<hr>
						<c:if test="${not empty rodada.jogo.equipes }">
							<h3><strong>Equipes na Rodada</strong></h3>
							<table id="table_id" class="table table-striped table-hover">
								<thead>
									<tr>
										<th>Nome</th>
										<c:if test="${permissao eq 'professor' }">
											<th></th>
										</c:if>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="equipe" items="${rodada.jogo.equipes}">
										<tr>
											<td>
												<a href="<c:url value="/jogo/${jogo.id}/equipe/${equipe.id }"></c:url>">${equipe.nome}</a>
											</td>
											<c:if test="${permissao eq 'professor' }">
												<td>
													<a id="submeter" data-toggle="modal" data-target="#desvincular-equipe" href="#"
														data-href="<c:url value="equipe/${equipe.id}/desvincular" ></c:url>" data-name="${equipe.nome }">
														<button class="btn btn-primary">Desvincular&nbsp;<i class="glyphicon glyphicon-remove"></i></button>
													</a>
												</td>
											</c:if>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</c:if>	
					</div>
					
					<div class="col-sm-12">
						<hr>
						<div class="row placeholders">
							<div class="form-group">	
								<c:if test="${(permissao eq 'professor')}">			
									<div class="col-sm-2">				
										<a id="editar" href="<c:url value="/jogo/${jogo.id}/rodada/${rodada.id }/editar" ></c:url>">
											<button class="btn btn-primary btn-lg">Editar&nbsp;<i class="glyphicon glyphicon-edit"></i></button>
										</a>
									</div>
									<div class="col-sm-2">
										<c:if test="${rodada.status == true}">									
											<a id="inativar" data-toggle="modal" data-target="#confirm-inativar-rodada" href="#" 
												data-href="<c:url value="/jogo/${jogo.id}/rodada/${rodada.id }/inativar">
												</c:url>" data-name="${rodada.nome }">
												<button class="btn btn-warning btn-lg">
													Encerrar&nbsp;<i class="glyphicon glyphicon-ban-circle"></i>
												</button>
											</a>
										</c:if>
										<c:if test="${rodada.status == false }">
											<a id="ativar" data-toggle="modal" data-target="#confirm-ativar-rodada" href="#" 
												data-href="<c:url value="/jogo/${jogo.id}/rodada/${rodada.id }/ativar">
												</c:url>" data-name="${rodada.nome }">
												<button class="btn btn-success btn-lg">
													Iniciar&nbsp;<i class="glyphicon glyphicon-ok-circle"></i>
												</button>
											</a>
										</c:if>
									</div>
									<div class="col-sm-2">
										<a id="excluir" data-toggle="modal" data-target="#confirm-delete-rodada" href="#" 
										data-href="<c:url value="/jogo/${jogo.id}/rodada/${rodada.id }/excluir"></c:url>" data-name="${rodada.nome }">
											<button class="btn btn-danger btn-lg">Excluir&nbsp;<i class="glyphicon glyphicon-trash"></i></button>
										</a>					
									</div>
								</c:if>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<c:if test="${permissao eq 'professor' }">			
		<!-- Modal Excluir Rodada -->
		<div class="modal fade" id="confirm-delete-rodada" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
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
		
		<!-- Modal Inativar Rodada -->
		<div class="modal fade" id="confirm-inativar-rodada" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
	        			<h4 class="modal-title" id="inativarModalLabel">Encerrar</h4>
	        			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					</div>
					<div class="modal-body"></div>
					<div class="modal-footer">
						<a href="#" class="btn btn-primary">Encerrar</a>
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
					</div>
				</div>
			</div>
		</div>
		
		<!-- Modal ativar Rodada -->
		<div class="modal fade" id="confirm-ativar-rodada" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
	        			<h4 class="modal-title" id="ativarModalLabel">Iniciar</h4>
	        			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					</div>
					<div class="modal-body"></div>
					<div class="modal-footer">
						<a href="#" class="btn btn-primary">Iniciar</a>
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
					</div>
				</div>
			</div>
		</div>
		
		<!-- Modal Desvincular equipe -->
		<div class="modal fade" id="desvincular-equipe" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
	</c:if>	
	<jsp:include page="../fragments/footer.jsp" />	
</body>
</html>
