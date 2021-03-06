<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 <div class="mastfoot">
	 <div class="inner">	
		 	<p>&copy; Projeto de Extensão Jogo de Empreendedorismo.</p><br>
		 	<p>Desenvolvido por Wellington Lucas Moura</p>
	 </div>
 </div>

<script src="<c:url value="/webjars/jquery/2.1.0/jquery.min.js" />"></script>
<script src="<c:url value="/webjars/jquery-ui/1.11.1/jquery-ui.min.js" />"></script>
<script src="<c:url value="/webjars/bootstrap/3.3.5/js/bootstrap.min.js" />"></script>
<script src="<c:url value="/webjars/datatables/1.10.9/js/jquery.dataTables.min.js" />"></script>
<script src="<c:url value="/webjars/datatables/1.10.9/js/dataTables.bootstrap.min.js" />"></script>
<script src="<c:url value="/webjars/bootstrap-datepicker/1.4.0/js/bootstrap-datepicker.js" />"></script>
<script src="<c:url value="/webjars/bootstrap-datepicker/1.4.0/locales/bootstrap-datepicker.pt-BR.min.js" />"></script>
<c:if test="${editor != 'rodada' }">
	<script src="<c:url value="/resources/js/funcoes.js" />"></script>
</c:if>
<c:if test="${editor eq 'rodada' }">
	<script src="<c:url value="/resources/js/rodada.js" />"></script>
	<script src="<c:url value="/resources/js/rodada.file.js" />"></script>
</c:if>
<c:if test="${editor eq 'equipe' && (action == 'cadastrar' || action == 'editar') }">
	<script src="<c:url value="/resources/js/validatorEquipe.js" />"></script>
</c:if>
<script src="<c:url value="/resources/js/formulario.js" />"></script>
<script src="<c:url value="/webjars/bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js" />"></script>
<script src="<c:url value="/webjars/bootstrapvalidator/0.5.3/js/language/pt_BR.js" />"></script>
<script src="<c:url value="/webjars/momentjs/2.10.6/moment.js" />"></script>
<script src="<c:url value="/webjars/bootstrap-fileinput/4.2.6/js/fileinput.min.js" />"></script>
<script src="<c:url value="/webjars/bootstrap-fileinput/4.2.6/js/fileinput_locale_pt-BR.js" />"></script>
<script src="<c:url value="/resources/js/dynamic-avatar-blur.js" />"></script>
<script src="<c:url value="/resources/js/readmore.min.js" />"></script>
<script src="<c:url value="/webjars/ckeditor/4.5.4/full/ckeditor.js"/>"></script>
<script src="<c:url value="/resources/js/cin.js" />"></script>
<c:if test="${editor eq 'equipe'}">
	<script>
		CKEDITOR.replace('ideiaDeNegocio');
		CKEDITOR.config.resize_enabled = false;
	</script>
</c:if>
<c:if test="${editor eq 'jogo' }">
	<script>
		CKEDITOR.replace('descricao');
		CKEDITOR.replace('regras');
		CKEDITOR.config.resize_enabled = false;
	</script>
</c:if>
<c:if test="${editor eq 'rodada' && (action eq 'editar' || action eq 'cadastrar') }">
	<script>
		CKEDITOR.replace('descricao');
		CKEDITOR.config.resize_enabled = false;
	</script>
</c:if>
