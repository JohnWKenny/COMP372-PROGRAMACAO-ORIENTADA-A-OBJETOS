package br.ufal.ic.p2.wepayu.models;

public class Banco extends MetodoPagamento {
    private String banco;
    private String agencia;
    private String contaCorrente;

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
    
}
