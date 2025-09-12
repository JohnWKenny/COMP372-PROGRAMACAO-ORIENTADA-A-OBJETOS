package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando há erro no lançamento de cartão de ponto
 */
public class ErroLancamentoCartaoException extends RuntimeException {
    public ErroLancamentoCartaoException(String msg) {
        super(msg);
    }
    
    public ErroLancamentoCartaoException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
