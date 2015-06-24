<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
	<head>
		<jsp:include page="../fragments/header-estrutura.jsp" />	
		<title>Novo Jogo</title>
	</head>
	<body>
	
		<c:if test="${action eq 'cadastrar' }">
			<c:set var="url" value="/jogo/novo-jogo"></c:set>
			<c:set var="titulo" value="Novo Jogo"></c:set>
		</c:if>
		<c:if test="${action eq 'editar' }">
			<c:set var="url" value="/jogo/editar"></c:set>
			<c:set var="titulo" value="Editar - ${jogo.nomeDoCurso } "></c:set>
		</c:if>
		
		<jsp:include page="../fragments/header.jsp" />
		<c:if test="${not empty erro}">
			<div class="alert alert-danger alert-dismissible" role="alert">
				<button type="button" class="close" data-dismiss="alert">
					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>
				<c:out value="${erro}"></c:out>
			</div>
		</c:if>
		<br>
		<div class ="container">
			<form:form id="adicionarJogoForm" role="form" class="form-horizontal" action="/cin_ufc/jogo/novo-jogo" commandName="jogo" enctype="multipart/form-data" servletRelativeAction="/novo-jogo" method="POST">
				<input type="hidden" name="idUsuario" value="${idUsuario}"/>
				<div class="form-group">
					<div class="form-item">
						<label for="inputNome" class="col-sm-2 control-label" >Nome do Curso:<span class="required">*</span></label>
						<div class="col-sm-4">
							<form:input type="text" class="form-control" id="inputNome" path="nomeDoCurso" placeholder="Engenharia de Software" />
						</div>
						<label for="inputSemestre" class="col-sm-1 control-labe" >Semestre:<span class="required">*</span></label>
						<div class="col-sm-2">
							<form:input type="text" class="form-control" id="inputSemestre" path="semestre" placeholder="2015.2" /><br>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="form-item">
						<label for="inicio" class="col-sm-2 control-label">Data de Início:</label>
						<div class="col-sm-2">
							<form:input id="inicio" type="text" path="inicio" cssClass="form-control data" placeholder="Data de início"/>
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
						<label for="termino" class="col-sm-2 control-label">Data de Término:</label>
						<div class="col-sm-2">
							<form:input id="termino" type="text" path="termino" cssClass="form-control data" placeholder="Data de término"/>
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
					<label for="descricao" class="col-sm-2 control-label">Descrição do Jogo:<span class="required">*</span></label>
					<div class="col-sm-8">
						<form:textarea name="descricao" id="descricao" path="descricao" />
					</div>
				</div>
				
				<div class="form-group">
					<label for="regras" class="col-sm-2 control-label">Regras do Jogo:</label>
					<div class="col-sm-8">
						<form:textarea name="regras" id="regras" path="regras" />
					</div>
				</div>		
									
				<div class="form-group form-item">
					<label for="atividades" class="col-sm-2 control-label">Anexos:</label>
					<div class="col-sm-10">
						<input type="file" name="anexos" class="multi" multiple="multiple" ></input>
						<c:if test="${not empty jogo.documentos }">
							<table id="table-anexos" class="table table-striped">
								<thead>
									<tr>
										<th data-column-id="nome" data-order="desc">Arquivo</th>
										<th data-column-id="excluir" width="5%">Excluir</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${jogo.documentos }" var="documento">
		                    			<tr id="documento-${documento.id}">
									        <td>
									            <a href="<c:url value="/documento/${jogo.id }" />">${jogo.nome }</a>
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
							Cadastrar
						</button>
					</div>
				</div>
			</form:form>
		</div>
		<jsp:include page="../fragments/footer.jsp" />
	</body>
</html>