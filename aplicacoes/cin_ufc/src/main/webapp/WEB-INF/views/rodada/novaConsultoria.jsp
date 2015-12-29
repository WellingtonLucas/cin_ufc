<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:if test="${action eq 'cadastrar' }">
	<c:set var="url" value="/jogo/${idJogo }/rodada/${rodada.id }/servico/salvar"></c:set>
	<c:set var="txtBtn" value="Criar"></c:set>
</c:if>
<c:if test="${action eq 'editar' }">
	<c:set var="url" value="/jogo/${idJogo }/rodada/${rodada.id }/servico/editar"></c:set>
	<c:set var="txtBtn" value="Atualizar"></c:set>
</c:if>
<form:form id="servico-form" role="form" class="form-horizontal" commandName="consulta" 
	servletRelativeAction="${url }" method="POST">
	<form:hidden path="id"/>
	<form:hidden path="rodada.id"/>
	<div class="panel panel-primary">
		<div class="panel-heading">
			${txtBtn } consultoria
		</div>
		<div class="panel-body">
			<div class="form-group">
				<div class="form-item">
					<label for="descricao" class="col-sm-2 control-label">Descrição:<span class="required">*</span></label>
					<div class="col-sm-10">
						<form:textarea path="descricao" class="form-control" />
						<div class="error-validation">
								<form:errors path="descricao"></form:errors>
						</div>
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="form-item">
					<label for="valor" class="col-sm-2 control-label">Valor Consulta (R$):<span class="required">*</span></label>
					<div class="col-sm-2">
						<form:input type="number" step="0.5" path="valor" min="0.0" class="form-control"/>
						<div class="error-validation">
								<form:errors path="valor"></form:errors>
						</div>
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-2"></div>
				<div class="col-sm-3">
					<span class="campo-obrigatorio"><span class="required">*</span> Campos obrigatórios</span>
				</div>
			</div>
		</div>
		<div class="panel-footer">
			<div class="form-group">
				<div class="col-sm-2"></div>
				<div class="col-sm-2">		
					<button type="submit" class="btn btn-primary btn-lg" >
					<i class="glyphicon glyphicon-floppy-disk"></i>&nbsp;&nbsp;${txtBtn } 
					</button>						
				</div>
				<a href="javascript:history.back();" class="btn btn-warning btn-lg">Cancelar</a>
			</div>
		</div>
	</div>
</form:form>