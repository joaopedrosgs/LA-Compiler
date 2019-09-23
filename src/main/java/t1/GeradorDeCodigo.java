package t1;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.LinkedList;

public class GeradorDeCodigo extends LABaseVisitor<String> {
    private PilhaDeTabelas pilhaDeTabelas;

    private SaidaParser sp;

    public GeradorDeCodigo(SaidaParser sp) {
        this.sp = sp;
        pilhaDeTabelas = new PilhaDeTabelas();
    }

    // Função auxiliar para verificar se um tipo é numérico
    private boolean IsNumber(String tipo){
        return tipo.equals("inteiro") || tipo.equals("real");
    }

    // Função auxiliar para verificar o tipo de retorno de uma operação binária
    private String tipoRetorno(String operacao, String op1, String op2){
        switch (operacao) {
            case "aritmetica":
                if (IsNumber(op1) && IsNumber(op2)) {
                    if (op1.equals("real") || op2.equals("real")) return "real";
                    else return "inteiro";
                } else if (op1.equals("literal") && op2.equals("literal")) {
                    return "literal";
                } else {
                    return "tipo_invalido";
                }
            case "relacional":
                if (IsNumber(op1) && IsNumber(op2)) {
                    return "logico";
                } else {
                    return "tipo_invalido";
                }
            case "logica":
                if (op1.equals("logico") && op2.equals("logico")) {
                    return "logico";
                } else {
                    return "tipo_invalido";
                }
        }
        return "";
    }

    // Função inicial, recebe a raiz da árvore sintática, inicializa as estruturas de dados e começa a percorrer a árvore
    @Override
    public String visitPrograma(LAParser.ProgramaContext ctx){

        // Começando código com as importações básicas do C
        sp.printCode("#include <stdio.h>\n" +
                "#include <stdlib.h>\n");

        sp.printCode(visitDeclaracoes(ctx.declaracoes()));
        sp.printCode(visitCorpo(ctx.corpo()));
        return "";
    }

    @Override
    public String visitCorpo(LAParser.CorpoContext ctx){
        StringBuilder codigo = new StringBuilder("int main() {\n");
        ctx.decl_local().stream().map(this::visitDecl_local).forEach(codigo::append);
        ctx.cmd().stream().map(this::visitCmd).forEach(codigo::append);
        codigo = codigo.append("return 0;\n}\n");
        return codigo.toString();
    }

    @Override
    public String visitDeclaracoes(LAParser.DeclaracoesContext ctx){
        StringBuilder codigo = new StringBuilder();
        ctx.decl_local_global().stream().map(this::visitDecl_local_global).forEach(codigo::append);
        return codigo.toString();
    }

    @Override
    public String visitDecl_local_global(LAParser.Decl_local_globalContext ctx){
        StringBuilder codigo = new StringBuilder();
        if(ctx.decl_local() != null){
            codigo.append(visitDecl_local(ctx.decl_local()));
        }
        else{
            codigo.append(visitDecl_global(ctx.decl_global()));
        }
        return codigo.toString();
    }

    @Override
    public String visitDecl_local(LAParser.Decl_localContext ctx){
        StringBuilder codigo = new StringBuilder();
        if(ctx.variavel() != null){
            codigo.append(visitVariavel(ctx.variavel()));
        }
        else if(ctx.id1 != null){
            String id_txt = ctx.id1.getText();
            pilhaDeTabelas.topo().adicionarSimbolo(id_txt, "constante", ctx.tipo_basico().getText());
        }
        else{
            String id_txt = ctx.id2.getText();
            if(pilhaDeTabelas.existeSimbolo(id_txt)){
                sp.println("Linha " + ctx.id2.getTokenSource().getLine() + ": identificador " + id_txt + " ja declarado anteriormente");
            }
            if(ctx.tipo().registro() != null){
                pilhaDeTabelas.structs.put(id_txt, new LinkedList<EntradaTabelaDeSimbolos>());
                pilhaDeTabelas.topo().adicionarSimbolo(id_txt, "registro", "");
            }
            else{
                pilhaDeTabelas.topo().adicionarSimbolo(id_txt, "tipo", ctx.tipo().getText());
            }
            visitTipo(ctx.tipo());
        }
        return codigo.toString();
    }

