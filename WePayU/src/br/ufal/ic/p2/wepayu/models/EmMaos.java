package br.ufal.ic.p2.wepayu.models;

/**
 * Classe que representa o método de pagamento em mãos.
 * 
 * <p>Esta classe implementa o método de pagamento onde o valor
 * é entregue diretamente ao empregado em mãos.</p>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class EmMaos extends MetodoPagamento{

    public EmMaos() {}

    @Override
    public String getMetodoPagamento() {
        return "emMaos";
    }

    @Override
    public void Pagamento() {
        System.out.println("Em mãos");
    }
    
}
