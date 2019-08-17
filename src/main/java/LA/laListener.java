// Generated from /home/pedro/NetBeansProjects/la-compiler/src/main/antlr/la.g4 by ANTLR 4.7.2
package LA;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link laParser}.
 */
public interface laListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link laParser#programa}.
	 * @param ctx the parse tree
	 */
	void enterPrograma(laParser.ProgramaContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#programa}.
	 * @param ctx the parse tree
	 */
	void exitPrograma(laParser.ProgramaContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#declaracoes}.
	 * @param ctx the parse tree
	 */
	void enterDeclaracoes(laParser.DeclaracoesContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#declaracoes}.
	 * @param ctx the parse tree
	 */
	void exitDeclaracoes(laParser.DeclaracoesContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#decl_local_global}.
	 * @param ctx the parse tree
	 */
	void enterDecl_local_global(laParser.Decl_local_globalContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#decl_local_global}.
	 * @param ctx the parse tree
	 */
	void exitDecl_local_global(laParser.Decl_local_globalContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#declaracao_local}.
	 * @param ctx the parse tree
	 */
	void enterDeclaracao_local(laParser.Declaracao_localContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#declaracao_local}.
	 * @param ctx the parse tree
	 */
	void exitDeclaracao_local(laParser.Declaracao_localContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#variavel}.
	 * @param ctx the parse tree
	 */
	void enterVariavel(laParser.VariavelContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#variavel}.
	 * @param ctx the parse tree
	 */
	void exitVariavel(laParser.VariavelContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#identificador}.
	 * @param ctx the parse tree
	 */
	void enterIdentificador(laParser.IdentificadorContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#identificador}.
	 * @param ctx the parse tree
	 */
	void exitIdentificador(laParser.IdentificadorContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#dimensao}.
	 * @param ctx the parse tree
	 */
	void enterDimensao(laParser.DimensaoContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#dimensao}.
	 * @param ctx the parse tree
	 */
	void exitDimensao(laParser.DimensaoContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#tipo}.
	 * @param ctx the parse tree
	 */
	void enterTipo(laParser.TipoContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#tipo}.
	 * @param ctx the parse tree
	 */
	void exitTipo(laParser.TipoContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#tipo_basico}.
	 * @param ctx the parse tree
	 */
	void enterTipo_basico(laParser.Tipo_basicoContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#tipo_basico}.
	 * @param ctx the parse tree
	 */
	void exitTipo_basico(laParser.Tipo_basicoContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#tipo_basico_ident}.
	 * @param ctx the parse tree
	 */
	void enterTipo_basico_ident(laParser.Tipo_basico_identContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#tipo_basico_ident}.
	 * @param ctx the parse tree
	 */
	void exitTipo_basico_ident(laParser.Tipo_basico_identContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#tipo_estendido}.
	 * @param ctx the parse tree
	 */
	void enterTipo_estendido(laParser.Tipo_estendidoContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#tipo_estendido}.
	 * @param ctx the parse tree
	 */
	void exitTipo_estendido(laParser.Tipo_estendidoContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#valor_constante}.
	 * @param ctx the parse tree
	 */
	void enterValor_constante(laParser.Valor_constanteContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#valor_constante}.
	 * @param ctx the parse tree
	 */
	void exitValor_constante(laParser.Valor_constanteContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#registro}.
	 * @param ctx the parse tree
	 */
	void enterRegistro(laParser.RegistroContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#registro}.
	 * @param ctx the parse tree
	 */
	void exitRegistro(laParser.RegistroContext ctx);
	/**
	 * Enter a parse tree produced by the {@code declaracao_global_procedimento}
	 * labeled alternative in {@link laParser#declaracao_global}.
	 * @param ctx the parse tree
	 */
	void enterDeclaracao_global_procedimento(laParser.Declaracao_global_procedimentoContext ctx);
	/**
	 * Exit a parse tree produced by the {@code declaracao_global_procedimento}
	 * labeled alternative in {@link laParser#declaracao_global}.
	 * @param ctx the parse tree
	 */
	void exitDeclaracao_global_procedimento(laParser.Declaracao_global_procedimentoContext ctx);
	/**
	 * Enter a parse tree produced by the {@code declaracao_global_funcao}
	 * labeled alternative in {@link laParser#declaracao_global}.
	 * @param ctx the parse tree
	 */
	void enterDeclaracao_global_funcao(laParser.Declaracao_global_funcaoContext ctx);
	/**
	 * Exit a parse tree produced by the {@code declaracao_global_funcao}
	 * labeled alternative in {@link laParser#declaracao_global}.
	 * @param ctx the parse tree
	 */
	void exitDeclaracao_global_funcao(laParser.Declaracao_global_funcaoContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#parametro}.
	 * @param ctx the parse tree
	 */
	void enterParametro(laParser.ParametroContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#parametro}.
	 * @param ctx the parse tree
	 */
	void exitParametro(laParser.ParametroContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#parametros}.
	 * @param ctx the parse tree
	 */
	void enterParametros(laParser.ParametrosContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#parametros}.
	 * @param ctx the parse tree
	 */
	void exitParametros(laParser.ParametrosContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#corpo}.
	 * @param ctx the parse tree
	 */
	void enterCorpo(laParser.CorpoContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#corpo}.
	 * @param ctx the parse tree
	 */
	void exitCorpo(laParser.CorpoContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#cmd}.
	 * @param ctx the parse tree
	 */
	void enterCmd(laParser.CmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#cmd}.
	 * @param ctx the parse tree
	 */
	void exitCmd(laParser.CmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#cmdLeia}.
	 * @param ctx the parse tree
	 */
	void enterCmdLeia(laParser.CmdLeiaContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#cmdLeia}.
	 * @param ctx the parse tree
	 */
	void exitCmdLeia(laParser.CmdLeiaContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#cmdEscreva}.
	 * @param ctx the parse tree
	 */
	void enterCmdEscreva(laParser.CmdEscrevaContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#cmdEscreva}.
	 * @param ctx the parse tree
	 */
	void exitCmdEscreva(laParser.CmdEscrevaContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#cmdSe}.
	 * @param ctx the parse tree
	 */
	void enterCmdSe(laParser.CmdSeContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#cmdSe}.
	 * @param ctx the parse tree
	 */
	void exitCmdSe(laParser.CmdSeContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#cmdCaso}.
	 * @param ctx the parse tree
	 */
	void enterCmdCaso(laParser.CmdCasoContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#cmdCaso}.
	 * @param ctx the parse tree
	 */
	void exitCmdCaso(laParser.CmdCasoContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#cmdPara}.
	 * @param ctx the parse tree
	 */
	void enterCmdPara(laParser.CmdParaContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#cmdPara}.
	 * @param ctx the parse tree
	 */
	void exitCmdPara(laParser.CmdParaContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#cmdEnquanto}.
	 * @param ctx the parse tree
	 */
	void enterCmdEnquanto(laParser.CmdEnquantoContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#cmdEnquanto}.
	 * @param ctx the parse tree
	 */
	void exitCmdEnquanto(laParser.CmdEnquantoContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#cmdFaca}.
	 * @param ctx the parse tree
	 */
	void enterCmdFaca(laParser.CmdFacaContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#cmdFaca}.
	 * @param ctx the parse tree
	 */
	void exitCmdFaca(laParser.CmdFacaContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#cmdAtribuicao}.
	 * @param ctx the parse tree
	 */
	void enterCmdAtribuicao(laParser.CmdAtribuicaoContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#cmdAtribuicao}.
	 * @param ctx the parse tree
	 */
	void exitCmdAtribuicao(laParser.CmdAtribuicaoContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#cmdChamada}.
	 * @param ctx the parse tree
	 */
	void enterCmdChamada(laParser.CmdChamadaContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#cmdChamada}.
	 * @param ctx the parse tree
	 */
	void exitCmdChamada(laParser.CmdChamadaContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#cmdRetorne}.
	 * @param ctx the parse tree
	 */
	void enterCmdRetorne(laParser.CmdRetorneContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#cmdRetorne}.
	 * @param ctx the parse tree
	 */
	void exitCmdRetorne(laParser.CmdRetorneContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#selecao}.
	 * @param ctx the parse tree
	 */
	void enterSelecao(laParser.SelecaoContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#selecao}.
	 * @param ctx the parse tree
	 */
	void exitSelecao(laParser.SelecaoContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#item_selecao}.
	 * @param ctx the parse tree
	 */
	void enterItem_selecao(laParser.Item_selecaoContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#item_selecao}.
	 * @param ctx the parse tree
	 */
	void exitItem_selecao(laParser.Item_selecaoContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#constantes}.
	 * @param ctx the parse tree
	 */
	void enterConstantes(laParser.ConstantesContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#constantes}.
	 * @param ctx the parse tree
	 */
	void exitConstantes(laParser.ConstantesContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#numero_intervalo}.
	 * @param ctx the parse tree
	 */
	void enterNumero_intervalo(laParser.Numero_intervaloContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#numero_intervalo}.
	 * @param ctx the parse tree
	 */
	void exitNumero_intervalo(laParser.Numero_intervaloContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#op_unario}.
	 * @param ctx the parse tree
	 */
	void enterOp_unario(laParser.Op_unarioContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#op_unario}.
	 * @param ctx the parse tree
	 */
	void exitOp_unario(laParser.Op_unarioContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#exp_aritimetica}.
	 * @param ctx the parse tree
	 */
	void enterExp_aritimetica(laParser.Exp_aritimeticaContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#exp_aritimetica}.
	 * @param ctx the parse tree
	 */
	void exitExp_aritimetica(laParser.Exp_aritimeticaContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#termo}.
	 * @param ctx the parse tree
	 */
	void enterTermo(laParser.TermoContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#termo}.
	 * @param ctx the parse tree
	 */
	void exitTermo(laParser.TermoContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#fator}.
	 * @param ctx the parse tree
	 */
	void enterFator(laParser.FatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#fator}.
	 * @param ctx the parse tree
	 */
	void exitFator(laParser.FatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#op1}.
	 * @param ctx the parse tree
	 */
	void enterOp1(laParser.Op1Context ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#op1}.
	 * @param ctx the parse tree
	 */
	void exitOp1(laParser.Op1Context ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#op2}.
	 * @param ctx the parse tree
	 */
	void enterOp2(laParser.Op2Context ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#op2}.
	 * @param ctx the parse tree
	 */
	void exitOp2(laParser.Op2Context ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#op3}.
	 * @param ctx the parse tree
	 */
	void enterOp3(laParser.Op3Context ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#op3}.
	 * @param ctx the parse tree
	 */
	void exitOp3(laParser.Op3Context ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#parcela}.
	 * @param ctx the parse tree
	 */
	void enterParcela(laParser.ParcelaContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#parcela}.
	 * @param ctx the parse tree
	 */
	void exitParcela(laParser.ParcelaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parcela_unario_id}
	 * labeled alternative in {@link laParser#parcela_unario}.
	 * @param ctx the parse tree
	 */
	void enterParcela_unario_id(laParser.Parcela_unario_idContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parcela_unario_id}
	 * labeled alternative in {@link laParser#parcela_unario}.
	 * @param ctx the parse tree
	 */
	void exitParcela_unario_id(laParser.Parcela_unario_idContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parcela_unario_chamada}
	 * labeled alternative in {@link laParser#parcela_unario}.
	 * @param ctx the parse tree
	 */
	void enterParcela_unario_chamada(laParser.Parcela_unario_chamadaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parcela_unario_chamada}
	 * labeled alternative in {@link laParser#parcela_unario}.
	 * @param ctx the parse tree
	 */
	void exitParcela_unario_chamada(laParser.Parcela_unario_chamadaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parcela_unario_inteiro}
	 * labeled alternative in {@link laParser#parcela_unario}.
	 * @param ctx the parse tree
	 */
	void enterParcela_unario_inteiro(laParser.Parcela_unario_inteiroContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parcela_unario_inteiro}
	 * labeled alternative in {@link laParser#parcela_unario}.
	 * @param ctx the parse tree
	 */
	void exitParcela_unario_inteiro(laParser.Parcela_unario_inteiroContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parcela_unario_real}
	 * labeled alternative in {@link laParser#parcela_unario}.
	 * @param ctx the parse tree
	 */
	void enterParcela_unario_real(laParser.Parcela_unario_realContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parcela_unario_real}
	 * labeled alternative in {@link laParser#parcela_unario}.
	 * @param ctx the parse tree
	 */
	void exitParcela_unario_real(laParser.Parcela_unario_realContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parcela_unario_expr}
	 * labeled alternative in {@link laParser#parcela_unario}.
	 * @param ctx the parse tree
	 */
	void enterParcela_unario_expr(laParser.Parcela_unario_exprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parcela_unario_expr}
	 * labeled alternative in {@link laParser#parcela_unario}.
	 * @param ctx the parse tree
	 */
	void exitParcela_unario_expr(laParser.Parcela_unario_exprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parcela_nao_unario_id}
	 * labeled alternative in {@link laParser#parcela_nao_unario}.
	 * @param ctx the parse tree
	 */
	void enterParcela_nao_unario_id(laParser.Parcela_nao_unario_idContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parcela_nao_unario_id}
	 * labeled alternative in {@link laParser#parcela_nao_unario}.
	 * @param ctx the parse tree
	 */
	void exitParcela_nao_unario_id(laParser.Parcela_nao_unario_idContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parcela_nao_unario_cadeia}
	 * labeled alternative in {@link laParser#parcela_nao_unario}.
	 * @param ctx the parse tree
	 */
	void enterParcela_nao_unario_cadeia(laParser.Parcela_nao_unario_cadeiaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parcela_nao_unario_cadeia}
	 * labeled alternative in {@link laParser#parcela_nao_unario}.
	 * @param ctx the parse tree
	 */
	void exitParcela_nao_unario_cadeia(laParser.Parcela_nao_unario_cadeiaContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#exp_relacional}.
	 * @param ctx the parse tree
	 */
	void enterExp_relacional(laParser.Exp_relacionalContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#exp_relacional}.
	 * @param ctx the parse tree
	 */
	void exitExp_relacional(laParser.Exp_relacionalContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#op_relacional}.
	 * @param ctx the parse tree
	 */
	void enterOp_relacional(laParser.Op_relacionalContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#op_relacional}.
	 * @param ctx the parse tree
	 */
	void exitOp_relacional(laParser.Op_relacionalContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#expressao}.
	 * @param ctx the parse tree
	 */
	void enterExpressao(laParser.ExpressaoContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#expressao}.
	 * @param ctx the parse tree
	 */
	void exitExpressao(laParser.ExpressaoContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#termo_logico}.
	 * @param ctx the parse tree
	 */
	void enterTermo_logico(laParser.Termo_logicoContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#termo_logico}.
	 * @param ctx the parse tree
	 */
	void exitTermo_logico(laParser.Termo_logicoContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#fator_logico}.
	 * @param ctx the parse tree
	 */
	void enterFator_logico(laParser.Fator_logicoContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#fator_logico}.
	 * @param ctx the parse tree
	 */
	void exitFator_logico(laParser.Fator_logicoContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#parcela_logica}.
	 * @param ctx the parse tree
	 */
	void enterParcela_logica(laParser.Parcela_logicaContext ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#parcela_logica}.
	 * @param ctx the parse tree
	 */
	void exitParcela_logica(laParser.Parcela_logicaContext ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#op_logico_1}.
	 * @param ctx the parse tree
	 */
	void enterOp_logico_1(laParser.Op_logico_1Context ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#op_logico_1}.
	 * @param ctx the parse tree
	 */
	void exitOp_logico_1(laParser.Op_logico_1Context ctx);
	/**
	 * Enter a parse tree produced by {@link laParser#op_logico_2}.
	 * @param ctx the parse tree
	 */
	void enterOp_logico_2(laParser.Op_logico_2Context ctx);
	/**
	 * Exit a parse tree produced by {@link laParser#op_logico_2}.
	 * @param ctx the parse tree
	 */
	void exitOp_logico_2(laParser.Op_logico_2Context ctx);
}