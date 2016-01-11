<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="panel panel-primary">
	<div class="panel-heading">
		Solicitar consultoria
	</div>
	<div class="panel-body">
	 	<jsp:include page="../fragments/mensagens.jsp" />
		<h4><strong><label class="col-sm-2 field" for="descricaoC">Descrição:</label></strong></h4>
		<div class="col-sm-10 field-value">
			<article>
				<label id="descricaoC">
					${consulta.descricao }
				</label>
			</article>
		</div>
		<div class="form-group">
			<h4><strong><label class="col-sm-2 field">Valor (R$):</label></strong></h4>
			<div class="col-sm-10 field-value">
				${consulta.valor}
			</div>
		</div>
	</div>
	<div class="panel-footer">
		<form:form id="form-adquirir" role="form" class="form-horizontal" commandName="consulta" 
			servletRelativeAction="/jogo/${idJogo }/rodada/${rodada.id }/adquirirServico/${equipe.id }" method="POST">
			<form:hidden path="id"/>
			<form:hidden path="rodada.id"/>
			<button type="submit" class="btn btn-primary btn-lg" >
				Solicitar consultoria 
			</button>		
		</form:form>		
	</div>
</div>