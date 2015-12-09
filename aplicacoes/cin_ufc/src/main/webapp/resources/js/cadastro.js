$(document).ready(function() {
	$('#cadastro').bootstrapValidator({
        feedbackIcons: {
        	valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
		
		fields : {
			nome : {
				validators : {
					stringLength : {
						min : 5,
						message : 'O nome deve ter no mínimo 5 caracteres'
					},
					notEmpty : {
						message : 'O nome é obrigatório'
					}
				}
			},
			curso : {
				validators : {
					stringLength : {
						min : 5,
						message : 'O curso deve ter no mínimo 5 caracteres'
					},
					notEmpty : {
						message : 'O nome do curso é obrigatório'
					}
				}
			},
			email : {
				validators : {				
					notEmpty : {
						message : 'O email é obrigatório'
					}
				}
			},
			senha : {
				validators : {
					stringLength : {
						min : 5,
						message : 'A senha deve ter no mínimo 5 caracteres'
					},
					notEmpty : {
						message : 'A senha é obrigatória'
					}
				}
			}
		}
	});
	
	$('div.error-validation:has(span)').find('span').css('color', '#a94442');
	    
	$('div.error-validation:has(span)').find('span').parent().parent().parent().addClass('has-error has-feedback');
		

});
