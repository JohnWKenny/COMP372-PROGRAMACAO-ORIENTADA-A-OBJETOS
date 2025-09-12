package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando um valor deve ser não-negativo mas é negativo
 */
public class ValorDeveSerNaoNegativoException extends RuntimeException {
    public ValorDeveSerNaoNegativoException(String msg) {
        super(msg);
    }
}
