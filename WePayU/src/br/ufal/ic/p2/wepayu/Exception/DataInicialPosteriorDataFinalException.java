package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando uma data inicial é posterior à data final
 */
public class DataInicialPosteriorDataFinalException extends RuntimeException {
    public DataInicialPosteriorDataFinalException(String msg) {
        super(msg);
    }
}
