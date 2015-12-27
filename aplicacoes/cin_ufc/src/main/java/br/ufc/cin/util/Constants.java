package br.ufc.cin.util;

public class Constants {
	public static final String USUARIO_LOGADO = "usuario";

	/** Páginas */
	/** JOGO */
	public static final String PAGINA_LISTAR_JOGO = "jogo/listar";
	
	public static final String PAGINA_LISTAR_USUARIOS = "jogo/participantes";

	public static final String PAGINA_ADD_PARTICIPANES_JOGO = "jogo/adicionarParticipante";
	
	public static final String PAGINA_CADASTRAR_JOGO = "jogo/novoJogo";
	
	public static final String PAGINA_DETALHES_JOGO = "jogo/detalhes";

	/** EQUIPE */
	public static final String PAGINA_LISTAR_EQUIPES = "equipe/equipes";
	
	public static final String PAGINA_DETALHES_EQUIPE = "equipe/detalhes";

	public static final String PAGINA_CADASTRAR_EQUIPE = "equipe/novaEquipe";
	
	public static final String PAGINA_AVALIACOES_EQUIPE = "equipe/avaliacoes";
	
	public static final String PAGINA_AVALIACAO_EQUIPE = "equipe/avaliacao";
	
	public static final String PAGINA_HISTORICO_EQUIPE = "equipe/historicoEquipeRodada";
	
	public static final String PAGINA_ADD_PARTICIPANTES_EQUIPE = "equipe/adicionarParticipante";
	
	/** RODADA */
	public static final String PAGINA_LISTAR_RODADAS = "rodada/listar";
	
	public static final String PAGINA_NOVA_RODADA = "rodada/novaRodada";
	
	public static final String PAGINA_DETALHES_RODADA = "rodada/detalhes";
	
	public static final String PAGINA_APOSTAS_RODADA = "rodada/apostas";
	
	public static final String PAGINA_APOSTA_RODADA = "rodada/aposta";
	
	public static final String PAGINA_SOLICITAR_REABERTURA_RODADA = "rodada/solicitarReabertura";
	
	public static final String PAGINA_SUBMISSOES_RODADA = "rodada/submissoes";
	
	public static final String PAGINA_SERVICOS_RODADA = "rodada/servicos";
	
	public static final String PAGINA_SOLICITACOES_RODADA = "rodada/solicitacoes";
	
	/** FORMULARIO */
	public static final String PAGINA_DETALHES_FORM = "formulario/detalhes";

	public static final String PAGINA_LISTAR_FORMULARIOS = "formulario/listar";
	
	public static final String PAGINA_CADASTRAR_FORMULARIO = "formulario/formulario";
	
	public static final String PAGINA_RESPONDER_FORMULARIO = "formulario/responder";
	
	/** JOGADOR */
	public static final String PAGINA_PERFIL_JOGADOR = "jogador/perfil";
	
	public static final String PAGINA_PROFILE_JOGADOR = "jogador/profile";
	
	public static final String PAGINA_USUARIO_JOGADOR = "jogador/usuario";
	
	public static final String PAGINA_HISTORICO_JOGADOR = "jogador/historico";
	
	public static final String PAGINA_AVALIACAO_JOGADOR = "jogador/avaliacao";
	
	public static final String PAGINA_AVALIACOES_JOGADOR = "jogador/avaliacoes";
	
	public static final String PAGINA_APOSTAS_JOGADOR = "jogador/apostas";
	
	/** RANKING */
	public static final String PAGINA_RANKINGS_JOGO = "ranking/rankingsJogo";
	
	public static final String PAGINA_RANKINGS_RODADA = "ranking/rankingsRodada";
	
	/** Redirecionamentos */

	public static final String REDIRECT_PAGINA_LISTAR_JOGO = "redirect:/jogo/listar";
	
	public static final String REDIRECT_PAGINA_LISTAR_FORMULARIOS = "redirect:/formularios";
	
	public static final String REDIRECT_PAGINA_FORMULARIO = "redirect:/formulario";
	
