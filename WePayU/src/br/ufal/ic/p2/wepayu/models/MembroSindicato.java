package br.ufal.ic.p2.wepayu.models;

import java.util.ArrayList;

/**
 * Classe que representa um membro do sindicato no sistema WePayU.
 * 
 * <p>Esta classe armazena as informações de um empregado que é membro
 * do sindicato, incluindo sua taxa sindical e histórico de taxas
 * de serviço lançadas.</p>
 * 
 * <p>Informações armazenadas:</p>
 * <ul>
 *   <li>ID único do membro</li>
 *   <li>Taxa sindical mensal</li>
 *   <li>Histórico de taxas de serviço</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class MembroSindicato {
    private String idMembro;
    private double taxaSindical;
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
}
