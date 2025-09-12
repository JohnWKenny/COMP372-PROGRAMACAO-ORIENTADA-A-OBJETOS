package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando um empregado não é comissionado
 */
public class EmpregadoNaoEhComissionadoException extends RuntimeException {
    public EmpregadoNaoEhComissionadoException(String msg) {
        super(msg);
    }
}
