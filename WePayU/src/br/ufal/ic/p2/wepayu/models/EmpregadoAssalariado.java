package br.ufal.ic.p2.wepayu.models;

/**
 * Classe que representa um empregado assalariado no sistema WePayU.
 * 
 * <p>Um empregado assalariado recebe um salário fixo mensal, independentemente
 * das horas trabalhadas ou vendas realizadas. O salário é pago mensalmente
 * em uma data específica.</p>
 * 
 * <p>Características:</p>
 * <ul>
 *   <li>Salário fixo mensal</li>
 *   <li>Pagamento independente de horas trabalhadas</li>
 *   <li>Pode ser sindicalizado</li>
 *   <li>Suporta diferentes métodos de pagamento</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class EmpregadoAssalariado extends Empregado {
    private double salarioMensal;

    /**
     * Construtor padrão da classe EmpregadoAssalariado.
     */
    public EmpregadoAssalariado() { }

    /**
     * Construtor da classe EmpregadoAssalariado com dados básicos.
     * 
     * @param nome Nome completo do empregado
     * @param endereco Endereço residencial do empregado
     * @param salario Salário mensal do empregado
     */
    public EmpregadoAssalariado(String nome, String endereco, double salario){
        super(nome, endereco);
        setSalarioMensal(salario);
    }

    /**
     * Obtém o salário mensal do empregado assalariado.
     * 
     * @return Salário mensal em valor numérico
     */
    public double getSalarioMensal() {
        return salarioMensal;
    }
    /**
     * Define o salário mensal do empregado assalariado.
     * 
     * @param salarioMensal Novo salário mensal
     */
    public void setSalarioMensal(double salarioMensal) { this.salarioMensal = salarioMensal; }

    /**
     * Retorna o tipo do empregado.
     * 
     * @return Sempre retorna "assalariado"
     */
    @Override
    public String getTipo() {
        return "assalariado";
    }

    /**
     * Retorna o salário do empregado em formato monetário.
     * 
     * <p>O salário é formatado com 2 casas decimais e vírgula como separador decimal.</p>
     * 
     * @return Salário em formato monetário (ex: "1000,50")
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
}
