package LA;

/**
 * Created by lucas on 26/08/19.
 */
public class AnalisadorSemanticoLA extends laBaseVisitor<Void> {
    PilhaDeTabelas pilhaDeTabelas = new PilhaDeTabelas();

    @Override
    public Void visitPrograma(laParser.ProgramaContext ctx) {
        pilhaDeTabelas.empilhar(new TabelaDeSimbolos("global"));

        super.visitPrograma(ctx);

        pilhaDeTabelas.desempilhar();

        return null;
    }

    @Override public Void visitIdentificador(laParser.IdentificadorContext ctx) {
        pilhaDeTabelas.topo().adicionarSimbolo(ctx.ident1.getText(),"variavel");
        return visitChildren(ctx);
    }

}
