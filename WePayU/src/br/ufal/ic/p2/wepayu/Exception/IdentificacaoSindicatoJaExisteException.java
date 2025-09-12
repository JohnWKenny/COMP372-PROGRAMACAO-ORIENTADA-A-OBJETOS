package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando há outro empregado com a mesma identificação de sindicato
 */
public class IdentificacaoSindicatoJaExisteException extends RuntimeException {
    public IdentificacaoSindicatoJaExisteException(String msg) {
        super(msg);
    }
}
