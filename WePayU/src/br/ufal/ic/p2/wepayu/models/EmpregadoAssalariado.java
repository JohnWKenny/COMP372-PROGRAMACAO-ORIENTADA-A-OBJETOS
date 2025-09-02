package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoExisteException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class EmpregadoAssalariado extends Empregado {
    private String salarioMensal;

    public EmpregadoAssalariado(String nome, String endereco, String salario) throws EmpregadoNaoExisteException {
        super(nome, endereco);
        this.salarioMensal = salario;
    }

    public String getSalarioMensal() {
        return salarioMensal;
    }
    public void setSalarioMensal(String salarioMensal) {}

    @Override
    public String getTipo() {
        return "assalariado";
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
