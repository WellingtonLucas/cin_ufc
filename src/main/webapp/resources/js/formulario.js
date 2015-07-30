$(function () {
    $('#btnAdd').click(function () {
        var num     = $('.clonedInput').length, // Checks to see how many "duplicatable" input fields we currently have
            newNum  = new Number(num + 1),      // The numeric ID of the new input field being added, increasing by 1 each time
            newElem = $('#entry' + num).clone().attr('id', 'entry' + newNum).fadeIn('slow'); // create the new element via clone(), and manipulate it's ID using newNum value
    
        // H2 - section
        newElem.find('.heading-reference').attr('id', 'ID' + newNum + '_reference').attr('name', 'ID' + newNum + '_reference').html('Questão ' + newNum);

        // Questão 1 - text
        newElem.find('.qt1').attr('for', 'ID' + newNum + '_questao').html('Questão ' + newNum + ':');
        newElem.find('.questao1')
        	.attr('id', 'ID' + newNum + '_questao')
        	.attr('name', 'perguntas['+ (newNum -1) +'].descricao').val('');
        //.attr('path', 'perguntas['+ (newNum -1) +'].descricao')

        // Opção 1 - text
        newElem.find('.opt1').attr('for', 'ID' + newNum + '_opcao');
		newElem.find('.optR').attr('id', 'ID' + newNum + '_opcaoR').attr('name', 'ID' + newNum + '_opcaoR').val('');
        newElem.find('.opcao1')
        	.attr('id', 'ID' + newNum + '_opcao')
        	.attr('name', 'perguntas['+ (newNum -1) +'].opcoes[0].descricao').val('');
        //.attr('path', 'perguntas['+ (newNum -1) +'].opcoes[0].descricao')
        
		// Opção 2 - text
        newElem.find('.opt2').attr('for', 'ID' + newNum + '_opcao2');
		newElem.find('.optR').attr('id', 'ID' + newNum + '_opcaoR2').attr('name', 'ID' + newNum + '_opcaoR').val('');
        newElem.find('.opcao2')
        	.attr('id', 'ID' + newNum + '_opcao2')
        	.attr('name', 'perguntas['+ (newNum -1) +'].opcoes[1].descricao').val('');
        //.attr('path', 'perguntas['+ (newNum -1) +'].opcoes[1].descricao')
        
		// Opção 3 - text
        newElem.find('.opt3').attr('for', 'ID' + newNum + '_opcao3');
		newElem.find('.optR').attr('id', 'ID' + newNum + '_opcaoR3').attr('name', 'ID' + newNum + '_opcaoR').val('');
        newElem.find('.opcao3')
        	.attr('id', 'ID' + newNum + '_opcao3')
        	.attr('name', 'perguntas['+ (newNum -1) +'].opcoes[2].descricao').val('');
        //.attr('path', 'perguntas['+ (newNum) +'].opcoes[2].descricao')

        // Opção 4 - text
        newElem.find('.opt4').attr('for', 'ID' + newNum + '_opcao4');
		newElem.find('.optR').attr('id', 'ID' + newNum + '_opcaoR4').attr('name', 'ID' + newNum + '_opcaoR').val('');
        newElem.find('.opcao4')
        	.attr('id', 'ID' + newNum + '_opcao4')
        	.attr('name', 'perguntas['+ (newNum -1) +'].opcoes[3].descricao').val('');
        //.attr('path', 'perguntas['+ (newNum) +'].opcoes[3].descricao')
        
		// Opção 5 - text
        newElem.find('.opt5').attr('for', 'ID' + newNum + '_opcao5');
		newElem.find('.optR').attr('id', 'ID' + newNum + '_opcaoR5').attr('name', 'ID' + newNum + '_opcaoR').val('');
        newElem.find('.opcao5')
        	.attr('id', 'ID' + newNum + '_opcao5')
        	.attr('name', 'perguntas['+ (newNum -1) +'].opcoes[4].descricao').val('');
        	//.attr('path', 'perguntas['+ (newNum -1) +'].opcoes[4].descricao')
		
    // Insert the new element after the last "duplicatable" input field
        $('#entry' + num).after(newElem);
        $('#ID' + newNum + '_questao').focus();

    // Enable the "remove" button. This only shows once you have a duplicated section.
        $('#btnDel').attr('disabled', false);

    // Right now you can only add 4 sections, for a total of 5. Change '5' below to the max number of sections you want to allow.
        if (newNum == 5)
        $('#btnAdd').attr('disabled', true).prop('value', "Você excedeu o limite de "+newNum+" perguntas."); // value here updates the text in the 'add' button when the limit is reached 
    });

    $('#btnDel').click(function () {
    // Confirmation dialog box. Works on all desktop browsers and iPhone.
        if (confirm("Tem certeza que quer remover a questão? Ela não poderá ser recuperada."))
            {
                var num = $('.clonedInput').length;
                // how many "duplicatable" input fields we currently have
                $('#entry' + num).slideUp('slow', function () {$(this).remove();
                // if only one element remains, disable the "remove" button
                if (num -1 === 1)
                $('#btnDel').attr('disabled', true);
                // enable the "add" button
                $('#btnAdd').attr('disabled', false).prop('value', "Nova questão");});
            }
        return false; // Removes the last section you added
    });
    // Enable the "add" button
    $('#btnAdd').attr('disabled', false);
    // Disable the "remove" button
    $('#btnDel').attr('disabled', true);
});