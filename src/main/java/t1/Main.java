package t1;


import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        if(args.length < 2) {
            System.out.println("Bad input");
        }
        SaidaParser sp = new SaidaParser();
        LAParser.ProgramaContext arvore = null;
        try {
            CharStream input = CharStreams.fromFileName(args[0]);
            LALexer lexer = new LALexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            LAParser parser = new LAParser(tokens);

            parser.addErrorListener(new ErrorListener(sp));

            arvore = parser.programa();
        } catch (ParseCancellationException pce) {
            if (pce.getMessage() != null) {
                sp.println(pce.getMessage());
            }
        }
        if (!sp.HasError()) {
            AnalisadorSemanticoLA asla = new AnalisadorSemanticoLA();
            asla.visitPrograma(arvore);
            if (!sp.HasError()) {

            } else {
                sp.print("Fim da compilacao");
            }
        } else {
            sp.print("Fim da compilacao");
        }
        String out = sp.toString();
        System.out.println(out);
        try {
            File fd = new File(args[1]);
            fd.createNewFile();
            PrintWriter pw = new PrintWriter(new FileWriter(fd));
            pw.println(out);
            pw.close();
        } catch (IOException e) {
            System.out.println("Falha ao abrir o arquivo de saida");
        }



    }
}
