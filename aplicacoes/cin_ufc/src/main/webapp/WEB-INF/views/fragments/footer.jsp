<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 <div class="mastfoot">
	 <div class="inner">	
		 	<p>&copy; Projeto de Extensão 2015</p>
	 </div>
 </div>


<script src="<c:url value="/webjars/jquery/2.1.0/jquery.min.js" />"></script>
<script src="<c:url value="/webjars/jquery-ui/1.11.1/jquery-ui.min.js" />"></script>
<script src="<c:url value="/webjars/bootstrap/3.3.5/js/bootstrap.min.js" />"></script>
<script src="<c:url value="/webjars/datatables/1.9.4/media/js/jquery.dataTables.min.js" />"></script>
<script src="<c:url value="/resources/js/bootstrap-datepicker.js" />"></script>
<script src="<c:url value="/resources/js/bootstrap-datepicker.pt-BR.js" />"></script>
<c:if test="${editor != 'rodada' }">
	<script src="<c:url value="/resources/js/funcoes.js" />"></script>
</c:if>
<c:if test="${editor eq 'rodada' }">
	<script src="<c:url value="/resources/js/rodada.js" />"></script>
</c:if>
<c:if test="${editor eq 'rodada' && (action == 'cadastrar' || action == 'editar') }">
	<script src="<c:url value="/resources/js/rodada.file.js" />"></script>
</c:if>
<c:if test="${editor eq 'equipe' && (action == 'cadastrar' || action == 'editar') }">
	<script src="<c:url value="/resources/js/validatorEquipe.js" />"></script>
</c:if>
<script src="<c:url value="/resources/js/funcoes2.js" />"></script>
<script src="<c:url value="/resources/js/formulario.js" />"></script>
<script src="<c:url value="/resources/js/bootstrapValidator.min.js" />"></script>
<script src="<c:url value="/resources/js/language/pt_BR.js" />"></script>
<script src="<c:url value="/resources/js/jquery.mask.min.js" />"></script>
<script src="<c:url value="/resources/js/moment.js" />"></script>
<script src="<c:url value="/resources/js/moment-with-locales.js" />"></script>
<script src="<c:url value="/resources/js/select2.min.js" />"></script>
<script src="<c:url value="/resources/js/select2_locale_pt-BR.js" />"></script>
<script src="<c:url value="/resources/js/fileinput.min.js" />"></script>
<script src="<c:url value="/resources/js/jquery.MultiFile.js" />"></script>
<script src="<c:url value="/resources/js/jquery.fileupload.js" />"></script>
<%-- <script src="<c:url value="/resources/js/jquery.fileupload-image.js" />"></script>
<script src="<c:url value="/resources/js/jquery.fileupload-validate.js" />"></script> --%>
<%-- <script src="<c:url value="/resources/js/jquery.iframe-transport.js" />"></script> --%>
<%-- <script src="<c:url value="/resources/js/jquery.ui.widget.js" />"></script> --%>
<script src="<c:url value="/resources/js/jquery.searchable.js" />"></script>
<script src="<c:url value="/resources/js/searchable.js" />"></script>
<script src="<c:url value="/resources/js/dynamic-avatar-blur.js" />"></script>
<script src="<c:url value="/resources/js/readmore.min.js" />"></script>
<script src="<c:url value="/resources/ckeditor/ckeditor.js"/>"></script>
<script src="<c:url value="/resources/ckeditor/adapters/jquery.js"/>"></script>
<c:if test="${editor eq 'equipe' }">
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
<c:if test="${editor eq 'rodada' }">
	<script>
		CKEDITOR.replace('descricao');
		CKEDITOR.config.resize_enabled = false;
	</script>
</c:if>

<script>
	$('article').readmore();
</script>
