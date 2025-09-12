package br.ufal.ic.p2.wepayu.models;

/**
 * Classe que representa o método de pagamento via depósito bancário.
 * 
 * <p>Esta classe implementa o método de pagamento através de depósito
 * em conta bancária, armazenando as informações necessárias para
 * realizar o depósito.</p>
 * 
 * <p>Informações armazenadas:</p>
 * <ul>
 *   <li>Nome do banco</li>
 *   <li>Número da agência</li>
 *   <li>Número da conta corrente</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class Banco extends MetodoPagamento {
    private String banco;
    private String agencia;
    private String contaCorrente;

    public Banco() { }

    public Banco(String banco, String agencia, String contaCorrente) {
        this.banco = banco;
        this.agencia = agencia;
        this.contaCorrente = contaCorrente;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }


    public String getContaCorrente() {
        return contaCorrente;
    }

    public void setContaCorrente(String contaCorrente) {
        this.contaCorrente = contaCorrente;
    }

    @Override
    public void Pagamento() {
        System.out.println("No banco");
    }

    @Override
    public String getMetodoPagamento() {
        return "banco";
    }
    
}
