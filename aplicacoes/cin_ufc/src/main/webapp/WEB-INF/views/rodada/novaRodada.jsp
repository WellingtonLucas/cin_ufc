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
			<c:set var="url" value="/jogo/${jogo.id }/nova-rodada"></c:set>
			<c:set var="titulo" value="Nova Rodada"></c:set>
		</c:if>
		<c:if test="${action eq 'editar' }">
			<c:set var="url" value="/${jogo.id }/rodada/editar"></c:set>
			<c:set var="titulo" value="Editar - ${rodada.nome } "></c:set>
		</c:if>
		
		<jsp:include page="../fragments/header-estrutura.jsp" />	
		<title>${titulo}</title>
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
		<c:if test="${not empty info}">
			<div class="alert alert-success alert-dismissible" role="alert">
				<button type="button" class="close" data-dismiss="alert">
					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>
				<c:out value="${info}"></c:out>
			</div>
		</c:if>
		<br>
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
					<form:form id="adicionarRodadaForm" role="form" class="form-horizontal" commandName="rodada" servletRelativeAction="${url }" method="POST">
						<form:hidden path="id"/>
						<form:hidden path="status"/>
						<form:hidden path="statusPrazo"/>
						<form:hidden path="statusAvaliacao"/>
						<form:hidden path="statusRaking"/>
						<form:hidden path="statusNota"/>
						<c:if test="${not empty rodada.modelo.id }">
							<form:hidden path="modelo.id"/>
						</c:if>
						<div class="form-group">
							<div class="form-item">
								<label for="nome" class="col-sm-2 control-label" >Nome da Rodada:<span class="required">*</span></label>
								<div class="col-sm-8">
									<form:input type="text" class="form-control" id="nome" path="nome" placeholder="Rodada 1" />
									<div class="error-validation">
										<form:errors path="nome"></form:errors>
									</div>
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<div class="form-item">
								<label for="inicio" class="col-sm-2 control-label">Data de Início:<span class="required">*</span></label>
								<div class="col-sm-2">
									<form:input id="inicio" type="text" path="inicio" cssClass="form-control data" placeholder="DD/MM/AAAA"/>
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
							<div class="col-sm-2"></div>
							<div class="form-item">
								<label for="termino" class="col-sm-2 control-label">Data de Término:<span class="required">*</span></label>
								<div class="col-sm-2">
									<form:input id="termino" type="text" path="termino" cssClass="form-control data" placeholder="DD/MM/AAAA"/>
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
								<label for="prazoSubmissao" class="col-sm-2 control-label">Prazo de submissão:<span class="required">*</span></label>
								<div class="col-sm-2">
									<form:input type="text" path="prazoSubmissao" cssClass="form-control data" placeholder="DD/MM/AAAA"/>
									<div class="error-validation">
										<form:errors path="prazoSubmissao"></form:errors>
									</div>
									<c:if test="${not empty error_prazoSubmissao}">
										<div class="error-validation">
											<span>${error_prazoSubmissao}</span>
										</div>
									</c:if>
								</div>
							</div>
							<div class="col-sm-2"></div>
							<div class="form-item">
								<label for="terminoAvaliacao" class="col-sm-2 control-label">Prazo de avaliação:<span class="required">*</span></label>
								<div class="col-sm-2">
									<form:input type="text" path="terminoAvaliacao" cssClass="form-control data" placeholder="DD/MM/AAAA"/>
									<div class="error-validation">
										<form:errors path="terminoAvaliacao"></form:errors>
									</div>
									<c:if test="${not empty error_terminoAvaliacao}">
										<div class="error-validation">
											<span>${error_terminoAvaliacao}</span>
										</div>
									</c:if>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="form-item">
								<label for="valorLiberado" class="col-sm-2 control-label">Valor de aposta (R$):</label>
								<div class="col-sm-2">
									<form:input type="number" step="0.5" path="valorLiberado" min="0.0" class="form-control"/>
									<div class="error-validation">
											<form:errors path="valorLiberado"></form:errors>
									</div>
								</div>
							</div>
							<div class="col-sm-2"></div>
							<div class="form-item">
								<label for="tudo" class="col-sm-2 control-label">All in:<span class="required">*</span></label>
								<c:if test="${rodada.allIn == null }">
									<div class="col-sm-2">
										<label class="radio-inline">
											<input type="radio" name="tudo" value="sim"> Sim
										</label>
										<label class="radio-inline">
											<input type="radio" name="tudo" value="nao" checked="checked"> Não
										</label>
									</div>
								</c:if>
								<c:if test="${rodada.allIn != null }">
									<div class="col-sm-2">
										<c:if test="${rodada.allIn}">
											<label class="radio-inline">
												<input type="radio" name="tudo" value="sim" checked="checked"> Sim
											</label>
											<label class="radio-inline">
												<input type="radio" name="tudo" value="nao"> Não
											</label>
										</c:if>
										<c:if test="${!rodada.allIn}">
											<label class="radio-inline">
												<input type="radio" name="tudo" value="sim"> Sim
											</label>
											<label class="radio-inline">
												<input type="radio" name="tudo" value="nao"  checked="checked"> Não
											</label>
										</c:if>
									</div>
								</c:if>
								
							</div>
						</div>
						<div class="form-group">
							<div class="form-item">
								<label for="descricao" class="col-sm-2 control-label">Descrição da Rodada:</label>
								<div class="col-sm-8">
									<form:textarea name="descricao" id="descricao" path="descricao" class="form-control"/>
									<div class="error-validation">
											<form:errors path="descricao"></form:errors>
									</div>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="form-item">
								<label for="prazoSubmissao" class="col-sm-2 control-label">Formulário da Rodada:<span class="required">*</span></label>
								<div class="col-sm-8">
									<form:select id="selec" path="formulario.id" class="form-control">
										<c:forEach var="formulario" items="${formularios}">
									     	<form:option value="${formulario.id }">${formulario.titulo}</form:option>
									    </c:forEach>
									</form:select>
								</div>
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
									Salvar <i class="glyphicon glyphicon-floppy-disk"></i>
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
		
		<jsp:include page="../fragments/footer.jsp" />
	</body>
</html>