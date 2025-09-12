package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando há erro na criação de um empregado
 */
public class ErroCriacaoEmpregadoException extends RuntimeException {
    public ErroCriacaoEmpregadoException(String msg) {
        super(msg);
    }
    
    public ErroCriacaoEmpregadoException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
