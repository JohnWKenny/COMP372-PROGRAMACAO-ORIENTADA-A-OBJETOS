package br.ufal.ic.p2.wepayu.commands;

/**
 * Interface para gerenciamento de comandos no sistema WePayU.
 * 
 * <p>Esta interface define os contratos para o gerenciamento de comandos
 * que implementam o padrão Command, permitindo operações de executar,
 * desfazer e refazer comandos.</p>
 * 
 * <p>Funcionalidades:</p>
 * <ul>
 *   <li>Execução de comandos</li>
 *   <li>Desfazer comandos (undo)</li>
 *   <li>Refazer comandos (redo)</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public interface CommandManagerInterface {
    /**
     * Executa um comando e o adiciona ao histórico.
     * 
     * @param command Comando a ser executado
     */
    void executar(Command command);
    /**
     * Desfaz o último comando executado.
     * 
     * @throws Exception Se não houver comandos para desfazer
     */
    void undo();
    /**
     * Refaz o último comando que foi desfeito.
     * 
     * @throws Exception Se não houver comandos para refazer
     */
    void redo();
}
