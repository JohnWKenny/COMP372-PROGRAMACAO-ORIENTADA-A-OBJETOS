package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando a identificação do empregado não pode ser nula
 */
public class IdentificacaoEmpregadoNaoPodeSerNulaException extends RuntimeException {
    public IdentificacaoEmpregadoNaoPodeSerNulaException(String msg) {
        super(msg);
    }
}
