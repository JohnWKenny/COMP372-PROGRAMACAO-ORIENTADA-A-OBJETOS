package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando a data não pode ser nula
 */
public class DataNaoPodeSerNulaException extends RuntimeException {
    public DataNaoPodeSerNulaException(String msg) {
        super(msg);
    }
}
