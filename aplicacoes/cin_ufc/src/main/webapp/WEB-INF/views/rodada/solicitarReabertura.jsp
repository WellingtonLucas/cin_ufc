<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<form:form id="reaberturaForm" role="form"
	class="form-horizontal" commandName="reaberturaSubmissao"
	servletRelativeAction="/jogo/${jogo.id}/rodada/${rodada.id}/solicitarReabertura"
	method="POST">
	<form:hidden path="equipe.id" value="${equipe.id }"/>
	<form:hidden path="rodada.id"  value="${rodada.id }"/>
	<div class="modal fade" id="squarespaceModal" tabindex="-1"
		role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span><span class="sr-only">Close</span>
					</button>
					<h3 class="modal-title" id="lineModalLabel"><strong>Escolha a quantidade de dias após o prazo</strong></h3>
				</div>
	
				<div class="modal-body">
						<div class="form-group">
							<div class="col-sm-1"></div>
							<label for="quantidadeDia" class="col-sm-2 control-label">Quantidade:</label>
							<div class="col-sm-9">
								<label class="radio-inline icheck-label">
								  	<input type="radio" name="quantidadeDia" id="v1" value="1"> 1
								</label>
								<label class="radio-inline">
								  	<input type="radio" name="quantidadeDia" id="v2" value="2"> 2
								</label>
								<label class="radio-inline">
									<input type="radio" name="quantidadeDia" id="v3" value="3"> 3
								</label>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-1"></div>
							<h3 class="col-sm-11">
								<small>Lembrete: cada dia tem um custo adicional de <strong>R$ <fmt:formatNumber maxFractionDigits="2" value="${rodada.valorReabertura}"></fmt:formatNumber></strong> para sua empresa.</small>
							</h3>
						</div>
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn btn-primary">Submeter</button>
					<button type="button" class="btn btn-default" data-dismiss="modal"
						role="button">Cancelar</button>
				</div>
			</div>
		</div>
	
	</div>
</form:form>