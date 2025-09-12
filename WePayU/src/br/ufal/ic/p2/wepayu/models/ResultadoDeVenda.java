package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.utils.ValorMonetarioUtils;

public class ResultadoDeVenda {
    private String data;
    private Double valor;

    public ResultadoDeVenda() { }

    public ResultadoDeVenda(String data, String valor) {
        this.setData(data);
        // Trunca o valor para 2 casas decimais usando ValorMonetarioUtils
        this.setValor(ValorMonetarioUtils.truncarValorMonetario(valor.replace(',', '.')));
    }
    
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    public Double getValor() {
        return valor;
    }
    public void setValor(Double valor) {
        this.valor = valor;
    }
}