    @Override
    public String visitVariavel(LAParser.VariavelContext ctx){
        StringBuilder codigo = new StringBuilder();
        if(ctx.tipo().registro() != null){
            LinkedList<String> listaIds = new LinkedList<>();
            visitIdentificador(ctx.id);
            listaIds.add(ctx.id.getText());
            for(LAParser.IdentificadorContext id : ctx.outrosIds){
                visitIdentificador(id);
                listaIds.add(id.getText());
            }
            visitTipo(ctx.tipo());
            LinkedList<EntradaTabelaDeSimbolos> novosSimbolos = new LinkedList<>();
            ArrayList<EntradaTabelaDeSimbolos> topo = (ArrayList<EntradaTabelaDeSimbolos>) pilhaDeTabelas.topo().getListaSimbolos();
            for(String id : listaIds){
                for (EntradaTabelaDeSimbolos simbolo : topo) {
                    novosSimbolos.add(new EntradaTabelaDeSimbolos(id + "." + simbolo.getNome(), simbolo.getTipo(), simbolo.getTipoDeDado()));
                }
            }
            pilhaDeTabelas.desempilhar();
            for(EntradaTabelaDeSimbolos simbolo : novosSimbolos){
                pilhaDeTabelas.topo().adicionarSimbolo(simbolo);
            }
        }
        else if(ctx.parent.parent.parent instanceof LAParser.Decl_localContext){
            String idRegistro = ((LAParser.Decl_localContext)ctx.parent.parent.parent).id2.getText();
            String tipo_txt = ctx.tipo().getText();
            visitIdentificador(ctx.id);
            pilhaDeTabelas.structs.get(idRegistro).add(new EntradaTabelaDeSimbolos(ctx.id.getText(), "variavel", tipo_txt));
            for(LAParser.IdentificadorContext id : ctx.outrosIds){
                visitIdentificador(id);
                pilhaDeTabelas.structs.get(idRegistro).add(new EntradaTabelaDeSimbolos(id.getText(), "variavel", tipo_txt));
            }
        }
        // variaveis simples
        else{
            visitIdentificador(ctx.id);
            String id_txt = ctx.id.getText();
            if(id_txt.indexOf('[') != -1) id_txt = id_txt.substring(0, id_txt.indexOf('['));
            String tipo_txt = ctx.tipo().getText();

            if(pilhaDeTabelas.structs.get(tipo_txt) != null){
                LinkedList <EntradaTabelaDeSimbolos> listaSimbolos = pilhaDeTabelas.structs.get(tipo_txt);
                if(!pilhaDeTabelas.existeSimbolo(id_txt)){
                    for(EntradaTabelaDeSimbolos simbolo : listaSimbolos){
                        pilhaDeTabelas.topo().adicionarSimbolo(id_txt + "." + simbolo.getNome(), simbolo.getTipo(), simbolo.getTipoDeDado());
                    }
                    pilhaDeTabelas.topo().adicionarSimbolo(id_txt, "variavel", tipo_txt);
                }
                else{
                    sp.println("Linha " + ctx.id.start.getLine() + ": identificador " + id_txt + " ja declarado anteriormente");
                }

                for(LAParser.IdentificadorContext id : ctx.outrosIds){
                    visitIdentificador(id);
                    id_txt = id.getText();
                    if(!pilhaDeTabelas.existeSimbolo(id_txt)){
                        for(EntradaTabelaDeSimbolos simbolo : listaSimbolos){
                            pilhaDeTabelas.topo().adicionarSimbolo(id_txt + "." + simbolo.getNome(), simbolo.getTipo(), simbolo.getTipoDeDado());
                        }
                        pilhaDeTabelas.topo().adicionarSimbolo(id_txt, "variavel", tipo_txt);
                    }
                    else{
                        sp.println("Linha " + id.start.getLine() + ": identificador " + id_txt + " ja declarado anteriormente");
                    }
                }
            }
            else{
                if(!pilhaDeTabelas.existeSimbolo(id_txt)){

                    switch(tipo_txt) {
                        case "inteiro": codigo.append("int "); break;
                        case "real": codigo.append("float "); break;
                        case "literal": codigo.append("char "); break;
                        default: break;
                    }

                    if(tipo_txt.charAt(0) == '^'){
                        pilhaDeTabelas.topo().adicionarSimbolo("^" + id_txt, "variavel", tipo_txt.substring(1));
                        pilhaDeTabelas.topo().adicionarSimbolo(id_txt, "variavel", tipo_txt);

                        codigo.append("*");

                    }
                    else{
                        pilhaDeTabelas.topo().adicionarSimbolo(id_txt, "variavel", tipo_txt);
                    }

                    codigo.append(id_txt);

                    if (tipo_txt.equals("literal")) codigo.append("[50]");
                }
                else if(!(ctx.parent instanceof LAParser.RegistroContext)){
                    sp.println("Linha " + ctx.id.start.getLine() + ": identificador " + id_txt + " ja declarado anteriormente");
                }

                for(LAParser.IdentificadorContext id : ctx.outrosIds){
                    codigo.append(", ");
                    visitIdentificador(id);
                    id_txt = id.getText();
                    if(id_txt.indexOf('[') != -1) id_txt = id_txt.substring(0, id_txt.indexOf('['));
                    if(!pilhaDeTabelas.existeSimbolo(id_txt)){
                        if(tipo_txt.charAt(0) == '^'){
                            pilhaDeTabelas.topo().adicionarSimbolo("^" + id_txt, "variavel", tipo_txt.substring(1));
                            pilhaDeTabelas.topo().adicionarSimbolo(id_txt, "variavel", tipo_txt);
                            codigo.append("*");
                        }
                        else{
                            pilhaDeTabelas.topo().adicionarSimbolo(id_txt, "variavel", tipo_txt);
                        }

                        codigo.append(id_txt);

                        if (tipo_txt.equals("literal")) codigo.append("[50]");
                    }
                    else if(!(ctx.parent instanceof LAParser.RegistroContext)){
                        sp.println("Linha " + id.start.getLine() + ": identificador " + id_txt + " ja declarado anteriormente");
                    }
                }

            }
            visitTipo(ctx.tipo());
        }

        codigo.append(";\n");
        return codigo.toString();
    }

