package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando não é possível dar comandos após encerrar o sistema
 */
public class NaoPodeComandosAposEncerrarSistemaException extends RuntimeException {
    public NaoPodeComandosAposEncerrarSistemaException(String msg) {
        super(msg);
    }
}
