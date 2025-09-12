package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando o atributo não pode ser nulo
 */
public class AtributoNaoPodeSerNuloException extends RuntimeException {
    public AtributoNaoPodeSerNuloException(String msg) {
        super(msg);
    }
}
