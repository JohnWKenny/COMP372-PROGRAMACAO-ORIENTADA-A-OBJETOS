package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando não há comando para desfazer
 */
public class NaoHaComandoDesfazerException extends RuntimeException {
    public NaoHaComandoDesfazerException(String msg) {
        super(msg);
    }
}
