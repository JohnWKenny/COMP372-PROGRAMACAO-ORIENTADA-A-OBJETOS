package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando um empregado não é sindicalizado
 */
public class EmpregadoNaoEhSindicalizadoException extends RuntimeException {
    public EmpregadoNaoEhSindicalizadoException(String msg) {
        super(msg);
    }
}
