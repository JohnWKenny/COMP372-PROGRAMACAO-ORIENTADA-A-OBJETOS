package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando um membro do sindicato não é encontrado
 */
public class MembroSindicatoNaoEncontradoException extends RuntimeException {
    public MembroSindicatoNaoEncontradoException(String msg) {
        super(msg);
    }
}
