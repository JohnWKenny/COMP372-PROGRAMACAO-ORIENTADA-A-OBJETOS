package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando a comissão não pode ser nula
 */
public class ComissaoNaoPodeSerNulaException extends RuntimeException {
    public ComissaoNaoPodeSerNulaException(String msg) {
        super(msg);
    }
}
