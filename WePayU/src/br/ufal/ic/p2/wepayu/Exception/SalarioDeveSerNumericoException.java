package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando o salário deve ser numérico mas não é
 */
public class SalarioDeveSerNumericoException extends RuntimeException {
    public SalarioDeveSerNumericoException(String msg) {
        super(msg);
    }
}