	public static final String REDIRECT_PAGINA_LOGIN = "redirect:/login";
	
	public static final String REDIRECT_PAGINA_NOVO_JOGO = "redirect:/jogo/novo-jogo";
	
	/** Mensagens */
	public static final String MENSAGEM_ERRO_AO_CADASTRAR_JOGO = "Verifique os campos obrigatórios e tente novamente.";

	public static final String MENSAGEM_ERRO_AO_CADASTRAR_RODADA = "Aconteceu algum erro ao cadastrar a rodada. Verifique os campos obrigatórios e tente novamente.";
	
	public static final String MENSAGEM_ERRO_AO_CADASTRAR_EQUIPE = "Verifique os campos obrigatórios e tente novamente.";
	
	public static final String MENSAGEM_PERMISSAO_NEGADA = "Você não possui permissão de acesso.";

	public static final String MENSAGEM_CAMPO_OBRIGATORIO = "Campo obrigatório";

	public static final String MENSAGEM_JOGO_INEXISTENTE = "Jogo inexistente.";
	
	public static final String MENSAGEM_EQUIPE_INEXISTENTE = "Equipe inexistente.";

	public static final String MENSAGEM_JOGO_ATUALIZADO = "Jogo atualizado com sucesso!";

	public static final String MENSAGEM_EQUIPE_ATUALIZADA = "Equipe atualizada com sucesso";
	
	public static final String MENSAGEM_JOGO_CADASTRADO = "Jogo cadastrado com sucesso";
	
	public static final String MENSAGEM_EQUIPE_CADASTRADA = "Equipe cadastrada com sucesso";

	public static final String MENSAGEM_JOGO_REMOVIDO = "Jogo removido com sucesso";	
	
	public static final String MENSAGEM_EQUIPE_REMOVIDA = "Equipe removida com sucesso";

	public static final String MENSAGEM_DATA_TERMINO_FUTURA = "A data de término não pode ser anterior à data atual";

	public static final String MENSAGEM_DATA_INICIO_TERMINO = "A data de início deve ser anterior à data de término";

	public static final String MENSAGEM_CAMPO_OBRIGATORIO_SUBMISSAO = "É necessário preencher todas as informações do jogo para submetê-lo";

	public static final String MENSAGEM_DATA_INVALIDA = "Data inválida";

	public static final String MENSAGEM_ERRO_UPLOAD = "Ocorreu um erro no upload de arquivos";

	public static final String MENSAGEM_DOCUMENTO_INEXISTENTE = "Documento inexistente";

	public static final String MENSAGEM_USUARIO_NAO_ENCONTRADO = "Usuário não encontrado";
	
	public static final String MENSAGEM_ADD_ANEXO = "Adicione algum anexo";
	
	public static final String MENSAGEM_USUARIOS_NAO_ASSOCIADOS = "Não existem participantes associados ao jogo!";
	
	public static final String MENSAGEM_EQUIPES_NAO_CRIADAS = "Não existem equipes no jogo.";
	
	public static final String MENSAGEM_FORM_NAO_CRIADOS = "Não existem formulários no jogo. Crie formulários.";
	
	public static final String MENSAGEM_FORM_NAO_EXISTENTE = "Formulário não existe. Ou não está ativo.";
	
	public static final String MENSAGEM_EXCEPTION = "O sistema se comportou de forma inesperada, tente novamente ou contacte o administrador.";
	
	public static final String MENSAGEM_CONSULTORIA_CADASTRADA = "Consultoria cadastrada com sucesso.";
	
	public static final String MENSAGEM_CONSULTORIA_ATUALIZADA = "Consultoria atualizada com sucesso.";
	
	public static final String MENSAGEM_ERRO_CONSULTORIA_CAMPOS_OBG = "Tente novamente, algum campo obrigatório ficou em branco.";
	
	public static final String MENSAGEM_SOLICITACAO_CONSULTORIA = "Solicitação de consultoria enviada com sucesso.";
}
