package br.ufal.ic.p2.wepayu.models;

import java.util.ArrayList;

/**
 * Classe que representa um empregado comissionado no sistema WePayU.
 * 
 * <p>Um empregado comissionado recebe um salário base mensal mais uma
 * comissão sobre as vendas realizadas. As vendas são registradas
 * através de resultados de venda que contêm a data e o valor da venda.</p>
 * 
 * <p>Características:</p>
 * <ul>
 *   <li>Salário base mensal</li>
 *   <li>Taxa de comissão sobre vendas</li>
 *   <li>Registro de vendas realizadas</li>
 *   <li>Pode ser sindicalizado</li>
 *   <li>Suporta diferentes métodos de pagamento</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class EmpregadoComissionado extends Empregado {
    private double salarioMensal;
    private double taxaDeComissao;
    private ArrayList<ResultadoDeVenda> resultadoDeVenda= new ArrayList<>();

    /**
     * Construtor padrão da classe EmpregadoComissionado.
     */
    public EmpregadoComissionado() { }

    /**
     * Construtor da classe EmpregadoComissionado com dados básicos.
     * 
     * @param nome Nome completo do empregado
     * @param endereco Endereço residencial do empregado
     * @param salario Salário base mensal do empregado
     * @param taxaDeComissao Taxa de comissão sobre vendas
     */
    public EmpregadoComissionado(String nome, String endereco, double salario, double taxaDeComissao) {
        super(nome, endereco);
        this.salarioMensal = salario;
        this.taxaDeComissao = taxaDeComissao;
        this.setAgendaPagamento(AgendaPagamento.getAgendaPadrao("comissionado"));
    }

    /**
     * Obtém o salário base mensal do empregado comissionado.
     * 
     * @return Salário base mensal em valor numérico
     */
    public double getSalarioMensal() {
        return salarioMensal;
    }

    /**
     * Define o salário base mensal do empregado comissionado.
     * 
     * @param salarioMensal Novo salário base mensal
     */
    public void setSalarioMensal(double salarioMensal) {
        this.salarioMensal = salarioMensal;
    }

    /**
     * Obtém a taxa de comissão do empregado comissionado.
     * 
     * @return Taxa de comissão em valor numérico
     */
    @Override
    public double getTaxaDeComissao() {
        return taxaDeComissao;
    }

    /**
     * Define a taxa de comissão do empregado comissionado.
     * 
     * @param taxaDeComissao Nova taxa de comissão
     */
    @Override
    public void setTaxaDeComissao(double taxaDeComissao) {
        this.taxaDeComissao = taxaDeComissao;
    }

    /**
     * Obtém a lista de resultados de venda do empregado comissionado.
     * 
     * @return Lista de resultados de venda
     */
    @Override
    public ArrayList<ResultadoDeVenda> getResultadoDeVenda() {
        return resultadoDeVenda;
    }

    /**
     * Define a lista de resultados de venda do empregado comissionado.
     * 
     * @param resultadoDeVenda Nova lista de resultados de venda
     */
    public void setResultadoDeVenda(ArrayList<ResultadoDeVenda> resultadoDeVenda) {
        this.resultadoDeVenda = resultadoDeVenda;
    }

    /**
     * Retorna o tipo do empregado.
     * 
     * @return Sempre retorna "comissionado"
     */
    @Override
    public String getTipo() {
        return "comissionado";
    }

    /**
     * Retorna o salário base do empregado em formato monetário.
     * 
     * <p>O salário é formatado com 2 casas decimais e vírgula como separador decimal.</p>
     * 
     * @return Salário base em formato monetário (ex: "1000,50")
     */
    @Override
    public String getSalario() {
        return truncarValorMonetario(this.salarioMensal);
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

    /**
     * Adiciona um resultado de venda à lista do empregado comissionado.
     * 
     * @param resultadoDeVenda Resultado de venda a ser adicionado
     */
    @Override
    public void lancarResultadoDeVenda(ResultadoDeVenda resultadoDeVenda) {
        this.resultadoDeVenda.add(resultadoDeVenda);
    }
}
