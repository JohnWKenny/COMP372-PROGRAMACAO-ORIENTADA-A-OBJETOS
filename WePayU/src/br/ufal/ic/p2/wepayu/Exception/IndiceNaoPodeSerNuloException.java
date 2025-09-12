package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando o índice não pode ser nulo
 */
public class IndiceNaoPodeSerNuloException extends RuntimeException {
    public IndiceNaoPodeSerNuloException(String msg) {
        super(msg);
    }
}
