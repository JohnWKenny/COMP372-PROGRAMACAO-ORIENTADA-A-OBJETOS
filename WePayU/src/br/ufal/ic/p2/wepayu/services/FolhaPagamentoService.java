package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.Exception.*;

/**
 * Interface para operações de folha de pagamento no sistema WePayU.
 * 
 * <p>Esta interface define os contratos para operações relacionadas
 * ao processamento e cálculo da folha de pagamento, incluindo
 * cálculo de totais e geração de arquivos de folha.</p>
 * 
 * <p>Funcionalidades principais:</p>
 * <ul>
 *   <li>Cálculo do total da folha de pagamento</li>
 *   <li>Processamento e geração de folha de pagamento</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public interface FolhaPagamentoService {
    String totalFolha(String data) throws DataInvalidaException;
    
    void rodaFolha(String data, String arquivo) throws DataInvalidaException;
}
