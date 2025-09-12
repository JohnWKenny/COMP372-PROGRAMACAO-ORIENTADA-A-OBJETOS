package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando a comissão deve ser não-negativa mas é negativa
 */
public class ComissaoDeveSerNaoNegativaException extends RuntimeException {
    public ComissaoDeveSerNaoNegativaException(String msg) {
        super(msg);
    }
}
