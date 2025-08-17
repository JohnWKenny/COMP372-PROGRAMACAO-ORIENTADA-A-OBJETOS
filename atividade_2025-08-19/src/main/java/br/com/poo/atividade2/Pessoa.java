package br.com.poo.atividade2;

/**
 *
 * @author John
 */
public abstract class Pessoa {
    private String nome;
    private double rendaAnual;
    private double imposto;

    public final String getNome() {
        return this.nome;
    }

    public final void setNome(String nome) {
        this.nome = nome;
    }

    public final double getRendaAnual() {
        return this.rendaAnual;
    }

    public final void setRendaAnual(double rendaAnual) {
        this.rendaAnual = rendaAnual;
    }

    public final double getImposto() {
        return imposto;
    }

    public final void setImposto(double imposto) {
        this.imposto = imposto;
    }
    
    public abstract double calcImposto();      
}