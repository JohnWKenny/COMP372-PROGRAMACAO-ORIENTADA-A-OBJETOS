package br.ufal.ic.p2.wepayu.commands;

/**
 * Interface para o padrão Memento no sistema WePayU.
 * 
 * <p>Esta interface define o contrato para objetos Memento que permitem
 * salvar e restaurar o estado interno de objetos, implementando
 * o padrão Memento para funcionalidades de undo/redo.</p>
 * 
 * <p>Funcionalidades:</p>
 * <ul>
 *   <li>Restauração de estado anterior</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public interface Memento {
    /**
     * Restaura o estado anterior do objeto.
     */
    void restaurar();
}
