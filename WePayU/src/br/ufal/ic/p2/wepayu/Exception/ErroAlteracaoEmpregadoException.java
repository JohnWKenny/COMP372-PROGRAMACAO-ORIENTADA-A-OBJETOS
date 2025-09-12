package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando há erro na alteração de um empregado
 */
public class ErroAlteracaoEmpregadoException extends RuntimeException {
    public ErroAlteracaoEmpregadoException(String msg) {
        super(msg);
    }
    
    public ErroAlteracaoEmpregadoException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
