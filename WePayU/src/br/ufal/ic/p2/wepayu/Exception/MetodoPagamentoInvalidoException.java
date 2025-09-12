package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando um método de pagamento é inválido
 */
public class MetodoPagamentoInvalidoException extends RuntimeException {
    public MetodoPagamentoInvalidoException(String msg) {
        super(msg);
    }
}
