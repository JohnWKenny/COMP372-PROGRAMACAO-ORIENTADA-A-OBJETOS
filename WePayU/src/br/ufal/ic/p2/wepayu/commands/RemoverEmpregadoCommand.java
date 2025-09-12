package br.ufal.ic.p2.wepayu.commands;

import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEncontradoException;
import br.ufal.ic.p2.wepayu.Exception.ErroRemocaoEmpregadoException;
import java.util.Map;

/**
 * Comando para remover um empregado do sistema WePayU.
 * 
 * <p>Este comando implementa o padrão Command para remover empregados,
 * permitindo que a operação seja desfeita através do sistema de undo/redo.</p>
 * 
 * <p>Funcionalidades:</p>
 * <ul>
 *   <li>Remoção de empregados do sistema</li>
 *   <li>Armazenamento do empregado removido para desfazer</li>
 *   <li>Restauração do empregado ao desfazer</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class RemoverEmpregadoCommand implements Command {
    private String id;
    private Empregado empregadoRemovido;
    private Map<String, Empregado> empregados;
    
    /**
     * Construtor do comando de remoção de empregado.
     * 
     * @param id ID do empregado a ser removido
     * @param empregados Mapa de empregados do sistema
     */
    public RemoverEmpregadoCommand(String id, Map<String, Empregado> empregados) {
        this.id = id;
        this.empregados = empregados;
    }
    
    /**
     * Executa a remoção do empregado.
     * 
     * <p>Este método remove o empregado do mapa de empregados
     * e armazena uma referência para permitir desfazer a operação.</p>
     * 
     * @throws EmpregadoNaoEncontradoException Se o empregado não for encontrado
     * @throws ErroRemocaoEmpregadoException Se ocorrer erro durante a remoção
     */
    @Override
    public void executar() {
        try {
            this.empregadoRemovido = empregados.get(id);
            if (empregadoRemovido == null) {
                throw new EmpregadoNaoEncontradoException("Empregado não encontrado: " + id);
            }
            empregados.remove(id);
        } catch (EmpregadoNaoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new ErroRemocaoEmpregadoException("Erro ao remover empregado: " + e.getMessage(), e);
        }
    }
    
    /**
     * Desfaz a remoção do empregado.
     * 
     * <p>Este método restaura o empregado removido ao mapa de empregados
     * usando a referência armazenada durante a execução.</p>
     */
    @Override
    public void desfazer() {
        if (empregadoRemovido != null) {
            empregados.put(id, empregadoRemovido);
        }
    }
}