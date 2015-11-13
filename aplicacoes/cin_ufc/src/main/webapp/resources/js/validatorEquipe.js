$(document).ready(function() {
	$('#adicionarEquipeForm').bootstrapValidator({
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
            			message: 'Escolha um nome para sua equipe.'
            		}
                }
            },
        }
       
	});
});

