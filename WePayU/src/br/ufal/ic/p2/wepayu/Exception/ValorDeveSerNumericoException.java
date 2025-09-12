package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando um valor deve ser numérico mas não é
 */
public class ValorDeveSerNumericoException extends RuntimeException {
    public ValorDeveSerNumericoException(String msg) {
        super(msg);
    }
}
