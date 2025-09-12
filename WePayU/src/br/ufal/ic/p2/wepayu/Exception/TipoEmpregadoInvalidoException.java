package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando um empregado não é do tipo esperado
 */
public class TipoEmpregadoInvalidoException extends RuntimeException {
    public TipoEmpregadoInvalidoException(String msg) {
        super(msg);
    }
}
