package br.ufal.ic.p2.wepayu.commands;

/**
 * Interface que define o padrão Command para operações que podem ser desfeitas.
 * 
 * <p>Esta interface define os contratos para comandos que implementam
 * o padrão Command, permitindo operações de executar e desfazer.
 * É utilizada pelo sistema de undo/redo do WePayU.</p>
 * 
 * <p>Funcionalidades:</p>
 * <ul>
 *   <li>Execução de comandos</li>
 *   <li>Desfazer comandos executados</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public interface Command {
    /**
     * Executa o comando.
     */
    void executar();
    /**
     * Desfaz o comando executado.
     */
    void desfazer();
}


