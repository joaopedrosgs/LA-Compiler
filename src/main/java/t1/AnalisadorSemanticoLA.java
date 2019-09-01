package t1;

public class AnalisadorSemanticoLA extends LABaseVisitor<Void> {
    PilhaDeTabelas pilhaDeTabelas = new PilhaDeTabelas();

    @Override
    public Void visitPrograma(LAParser.ProgramaContext ctx) {
        pilhaDeTabelas.empilhar(new TabelaDeSimbolos("global"));

        super.visitPrograma(ctx);

        pilhaDeTabelas.desempilhar();

        return null;
    }

    @Override
    public Void visitIdentificador(LAParser.IdentificadorContext ctx) {
        pilhaDeTabelas.topo().adicionarSimbolo(ctx.ident1.getText(),"variavel");
        return visitChildren(ctx);
    }

}
