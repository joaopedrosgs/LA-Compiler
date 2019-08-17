package br.ufscar.la.compiler;


import LA.laLexer;
import LA.laParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Bad input");
            return;
        }
        SaidaParser saidaParser = new SaidaParser();
        CharStream input = CharStreams.fromFileName(args[0]);

        laLexer lexer = new laLexer(input);
        CommonTokenStream stream = new CommonTokenStream(lexer);
        laParser parser = new laParser(stream);
        parser.removeErrorListeners();
        ErrorListener errorListener = new ErrorListener(saidaParser);
        parser.addErrorListener(errorListener);

        laParser.ProgramaContext arvore = null;

        try {
            arvore = parser.programa();
            if (saidaParser.HasError()) {
                throw new ParseCancellationException("Exceção gerada no léxico/sintático.");
            }
        } catch (ParseCancellationException pce) {
            if (pce.getMessage() != null) {
                saidaParser.println("Fim da compilacao");
            }
        }

        if (saidaParser.HasError()) {
            System.out.println(saidaParser.toString());
        }


    }
}
