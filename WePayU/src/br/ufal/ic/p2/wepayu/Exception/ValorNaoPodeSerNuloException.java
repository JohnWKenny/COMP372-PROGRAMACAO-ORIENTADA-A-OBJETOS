package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando o valor não pode ser nulo
 */
public class ValorNaoPodeSerNuloException extends RuntimeException {
    public ValorNaoPodeSerNuloException(String msg) {
        super(msg);
    }
}