    @Override
    public String visitIdentificador(LAParser.IdentificadorContext ctx) {
        StringBuilder nome = new StringBuilder(ctx.id.getText());
        for(Token id : ctx.outrosIds){
            nome.append(".").append(id.getText());
        }
        visitDimensao(ctx.dimensao());
        // retorna o tipo desse identificador
        return pilhaDeTabelas.tipoDeDadoDoSimbolo(nome.toString());
    }

    @Override
    public String visitDimensao(LAParser.DimensaoContext ctx){
        for(LAParser.Exp_aritmeticaContext exp : ctx.exp_aritmetica()){
            visitExp_aritmetica(exp);
        }
        return "";
    }

    @Override
    public String visitTipo(LAParser.TipoContext ctx){
        if(ctx.registro() != null){
            visitRegistro(ctx.registro());
        }
        else{
            visitTipo_estendido(ctx.tipo_estendido());
        }
        return "";
    }

    @Override
    public String visitTipo_basico(LAParser.Tipo_basicoContext ctx) {
        return super.visitTipo_basico(ctx);
    }

    @Override
    public String visitTipo_basico_ident(LAParser.Tipo_basico_identContext ctx){
        if(ctx.tipo_basico() != null){
            return ctx.tipo_basico().getText();
        }
        else{
            return ctx.IDENT().getText();
        }
    }

