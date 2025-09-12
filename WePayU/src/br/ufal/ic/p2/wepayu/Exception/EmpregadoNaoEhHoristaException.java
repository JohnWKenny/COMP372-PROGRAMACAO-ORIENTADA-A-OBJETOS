package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando um empregado não é horista
 */
public class EmpregadoNaoEhHoristaException extends RuntimeException {
    public EmpregadoNaoEhHoristaException(String msg) {
        super(msg);
    }
}
