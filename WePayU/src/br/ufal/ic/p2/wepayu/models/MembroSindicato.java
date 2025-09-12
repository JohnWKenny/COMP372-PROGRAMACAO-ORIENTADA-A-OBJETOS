package br.ufal.ic.p2.wepayu.models;

import java.util.ArrayList;

/**
 * Classe que representa um membro do sindicato no sistema WePayU.
 * 
 * <p>Esta classe armazena as informações de um empregado que é membro
 * do sindicato, incluindo sua taxa sindical, dívida sindical acumulada
 * e histórico de taxas de serviço lançadas.</p>
 * 
 * <p>Informações armazenadas:</p>
 * <ul>
 *   <li>ID único do membro</li>
 *   <li>Taxa sindical diária</li>
 *   <li>Dívida sindical acumulada (para empregados horistas)</li>
 *   <li>Histórico de taxas de serviço</li>
 * </ul>
 * 
 * <p><strong>Sistema de Dívida Sindical:</strong></p>
 * <p>O campo {@code dividaSindical} é usado principalmente para empregados horistas,
 * permitindo o acúmulo de taxas sindicais quando o salário não é suficiente para
 * cobrir todos os descontos. A dívida é gerenciada automaticamente pelo sistema
 * de folha de pagamento.</p>
 * 
 * @author John Wallex
 * @version 1.1
 * @since 2025
 */
public class MembroSindicato {
    private String idMembro;
    private double taxaSindical;
    private double dividaSindical = 0.0;
    private ArrayList<TaxaServico> taxasDeServicos = new ArrayList<>();

    public MembroSindicato() { }

    public MembroSindicato(String idMembro, String taxaSindical) {
        this.setIdMembro(idMembro);
        this.setTaxaSindical(Double.parseDouble(taxaSindical));
    }

    public String getIdMembro() {
        return idMembro;
    }

    public void setIdMembro(String idMembro) {
        this.idMembro = idMembro;
    }

    public double getTaxaSindical() {
        return taxaSindical;
    }

    public void setTaxaSindical(double taxaSindical) {
        this.taxaSindical = taxaSindical;
    }

    public ArrayList<TaxaServico> getTaxasDeServicos() {
        return taxasDeServicos;
    }

    public void setTaxasDeServicos(ArrayList<TaxaServico> taxasDeServicos) {
        this.taxasDeServicos = taxasDeServicos;
    }

    public void addTaxaServico(TaxaServico taxaServico){
        this.taxasDeServicos.add(taxaServico);
    }

    /**
     * Retorna o valor atual da dívida sindical acumulada.
     * 
     * <p>Este valor representa o total de taxas sindicais que o membro
     * deve ao sindicato, principalmente usado para empregados horistas
     * que podem ter salários insuficientes para cobrir todos os descontos.</p>
     * 
     * @return O valor da dívida sindical acumulada
     */
    public double getDividaSindical() {
        return dividaSindical;
    }

    /**
     * Define o valor da dívida sindical acumulada.
     * 
     * <p>Este método é usado pelo sistema de folha de pagamento para
     * gerenciar a dívida sindical, adicionando novas taxas ou reduzindo
     * quando o empregado consegue pagar parte ou toda a dívida.</p>
     * 
     * @param dividaSindical O novo valor da dívida sindical
     */
    public void setDividaSindical(double dividaSindical) {
        this.dividaSindical = dividaSindical;
    }
}