    @Override
    public String visitTipo_estendido(LAParser.Tipo_estendidoContext ctx){
        return visitTipo_basico_ident(ctx.tipo_basico_ident());
    }

    @Override
    public String visitValor_constante(LAParser.Valor_constanteContext ctx) {
        return super.visitValor_constante(ctx);
    }

    @Override
    public String visitRegistro(LAParser.RegistroContext ctx){
        if(ctx.parent.parent instanceof LAParser.VariavelContext) pilhaDeTabelas.empilhar(new TabelaDeSimbolos("registro"));
        for(LAParser.VariavelContext var : ctx.variavel()){
            visitVariavel(var);
        }
        return "";
    }

    @Override
    public String visitDecl_global(LAParser.Decl_globalContext ctx){
        String codigo = "";
        if(ctx.ident1 != null){ // caso seja identificador
            pilhaDeTabelas.topo().adicionarSimbolo(ctx.ident1.getText(), "procedimento", "");
            pilhaDeTabelas.functions.put(ctx.ident1.getText(), new LinkedList<>());
            pilhaDeTabelas.empilhar(new TabelaDeSimbolos("procedimento"));
            pilhaDeTabelas.topo().adicionarSimbolo(ctx.ident1.getText(), "procedimento", "");
            if(ctx.params1 != null){
                visitParametros(ctx.params1);
            }
            for(LAParser.Decl_localContext decl : ctx.decl1){
                visitDecl_local(decl);
            }
            for(LAParser.CmdContext cmd : ctx.c1){
                visitCmd(cmd);
            }
            pilhaDeTabelas.desempilhar();
        }else { // caso seja funcao
            pilhaDeTabelas.topo().adicionarSimbolo(ctx.ident2.getText(), "funcao", ctx.tipo_estendido().getText());
            pilhaDeTabelas.functions.put(ctx.ident2.getText(), new LinkedList<>());
            pilhaDeTabelas.empilhar(new TabelaDeSimbolos("funcao"));
            pilhaDeTabelas.topo().adicionarSimbolo(ctx.ident2.getText(), "funcao", ctx.tipo_estendido().getText());
            if(ctx.params2 != null){
                visitParametros(ctx.params2);
            }
            visitTipo_estendido(ctx.tipo_estendido());
            for(LAParser.Decl_localContext decl : ctx.decl2){
                visitDecl_local(decl);
            }
            for(LAParser.CmdContext cmd : ctx.c2){
                visitCmd(cmd);
            }
            pilhaDeTabelas.desempilhar();
        }
        return codigo;
    }



    @Override
    public String visitParametro(LAParser.ParametroContext ctx){
        String tipo_txt = ctx.tipo_estendido().getText();
        LinkedList<EntradaTabelaDeSimbolos> listaAtributos = pilhaDeTabelas.structs.get(tipo_txt);
        VisitFuncAndAddList(ctx.id1, tipo_txt,listaAtributos);
        for(LAParser.IdentificadorContext id : ctx.id2){
            VisitFuncAndAddList(id, tipo_txt,listaAtributos);
        }
        return visitTipo_estendido(ctx.tipo_estendido());
    }

    @Override
    public String visitParametros(LAParser.ParametrosContext ctx){
        LAParser.Decl_globalContext declGlobal = ((LAParser.Decl_globalContext)(ctx.parent));
        String nomeFuncao;
        if(declGlobal.ident1 != null) nomeFuncao = declGlobal.ident1.getText(); // procedimento
        else nomeFuncao = declGlobal.ident2.getText(); // função
        String tipoParam = visitParametro(ctx.param1);
        pilhaDeTabelas.functions.get(nomeFuncao).add(tipoParam);
        for(LAParser.ParametroContext param : ctx.param2){
            tipoParam = visitParametro(param);
            pilhaDeTabelas.functions.get(nomeFuncao).add(tipoParam);
        }
        return "";
    }

