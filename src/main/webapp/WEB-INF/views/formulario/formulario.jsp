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
	<c:set var="descricao" value="Crie seu formulário"></c:set>
	<c:set var="subDescri" value="Esta funcionalidade permite a criação de um formulário com perguntas e alternativas."></c:set>
	<c:set var="titulo" value="Novo Formulário"></c:set>
	<c:set var="txtBtn" value="Cadastrar"></c:set>
</c:if>
<c:if test="${action eq 'editar' }">
	<c:set var="url" value="/${idJogo }/formulario/editar"></c:set>
	<c:set var="descricao" value="Edite seu formulário"></c:set>
	<c:set var="subDescri" value="Esta funcionalidade permite a edição do seu formulário."></c:set>
	<c:set var="titulo" value="Editar - Formulário "></c:set>
	<c:set var="txtBtn" value="Atualizar"></c:set>
</c:if>
<c:if test="${action eq 'copiar' }">
	<c:set var="url" value="/${idJogo }/formulario/salvar"></c:set>
	<c:set var="descricao" value="Você está copiando um formulário"></c:set>
	<c:set var="subDescri" value="Esta funcionalidade permite a edição do seu formulário copiado."></c:set>
	<c:set var="titulo" value="Copiar - Formulário "></c:set>
	<c:set var="txtBtn" value="Salvar Cópia"></c:set>
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
			<h2>${descricao }</h2>
			<h3>${subDescri }</h3>
		</div>
		<hr>
		<form:form id="sign-up_area" role="form" class="form-horizontal" servletRelativeAction="${url }" 
		commandName="formulario" method="POST">
			
			<c:if test="${formulario.id != null}">
				<c:if test="${action eq 'editar' }">							
					<form:input type="hidden" path="id" value="${formulario.id}" />
				</c:if>
			</c:if>
			<c:if test="${formulario.id != null }">
				<div class="form-group">
					<label class="col-xs-2 control-label">Titulo do formulário</label>
					<div class="col-xs-10">
						<form:input path="titulo" type="text" class="form-control" name="titulo" required="true" />
					</div>
				</div>
				<c:forEach var="pergunta" items="${formulario.perguntas}" varStatus="questId">
					<div id="entry${questId.count}" class="clonedInput panel panel-default">
						<div class="panel-heading">
							<div class="form-group">
								<label class="col-sm-2 qt1 control-label" for="perguntas[${ questId.index}].descricao">Questão ${ questId.count}:</label>
								<div class="col-sm-9">
									<input id="perguntas[${ questId.index}].descricao" name="perguntas[${ questId.index}].descricao" type="text" value="${pergunta.descricao }"
									class="questao1 form-control" required />
									<c:if test="${action eq 'editar' }">
										<input type="hidden" name="perguntas[${ questId.index}].id" value="${pergunta.id }"/>
									</c:if>
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<div class="radio panel-body">
								<label class="col-sm-2 opt1 control-label" for="opcao1"></label>
								<input type="radio" name="opcaoR" id="opcaoR" class="optR" disabled="disabled">
								<div class="col-sm-6">
									<input id="opcao1" name="perguntas[${ questId.index}].opcoes[0].descricao" type="text" 
										value="${pergunta.opcoes[0].descricao }" class="opcao1 form-control" required />
									<c:if test="${action eq 'editar' }">	
										<input type="hidden" name="perguntas[${ questId.index}].opcoes[0].id" value="${pergunta.opcoes[0].id }"/>
									</c:if>	
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="radio panel-body">
								<label class="col-xs-2 opt2 control-label" for="opcao2">
								</label>
								 <input	type="radio" name="opcaoR" id="opcaoR2" class="optR" disabled="disabled">
								<div class="col-xs-6">
									<input id="opcao2" name="perguntas[${ questId.index}].opcoes[1].descricao" type="text" 
										value="${pergunta.opcoes[1].descricao }" class="opcao2 form-control" required />
									<c:if test="${action eq 'editar' }">
										<input type="hidden" name="perguntas[${ questId.index}].opcoes[1].id" value="${pergunta.opcoes[1].id }"/>
									</c:if>	
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="radio panel-body">
								<label class="col-xs-2 opt3 control-label" for="opcao3"> 
								</label>
								<input type="radio" name="opcaoR" id="opcaoR3" class="optR" disabled="disabled">
								<div class="col-xs-6">
									<input id="opcao3" name="perguntas[${ questId.index}].opcoes[2].descricao" type="text" 
										value="${pergunta.opcoes[2].descricao }" class="opcao3 form-control" required />
									<c:if test="${action eq 'editar' }">
										<input type="hidden" name="perguntas[${ questId.index}].opcoes[2].id" value="${pergunta.opcoes[2].id }"/>
									</c:if>	
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="radio panel-body">
								<label class="col-xs-2 opt4 control-label" for="opcao4"> 
								</label>
								<input type="radio" name="opcaoR" id="opcaoR4" class="optR" disabled="disabled">
								<div class="col-xs-6">
									<input id="opcao4" name="perguntas[${ questId.index}].opcoes[3].descricao" type="text" 
										value="${pergunta.opcoes[3].descricao }" class="opcao4 form-control" required />
									<c:if test="${action eq 'editar' }">
										<input type="hidden" name="perguntas[${ questId.index}].opcoes[3].id" value="${pergunta.opcoes[3].id }"/>
									</c:if>	
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="radio panel-body">
								<label class="col-xs-2 opt5 control-label" for="opcao5"></label>
								<input	type="radio" name="opcaoR" id="opcaoR5" class="optR" disabled="disabled">
								<div class="col-xs-6">
									<input id="opcao5" name="perguntas[${ questId.index}].opcoes[4].descricao" type="text" 
										value="${pergunta.opcoes[4].descricao }" class="opcao5 form-control" required />
									<c:if test="${action eq 'editar' }">
										<input type="hidden" name="perguntas[${ questId.index}].opcoes[4].id" value="${pergunta.opcoes[4].id }"/>
									</c:if>	
								</div>
							</div>
						</div>
					</div>
				</c:forEach>
			</c:if>
			
			<c:if test="${formulario.id == null}">
				<div class="form-group">
					<label class="col-xs-2 control-label">Titulo do formulário</label>
					<div class="col-xs-10">
						<form:input path="titulo" type="text" class="form-control" name="titulo" required="true" />
					</div>
				</div>
				<div id="entry1" class="clonedInput panel panel-default">
					<div class="panel-heading">
						<div class="form-group">
							<label class="col-sm-2 qt1 control-label" for="perguntas[0].descricao">Questão 1:</label>
							<div class="col-sm-9">
								<input id="perguntas[0].descricao" name="perguntas[0].descricao" type="text" placeholder=""
								class="questao1 form-control" required />
							</div>
						</div>
					</div>
					
					<!-- Text input-->
					<div class="form-group">
						<div class="radio panel-body">
							<label class="col-sm-2 opt1 control-label" for="opcao1"></label>
							<input type="radio" name="opcaoR" id="opcaoR" class="optR" disabled="disabled">
							<div class="col-sm-6">
								<input id="opcao1" name="perguntas[0].opcoes[0].descricao" type="text" placeholder=""
									class="opcao1 form-control" required />
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="radio panel-body">
							<label class="col-xs-2 opt2 control-label" for="opcao2">
							</label>
							 <input	type="radio" name="opcaoR" id="opcaoR2" class="optR" disabled="disabled">
							<div class="col-xs-6">
								<input id="opcao2" name="perguntas[0].opcoes[1].descricao" type="text" placeholder=""
									class="opcao2 form-control" required />
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="radio panel-body">
							<label class="col-xs-2 opt3 control-label" for="opcao3"> 
							</label>
							<input type="radio" name="opcaoR" id="opcaoR3" class="optR" disabled="disabled">
							<div class="col-xs-6">
								<input id="opcao3" name="perguntas[0].opcoes[2].descricao" type="text" placeholder=""
									class="opcao3 form-control" required />
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="radio panel-body">
							<label class="col-xs-2 opt4 control-label" for="opcao4"> 
							</label>
							<input type="radio" name="opcaoR" id="opcaoR4" class="optR" disabled="disabled">
							<div class="col-xs-6">
								<input id="opcao4" name="perguntas[0].opcoes[3].descricao" type="text" placeholder=""
									class="opcao4 form-control" required />
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="radio panel-body">
							<label class="col-xs-2 opt5 control-label" for="opcao5"></label>
							<input	type="radio" name="opcaoR" id="opcaoR5" class="optR" disabled="disabled">
							<div class="col-xs-6">
								<input id="opcao5" name="perguntas[0].opcoes[4].descricao" type="text" placeholder=""
									class="opcao5 form-control" required />
							</div>
						</div>
					</div>
				</div>
			</c:if>
			
			<div class="form-group">
				<div class="col-sm-12">
					<button type="button" id="btnAdd" name="btnAdd"	class="btn btn-success btn-lg col-sm-2">
						Nova questão  <i class="glyphicon glyphicon-plus"></i>
					</button>
					<div class="col-sm-1"></div>
					<!-- Colocar o modal padrão -->	
					<button type="button" id="btnDel" name="btnDel"	class="btn btn-danger btn-lg col-sm-2">
						Remover questão  <i class="glyphicon glyphicon-trash"></i>
					</button>
				</div>
			</div>
			
			<div class="panel panel-default">
				<div class="panel-body">
					<div class="form-group">
						<label for="termino" class="col-sm-2 control-label">Aberto até:</label>
						<div class="col-sm-2">
							<form:input id="termino" type="text" path="prazo" cssClass="form-control data" placeholder="DD/MM/YYYY"/>
							<div class="error-validation">
								<form:errors path="prazo"></form:errors>
							</div>
							<c:if test="${not empty error_termino}">
								<div class="error-validation">
									<span>${error_termino}</span>
								</div>
							</c:if>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-2 control-label" for="nota">Nota:</label>
						<div class="col-sm-10">
							<form:textarea id="nota" name="nota" path="nota" class="form-control"
							placeholder="Não irá adicionar alguma nota?"></form:textarea>
						</div>
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-12">
					<button id="submit_button" name="submit_button"
						class="btn btn-primary btn-lg col-sm-2">${txtBtn }</button>
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