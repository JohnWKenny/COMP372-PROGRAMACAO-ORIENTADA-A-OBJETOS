package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando um empregado não é encontrado durante execução de comando
 */
public class EmpregadoNaoEncontradoException extends RuntimeException {
    public EmpregadoNaoEncontradoException(String msg) {
        super(msg);
    }
}
