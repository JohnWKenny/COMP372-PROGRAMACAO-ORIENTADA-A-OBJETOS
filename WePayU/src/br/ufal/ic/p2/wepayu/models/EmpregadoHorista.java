package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoExisteException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class EmpregadoHorista extends Empregado {
    private String salarioPorHora;

    public EmpregadoHorista(String nome, String endereco, String salario) throws EmpregadoNaoExisteException {
        super(nome, endereco);
        this.salarioPorHora = salario;
    }

    public String getSalarioPorHora() {
        return salarioPorHora;
    }

    public void setSalarioPorHora(String salarioPorHora) {
        this.salarioPorHora = salarioPorHora;
    }
    @Override
    public String getTipo() {
        return "horista";
    }
    @Override
    public String getSalario() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("0.00", symbols);

        String salarioComVirgula = this.salarioPorHora;

        String salarioComPonto = salarioComVirgula.replace(',', '.');

        double valorNumerico = Double.parseDouble(salarioComPonto);

        return df.format(valorNumerico);
    }
}
