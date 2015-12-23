$(document).ready(function() {

	$("#entrega").fileinput({
		showUpload: true,
		overwriteInitial: false,
		initialCaption: "Selecione...",
		browseLabel: "Buscar",
		browseClass: "btn btn-primary",
		removeLabel: "Excluir",
		msgSelected: "{n} arquivos selecionados",
		maxFileSize: 10240,
		maxFileCount: 1,
		msgSizeTooLarge: 'O arquivo "{name}" (<b>{size} KB</b>) excede o tamanho de <b>{maxSize} KB</b>. Por favor tente novamente!',
		msgFilesTooMany: "Selecione apenas um arquivo por vez. Tente novamente.",
		allowedFileExtensions: ['pdf','docx','odt','doc'],
		msgInvalidFileExtension: 'Extensão do arquivo "{name}" é inválida. Somente "{extensions}" são suportados.',
		msgLoading: "Carregando arquivo {index} de {files} &hellip;"
	});
	
	
});

