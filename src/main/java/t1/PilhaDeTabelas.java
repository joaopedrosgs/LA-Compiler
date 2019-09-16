package t1;
import java.util.LinkedList;
import java.util.List;

public class PilhaDeTabelas {

    private LinkedList<TabelaDeSimbolos> pilha;

    public PilhaDeTabelas() {
        pilha = new LinkedList<TabelaDeSimbolos>();
    }

    public void empilhar(TabelaDeSimbolos ts) { pilha.push(ts); }

    public TabelaDeSimbolos topo() {
        return pilha.peek();
    }

    public boolean existeSimbolo(String nome) {
        for (TabelaDeSimbolos ts : pilha) {
            if (ts.existeSimbolo(nome)) {
                return true;
            }
        }
        return false;
    }

    public void desempilhar() {
        TabelaDeSimbolos ret = pilha.pop();
        System.out.println(ret.toString());
    }

    public String getSimboloTipo(String nome){
        for(TabelaDeSimbolos tabela: pilha){
            if(tabela.existeSimbolo(nome)){
                return tabela.getTipo(nome);
            }
        }
        return "falso";
    }

    public List getTodasTabelas() {
        return pilha;
    }
}
