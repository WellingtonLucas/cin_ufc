$(document).ready(function() {
	$('#adicionarJogoForm').bootstrapValidator({
        feedbackIcons: {
        	valid: 'glyphicon glyphicon-ok',
        	/*invalid: 'glyphicon glyphicon-remove',*/
            validating: 'glyphicon glyphicon-refresh'
        },
        
        fields: {
            nomeDoCurso: {
                validators: {
                    stringLength: {
                        min: 5,
                        message: 'O nome deve ter no mínimo 5 caracteres'
                    },
                    notEmpty:{
            			message: 'O nome do curso é obrigatório.'
            		}
                }
            },
            semestre: {
                validators: {
            		regexp:{
            			regexp: /[0-9]{4,4}\.[0-9]{1,1}/,
            			message: 'Siga o formato do exemplo'
            		},
            		notEmpty:{
            			message: 'O semestre é obrigatório.'
            		}
                }
            },
            descricao: {
                validators: {
            		notEmpty:{
            			message: 'A descrição do jogo é obrigatória.'
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
            anexos :{
            	validators: {
            		callback: {
                        message: 'Adicione pelo menos um anexo',
                        callback: function(value, validator) {
                        	var lines = $('#table-anexos').find('tr').length;
                        	if(lines == 0 || lines == 1) {
                        		if(validator.getFieldElements('anexos').val() == "") {
                        			return false;
                        		}
                        	}
                        	return true;
                        }
                    }
            	}
            }
        }
       
	});
	 
	/*// Usado para não apagar a máscara e enviar somente o valor para o servidor
	$("#adicionarProjetoForm, #submeterProjetoForm").submit(function() {
		$('#valorDaBolsa').val($('#bolsa').maskMoney('unmasked')[0]);
	});
	*/
	/*// Máscaras
    $('[name="bolsa"]').maskMoney({prefix:'R$ ', showSymbol:true, thousands:'.', decimal:','});
    if($('[name="bolsa"]').val() != '') {
    	$('[name="bolsa"]').maskMoney('mask');
    }
	*/
    $("#inicio").datepicker({
		format : "dd/mm/yyyy",
		todayBtn : "linked",
		language : "pt-BR",
		todayHighlight : true,
	}).on('changeDate', function(e) {
		$(this).datepicker('hide');
        $('#adicionarJogoForm').bootstrapValidator('revalidateField', 'inicio');
    });
    
    $("#termino").datepicker({
		format : "dd/mm/yyyy",
		todayBtn : "linked",
		language : "pt-BR",
		todayHighlight : true,
		startDate: new Date()
	}).on('changeDate', function(e) {
		$(this).datepicker('hide');
		$('#adicionarJogoForm').bootstrapValidator('revalidateField', 'inicio');
		$('#adicionarJogoForm').bootstrapValidator('revalidateField', 'termino');
    });
    
    $("#prazo").datepicker({
		format : "dd/mm/yyyy",
		todayBtn : "linked",
		language : "pt-BR",
		todayHighlight : true,
		startDate: new Date()
	}).on('changeDate', function(e) {
		$(this).datepicker('hide');
		$('#atribuirPareceristaForm').bootstrapValidator('revalidateField', 'prazo');
    });
    
    /*$("#participantes, #parecerista, #posicionamento, #avaliacao").select2({
   	 	placeholder: "Buscar...",
   	 	dropdownCssClass: "bigdrop"
    });*/
    
    $('div.error-validation:has(span)').find('span').css('color', '#a94442');
    
	$('div.error-validation:has(span)').find('span').parent().parent().parent().addClass('has-error has-feedback');
	
	$('#confirm-delete').on('show.bs.modal', function(e) {
		$(this).find('.modal-body').text('Tem certeza de que deseja excluir o jogo \"' + $(e.relatedTarget).data('name') + '\"?');
		$(this).find('.btn-danger').attr('href', $(e.relatedTarget).data('href'));
	});

	$('#confirm-delete2').on('show.bs.modal', function(e) {
		$(this).find('.modal-body').text('Tem certeza de que deseja excluir a equipe \"' + $(e.relatedTarget).data('name') + '\"?');
		$(this).find('.btn-danger').attr('href', $(e.relatedTarget).data('href'));
	});
	
	$('#confirm-delete3').on('show.bs.modal', function(e) {
		$(this).find('.modal-body').text('Tem certeza de que deseja excluir o formulário \"' + $(e.relatedTarget).data('name') + '\"?');
		$(this).find('.btn-danger').attr('href', $(e.relatedTarget).data('href'));
	});
	
	$('#delete-file').on('show.bs.modal', function(e) {
		$(this).find('.modal-body').text('Tem certeza de que deseja excluir o arquivo \"' + $(e.relatedTarget).data('name') + '\"?');
		$(this).find('.btn-danger').attr('data-id', $(e.relatedTarget).data('id'));
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

	$('#confirm-submit').on('show.bs.modal', function(e) {
		$(this).find('.modal-body').text('Tem certeza de que deseja desvincular o aluno \"' + $(e.relatedTarget).data('name') + '\"?');
		$(this).find('.btn-primary').attr('href', $(e.relatedTarget).data('href'));
	});
	
	$('#confirm-inativar').on('show.bs.modal', function(e) {
		$(this).find('.modal-body').text('Tem certeza de que deseja inativar a equipe \"' + $(e.relatedTarget).data('name') + '\"?');
		$(this).find('.btn-primary').attr('href', $(e.relatedTarget).data('href'));
	});
	
	$('#confirm-ativar').on('show.bs.modal', function(e) {
		$(this).find('.modal-body').text('Tem certeza de que deseja ativar a equipe \"' + $(e.relatedTarget).data('name') + '\"?');
		$(this).find('.btn-primary').attr('href', $(e.relatedTarget).data('href'));
	});
	
	/*$('#confirm-parecer').on('show.bs.modal', function(e) {
		var indexSelect = document.getElementById("posicionamento").selectedIndex;
		var valueSelected = emitirParecerForm.posicionamento.options[indexSelect].value;

		
		$(this).find('.modal-body').text('Tem certeza de que deseja submeter o parecer do projeto \"' + $(e.relatedTarget).data('name') + '\" com o posicionamento '+valueSelected+'?');
		$(this).find('.btn-primary').attr('href', $(e.relatedTarget).data('href'));
	});
	*/
	
	$(".file").fileinput({
		showUpload: false,
		overwriteInitial: false,
		initialCaption: "Selecione...",
		browseLabel: "Buscar",
		browseClass: "btn btn-default",
		removeLabel: "Excluir",
		msgSelected: "{n} arquivos selecionados",
		msgLoading: "Carregando arquivo {index} de {files} &hellip;"
	});
	
	/*$('#comentar').on('click', function(e){
		var texto = $('#comentario').val();
	    var projetoId = $('#projetoId').val();
	       
	    $.ajax({
	    	type: "POST",
	    	url: '/cin_ufc/comentario/comentar',
	        data : {
	        	texto : texto,
	        	projetoId : projetoId
			}
	    })
	    .success(function(comentario) {
	    	if(comentario.id) {
	    		var data = moment(comentario.data).format('DD/MM/YYYY');
	    		var hora = moment(comentario.data).format('HH:mm');
	    		$('#comentario').val('');
	    		$('ul.cbp_tmtimeline').append(
    				'<li><time class="cbp_tmtime"><span>' + data + '</span><span>' + hora + '</span></time>' +
    				'<div class="cbp_tmlabel"><h2>' + comentario.autor.nome + '</h2><p>' + comentario.texto + '</p></div></li>'
	    		);
	    	}
		})
	});
	
	*/
		/*$('article').readmore();*/
	
});

