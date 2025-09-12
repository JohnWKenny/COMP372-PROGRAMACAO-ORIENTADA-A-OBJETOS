package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando as horas não podem ser nulas
 */
public class HorasNaoPodemSerNulasException extends RuntimeException {
    public HorasNaoPodemSerNulasException(String msg) {
        super(msg);
    }
}