    @Override
    public String visitCmd(LAParser.CmdContext ctx){
        StringBuilder codigo = new StringBuilder();
        if (ctx.cmdLeia() != null){
            codigo.append(visitCmdLeia(ctx.cmdLeia()));
        }else if (ctx.cmdEscreva() != null){
            codigo.append(visitCmdEscreva(ctx.cmdEscreva()));
        }else if (ctx.cmdSe() != null){
            codigo.append(visitCmdSe(ctx.cmdSe()));
        }else if (ctx.cmdCaso() != null){
            codigo.append(visitCmdCaso(ctx.cmdCaso()));
        }else if (ctx.cmdPara() != null){
            codigo.append(visitCmdPara(ctx.cmdPara()));
        }else if (ctx.cmdEnquanto() != null){
            codigo.append(visitCmdEnquanto(ctx.cmdEnquanto()));
        }else if (ctx.cmdFaca() != null){
            codigo.append(visitCmdFaca(ctx.cmdFaca()));
        }else if (ctx.cmdAtribuicao() != null){
            codigo.append(visitCmdAtribuicao(ctx.cmdAtribuicao()));
        }else if (ctx.cmdChamada() != null){
            codigo.append(visitCmdChamada(ctx.cmdChamada()));
        }else if (ctx.cmdRetorne() != null){
            codigo.append(visitCmdRetorne(ctx.cmdRetorne()));
        }

        codigo.append("\n");

        return codigo.toString();
    }
    @Override
    public String visitCmdEscreva(LAParser.CmdEscrevaContext ctx){
        StringBuilder codigo = new StringBuilder("printf(");
        String tipo = visitExpressao(ctx.exp1);

        switch(tipo) {
            case "inteiro": codigo.append("\"%d\""); break;
            case "real": codigo.append("\"%f\""); break;
            case "literal": codigo.append("\"%s\""); break;
            default: break;
        }

        codigo.append("," + ctx.exp1.getText() + ");");
        ctx.exp2.forEach(this::visitExpressao);
        return codigo.toString();
    }
    @Override
    public String visitCmdLeia(LAParser.CmdLeiaContext ctx){
        StringBuilder codigo = new StringBuilder("scanf(");
        String id_txt = ctx.id1.getText();

        if(id_txt.indexOf('[') != -1) id_txt = id_txt.substring(0, id_txt.indexOf('['));

        String tipo = visitIdentificador(ctx.id1);

        switch(tipo) {
            case "inteiro": codigo.append("\"%d\""); break;
            case "real": codigo.append("\"%f\""); break;
            case "literal": codigo.append("\"%s\""); break;
            default: break;
        }

        codigo.append(",&" + id_txt + ");");

        for(LAParser.IdentificadorContext id : ctx.id2){
            id_txt = id.getText();
            if(id_txt.indexOf('[') != -1) id_txt = id_txt.substring(0, id_txt.indexOf('['));

            tipo = visitIdentificador(id);

            switch(tipo) {
                case "inteiro": codigo.append("\"%d\""); break;
                case "real": codigo.append("\"%f\""); break;
                case "literal": codigo.append("\"%s\""); break;
                default: break;
            }

            codigo.append(",&" + id_txt + ")");
        }
        return codigo.toString();
    }



    @Override
    public String visitCmdSe(LAParser.CmdSeContext ctx){
        visitExpressao(ctx.e1);
        ctx.c1.forEach(this::visitCmd);
        if (ctx.c2 != null) {
            ctx.c2.forEach(this::visitCmd);

        }
        return "";
    }

    @Override
    public String visitCmdCaso (LAParser.CmdCasoContext ctx){
        visitExp_aritmetica(ctx.exp_aritmetica());
        visitSelecao(ctx.selecao());
        if(ctx.cmd() != null){
            ctx.cmd().forEach(this::visitCmd);
        }
        return "";
    }

