package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando há erro na criação de membro do sindicato
 */
public class ErroCriacaoMembroSindicatoException extends RuntimeException {
    public ErroCriacaoMembroSindicatoException(String msg) {
        super(msg);
    }
    
    public ErroCriacaoMembroSindicatoException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
