$(document).ready(function() {

	$(".file").fileinput({
		showUpload: false,
		overwriteInitial: false,
		initialCaption: "Selecione...",
		browseLabel: "Buscar",
		browseClass: "btn btn-default",
		removeLabel: "Excluir",
		msgSelected: "{n} arquivos selecionados",
		maxFileSize: 10000,
		msgSizeTooLarge: 'O arquivo "{name}" (<b>{size} KB</b>) excede o tamanho de <b>{maxSize} KB</b>. Por favor tente novamente!',
		allowedFileTypes: ['text'],
		allowedFileExtensions: ['pdf','docx','odt','doc'],
		msgInvalidFileType:'Tipo de arquivo inválido "{name}". Apenas os tipos "{types}" são suportados.',
		msgLoading: "Carregando arquivo {index} de {files} &hellip;"
	});
	
	
});