    @Override
    public String visitCmdPara (LAParser.CmdParaContext ctx){
        pilhaDeTabelas.empilhar(new TabelaDeSimbolos("para"));
        visitExp_aritmetica(ctx.ea1);
        visitExp_aritmetica(ctx.ea2);
        ctx.cmd().forEach(this::visitCmd);
        pilhaDeTabelas.desempilhar();
        return "";
    }

    @Override
    public String visitCmdEnquanto (LAParser.CmdEnquantoContext ctx){
        pilhaDeTabelas.empilhar(new TabelaDeSimbolos("enquanto"));
        visitExpressao(ctx.expressao());
        ctx.cmd().forEach(this::visitCmd);
        pilhaDeTabelas.desempilhar();
        return "";
    }

    @Override
    public String visitCmdFaca (LAParser.CmdFacaContext ctx){
        pilhaDeTabelas.empilhar(new TabelaDeSimbolos("faca"));
        ctx.cmd().forEach(this::visitCmd);
        visitExpressao(ctx.expressao());
        pilhaDeTabelas.desempilhar();
        return "";
    }

    @Override
    public String visitCmdAtribuicao(LAParser.CmdAtribuicaoContext ctx){
        String tipoId = visitIdentificador(ctx.identificador());
        if(tipoId.equals("")) tipoId = "tipo_invalido";
        String id_txt = ctx.identificador().getText();
        String id_txt_certo = id_txt + "";
        if(id_txt.indexOf('[') != -1) id_txt = id_txt.substring(0, id_txt.indexOf('['));
        // variavel ponteiro atribuindo no conteudo apontado
        if(ctx.ponteiro != null){
            id_txt = "^" + id_txt;
            id_txt_certo = "^" + id_txt_certo;
            tipoId = tipoId.substring(1);
        }
        // variavel ponteiro atribuindo um endereço
        else if(tipoId.charAt(0) == '^'){
            id_txt = "^" + id_txt;
        }

        String tipoExp = visitExpressao(ctx.expressao());
        if(!pilhaDeTabelas.existeSimbolo(id_txt)){
            sp.println("Linha " + ctx.identificador().start.getLine() + ": identificador " + id_txt_certo + " nao declarado");
        }
        else{
            if(tipoId.charAt(0) == '^'){
                if(tipoExp.charAt(0) != '&' || !tipoId.substring(1).equals(tipoExp.substring(1))){
                    sp.println("Linha " + ctx.identificador().start.getLine() + ": atribuicao nao compativel para " + id_txt);
                }
            }
            else if(!tipoId.equals(tipoExp) && !(IsNumber(tipoId) && IsNumber(tipoExp))){
                sp.println("Linha " + ctx.identificador().start.getLine() + ": atribuicao nao compativel para " + id_txt_certo);
            }
        }
        return "";
    }

    @Override
    public String visitCmdChamada(LAParser.CmdChamadaContext ctx){
        visitExpressao(ctx.exp);
        ctx.outrasExp.forEach(this::visitExpressao);
        return "";
    }

    @Override
    public String visitCmdRetorne(LAParser.CmdRetorneContext ctx){
        if(!pilhaDeTabelas.topo().getEscopo().equals("funcao"))
            sp.println("Linha " + ctx.start.getLine() + ": comando retorne nao permitido nesse escopo");
        visitExpressao(ctx.expressao());
        return "";
    }

    @Override
    public String visitSelecao(LAParser.SelecaoContext ctx){
        ctx.item_selecao().forEach(this::visitItem_selecao);
        return "";
    }

    @Override
    public String visitItem_selecao(LAParser.Item_selecaoContext ctx){
        visitConstantes(ctx.constantes());
        ctx.cmd().forEach(this::visitCmd);
        return "";
    }

