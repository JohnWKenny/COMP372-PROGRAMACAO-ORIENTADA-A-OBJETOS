package br.ufal.ic.p2.wepayu.services;

/**
 * Interface para operações de persistência no sistema WePayU.
 * 
 * <p>Esta interface define os contratos para operações relacionadas
 * à persistência de dados do sistema, incluindo salvamento,
 * carregamento e gerenciamento do estado do sistema.</p>
 * 
 * <p>Funcionalidades principais:</p>
 * <ul>
 *   <li>Salvamento de dados do sistema</li>
 *   <li>Carregamento de dados do sistema</li>
 *   <li>Limpeza de dados do sistema</li>
 *   <li>Encerramento do sistema</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public interface PersistenciaService {
    void salvarSistema();
    
    void carregarSistema();
    
    void zerarSistema();

    void encerrarSistema();
}
