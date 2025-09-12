package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando o salário deve ser não-negativo mas é negativo
 */
public class SalarioDeveSerNaoNegativoException extends RuntimeException {
    public SalarioDeveSerNaoNegativoException(String msg) {
        super(msg);
    }
}