    @Override
    public String visitConstantes(LAParser.ConstantesContext ctx){
        visitNumero_intervalo(ctx.ni1);
        ctx.ni2.forEach(this::visitNumero_intervalo);
        return "";
    }

    @Override
    public String visitNumero_intervalo(LAParser.Numero_intervaloContext ctx){
        if(ctx.opu1 != null){
            visitOp_unario(ctx.opu1);
        }
        else if(ctx.opu2 != null){
            visitOp_unario(ctx.opu2);
        }
        return "";
    }

    @Override
    public String visitOp_unario(LAParser.Op_unarioContext ctx) {
        return super.visitOp_unario(ctx);
    }

    @Override
    public String visitExp_aritmetica(LAParser.Exp_aritmeticaContext ctx){
        String tipo = visitTermo(ctx.t1);
        for(LAParser.TermoContext t : ctx.outrosTermos){
            String tipoTermo = visitTermo(t);
            tipo = tipoRetorno("aritmetica", tipo, tipoTermo);
        }
        return tipo;
    }

    @Override
    public String visitTermo(LAParser.TermoContext ctx){
        String tipo = visitFator(ctx.f1);
        for(LAParser.FatorContext f : ctx.outrosFatores){
            String tipoFator = visitFator(f);
            tipo = tipoRetorno("aritmetica", tipo, tipoFator);
        }
        return tipo;
    }

    @Override
    public String visitFator(LAParser.FatorContext ctx){
        String tipo = visitParcela(ctx.p1);
        for(LAParser.ParcelaContext p : ctx.outrasParcelas){
            String tipoParcela = visitParcela(p);
            tipo = tipoRetorno("aritmetica", tipo, tipoParcela);
        }
        return tipo;
    }

    @Override
    public String visitOp1(LAParser.Op1Context ctx) {
        return super.visitOp1(ctx);
    }

    @Override
    public String visitOp2(LAParser.Op2Context ctx) {
        return super.visitOp2(ctx);
    }

    @Override
    public String visitOp3(LAParser.Op3Context ctx) {
        return super.visitOp3(ctx);
    }

    @Override
    public String visitParcela (LAParser.ParcelaContext ctx){
        if(ctx.parcela_unario() != null){
            String tipo = visitParcela_unario(ctx.parcela_unario());
            if(ctx.op_unario() != null && !tipo.equals("inteiro") && !tipo.equals("real")){
                return "tipo_indefinido";
            }
            return tipo;
        }
        else{
            return visitParcela_nao_unario(ctx.parcela_nao_unario());
        }
    }

    @Override
    public String visitParcela_unario (LAParser.Parcela_unarioContext ctx){
        if(ctx.identificador() != null) {
            String tipo = visitIdentificador(ctx.identificador());
            String id_txt = ctx.identificador().getText();
            if(id_txt.indexOf('[') != -1) id_txt = id_txt.substring(0, id_txt.indexOf('['));
            if(!pilhaDeTabelas.existeSimbolo(id_txt)){
                sp.println("Linha " + ctx.identificador().start.getLine() + ": identificador " + id_txt + " nao declarado");
            }
            return tipo;
        }
        else if(ctx.IDENT() != null){
            String id_txt = ctx.IDENT().getText();
            if(!pilhaDeTabelas.existeSimbolo(id_txt)){
                sp.println("Linha " + ctx.IDENT().getSymbol().getLine() + ": identificador " + id_txt + " nao declarado");
            }
            String tipoExp = visitExpressao(ctx.exp1);
            LinkedList<String> listaParams = pilhaDeTabelas.functions.get(id_txt);
            boolean compativel = true;
            compativel = !listaParams.isEmpty() && listaParams.get(0).equals(tipoExp);
            int i = 0;
            for(LAParser.ExpressaoContext e : ctx.exp2){
                tipoExp = visitExpressao(e);
                i++;
                compativel = i <= listaParams.size() && compativel && listaParams.get(i).equals(tipoExp);
            }
            if(listaParams.size() != ctx.exp2.size()+1) compativel = false;
            if(!compativel) sp.println("Linha " + ctx.start.getLine() + ": incompatibilidade de parametros na chamada de " + id_txt);
            return pilhaDeTabelas.tipoDeDadoDoSimbolo(id_txt);
        }
        else if(ctx.NUM_INT() != null){
            return "inteiro";
        }
        else if(ctx.NUM_REAL() != null){
            return "real";
        }
        else{
            return visitExpressao(ctx.exp3);
        }
    }

