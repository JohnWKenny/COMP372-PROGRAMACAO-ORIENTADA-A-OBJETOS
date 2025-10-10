package br.ufal.ic.p2.wepayu.models;

import java.util.ArrayList;

/**
 * Classe abstrata que representa um empregado no sistema WePayU.
 * 
 * <p>Esta classe define a estrutura base para todos os tipos de empregados
 * no sistema, incluindo informações pessoais, sindicalização e método de pagamento.
 * É uma classe abstrata que deve ser estendida por tipos específicos de empregados.</p>
 * 
 * <p>Tipos de empregados suportados:</p>
 * <ul>
 *   <li><strong>EmpregadoAssalariado:</strong> Recebe salário fixo mensal</li>
 *   <li><strong>EmpregadoHorista:</strong> Recebe por horas trabalhadas</li>
 *   <li><strong>EmpregadoComissionado:</strong> Recebe comissão sobre vendas</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public abstract class Empregado {
    private String Id;
    private String nome;
    private String endereco;
    private MembroSindicato sindicato;
    private MetodoPagamento metodoPagamento = new EmMaos();
    private AgendaPagamento agendaPagamento;

    /**
     * Construtor padrão da classe Empregado.
     */
    public Empregado() { }

    /**
     * Construtor da classe Empregado com nome e endereço.
     * 
     * @param nome Nome completo do empregado
     * @param endereco Endereço residencial do empregado
     */
    public Empregado(String nome, String endereco) {
        setNome(nome);
        setEndereco(endereco);
        this.agendaPagamento = new AgendaPagamento();
    }

    /**
     * Obtém o nome do empregado.
     * 
     * @return Nome completo do empregado
     */
    public final String getNome() {
        return nome;
    }

    /**
     * Define o nome do empregado.
     * 
     * @param nome Nome completo do empregado
     */
    public final void setNome(String nome) { this.nome = nome; }

    /**
     * Obtém o endereço do empregado.
     * 
     * @return Endereço residencial do empregado
     */
    public final String getEndereco() {
        return endereco;
    }

    /**
     * Define o endereço do empregado.
     * 
     * @param endereco Endereço residencial do empregado
     */
    public final void setEndereco(String endereco) { this.endereco = endereco; }

    /**
     * Obtém o tipo do empregado.
     * 
     * <p>Método abstrato que deve ser implementado pelas subclasses
     * para retornar o tipo específico do empregado.</p>
     * 
     * @return Tipo do empregado ("assalariado", "horista" ou "comissionado")
     */
    public abstract String getTipo();

    /**
     * Obtém o salário do empregado.
     * 
     * <p>Método abstrato que deve ser implementado pelas subclasses
     * para retornar o salário específico do tipo de empregado.</p>
     * 
     * @return Salário do empregado em formato monetário
     */
    public abstract String getSalario();

    /**
     * Verifica se o empregado é sindicalizado.
     * 
     * @return "true" se o empregado for sindicalizado, "false" caso contrário
     */
    public final String ehSindicalizado(){
        if(sindicato == null){
            return "false";
        }
        return "true";
    }

    /**
     * Define a taxa de comissão do empregado.
     * 
     * <p>Método padrão que não faz nada. Deve ser sobrescrito
     * nas subclasses que suportam comissão.</p>
     * 
     * @param taxaDeComissao Taxa de comissão
     */
    public void setTaxaDeComissao(double taxaDeComissao) { }

    /**
     * Obtém a taxa de comissão do empregado.
     * 
     * <p>Método padrão que retorna 0. Deve ser sobrescrito
     * nas subclasses que suportam comissão.</p>
     * 
     * @return Taxa de comissão (0 por padrão)
     */
    public double getTaxaDeComissao() { return 0; }

    /**
     * Obtém o método de pagamento do empregado.
     * 
     * @return Método de pagamento configurado
     */
    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    /**
     * Define o método de pagamento do empregado.
     * 
     * @param metodoPagamento Novo método de pagamento
     */
    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    /**
     * Obtém os cartões de ponto do empregado.
     * 
     * <p>Método padrão que retorna null. Deve ser sobrescrito
     * nas subclasses que suportam cartões de ponto.</p>
     * 
     * @return Lista de cartões de ponto (null por padrão)
     */
    public ArrayList<CartaoDePonto> getCartoes() { return null; }
    /**
     * Obtém os resultados de venda do empregado.
     * 
     * <p>Método padrão que retorna null. Deve ser sobrescrito
     * nas subclasses que suportam vendas.</p>
     * 
     * @return Lista de resultados de venda (null por padrão)
     */
    public ArrayList<ResultadoDeVenda> getResultadoDeVenda() { return null; }

    /**
     * Obtém o ID único do empregado.
     * 
     * @return ID único do empregado
     */
    public final String getId() {
        return Id;
    }

    /**
     * Define o ID único do empregado.
     * 
     * @param Id ID único do empregado
     */
    public final void setId(String Id) {
        this.Id = Id;
    }

    /**
     * Lança um cartão de ponto para o empregado.
     * 
     * <p>Método padrão que não faz nada. Deve ser sobrescrito
     * nas subclasses que suportam cartões de ponto.</p>
     * 
     * @param cartao Cartão de ponto a ser lançado
     */
    public void lancarCartao(CartaoDePonto cartao) { }

    /**
     * Lança um resultado de venda para o empregado.
     * 
     * <p>Método padrão que não faz nada. Deve ser sobrescrito
     * nas subclasses que suportam vendas.</p>
     * 
     * @param resultadoDeVenda Resultado de venda a ser lançado
     */
    public void lancarResultadoDeVenda(ResultadoDeVenda resultadoDeVenda) { }

    /**
     * Obtém as informações do sindicato do empregado.
     * 
     * @return Objeto MembroSindicato se o empregado for sindicalizado, null caso contrário
     */
    public final MembroSindicato getSindicato() {
        return sindicato;
    }

    /**
     * Define as informações do sindicato do empregado.
     * 
     * @param sindicato Objeto MembroSindicato com as informações sindicais
     */
    public final void setSindicato(MembroSindicato sindicato) {
        this.sindicato = sindicato;
    }
    
    /**
     * Obtém a agenda de pagamento do empregado.
     * 
     * @return Agenda de pagamento do empregado
     */
    public final AgendaPagamento getAgendaPagamento() {
        return agendaPagamento;
    }
    
    /**
     * Define a agenda de pagamento do empregado.
     * 
     * @param agendaPagamento Nova agenda de pagamento
     */
    public final void setAgendaPagamento(AgendaPagamento agendaPagamento) {
        this.agendaPagamento = agendaPagamento;
    }
    
    /**
     * Define a agenda de pagamento do empregado por string.
     * 
     * @param agenda String representando a agenda de pagamento
     */
    public final void setAgendaPagamento(String agenda) {
        this.agendaPagamento = new AgendaPagamento(agenda);
    }
}