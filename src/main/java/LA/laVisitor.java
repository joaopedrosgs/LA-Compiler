// Generated from src/main/antlr4/la.g4 by ANTLR 4.7.2
package LA;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link laParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface laVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link laParser#programa}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrograma(laParser.ProgramaContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#declaracoes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracoes(laParser.DeclaracoesContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#decl_local_global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecl_local_global(laParser.Decl_local_globalContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#declaracao_local}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracao_local(laParser.Declaracao_localContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#variavel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariavel(laParser.VariavelContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#identificador}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentificador(laParser.IdentificadorContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#dimensao}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDimensao(laParser.DimensaoContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#tipo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipo(laParser.TipoContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#tipo_basico}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipo_basico(laParser.Tipo_basicoContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#tipo_basico_ident}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipo_basico_ident(laParser.Tipo_basico_identContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#tipo_estendido}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipo_estendido(laParser.Tipo_estendidoContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#valor_constante}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValor_constante(laParser.Valor_constanteContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#registro}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegistro(laParser.RegistroContext ctx);
	/**
	 * Visit a parse tree produced by the {@code declaracao_global_procedimento}
	 * labeled alternative in {@link laParser#declaracao_global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracao_global_procedimento(laParser.Declaracao_global_procedimentoContext ctx);
	/**
	 * Visit a parse tree produced by the {@code declaracao_global_funcao}
	 * labeled alternative in {@link laParser#declaracao_global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracao_global_funcao(laParser.Declaracao_global_funcaoContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#parametro}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParametro(laParser.ParametroContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#parametros}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParametros(laParser.ParametrosContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#corpo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCorpo(laParser.CorpoContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmd(laParser.CmdContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#cmdLeia}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdLeia(laParser.CmdLeiaContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#cmdEscreva}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdEscreva(laParser.CmdEscrevaContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#cmdSe}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdSe(laParser.CmdSeContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#cmdCaso}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdCaso(laParser.CmdCasoContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#cmdPara}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdPara(laParser.CmdParaContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#cmdEnquanto}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdEnquanto(laParser.CmdEnquantoContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#cmdFaca}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdFaca(laParser.CmdFacaContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#cmdAtribuicao}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdAtribuicao(laParser.CmdAtribuicaoContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#cmdChamada}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdChamada(laParser.CmdChamadaContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#cmdRetorne}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdRetorne(laParser.CmdRetorneContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#selecao}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelecao(laParser.SelecaoContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#item_selecao}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitItem_selecao(laParser.Item_selecaoContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#constantes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantes(laParser.ConstantesContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#numero_intervalo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumero_intervalo(laParser.Numero_intervaloContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#op_unario}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp_unario(laParser.Op_unarioContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#exp_aritimetica}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp_aritimetica(laParser.Exp_aritimeticaContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#termo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTermo(laParser.TermoContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#fator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFator(laParser.FatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#op1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp1(laParser.Op1Context ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#op2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp2(laParser.Op2Context ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#op3}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp3(laParser.Op3Context ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#parcela}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParcela(laParser.ParcelaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parcela_unario_id}
	 * labeled alternative in {@link laParser#parcela_unario}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParcela_unario_id(laParser.Parcela_unario_idContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parcela_unario_chamada}
	 * labeled alternative in {@link laParser#parcela_unario}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParcela_unario_chamada(laParser.Parcela_unario_chamadaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parcela_unario_inteiro}
	 * labeled alternative in {@link laParser#parcela_unario}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParcela_unario_inteiro(laParser.Parcela_unario_inteiroContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parcela_unario_real}
	 * labeled alternative in {@link laParser#parcela_unario}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParcela_unario_real(laParser.Parcela_unario_realContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parcela_unario_expr}
	 * labeled alternative in {@link laParser#parcela_unario}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParcela_unario_expr(laParser.Parcela_unario_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parcela_nao_unario_id}
	 * labeled alternative in {@link laParser#parcela_nao_unario}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParcela_nao_unario_id(laParser.Parcela_nao_unario_idContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parcela_nao_unario_cadeia}
	 * labeled alternative in {@link laParser#parcela_nao_unario}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParcela_nao_unario_cadeia(laParser.Parcela_nao_unario_cadeiaContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#exp_relacional}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp_relacional(laParser.Exp_relacionalContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#op_relacional}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp_relacional(laParser.Op_relacionalContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#expressao}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressao(laParser.ExpressaoContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#termo_logico}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTermo_logico(laParser.Termo_logicoContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#fator_logico}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFator_logico(laParser.Fator_logicoContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#parcela_logica}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParcela_logica(laParser.Parcela_logicaContext ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#op_logico_1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp_logico_1(laParser.Op_logico_1Context ctx);
	/**
	 * Visit a parse tree produced by {@link laParser#op_logico_2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp_logico_2(laParser.Op_logico_2Context ctx);
}