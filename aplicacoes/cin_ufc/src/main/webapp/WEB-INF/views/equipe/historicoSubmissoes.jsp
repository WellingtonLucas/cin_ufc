<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<div class="panel panel-primary">
	<div class="panel-heading">
		<strong>Histórico de submissões</strong>
	</div>
	<div class="panel-body">
		<div class="col-sm-12">
			<div class="row placeholders">
				<div class="timeline-centered">
					<c:forEach var="entrega" items="${entregas}" varStatus="entregaId">
						<c:if test="${entregaId.index == 0 }">
							<div class="timeline-entry">
								<div class="timeline-entry-inner">
									<time class="timeline-time" datetime="${entrega.dia }">
										<span><fmt:formatDate pattern="dd/MM/yyyy" value="${entrega.dia }" /></span>
										<span>Hora: <span><fmt:formatDate pattern="HH:mm:ss" value="${entrega.dia }" /></span></span>
									</time>
									<div class="timeline-icon bg-success">
										<i class="entypo-feather"></i>
									</div>
									<div class="timeline-label">
										<h2>${entrega.usuario.nome } ${entrega.usuario.sobreNome }:
									    	<a href="<c:url value="/documento/downloadDocumento/${entrega.documento.id }"></c:url>">${entrega.documento.nomeOriginal}</a>
										</h2>
										<h2>Entrega feita na rodada: ${entrega.rodada.nome}</h2>
									</div>
								</div>
							</div>
						</c:if>
						<c:if test="${(entregaId.index != 0) && (entregaId.index % 2 == 1) }">	
							<div class="timeline-entry left-aligned">
								<div class="timeline-entry-inner">
									<time class="timeline-time" datetime="${entrega.dia }">
										<span><fmt:formatDate pattern="dd/MM/yyyy" value="${entrega.dia }" /></span>
										<span>Hora: <span><fmt:formatDate pattern="HH:mm:ss" value="${entrega.dia }" /></span></span>
									</time>
									<div class="timeline-icon bg-secondary">
										<i class="entypo-suitcase"></i>
									</div>
									<div class="timeline-label">
										<h2>${entrega.usuario.nome } ${entrega.usuario.sobreNome }:
									    	<a href="<c:url value="/documento/downloadDocumento/${entrega.documento.id }"></c:url>">${entrega.documento.nomeOriginal}</a>
										</h2>
										<h2>Entrega feita na rodada: ${entrega.rodada.nome}</h2>
									</div>
								</div>
							</div>
						</c:if>
						<c:if test="${(entregaId.index != 0) && (entregaId.index % 2 == 0) }">	
							<div class="timeline-entry">
								<div class="timeline-entry-inner">
									<time class="timeline-time" datetime="${entrega.dia }">
										<span><fmt:formatDate pattern="dd/MM/yyyy" value="${entrega.dia }" /></span>
										<span>Hora: <span><fmt:formatDate pattern="HH:mm:ss" value="${entrega.dia }" /></span></span>
									</time>
									<div class="timeline-icon bg-secondary">
										<i class="entypo-suitcase"></i>
									</div>
									<div class="timeline-label">
										<h2>${entrega.usuario.nome } ${entrega.usuario.sobreNome }:
									    	<a href="<c:url value="/documento/downloadDocumento/${entrega.documento.id }"></c:url>">${entrega.documento.nomeOriginal}</a>
										</h2>
										<h2>Entrega feita na rodada: ${entrega.rodada.nome}</h2>
									</div>
								</div>
							</div>
						</c:if>
			
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
</div>