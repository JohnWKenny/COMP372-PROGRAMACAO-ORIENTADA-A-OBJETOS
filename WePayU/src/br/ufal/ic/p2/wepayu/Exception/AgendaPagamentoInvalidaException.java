package br.ufal.ic.p2.wepayu.Exception;

/**
 * Exceção lançada quando uma agenda de pagamento inválida é fornecida.
 * 
 * <p>Esta exceção é lançada quando se tenta definir uma agenda de pagamento
 * que não está entre as agendas válidas disponíveis no sistema.</p>
 * 
 * <p>Agendas válidas disponíveis:</p>
 * <ul>
 *   <li>"semanal 5" - Pagamento semanal às sextas-feiras</li>
 *   <li>"semanal 2 5" - Pagamento a cada duas semanas às sextas-feiras</li>
 *   <li>"mensal $" - Pagamento mensal no último dia útil do mês</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class AgendaPagamentoInvalidaException extends Exception {
    
    /**
     * Construtor padrão da exceção.
     */
    public AgendaPagamentoInvalidaException() {
        super();
    }
    
    /**
     * Construtor da exceção com mensagem específica.
     * 
     * @param message Mensagem de erro descrevendo a agenda inválida
     */
    public AgendaPagamentoInvalidaException(String message) {
        super(message);
    }
    
    /**
     * Construtor da exceção com mensagem e causa.
     * 
     * @param message Mensagem de erro descrevendo a agenda inválida
     * @param cause Causa da exceção
     */
    public AgendaPagamentoInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }
}
