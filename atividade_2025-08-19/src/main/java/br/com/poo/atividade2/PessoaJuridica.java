package br.com.poo.atividade2;

/**
 *
 * @author John
 */
public class PessoaJuridica extends Pessoa {
    private int numFuncionarios;

    public PessoaJuridica(String nome, double rendaAnual, int numFuncionarios) {
        this.setNome(nome);
        this.setRendaAnual(rendaAnual);
        this.setNumFuncionarios(numFuncionarios);
    }

    public final int getNumFuncionarios() {
        return numFuncionarios;
    }

    public final void setNumFuncionarios(int numFuncionarios) {
        this.numFuncionarios = numFuncionarios;
    }
    
    @Override
    public double calcImposto() {
        if(this.getNumFuncionarios() <= 10){
            this.setImposto(this.getRendaAnual() * 0.16f);
        } else {
            this.setImposto(this.getRendaAnual() * 0.14f);
        }
        
        return this.getImposto();
    }
}