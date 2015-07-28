<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
<c:if test="${action eq 'cadastrar' }">
	<c:set var="url" value="/${idJogo }/formulario/salvar"></c:set>
	<c:set var="titulo" value="Novo Formulário"></c:set>
</c:if>
<c:if test="${action eq 'editar' }">
	<c:set var="url" value="/${idJogo }/formulario/editar"></c:set>
	<c:set var="titulo" value="Editar - ${jogo.nomeDoCurso } "></c:set>
</c:if>

<jsp:include page="../fragments/header-estrutura.jsp" />
<title> ${titulo} </title>
</head>
<body>

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
	<div class="container">

		<div class="well well-lg">
			<h2>Crie seu formulário</h2>
			<h3>Esta funcionalidade permite a criação de um formulário com
				perguntas e alternativas.</h3>
		</div>
		<hr>
		<form:form servletRelativeAction="${url }" commandName="formulario" method="POST" id="sign-up_area" role="form" class="form-horizontal">
			
			<c:if test="${formulario.id != null}">
				<form:input type="hidden" path="id" value="${formulario.id}" />
				<form:input type="hidden" path="jogo.id" value="${formulario.jogo.id}" />
			</c:if>
			
			<c:if test="${formulario.id == null}">
				<div class="form-group">
					<label class="col-xs-2 control-label">Titulo do formulário</label>
					<div class="col-xs-10">
						<form:input path="titulo" type="text" class="form-control" name="titulo" required="true" />
					</div>
				</div>
				<div id="entry1" class="clonedInput panel panel-default">

					<div class="form-group">
						 <div class="panel-heading">
							<label class="col-sm-2 qt1 control-label" for="perguntas[0].descricao">Questão 1:</label>
							<div class="col-sm-9">
								<input id="perguntas[0].descricao" name="formulario.perguntas[0].descricao" type="text" placeholder=""
								class="questao1 form-control"	/>
							</div>
	
						</div>
					</div>
					
					<!-- Text input-->
					<div class="form-group">
						<div class="radio panel-body">
							<label class="col-sm-2 opt1 control-label" for="opcao1"></label>
							<input type="radio" name="opcaoR" id="opcaoR" class="optR">
							<div class="col-sm-6">
								<input id="opcao1" name="formulario.perguntas[0].opcoes[0].descricao" type="text" placeholder=""
									class="opcao1 form-control" required />
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="radio panel-body">
							<label class="col-xs-2 opt2 control-label" for="opcao2">
							</label>
							 <input	type="radio" name="opcaoR" id="opcaoR2" class="optR">
							<div class="col-xs-6">
								<input id="opcao2" name="formulario.perguntas[0].opcoes[1].descricao" type="text" placeholder=""
									class="opcao2 form-control" required />
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="radio panel-body">
							<label class="col-xs-2 opt3 control-label" for="opcao3"> 
							</label>
							<input type="radio" name="opcaoR" id="opcaoR3" class="optR">
							<div class="col-xs-6">
								<input id="opcao3" name="formulario.perguntas[0].opcoes[2].descricao" type="text" placeholder=""
									class="opcao3 form-control" required />
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="radio panel-body">
							<label class="col-xs-2 opt4 control-label" for="opcao4"> 
							</label>
							<input type="radio" name="opcaoR" id="opcaoR4" class="optR">
							<div class="col-xs-6">
								<input id="opcao4" name="formulario.perguntas[0].opcoes[3].descricao" type="text" placeholder=""
									class="opcao4 form-control" required />
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="radio panel-body">
							<label class="col-xs-2 opt5 control-label" for="opcao5"></label>
							<input	type="radio" name="opcaoR" id="opcaoR5" class="optR">
							<div class="col-xs-6">
								<input id="opcao5" name="formulario.perguntas[0].opcoes[4].descricao" type="text" placeholder=""
									class="opcao5 form-control" required />
							</div>
						</div>
					</div>
				</div>
			</c:if>
			<!-- end #entry1 -->
			<!-- Button (Double) -->
			
			<div class="col-sm-12">
				<button type="button" id="btnAdd" name="btnAdd"	class="btn btn-primary btn-lg col-sm-2">
					Nova questão  <i class="glyphicon glyphicon-plus"></i>
				</button>
				<div class="col-sm-1"></div>
				<!-- Colocar o modal padrão -->	
				<button type="button" id="btnDel" name="btnDel"	class="btn btn-danger btn-lg col-sm-2">
					Remover questão  <i class="glyphicon glyphicon-trash"></i>
				</button>
			</div>

			<!-- Textarea -->
			<div class="col-sm-12">
				<div class="form-group">
					<label class="control-label" for="nota">Nota:</label>
					<textarea id="nota" name="formulario.nota" class="form-control"
						placeholder="Não irá adicionar alguma nota?"></textarea>
				</div>
			</div>
			<!-- Button -->
			<div class="col-sm-12">
				<div class="form-group">
					<button id="submit_button" name="submit_button"
						class="btn btn-primary btn-lg">Cadastrar</button>
				</div>
			</div>

		</form:form>
		<!-- end attribution -->
	</div>
	
	<!-- Modal Excluir Questão ADAPTAR-->
	<div class="modal fade" id="delete-file" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="excluirArquivoModalLabel">Excluir</h4>
				</div>
				<div class="modal-body"></div>
				<div class="modal-footer">
					<button type="button" class="btn btn-danger confirm-delete-file"
						data-dismiss="modal">Excluir</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>