package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando há erro na execução de um comando
 */
public class ErroExecucaoComandoException extends RuntimeException {
    public ErroExecucaoComandoException(String msg) {
        super(msg);
    }
    
    public ErroExecucaoComandoException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
