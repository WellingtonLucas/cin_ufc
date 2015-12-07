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
                    callback: {
                        message: 'A data de início deve ser anterior à data do prazo de submissão.',
                        callback: function(value, validator) {
                        	var prazoSubmissao = validator.getFieldElements('prazoSubmissao').val();
                        	if(value != "" && prazoSubmissao != "") {
                        		prazoSubmissao = moment(prazoSubmissao, "DD/MM/YYYY").format("DD/MM/YYYY");
	                        	var inicio = moment(value, "DD/MM/YYYY").format("DD/MM/YYYY");
	                        	if(moment(prazoSubmissao, "DD/MM/YYYY").isBefore(moment(inicio, "DD/MM/YYYY"))) {
	                        		return false;
	                        	}
                        	}
                        	return true;
                        }
                    },
                    callback: {
                        message: 'A data de início deve ser anterior à data do prazo de avaliação.',
                        callback: function(value, validator) {
                        	var terminoAvaliacao = validator.getFieldElements('terminoAvaliacao').val();
                        	if(value != "" && terminoAvaliacao != "") {
                        		terminoAvaliacao = moment(terminoAvaliacao, "DD/MM/YYYY").format("DD/MM/YYYY");
	                        	var inicio = moment(value, "DD/MM/YYYY").format("DD/MM/YYYY");
	                        	if(moment(terminoAvaliacao, "DD/MM/YYYY").isBefore(moment(inicio, "DD/MM/YYYY"))) {
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
            		callback: {
                        message: 'A data de término deve ser posterior à data de início',
                        callback: function(value, validator) {
                        	var inicio = validator.getFieldElements('inicio').val();
                        	if(value != "" && inicio != "") {
                        		inicio = moment(inicio, "DD/MM/YYYY").format("DD/MM/YYYY");
	                        	var termino = moment(value, "DD/MM/YYYY").format("DD/MM/YYYY");
	                        	if(moment(inicio, "DD/MM/YYYY").isAfter(moment(termino, "DD/MM/YYYY"))) {
	                        		return false;
	                        	}
                        	}
                        	return true;
                        }
                    },
                    callback: {
                        message: 'A data de término deve ser posterior à data do prazo de submissão.',
                        callback: function(value, validator) {
                        	var prazoSubmissao = validator.getFieldElements('prazoSubmissao').val();
                        	if(value != "" && prazoSubmissao != "") {
                        		prazoSubmissao = moment(prazoSubmissao, "DD/MM/YYYY").format("DD/MM/YYYY");
	                        	var termino = moment(value, "DD/MM/YYYY").format("DD/MM/YYYY");
	                        	if(moment(prazoSubmissao, "DD/MM/YYYY").isAfter(moment(termino, "DD/MM/YYYY"))) {
	                        		return false;
	                        	}
                        	}
                        	return true;
                        }
                    },
                    callback: {
                        message: 'A data de término deve ser posterior à data de avaliação.',
                        callback: function(value, validator) {
                        	var terminoAvaliacao = validator.getFieldElements('terminoAvaliacao').val();
                        	if(value != "" && terminoAvaliacao != "") {
                        		terminoAvaliacao = moment(terminoAvaliacao, "DD/MM/YYYY").format("DD/MM/YYYY");
	                        	var termino = moment(value, "DD/MM/YYYY").format("DD/MM/YYYY");
	                        	if(moment(terminoAvaliacao, "DD/MM/YYYY").isAfter(moment(termino, "DD/MM/YYYY"))) {
	                        		return false;
	                        	}
                        	}
                        	return true;
                        }
                    },
            		notEmpty:{
            			message: 'Data de termino é obrigatória.'
            		}
            	}
            },
            terminoAvaliacao: {
            	validators:{
            		callback: {
                        message: 'O prazo para avaliação deve ser anterior ao término da rodada.',
                        callback: function(value, validator) {
                        	var termino = validator.getFieldElements('termino').val();
                        	if(value != "" && termino != "") {
                        		termino = moment(termino, "DD/MM/YYYY").format("DD/MM/YYYY");
	                        	var terminoAvaliacao = moment(value, "DD/MM/YYYY").format("DD/MM/YYYY");
	                        	if(moment(termino, "DD/MM/YYYY").isBefore(moment(terminoAvaliacao, "DD/MM/YYYY"))) {
	                        		return false;
	                        	}
                        	}
                        	return true;
                        }
                    },
                    callback: {
                        message: 'O prazo para avaliação deve ser posterior ao prazo de submissão.',
                        callback: function(value, validator) {
                        	var prazoSubmissao = validator.getFieldElements('prazoSubmissao').val();
                        	if(value != "" && prazoSubmissao != "") {
                        		prazoSubmissao = moment(prazoSubmissao, "DD/MM/YYYY").format("DD/MM/YYYY");
	                        	var terminoAvaliacao = moment(value, "DD/MM/YYYY").format("DD/MM/YYYY");
	                        	if(moment(prazoSubmissao, "DD/MM/YYYY"). isAfter(moment(terminoAvaliacao, "DD/MM/YYYY"))) {
	                        		return false;
	                        	}
                        	}
                        	return true;
                        }
                    },
            		notEmpty:{
            			message: 'Data de prazo das avaliações é obrigatório.'
            		}
            	}
            },
            prazoSubmissao :{
            	validators: {
            		callback: {
                        message: 'O prazo de submissões deve ser anterior ao término da rodada.',
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
                    callback: {
                        message: 'O prazo de submissões deve ser posterior ao início da rodada.',
                        callback: function(value, validator) {
                        	var inicio = validator.getFieldElements('inicio').val();
                        	if(value != "" && inicio != "") {
                        		inicio = moment(inicio, "DD/MM/YYYY").format("DD/MM/YYYY");
	                        	var prazoSubmissao = moment(value, "DD/MM/YYYY").format("DD/MM/YYYY");
	                        	if(moment(inicio, "DD/MM/YYYY").isAfter(moment(prazoSubmissao, "DD/MM/YYYY"))) {
	                        		return false;
	                        	}
                        	}
                        	return true;
                        }
                    },
                    notEmpty:{
            			message: 'Prazo é obrigatório.'
            		}
            	}
            },
            selec: {
                validators: {
                    notEmpty: {
                        message: 'Selecione um formulário para esta rodada.'
                    }
                }
            }
        }
       
	});
	$('#reaberturaForm').bootstrapValidator({
        feedbackIcons: {
        	valid: 'glyphicon glyphicon-ok',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
        	quantidadeDia:{
            	validators:{
            		notEmpty:{
            			message: 'Selecione um valor para submeter um pedido.'
            		}
            	}
            }        	
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
		$('#adicionarRodadaForm').bootstrapValidator('revalidateField', 'termino');
		$('#adicionarRodadaForm').bootstrapValidator('revalidateField', 'prazoSubmissao');
		$('#adicionarRodadaForm').bootstrapValidator('revalidateField', 'terminoAvaliacao');
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
		$('#adicionarRodadaForm').bootstrapValidator('revalidateField', 'prazoSubmissao');
		$('#adicionarRodadaForm').bootstrapValidator('revalidateField', 'terminoAvaliacao');
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
		$('#adicionarRodadaForm').bootstrapValidator('revalidateField', 'terminoAvaliacao');
    });
    
    $("#terminoAvaliacao").datepicker({
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
		$('#adicionarRodadaForm').bootstrapValidator('revalidateField', 'terminoAvaliacao');
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
	
	$('#ativar-equipe').on('show.bs.modal', function(e) {
		$(this).find('.modal-body').text('Tem certeza de que deseja ativar a equipe \"' + $(e.relatedTarget).data('name') + '\"? Isso fará com que a equipe possa efetuar novas entregas.');
		$(this).find('.btn-primary').attr('href', $(e.relatedTarget).data('href'));
	});
	
	$('#inativar-equipe').on('show.bs.modal', function(e) {
		$(this).find('.modal-body').text('Tem certeza de que deseja inativar a equipe \"' + $(e.relatedTarget).data('name') + '\"? Isso fará com que a equipe não possa efetuar novas entregas após o prazo.');
		$(this).find('.btn-warning').attr('href', $(e.relatedTarget).data('href'));
	});
	
	$('#delete-file').on('show.bs.modal', function(e) {
		$(this).find('.modal-body').text('Tem certeza de que deseja excluir o arquivo \"' + $(e.relatedTarget).data('name') + '\"?');
		$(this).find('.btn-danger').attr('data-id', $(e.relatedTarget).data('id'));
	});

	$('#confirm-gerar-ranking').on('show.bs.modal', function(e) {
		$(this).find('.modal-body').text('Tem certeza que deseja gerar os Rankings para \"' + $(e.relatedTarget).data('name') + '\"?');
		$(this).find('.btn-success').attr('href', $(e.relatedTarget).data('href'));
	});
	
	$('#confirm-gerar-notas').on('show.bs.modal', function(e) {
		$(this).find('.modal-body').text('Tem certeza que deseja gerar as Notas para \"' + $(e.relatedTarget).data('name') + '\"?');
		$(this).find('.btn-success').attr('href', $(e.relatedTarget).data('href'));
	});
	
	$('.confirm-delete-file').on('click', function(e) {
		var id = $(this).attr('data-id');
		$.ajax({
			type: "POST",
			url: "/cin_ufc/documento/ajax/remover/" + id
		})
		.success(function( result ) {
			if(result.result == 'ok') {
				$('#documento-'+id).remove();
			} else {
				
			}
		});
	});
	
});

