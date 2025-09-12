package br.ufal.ic.p2.wepayu.models;

/**
 * Classe abstrata que representa um método de pagamento no sistema WePayU.
 * 
 * <p>Esta classe define a estrutura base para todos os tipos de métodos
 * de pagamento suportados pelo sistema, incluindo pagamento em mãos,
 * via correios e depósito bancário.</p>
 * 
 * <p>Tipos de métodos de pagamento suportados:</p>
 * <ul>
 *   <li><strong>EmMaos:</strong> Pagamento entregue em mãos</li>
 *   <li><strong>Correios:</strong> Pagamento enviado via correios</li>
 *   <li><strong>Banco:</strong> Pagamento via depósito bancário</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public abstract class MetodoPagamento {
    /**
     * Executa o processo de pagamento.
     * 
     * <p>Método abstrato que deve ser implementado pelas subclasses
     * para definir o comportamento específico de cada método de pagamento.</p>
     */
    public abstract void Pagamento();

    /**
     * Construtor padrão da classe MetodoPagamento.
     */
    public MetodoPagamento() { }

    /**
     * Obtém o nome do método de pagamento.
     * 
     * <p>Método abstrato que deve ser implementado pelas subclasses
     * para retornar o nome específico do método de pagamento.</p>
     * 
     * @return Nome do método de pagamento
     */
    public abstract String getMetodoPagamento();
}
