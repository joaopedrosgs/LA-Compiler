package t1;


public class AnalisadorSemanticoLA extends LABaseVisitor<Void> {

    private PilhaDeTabelas pilhaDeTabelas = new PilhaDeTabelas();
    private SaidaParser sp;

    private String auxTipoExp = "null"; // Variavel para conferir o comando de atruibuiçao

    public AnalisadorSemanticoLA(SaidaParser sp){
        this.sp = sp;
    }

    @Override
    public Void visitPrograma(LAParser.ProgramaContext ctx) {
        //programa : declaracoes 'algoritmo' corpo 'fim_algoritmo';
        pilhaDeTabelas.empilhar(new TabelaDeSimbolos("global"));
        visitDeclaracoes(ctx.declaracoes());
        super.visitPrograma(ctx);
        visitCorpo(ctx.corpo());

        pilhaDeTabelas.desempilhar();

        return null;
    }

    @Override
    public Void visitVariavel(LAParser.VariavelContext ctx) {
        /* variavel : identificador1=identificador (',' outrosIdentificadores+=identificador)* ':' tipo ; */

        //Variavel necessária para garantir que seja checado somente o tipo do ponteiro, e não a string começando com ^
        String tipoChecado = "null";
        if (!pilhaDeTabelas.topo().existeSimbolo(ctx.identificador1.getText())) {
            pilhaDeTabelas.topo().adicionarSimbolo(ctx.identificador1.getText(), ctx.tipo().getText());
            if(ctx.tipo().getText().startsWith("^")){
                //Pega o tipo sem o simbolo de ponteiro (^)
                tipoChecado = ctx.tipo().getText().substring(1);
            }else{
                tipoChecado = ctx.tipo().getText();
            }
            //Checa se é um simbolo valido da linguagem
            if (!tipoChecado.equals("literal") && !tipoChecado.equals("inteiro") && !tipoChecado.equals("real") && !tipoChecado.equals("logico")) {
                sp.println("Linha " + ctx.tipo().getStart().getLine() + ": tipo " + ctx.tipo().getText() + " nao declarado");
            }
        } else {
            //Caso o identificador já tenha sido declarado, acusa o erro
            sp.println("Linha " + ctx.getStart().getLine() + ": identificador " + ctx.identificador1.getText() + " ja declarado anteriormente");
        }
        if (ctx.outrosIdentificadores != null) {
            for (LAParser.IdentificadorContext outrosIdentCtx : ctx.outrosIdentificadores) {
                if (!pilhaDeTabelas.topo().existeSimbolo(outrosIdentCtx.getText())) {
                    pilhaDeTabelas.topo().adicionarSimbolo(outrosIdentCtx.getText(), ctx.tipo().getText());
                    if (!ctx.tipo().getText().equals("literal") && !ctx.tipo().getText().equals("inteiro") && !ctx.tipo().getText().equals("real") && !ctx.tipo().getText().equals("logico")) {
                        sp.println("Linha " + ctx.tipo().getStart().getLine() + ": tipo " + ctx.tipo().getText() + " nao declarado");
                    }
                } else {
                    sp.println("Linha " + outrosIdentCtx.getStart().getLine() + ": identificador " + outrosIdentCtx.getText() + " ja declarado anteriormente");
                }
            }
        }
        return null;
    }

    @Override
    public Void visitIdentificador(LAParser.IdentificadorContext ctx) {
        /* identificador : ident1=IDENT ('.' outrosIdent+=IDENT)* dimensao ; */
        if(!pilhaDeTabelas.existeSimbolo(ctx.ident1.getText())){
            sp.println("Linha " + ctx.getStart().getLine() + ": identificador " + ctx.ident1.getText() + " nao declarado");
        }
        return null;
    }

    @Override
    public Void visitCmdAtribuicao(LAParser.CmdAtribuicaoContext ctx) {
        /* cmdAtribuicao : ('^')? identificador '<-' expressao ; */
        String tipoVariavel = pilhaDeTabelas.getSimboloTipo(ctx.identificador().getText());
        auxTipoExp = "null";
        visitExpressao(ctx.expressao());

        if(auxTipoExp.equals("inteiro") && tipoVariavel.equals("real")){
            auxTipoExp = "real";
        }
        //Verifica se a atribuição ao ponteiro está correta, se nao, acusa o erro
        if(tipoVariavel.startsWith("^") && (auxTipoExp != "endereco")){
            sp.println("Linha "+ ctx.identificador().getStart().getLine() + ": atribuicao nao compativel para ^" + ctx.identificador().getText());
            return null;

            //Verifica se a variavel nao é ponteiro, e se a expressão atribuida é valida, caso não, acusa o erro
        }else if(!tipoVariavel.equals(auxTipoExp) && !tipoVariavel.startsWith("^")){
            sp.println("Linha "+ ctx.identificador().getStart().getLine() + ": atribuicao nao compativel para " + ctx.identificador().getText());
        }
        return null;
    }

    @Override
    public Void visitExp_relacional(LAParser.Exp_relacionalContext ctx) {
        /* exp_relacional : exp_a1=exp_aritimetica (op_rs+=op_relacional outrosExp_a+=exp_aritimetica)?  */
        if(ctx.exp_a1 != null){
            visitExp_aritimetica(ctx.exp_a1);
        }
        //Caso haja um operador relacional, considera a atribuição sendo lógica
        if(ctx.op_relacional != null){
            auxTipoExp = "logico";
        }
        return null;
    }
    //As seguintes funções visit atribuem o valor a variavel bufferTipoExp, para que a função visitCmdAtribuicao cheque se a atribuição está correta
    @Override
    public Void visitParcela_unario_id(LAParser.Parcela_unario_idContext ctx) {
        /*('^')? identificador # parcela_unario_id */
        visitIdentificador(ctx.identificador());
        auxTipoExp = pilhaDeTabelas.getSimboloTipo(ctx.identificador().getText());
        return null;
    }

    @Override
    public Void visitParcela_unario_inteiro(LAParser.Parcela_unario_inteiroContext ctx) {
        /* NUM_INT # parcela_unario_inteiro */
        auxTipoExp = "inteiro";
        return null;
    }

    @Override
    public Void visitParcela_unario_real(LAParser.Parcela_unario_realContext ctx) {
        /*NUM_REAL # parcela_unario_real */
        auxTipoExp = "real";
        return null;
    }

    @Override
    public Void visitParcela_nao_unario_cadeia(LAParser.Parcela_nao_unario_cadeiaContext ctx) {
        /* CADEIA # parcela_nao_unario_cadeia */
        auxTipoExp = "literal";
        return null;
    }

    @Override
    public Void visitParcela_nao_unario_id(LAParser.Parcela_nao_unario_idContext ctx) {
        /* '&' identificador # parcela_nao_unario_id */
        auxTipoExp = "endereco";
        return null;
    }


}
