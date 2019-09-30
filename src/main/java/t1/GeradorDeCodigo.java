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

    private String parseLogicalFactor(LAParser.Fator_logicoContext factor) {
        if(factor.nao == null){
            return factor.parcela_logica().getText();
        }
        else{
            return "!" + factor.parcela_logica().getText();
        }
    }

    private String parseLogicalTerm(LAParser.Termo_logicoContext term) {
        StringBuilder ret = new StringBuilder(parseLogicalFactor(term.f1));
        for(LAParser.Fator_logicoContext factor : term.f2){
            ret.append("&&");
            ret.append(parseLogicalFactor(factor));
        }
        return ret.toString();
    }

    private String parseExpression(LAParser.ExpressaoContext expr) {
        StringBuilder ret = new StringBuilder(parseLogicalTerm(expr.t1));
        for(LAParser.Termo_logicoContext term : expr.t2){
            ret.append("||");
            ret.append(parseLogicalTerm(term));
        }
        return ret.toString().replaceAll("=", "==").replaceAll("<>", "!=").replaceAll(">==", ">=").replaceAll("<==", "<=");
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
                "#include <stdlib.h>\n" +
                // Inclusão do string.h pois alguns casos necessitam do strcpy
                "#include <string.h>\n");

        sp.printCode(visitDeclaracoes(ctx.declaracoes()));
        sp.printCode(visitCorpo(ctx.corpo()));
        return "";
    }

    @Override
    public String visitCorpo(LAParser.CorpoContext ctx){
        StringBuilder codigo = new StringBuilder("int main() {\n");
        ctx.decl_local().stream().map(this::visitDecl_local).forEach(codigo::append);
        ctx.cmd().stream().map(this::visitCmd).forEach(codigo::append);
        codigo.append("return 0;\n}\n");
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

        // Variavel
        if(ctx.variavel() != null){
            codigo.append(visitVariavel(ctx.variavel()));
        }

        // Constante
        else if(ctx.id1 != null){
            String id_txt = ctx.id1.getText();
            String tipo_txt = ctx.tipo_basico().getText();
            String value = ctx.valor_constante().getText();

            codigo.append("const ");

            switch(tipo_txt) {
                case "inteiro": codigo.append("int "); break;
                case "real": codigo.append("float "); break;
                case "literal": codigo.append("char "); break;
                case "logico": codigo.append("bool "); break;
                default: break;
            }

            if (value.equals("verdadeiro")) value = "true";
            if (value.equals("falso")) value = "false";

            codigo.append(id_txt + "= " + value + ";\n");

            pilhaDeTabelas.topo().adicionarSimbolo(id_txt, "constante", ctx.tipo_basico().getText());
        }
        // Tipo extendido
        else{
            String id_txt = ctx.id2.getText();
            String tipo = ctx.tipo().getText();

            if(ctx.tipo().registro() != null){
                pilhaDeTabelas.structs.put(id_txt, new LinkedList<EntradaTabelaDeSimbolos>());
                pilhaDeTabelas.topo().adicionarSimbolo(id_txt, "registro", "");
            }
            else{
                pilhaDeTabelas.topo().adicionarSimbolo(id_txt, "tipo", ctx.tipo().getText());
            }

            codigo.append("typedef struct {\n");
            codigo.append(visitTipo(ctx.tipo()));
            codigo.append("}").append(id_txt).append(";\n");

        }
        return codigo.toString();
    }

    @Override
    public String visitVariavel(LAParser.VariavelContext ctx){
        StringBuilder codigo = new StringBuilder();

        // É um registro
        if(ctx.tipo().registro() != null){
            LinkedList<String> listaIds = new LinkedList<>();
            visitIdentificador(ctx.id);
            listaIds.add(ctx.id.getText());
            codigo.append("struct {\n");

            String struct_name = ctx.id.getText();

            for(LAParser.IdentificadorContext id : ctx.outrosIds){
                visitIdentificador(id);
                listaIds.add(id.getText());
            }
            visitTipo(ctx.tipo());
            LinkedList<EntradaTabelaDeSimbolos> novosSimbolos = new LinkedList<>();
            ArrayList<EntradaTabelaDeSimbolos> topo = (ArrayList<EntradaTabelaDeSimbolos>) pilhaDeTabelas.topo().getListaSimbolos();
            for(String id : listaIds) {
                for (EntradaTabelaDeSimbolos simbolo : topo) {
                    novosSimbolos.add(new EntradaTabelaDeSimbolos(id + "." + simbolo.getNome(), simbolo.getTipo(), simbolo.getTipoDeDado()));
                }
            }
            pilhaDeTabelas.desempilhar();
            for(EntradaTabelaDeSimbolos simbolo : novosSimbolos){
                pilhaDeTabelas.topo().adicionarSimbolo(simbolo);
            }

            codigo.append(visitTipo(ctx.tipo()));
            codigo.append("} " + struct_name);
        }
        else if(ctx.parent.parent.parent instanceof LAParser.Decl_localContext){
            String idRegistro = ((LAParser.Decl_localContext)ctx.parent.parent.parent).id2.getText();
            String tipo_txt = ctx.tipo().getText();
            String idAtrib = ctx.id.getText();

            visitIdentificador(ctx.id);
            pilhaDeTabelas.structs.get(idRegistro).add(new EntradaTabelaDeSimbolos(ctx.id.getText(), "variavel", tipo_txt));
            for(LAParser.IdentificadorContext id : ctx.outrosIds){
                visitIdentificador(id);
                pilhaDeTabelas.structs.get(idRegistro).add(new EntradaTabelaDeSimbolos(id.getText(), "variavel", tipo_txt));
            }

            boolean isArray = false;
            // Declarado um vetor. Pegamos o nome dele
            if(idAtrib.indexOf('[') != -1) {
                idAtrib = idAtrib.substring(0, idAtrib.indexOf('['));
                isArray = true;
            };

            boolean ehPonteiro = ctx.tipo().getText().charAt(0) == '^';

            if (ehPonteiro) tipo_txt = tipo_txt.substring(1);

            switch(tipo_txt) {
                case "inteiro": codigo.append("int "); break;
                case "real": codigo.append("float "); break;
                case "literal": codigo.append("char "); break;
                default: break;
            }

            if(ehPonteiro) codigo.append("*");
            codigo.append(idAtrib);
            if (tipo_txt.equals("literal")) codigo.append("[50]");
        }
        // variaveis básicas
        else{
            String id_txt = ctx.id.getText();
            boolean isArray = false;
            // Declarado um vetor. Pegamos o nome dele
            if(id_txt.indexOf('[') != -1) {
                id_txt = id_txt.substring(0, id_txt.indexOf('['));
                isArray = true;
            };

            String tipo_txt = ctx.tipo().getText();

            boolean ehPonteiro = ctx.tipo().getText().charAt(0) == '^';

            if (ehPonteiro) tipo_txt = tipo_txt.substring(1);

            switch(tipo_txt) {
                case "inteiro": codigo.append("int "); break;
                case "real": codigo.append("float "); break;
                case "literal": codigo.append("char "); break;
                default: codigo.append(tipo_txt).append(" ");
            }

            if(ehPonteiro){
                pilhaDeTabelas.topo().adicionarSimbolo("^" + id_txt, "variavel", tipo_txt);
                pilhaDeTabelas.topo().adicionarSimbolo(id_txt, "variavel", "^" + tipo_txt);

                codigo.append("*");

            }
            else{
                if(pilhaDeTabelas.structs.get(tipo_txt) != null){
                    LinkedList <EntradaTabelaDeSimbolos> listaSimbolos = pilhaDeTabelas.structs.get(tipo_txt);
                    if(!pilhaDeTabelas.existeSimbolo(id_txt)){
                        for(EntradaTabelaDeSimbolos simbolo : listaSimbolos){
                            pilhaDeTabelas.topo().adicionarSimbolo(id_txt + "." + simbolo.getNome(), simbolo.getTipo(), simbolo.getTipoDeDado());
                        }
                        pilhaDeTabelas.topo().adicionarSimbolo(id_txt, "variavel", tipo_txt);
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
                    }
                } else {
                    pilhaDeTabelas.topo().adicionarSimbolo(id_txt, "variavel", tipo_txt);
                }
            }

            codigo.append(id_txt);
            if (isArray) codigo.append(visitDimensao(ctx.id.dimensao()));

            if (tipo_txt.equals("literal")) codigo.append("[50]");

            for(LAParser.IdentificadorContext id : ctx.outrosIds){
                codigo.append(", ");
                visitIdentificador(id);
                id_txt = id.getText();
                if(id_txt.indexOf('[') != -1){
                    id_txt = id_txt.substring(0, id_txt.indexOf('['));
                    isArray = true;
                } else {
                    isArray = false;
                }
                if(!pilhaDeTabelas.existeSimbolo(id_txt)){
                    if(ehPonteiro){
                        pilhaDeTabelas.topo().adicionarSimbolo("^" + id_txt, "variavel", tipo_txt);
                        pilhaDeTabelas.topo().adicionarSimbolo(id_txt, "variavel", "^" + tipo_txt);
                        codigo.append("*");
                    }
                    else{
                        pilhaDeTabelas.topo().adicionarSimbolo(id_txt, "variavel", tipo_txt);
                    }

                    codigo.append(id_txt);
                    if (isArray) codigo.append(visitDimensao(id.dimensao()));

                    if (tipo_txt.equals("literal")) codigo.append("[50]");
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
        StringBuilder codigo = new StringBuilder("[");
        for(LAParser.Exp_aritmeticaContext exp : ctx.exp_aritmetica()){
            codigo.append(exp.getText());
            visitExp_aritmetica(exp);
        }
        codigo.append("]");
        return codigo.toString();
    }

    @Override
    public String visitTipo(LAParser.TipoContext ctx){
        StringBuilder codigo = new StringBuilder();
        if(ctx.registro() != null){
            codigo.append(visitRegistro(ctx.registro()));
        }
        else{
            codigo.append(visitTipo_estendido(ctx.tipo_estendido()));
        }
        return codigo.toString();
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
        StringBuilder codigo = new StringBuilder();
        if(ctx.parent.parent instanceof LAParser.VariavelContext) pilhaDeTabelas.empilhar(new TabelaDeSimbolos("registro"));
        for(LAParser.VariavelContext var : ctx.variavel()){
            codigo.append(visitVariavel(var));
        }
        return codigo.toString();
    }
    public String tipoLAparaTipoC(String tipo) {
        if(tipo.charAt(0) == '^') tipo = tipo.substring(1);
        if(tipo.equals("inteiro") || tipo.equals("logico")) return "int";
        if(tipo.equals("real")) return "float";
        if(tipo.equals("literal")) return "char*";
        return "";
    }
    @Override
    public String visitDecl_global(LAParser.Decl_globalContext ctx){
        StringBuilder codigo = new StringBuilder() ;
        if(ctx.ident1 != null){ // caso seja identificador

            pilhaDeTabelas.functions.put(ctx.ident1.getText(), new LinkedList<>());
            pilhaDeTabelas.empilhar(new TabelaDeSimbolos("procedimento"));
            pilhaDeTabelas.topo().adicionarSimbolo(ctx.ident1.getText(), "procedimento", "");
            codigo.append("void ").append(ctx.ident1.getText());
            if(ctx.params1 != null){
                codigo.append(visitParametros(ctx.params1)).append("{").append("\n");
            }
            for(LAParser.Decl_localContext decl : ctx.decl1){
                codigo.append(visitDecl_local(decl));
            }
            for(LAParser.CmdContext cmd : ctx.c1){
                codigo.append(visitCmd(cmd));
            }
            pilhaDeTabelas.desempilhar();
            codigo.append("\n}");
        } else if(ctx.ident2 != null) { // caso seja funcao
            pilhaDeTabelas.topo().adicionarSimbolo(ctx.ident2.getText(), "funcao", ctx.tipo_estendido().getText());
            pilhaDeTabelas.functions.put(ctx.ident2.getText(), new LinkedList<>());
            pilhaDeTabelas.empilhar(new TabelaDeSimbolos("funcao"));
            pilhaDeTabelas.topo().adicionarSimbolo(ctx.ident2.getText(), "funcao", ctx.tipo_estendido().getText());
            codigo.append(tipoLAparaTipoC(visitTipo_estendido(ctx.tipo_estendido()))).append(" ").append(ctx.ident2.getText());
            if(ctx.params2 != null){
                codigo.append(visitParametros(ctx.params2));
            }
            codigo.append("{\n");
            for(LAParser.Decl_localContext decl : ctx.decl2){
                codigo.append(visitDecl_local(decl));
            }
            for(LAParser.CmdContext cmd : ctx.c2){
                codigo.append(visitCmd(cmd));
            }
            pilhaDeTabelas.desempilhar();
            codigo.append("\n}");

        }

        return codigo.append("\n").toString();
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
        StringBuilder codigo =  new StringBuilder();
        codigo.append("(");
        LAParser.Decl_globalContext declGlobal = ((LAParser.Decl_globalContext)(ctx.parent));
        String nomeFuncao;
        if(declGlobal.ident1 != null) nomeFuncao = declGlobal.ident1.getText(); // procedimento
        else nomeFuncao = declGlobal.ident2.getText(); // função
        String tipoParam = visitParametro(ctx.param1);
        codigo.append(tipoLAparaTipoC(tipoParam)).append(" ").append(ctx.param1.id1.getText());
        pilhaDeTabelas.functions.get(nomeFuncao).add(tipoParam);
        for(LAParser.ParametroContext param : ctx.param2){
            tipoParam = visitParametro(param);
            codigo.append(", ").append(tipoLAparaTipoC(tipoParam)).append(" ").append(param.identificador.getText());
            pilhaDeTabelas.functions.get(nomeFuncao).add(tipoParam);
        }
        codigo.append(")");
        return codigo.toString();
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
        StringBuilder codigo = new StringBuilder("printf(\"");

        String tipo = visitExpressao(ctx.exp1);

        switch(tipo) {
            case "inteiro":
                codigo.append("%d");
                break;
            case "real":
                codigo.append("%f");
                break;
            case "literal":
                codigo.append("%s");
                break;
            // Tipo extendido
            default: ;
        }
        codigo.append("\", " + ctx.exp1.getText()).append(");\n");

        for (LAParser.ExpressaoContext exp : ctx.exp2) {
            codigo.append("printf(\"");
            String tip_exp = visitExpressao(exp);

            switch (tip_exp) {
                case "inteiro":
                    codigo.append("%d");
                    break;
                case "real":
                    codigo.append("%f");
                    break;
                case "literal":
                    codigo.append("%s");
                    break;
                default:
                    break;


            }
            codigo.append("\", " + exp.getText()).append(");\n");

        }


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
        StringBuilder codigo = new StringBuilder("if (");
        codigo.append(parseExpression(ctx.e1) + ") {\n");

        for (LAParser.CmdContext comm : ctx.c1) {
            codigo.append(visitCmd(comm));
        }
        if (ctx.c2 != null) {
            codigo.append("} else {\n");
            for (LAParser.CmdContext comm : ctx.c2) {
                codigo.append(visitCmd(comm));
            }
        }
        codigo.append("}\n");
        return codigo.toString();
    }

    @Override
    public String visitCmdCaso (LAParser.CmdCasoContext ctx){
        StringBuilder codigo = new StringBuilder("switch(");
        codigo.append(ctx.exp_aritmetica().getText() + "){\n");
        codigo.append(visitSelecao(ctx.selecao()));
        if(ctx.cmd() != null){
            for (LAParser.CmdContext comm : ctx.cmd()) {
                codigo.append("default:\n");
                codigo.append("\t"+visitCmd(comm));
            }
        }

        codigo.append("}\n");

        return codigo.toString();
    }

    @Override
    public String visitCmdPara (LAParser.CmdParaContext ctx){
        StringBuilder codigo = new StringBuilder("for (");

        String id = ctx.IDENT().getText();

        codigo.append(id + " = " + ctx.ea1.getText() + "; ");
        codigo.append(id + " <= " + ctx.ea2.getText() + "; ");
        codigo.append(id + "++) {\n");

        for(LAParser.CmdContext cmd : ctx.cmd()){
            codigo.append("\t" + visitCmd(cmd));
        }

        codigo.append("}\n");

        return codigo.toString();
    }

    @Override
    public String visitCmdEnquanto (LAParser.CmdEnquantoContext ctx){
        StringBuilder codigo = new StringBuilder("while( ");
        codigo.append(parseExpression(ctx.expressao()) + ") {\n");
        for (LAParser.CmdContext comm : ctx.cmd()) {
            codigo.append("\t"+visitCmd(comm));
        }

        codigo.append("}\n");
        return codigo.toString();
    }

    @Override
    public String visitCmdFaca (LAParser.CmdFacaContext ctx){
        StringBuilder codigo = new StringBuilder("do {\n");

        for (LAParser.CmdContext comm : ctx.cmd()) {
            codigo.append("\t"+visitCmd(comm));
        };

        codigo.append("} while (");
        codigo.append(parseExpression(ctx.expressao()) + ");");

        return codigo.toString();
    }

    @Override
    public String visitCmdAtribuicao(LAParser.CmdAtribuicaoContext ctx){
        StringBuilder codigo = new StringBuilder();

        // Estamos atribuindo algo à posição de um ponteiro
        if(ctx.ponteiro != null){
            codigo.append("*");
        }

        String tipoExp = visitExpressao(ctx.expressao());

        // Cópia de strings deve usar o strcpy, como no exemplo 16
        if(tipoExp.equals("literal")){
            codigo.append("strcpy(").append(ctx.identificador().getText())
                    .append(", ").append(ctx.expressao().getText()).append(");");
        } else {
            codigo.append(ctx.identificador().getText()).append(" = ")
                    .append(ctx.expressao().getText()).append(";");
        }

        return codigo.toString();
    }

    @Override
    public String visitCmdChamada(LAParser.CmdChamadaContext ctx){
        StringBuilder codigo = new StringBuilder();
        codigo.append(ctx.id1.getText()).append("(");
        codigo.append(ctx.exp.t1.getText());
        visitExpressao(ctx.exp);
        ctx.outrasExp.forEach(this::visitExpressao);
        codigo.append(");");
        return codigo.toString();
    }

    @Override
    public String visitCmdRetorne(LAParser.CmdRetorneContext ctx){
        StringBuilder codigo = new StringBuilder();
        if(!pilhaDeTabelas.topo().getEscopo().equals("funcao"))
            sp.println("Linha " + ctx.start.getLine() + ": comando retorne nao permitido nesse escopo");

        codigo.append("return ");
        visitExpressao(ctx.expressao());
        codigo.append(parseExpression(ctx.expressao()));
        return codigo.append(";").toString();
    }

    @Override
    public String visitSelecao(LAParser.SelecaoContext ctx){
        StringBuilder codigo = new StringBuilder();

        for (LAParser.Item_selecaoContext item : ctx.item_selecao()) {
            codigo.append(visitItem_selecao(item));
            codigo.append("break;\n");
        }


        return codigo.toString();
    }

    @Override
    public String visitItem_selecao(LAParser.Item_selecaoContext ctx){
        StringBuilder codigo = new StringBuilder();
        codigo.append(visitConstantes(ctx.constantes()));
        for (LAParser.CmdContext comm : ctx.cmd()) {
            codigo.append("\t" + visitCmd(comm));
        };
        return codigo.toString();
    }

    @Override
    public String visitConstantes(LAParser.ConstantesContext ctx){
        StringBuilder codigo = new StringBuilder();

        int inicio = Integer.parseInt(ctx.ni1.ni1.getText());

        if (ctx.ni1.ni2 != null) {
            int fim = Integer.parseInt(ctx.ni1.ni2.getText());
            while(inicio <= fim) {
                codigo.append("case "+ inicio++ +":\n");
            }
        } else {
            codigo.append("case "+ inicio +":\n");
        }
        return codigo.toString();
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
            return tipo;
        }
        else if(ctx.IDENT() != null){
            String id_txt = ctx.IDENT().getText();
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
