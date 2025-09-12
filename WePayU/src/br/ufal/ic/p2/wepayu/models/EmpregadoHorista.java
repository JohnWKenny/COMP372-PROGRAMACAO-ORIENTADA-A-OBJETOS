package br.ufal.ic.p2.wepayu.models;

import java.util.ArrayList;

/**
 * Classe que representa um empregado horista no sistema WePayU.
 * 
 * <p>Um empregado horista recebe pagamento baseado nas horas trabalhadas,
 * registradas através de cartões de ponto. As horas são divididas em
 * normais (até 8h por dia) e extras (acima de 8h por dia), com valores
 * diferentes para cada tipo.</p>
 * 
 * <p>Características:</p>
 * <ul>
 *   <li>Salário por hora trabalhada</li>
 *   <li>Cartões de ponto para registro de horas</li>
 *   <li>Horas normais e extras com valores diferentes</li>
 *   <li>Pode ser sindicalizado</li>
 *   <li>Suporta diferentes métodos de pagamento</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class EmpregadoHorista extends Empregado {
    private double salarioPorHora;
    private ArrayList<CartaoDePonto> cartoes = new  ArrayList<>();

    /**
     * Construtor padrão da classe EmpregadoHorista.
     */
    public EmpregadoHorista() { }

    /**
     * Construtor da classe EmpregadoHorista com dados básicos.
     * 
     * @param nome Nome completo do empregado
     * @param endereco Endereço residencial do empregado
     * @param salario Salário por hora do empregado
     */
    public EmpregadoHorista(String nome, String endereco, double salario) {
        super(nome, endereco);
        this.salarioPorHora = salario;
    }

    /**
     * Obtém o salário por hora do empregado horista.
     * 
     * @return Salário por hora em valor numérico
     */
    public double getSalarioPorHora() {
        return salarioPorHora;
    }

    /**
     * Define o salário por hora do empregado horista.
     * 
     * @param salarioPorHora Novo salário por hora
     */
    public void setSalarioPorHora(double salarioPorHora) {
        this.salarioPorHora = salarioPorHora;
    }

    /**
     * Obtém a lista de cartões de ponto do empregado horista.
     * 
     * @return Lista de cartões de ponto
     */
    @Override
    public ArrayList<CartaoDePonto> getCartoes() { return this.cartoes; }

    /**
     * Define a lista de cartões de ponto do empregado horista.
     * 
     * @param cartoes Nova lista de cartões de ponto
     */
    public void setCartoes(ArrayList<CartaoDePonto> cartoes) {
        this.cartoes = cartoes;
    }

    /**
     * Adiciona um cartão de ponto à lista do empregado horista.
     * 
     * @param cartao Cartão de ponto a ser adicionado
     */
    @Override
    public void lancarCartao(CartaoDePonto cartao) {
        this.cartoes.add(cartao);
    }

    /**
     * Retorna o tipo do empregado.
     * 
     * @return Sempre retorna "horista"
     */
    @Override
    public String getTipo() {
        return "horista";
    }

    /**
     * Retorna o salário por hora do empregado em formato monetário.
     * 
     * <p>O salário é formatado com 2 casas decimais e vírgula como separador decimal.</p>
     * 
     * @return Salário por hora em formato monetário (ex: "15,50")
     */
    @Override
    public String getSalario() {
        return truncarValorMonetario(this.salarioPorHora);
    }

    /**
     * Trunca um valor monetário para 2 casas decimais sem arredondamento.
     * 
     * <p>Este método converte um valor numérico para formato monetário brasileiro,
     * truncando (não arredondando) para 2 casas decimais e usando vírgula
     * como separador decimal.</p>
     * 
     * @param valor Valor numérico a ser formatado
     * @return Valor formatado em string com vírgula como separador decimal
     */
    private String truncarValorMonetario(double valor) {
        java.math.BigDecimal bd = java.math.BigDecimal.valueOf(valor);
        bd = bd.setScale(2, java.math.RoundingMode.DOWN);
        return bd.toString().replace('.', ',');
    }
}
