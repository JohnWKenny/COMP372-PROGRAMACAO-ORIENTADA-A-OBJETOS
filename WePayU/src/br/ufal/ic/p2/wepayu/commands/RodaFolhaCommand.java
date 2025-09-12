package br.ufal.ic.p2.wepayu.commands;

import br.ufal.ic.p2.wepayu.services.FolhaPagamentoService;
import br.ufal.ic.p2.wepayu.Exception.DataInvalidaException;

/**
 * Comando para processar a folha de pagamento no sistema WePayU.
 * 
 * <p>Este comando implementa o padrão Command para processar
 * a folha de pagamento em uma data específica, permitindo que
 * a operação seja desfeita através do sistema de undo/redo.</p>
 * 
 * <p>Funcionalidades:</p>
 * <ul>
 *   <li>Processamento da folha de pagamento</li>
 *   <li>Geração de arquivo de folha</li>
 *   <li>Validação de data</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class RodaFolhaCommand implements Command {
    private String data;
    private String arquivo;
    private FolhaPagamentoService folhaPagamentoService;
    
    public RodaFolhaCommand(String data, String arquivo, FolhaPagamentoService folhaPagamentoService) {
        this.data = data;
        this.arquivo = arquivo;
        this.folhaPagamentoService = folhaPagamentoService;
    }
    
    @Override
    public void executar() {
        try {
            folhaPagamentoService.rodaFolha(data, arquivo);
        } catch (DataInvalidaException e) {
            // Re-lança como RuntimeException para manter compatibilidade com Command
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao rodar folha: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void desfazer() {
        // rodaFolha não altera o estado do sistema, apenas gera arquivo
        // Não há nada para desfazer
    }
}
