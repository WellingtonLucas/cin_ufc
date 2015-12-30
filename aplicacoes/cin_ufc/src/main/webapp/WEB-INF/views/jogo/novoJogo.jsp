<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
	<head>
		<c:if test="${action eq 'cadastrar' }">
			<c:set var="url" value="/jogo/novo-jogo"></c:set>
			<c:set var="titulo" value="Novo Jogo"></c:set>
		</c:if>
		<c:if test="${action eq 'editar' }">
			<c:set var="url" value="/jogo/${jogo.id}/editar"></c:set>
			<c:set var="titulo" value="Editar - ${jogo.nomeDoCurso } "></c:set>
		</c:if>
		
		<jsp:include page="../fragments/header-estrutura.jsp" />	
		<title>${titulo}</title>
	</head>
	<body>
	
		<jsp:include page="../fragments/header.jsp" />
		<div class ="container">
			<h2>${titulo}</h2>
			<div class="panel panel-primary">
				<div class="panel-heading">
					<div class="panel-title">
						<i class="fa fa-plus"></i>&nbsp;&nbsp;${titulo}
						<a href="javascript:history.back();" class="btn btn-warning btn-sm pull-right">
							<i class="glyphicon glyphicon-arrow-left"></i>&nbsp;&nbsp;Voltar
						</a>
					</div>
				</div>	
				<div class="panel-body">
					<jsp:include page="../fragments/mensagens.jsp" />
					<form:form id="adicionarJogoForm" role="form" class="form-horizontal" commandName="jogo" enctype="multipart/form-data" servletRelativeAction="${url }" method="POST">
						<form:hidden path="id" />
						<form:hidden path="status" />
						<div class="form-group">
							<div class="form-item">
								<label for="nomeDoCurso" class="col-sm-2 control-label" >Nome do Curso:<span class="required">*</span></label>
								<div class="col-sm-4">
									<form:input type="text" class="form-control" id="nomeDoCurso" path="nomeDoCurso" placeholder="Engenharia de Software" />
									<div class="error-validation">
										<form:errors path="nomeDoCurso"></form:errors>
									</div>
								</div>
							</div>
							<div class="form-item">	
								<label for="semestre" class="col-sm-2 control-label" >Semestre:<span class="required">*</span></label>
								<div class="col-sm-2">
									<form:input type="text" class="form-control" id="semestre" path="semestre" placeholder="2015.2" />
									<div class="error-validation">
										<form:errors path="semestre"></form:errors>
									</div>
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<div class="form-item">
								<label for="inicio" class="col-sm-2 control-label">Data de Início:<span class="required">*</span></label>
								<div class="col-sm-2">
									<form:input id="inicio" type="text" path="inicio" cssClass="form-control data" placeholder="DD/MM/YYYY"/>
									<div class="error-validation">
										<form:errors path="inicio"></form:errors>
									</div>
									<c:if test="${not empty error_inicio}">
										<div class="error-validation">
											<span>${error_inicio}</span>
										</div>
									</c:if>
								</div>
							</div>
		
							<div class="form-item">
								<label for="termino" class="col-sm-4 control-label">Data de Término:<span class="required">*</span></label>
								<div class="col-sm-2">
									<form:input id="termino" type="text" path="termino" cssClass="form-control data" placeholder="DD/MM/YYYY"/>
									<div class="error-validation">
										<form:errors path="termino"></form:errors>
									</div>
									<c:if test="${not empty error_termino}">
										<div class="error-validation">
											<span>${error_termino}</span>
										</div>
									</c:if>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="form-item">
								<label for="descricao" class="col-sm-2 control-label">Descrição do Jogo:<span class="required">*</span></label>
								<div class="col-sm-8">
									<form:textarea name="descricao" id="descricao" path="descricao" class="form-control"/>
									<div class="error-validation">
											<form:errors path="descricao"></form:errors>
									</div>
									<c:if test="${not empty error_descricao}">
										<div class="error-validation">
											<span>${error_descricao}</span>
										</div>
									</c:if>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="regras" class="col-sm-2 control-label">Regras do Jogo:</label>
							<div class="col-sm-8">
								<form:textarea name="regras" id="regras" path="regras" />
							</div>
						</div>		
						<div class="form-group">
							<label for="fileupload" class="col-sm-2 control-label">Foto:</label>
							<div class="col-sm-8">
								<input type="file" id="idfoto" name="logo"></input>
							</div>
						</div>
						<c:if test="${action eq 'editar' }">
							<div class="form-group">
								<div class="col-sm-2"></div>
								<div class="col-sm-8">
									<p class="bg-info">Para alterar a imagem do jogo escolha outra.</p>
								</div>
							</div>
						</c:if>		
						<div class="form-group form-item">
							<label for="fileupload" class="col-sm-2 control-label">Anexos:</label>
							<div class="col-sm-8">
								<input type="file" id="fileupload" class="file" name="anexos" multiple></input>	
								<c:if test="${not empty jogo.documentos }">
									<table id="table-anexos" class="table table-striped">
										<thead>
											<tr>
												<th data-column-id="nome" data-order="desc">Anexos:</th>
												<th data-column-id="excluir" width="5%"></th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${jogo.documentos }" var="documento">
				                    			<tr id="documento-${documento.id}">
											        <td>
											            <a href="<c:url value="/documento/${documento.id }" />">${documento.nomeOriginal }</a>
											        </td>
											        <td>
											        	<a id="exluir-arquivo" data-toggle="modal" data-target="#delete-file" href="#" data-id="${documento.id}" data-name="${documento.nome }">
															<button class="btn btn-danger"><i class="fa fa-trash-o"></i></button>
														</a>
											        </td>
											    </tr>	
				                    		</c:forEach>
										</tbody>
									</table>
								</c:if>
							</div>
						</div>
						
						<div class="form-group">
							<div class="col-sm-2"></div>
							<div class="col-sm-2">
								<span class="campo-obrigatorio"><span class="required">*</span> Campos obrigatórios</span>
							</div>
						</div>
						
						<div class="form-group">
							<div class="col-sm-2"></div>
							<div class="col-sm-2">		
								<button type="submit" class="btn btn-primary btn-lg" >
									Salvar
								</button>						
							</div>
							<div class="col-sm-2">
								<a href="javascript:history.back();" class="btn btn-warning btn-lg">Cancelar</a>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
		
		<!-- Modal Excluir Arquivo -->
		<div class="modal fade" id="delete-file" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
	        			<h4 class="modal-title" id="excluirArquivoModalLabel">Excluir</h4>
					</div>
					<div class="modal-body"></div>
					<div class="modal-footer">
						<button type="button" class="btn btn-danger confirm-delete-file" data-dismiss="modal">Excluir</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
					</div>
				</div>
			</div>
		</div>
		
		<jsp:include page="../fragments/footer.jsp" />
	</body>
</html>