package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando um empregado não recebe em banco
 */
public class EmpregadoNaoRecebeEmBancoException extends RuntimeException {
    public EmpregadoNaoRecebeEmBancoException(String msg) {
        super(msg);
    }
}