    @Override
    public String visitParcela_nao_unario (LAParser.Parcela_nao_unarioContext ctx){
        if(ctx.identificador() != null){
            String tipo = visitIdentificador(ctx.identificador());
            String id_txt = ctx.identificador().getText();
            if(!pilhaDeTabelas.existeSimbolo(id_txt)){
                sp.println("Linha " + ctx.identificador().start.getLine() + ": identificador " + id_txt + " nao declarado");
            }
            return "&" + tipo;
        }
        else{
            return "literal";
        }
    }

    @Override
    public String visitExp_relacional (LAParser.Exp_relacionalContext ctx){
        String tipo = visitExp_aritmetica(ctx.e1);
        if(ctx.op_relacional() != null){
            String tipoExp = visitExp_aritmetica(ctx.e2);
            tipo = tipoRetorno("relacional", tipo, tipoExp);
        }
        return tipo;
    }

    @Override
    public String visitOp_relacional(LAParser.Op_relacionalContext ctx) {
        return super.visitOp_relacional(ctx);
    }

    // O visitor de uma expressão retorna uma string com o tipo de dado dessa expressão
    // Esse tipo é definido pelo retorno dos visitors de cada termo da expressão
    @Override
    public String visitExpressao(LAParser.ExpressaoContext ctx){
        String tipo = visitTermo_logico(ctx.t1);
        for(LAParser.Termo_logicoContext tl : ctx.t2){
            String tipoTermo = visitTermo_logico(tl);
            tipo = tipoRetorno("logica", tipo, tipoTermo);
        }
        return tipo;
    }

    @Override
    public String visitTermo_logico(LAParser.Termo_logicoContext ctx){
        String tipo = visitFator_logico(ctx.f1);
        for(LAParser.Fator_logicoContext fl : ctx.f2){
            String tipoFator = visitFator_logico(fl);
            tipo = tipoRetorno("logica", tipo, tipoFator);
        }
        return tipo;
    }

    @Override
    public String visitFator_logico(LAParser.Fator_logicoContext ctx){
        String tipo = visitParcela_logica(ctx.parcela_logica());
        if(ctx.nao != null && !tipo.equals("logico")){
            return "tipo_invalido";
        }
        else{
            return tipo;
        }
    }

    @Override
    public String visitParcela_logica(LAParser.Parcela_logicaContext ctx){
        if(ctx.exp_relacional() != null){
            return visitExp_relacional(ctx.exp_relacional());
        }
        else{
            return "logico";
        }
    }

    @Override
    public String visitOp_logico1(LAParser.Op_logico1Context ctx) {
        return super.visitOp_logico1(ctx);
    }

    @Override
    public String visitOp_logico2(LAParser.Op_logico2Context ctx) {
        return super.visitOp_logico2(ctx);
    }
    // utilidade
    private void VisitFuncAndAddList(LAParser.IdentificadorContext id, String tipo, LinkedList<EntradaTabelaDeSimbolos> listaAtributos) {
        visitIdentificador(id);
        pilhaDeTabelas.topo().adicionarSimbolo(id.getText(), "variavel", tipo);
        if(listaAtributos != null){
            for(EntradaTabelaDeSimbolos entrada : listaAtributos){
                pilhaDeTabelas.topo().adicionarSimbolo(id.getText() + "." + entrada.getNome(), entrada.getTipo(), entrada.getTipoDeDado());
            }
        }
    }


}
