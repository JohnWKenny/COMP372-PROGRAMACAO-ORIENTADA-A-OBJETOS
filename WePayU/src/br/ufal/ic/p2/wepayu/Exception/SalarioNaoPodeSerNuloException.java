package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando o salário não pode ser nulo
 */
public class SalarioNaoPodeSerNuloException extends RuntimeException {
    public SalarioNaoPodeSerNuloException(String msg) {
        super(msg);
    }
}
