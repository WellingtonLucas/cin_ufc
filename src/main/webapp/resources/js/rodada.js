$(document).ready(function() {
	$('#adicionarRodadaForm').bootstrapValidator({
        feedbackIcons: {
        	valid: 'glyphicon glyphicon-ok',
            validating: 'glyphicon glyphicon-refresh'
        },
        
        fields: {
            nome: {
                validators: {
                    stringLength: {
                        min: 5,
                        max:255,
                        message: 'O nome deve ter no mínimo 5 caracteres'
                    },
                    notEmpty:{
            			message: 'O nome da rodada é obrigatório.'
            		}
                }
            },
            descricao: {
                validators: {
            		notEmpty:{
            			message: 'A descrição da rodada é obrigatória.'
            		},
            		callback: {
                        message: 'A descrição deve conter mais de 10 caracteres.',
                        callback: function(value, validator) {
                            if (value === '') {
                                return true;
                            }
                            // Get the plain text without HTML
                            var div  = $('<div/>').html(value).get(0),
                                text = div.textContent || div.innerText;

                            return text.length >= 10;
                        }
                    }
                }
            },
            inicio :{
            	validators: {
            		callback: {
                        message: 'A data de início deve ser anterior à data de término',
                        callback: function(value, validator) {
                        	var termino = validator.getFieldElements('termino').val();
                        	if(value != "" && termino != "") {
                        		termino = moment(termino, "DD/MM/YYYY").format("DD/MM/YYYY");
	                        	var inicio = moment(value, "DD/MM/YYYY").format("DD/MM/YYYY");
	                        	if(moment(termino, "DD/MM/YYYY").isBefore(moment(inicio, "DD/MM/YYYY"))) {
	                        		return false;
	                        	}
                        	}
                        	return true;
                        }
                    },
                    notEmpty:{
            			message: 'Data de início é obrigatória.'
            		}
            	}
            }, 
            termino: {
            	validators:{
            		notEmpty:{
            			message: 'Data de termino é obrigatória.'
            		}
            	}
            },
            prazoSubmissao :{
            	validators: {
            		callback: {
                        message: 'O prazo deve ser anterior ao término da rodada.',
                        callback: function(value, validator) {
                        	var termino = validator.getFieldElements('termino').val();
                        	if(value != "" && termino != "") {
                        		termino = moment(termino, "DD/MM/YYYY").format("DD/MM/YYYY");
	                        	var prazoSubmissao = moment(value, "DD/MM/YYYY").format("DD/MM/YYYY");
	                        	if(moment(termino, "DD/MM/YYYY").isBefore(moment(prazoSubmissao, "DD/MM/YYYY"))) {
	                        		return false;
	                        	}
                        	}
                        	return true;
                        }
                    },
                    notEmpty:{
            			message: 'Prazo é obrigatório?.'
            		}
            	}
            },
        }
       
	});
    $("#inicio").datepicker({
		format : "dd/mm/yyyy",
		todayBtn : "linked",
		language : "pt-BR",
		todayHighlight : true,
	}).on('changeDate', function(e) {
		$(this).datepicker('hide');
        $('#adicionarRodadaForm').bootstrapValidator('revalidateField', 'inicio');
    });
    
    $("#termino").datepicker({
		format : "dd/mm/yyyy",
		todayBtn : "linked",
		language : "pt-BR",
		todayHighlight : true,
		startDate: new Date()
	}).on('changeDate', function(e) {
		$(this).datepicker('hide');
		$('#adicionarRodadaForm').bootstrapValidator('revalidateField', 'inicio');
		$('#adicionarRodadaForm').bootstrapValidator('revalidateField', 'termino');
    });
    
    $("#prazoSubmissao").datepicker({
		format : "dd/mm/yyyy",
		todayBtn : "linked",
		language : "pt-BR",
		todayHighlight : true,
		startDate: new Date()
	}).on('changeDate', function(e) {
		$(this).datepicker('hide');
		$('#adicionarRodadaForm').bootstrapValidator('revalidateField', 'inicio');
		$('#adicionarRodadaForm').bootstrapValidator('revalidateField', 'termino');
		$('#adicionarRodadaForm').bootstrapValidator('revalidateField', 'prazoSubmissao');
    });
    
    $('div.error-validation:has(span)').find('span').css('color', '#a94442');
    
	$('div.error-validation:has(span)').find('span').parent().parent().parent().addClass('has-error has-feedback');
	
	$('#confirm-delete-rodada').on('show.bs.modal', function(e) {
		$(this).find('.modal-body').text('Tem certeza de que deseja excluir a rodada \"' + $(e.relatedTarget).data('name') + '\"?');
		$(this).find('.btn-danger').attr('href', $(e.relatedTarget).data('href'));
	});
	
	$('#confirm-inativar-rodada').on('show.bs.modal', function(e) {
		$(this).find('.modal-body').text('Tem certeza de que deseja encerrar a rodada \"' + $(e.relatedTarget).data('name') + '\"?');
		$(this).find('.btn-primary').attr('href', $(e.relatedTarget).data('href'));
	});
	
	$('#confirm-ativar-rodada').on('show.bs.modal', function(e) {
		$(this).find('.modal-body').text('Tem certeza de que deseja iniciar a rodada \"' + $(e.relatedTarget).data('name') + '\"?');
		$(this).find('.btn-primary').attr('href', $(e.relatedTarget).data('href'));
	});
	
	$('#desvincular-equipe').on('show.bs.modal', function(e) {
		$(this).find('.modal-body').text('Tem certeza de que deseja desvincular a equipe \"' + $(e.relatedTarget).data('name') + '\"?');
		$(this).find('.btn-primary').attr('href', $(e.relatedTarget).data('href'));
	});
	
	
});

