package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando o tipo não pode ser nulo
 */
public class TipoNaoPodeSerNuloException extends RuntimeException {
    public TipoNaoPodeSerNuloException(String msg) {
        super(msg);
    }
}
