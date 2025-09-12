package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando não há empregado com o nome especificado
 */
public class EmpregadoNaoExisteComNomeException extends RuntimeException {
    public EmpregadoNaoExisteComNomeException(String msg) {
        super(msg);
    }
}
