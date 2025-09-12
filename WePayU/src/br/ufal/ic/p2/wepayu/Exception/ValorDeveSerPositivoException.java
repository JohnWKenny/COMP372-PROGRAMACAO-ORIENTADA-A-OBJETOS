package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando um valor deve ser positivo mas não é
 */
public class ValorDeveSerPositivoException extends RuntimeException {
    public ValorDeveSerPositivoException(String msg) {
        super(msg);
    }
}
