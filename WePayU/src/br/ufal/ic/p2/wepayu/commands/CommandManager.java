package br.ufal.ic.p2.wepayu.commands;

import br.ufal.ic.p2.wepayu.Exception.NaoHaComandoDesfazerException;
import java.util.Stack;

/**
 * Gerenciador de comandos que implementa o padrão Command com funcionalidades de undo/redo.
 * 
 * <p>Esta classe gerencia a execução, desfazer e refazer de comandos no sistema WePayU.
 * Utiliza duas pilhas para manter o histórico de comandos executados e comandos
 * que podem ser refeitos.</p>
 * 
 * <p>Funcionalidades:</p>
 * <ul>
 *   <li>Execução de comandos</li>
 *   <li>Desfazer comandos (undo)</li>
 *   <li>Refazer comandos (redo)</li>
 *   <li>Gerenciamento de histórico</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class CommandManager implements CommandManagerInterface {
    private Stack<Command> historico = new Stack<>();
    private Stack<Command> redoStack = new Stack<>();
    
    /**
     * Executa um comando e o adiciona ao histórico.
     * 
     * <p>Este método executa o comando fornecido e o adiciona à pilha
     * de histórico. Após executar um novo comando, a pilha de redo
     * é limpa para evitar inconsistências.</p>
     * 
     * @param command Comando a ser executado
     */
    @Override
    public void executar(Command command) {
        command.executar();
        historico.push(command);
        redoStack.clear();
    }
    
    /**
     * Desfaz o último comando executado.
     * 
     * <p>Este método remove o último comando da pilha de histórico,
     * executa sua operação de desfazer e o adiciona à pilha de redo
     * para permitir refazer a operação posteriormente.</p>
     * 
     * @throws NaoHaComandoDesfazerException Se não houver comandos para desfazer
     */
    @Override
    public void undo() {
        if (!historico.isEmpty()) {
            Command command = historico.pop();
            command.desfazer();
            redoStack.push(command);
        } else {
            throw new NaoHaComandoDesfazerException("Nao ha comando a desfazer.");
        }
    }
    
    /**
     * Refaz o último comando que foi desfeito.
     * 
     * <p>Este método remove o último comando da pilha de redo,
     * executa novamente o comando e o adiciona de volta à pilha
     * de histórico.</p>
     * 
     * @throws NaoHaComandoDesfazerException Se não houver comandos para refazer
     */
    @Override
    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.executar();
            historico.push(command);
        } else {
            throw new NaoHaComandoDesfazerException("Nao ha comando a refazer.");
        }
    }
}