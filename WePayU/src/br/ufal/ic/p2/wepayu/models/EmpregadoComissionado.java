package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoExisteException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class EmpregadoComissionado extends Empregado {
    private String salarioMensal;
    private String taxaDeComissao;
    private ResultadoDeVenda resultadoDeVenda= new ResultadoDeVenda();

    public EmpregadoComissionado(String nome, String endereco, String salario, String taxaDeComissao) throws EmpregadoNaoExisteException {
        super(nome, endereco);
        this.salarioMensal = salario;
        this.taxaDeComissao = taxaDeComissao;
    }

    public String getSalarioMensal() {
        return salarioMensal;
    }

    public void setSalarioMensal(String salarioMensal) {
        this.salarioMensal = salarioMensal;
    }

    @Override
    public String getTaxaDeComissao() {
        return taxaDeComissao;
    }

    public void setTaxaDeComissao(String taxaDeComissao) {
        this.taxaDeComissao = taxaDeComissao;
    }

    @Override
    public String getTipo() {
        return "comissionado";
    }

    @Override
    public String getSalario() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("0.00", symbols);

        String salarioComVirgula = this.salarioMensal;

        String salarioComPonto = salarioComVirgula.replace(',', '.');

        double valorNumerico = Double.parseDouble(salarioComPonto);

        return df.format(valorNumerico);
    }
}
