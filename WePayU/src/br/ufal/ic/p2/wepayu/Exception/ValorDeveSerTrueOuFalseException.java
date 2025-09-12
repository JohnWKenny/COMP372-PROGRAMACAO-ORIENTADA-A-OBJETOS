package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando um valor deve ser true ou false mas não é
 */
public class ValorDeveSerTrueOuFalseException extends RuntimeException {
    public ValorDeveSerTrueOuFalseException(String msg) {
        super(msg);
    }
}
