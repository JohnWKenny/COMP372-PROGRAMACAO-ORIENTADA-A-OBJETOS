package br.ufal.ic.p2.wepayu.commands;

import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.Exception.ErroCriacaoEmpregadoException;
import java.util.Map;

/**
 * Comando para criar um novo empregado no sistema WePayU.
 * 
 * <p>Este comando implementa o padrão Command para criar empregados,
 * permitindo que a operação seja desfeita através do sistema de undo/redo.</p>
 * 
 * <p>Funcionalidades:</p>
 * <ul>
 *   <li>Criação de empregados no sistema</li>
 *   <li>Armazenamento do ID do empregado para desfazer</li>
 *   <li>Remoção do empregado ao desfazer</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class CriarEmpregadoCommand implements Command {
    private String id;
    private Empregado empregado;
    private Map<String, Empregado> empregados;
    
    /**
     * Construtor do comando de criação de empregado.
     * 
     * @param empregado Empregado a ser criado
     * @param empregados Mapa de empregados do sistema
     */
    public CriarEmpregadoCommand(Empregado empregado, Map<String, Empregado> empregados) {
        this.empregado = empregado;
        this.empregados = empregados;
    }
    
    /**
     * Executa a criação do empregado.
     * 
     * <p>Este método adiciona o empregado ao mapa de empregados
     * e armazena seu ID para permitir desfazer a operação.</p>
     */
    @Override
    public void executar() {
        try {
            this.id = empregado.getId();
            empregados.put(id, empregado);
        } catch (Exception e) {
            throw new ErroCriacaoEmpregadoException("Erro ao criar empregado: " + e.getMessage(), e);
        }
    }
    
    /**
     * Desfaz a criação do empregado.
     * 
     * <p>Este método remove o empregado do mapa de empregados
     * usando o ID armazenado durante a execução.</p>
     */
    @Override
    public void desfazer() {
        if (id != null) {
            empregados.remove(id);
        }
    }
    
    /**
     * Obtém o ID do empregado criado.
     * 
     * @return ID do empregado
     */
    public String getId() {
        return id;
    }
}