package br.com.poo.atividade2;

/**
 *
 * @author John
 */
public class PessoaFisica extends Pessoa {
    private double gastoSaude;
    
    public PessoaFisica(String nome, double rendaAnual, double gastoSaude){
        this.setNome(nome);
        this.setRendaAnual(rendaAnual);
        this.setGastoSaude(gastoSaude);
    }
    
    public final double getGastoSaude() {
        return gastoSaude;
    }

    public final void setGastoSaude(double gastoSaude) {
        this.gastoSaude = gastoSaude;
    }

    @Override
    public double calcImposto() {
        if(this.getRendaAnual() < 20000){
            this.setImposto((this.getRendaAnual() * 0.15f) - 
                            (this.getGastoSaude() * 0.50f));
        } else {
            this.setImposto((this.getRendaAnual() * 0.25f) -
                            (this.getGastoSaude() * 0.50f));
        }
        
        return this.getImposto();
    }  
}