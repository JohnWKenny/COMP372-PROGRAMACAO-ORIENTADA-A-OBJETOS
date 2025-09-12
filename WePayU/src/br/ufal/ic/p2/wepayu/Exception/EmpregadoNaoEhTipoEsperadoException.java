package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando um empregado não é do tipo esperado para uma operação
 */
public class EmpregadoNaoEhTipoEsperadoException extends RuntimeException {
    public EmpregadoNaoEhTipoEsperadoException(String msg) {
        super(msg);
    }
}
