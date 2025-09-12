package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando a comissão deve ser numérica mas não é
 */
public class ComissaoDeveSerNumericaException extends RuntimeException {
    public ComissaoDeveSerNumericaException(String msg) {
        super(msg);
    }
}
