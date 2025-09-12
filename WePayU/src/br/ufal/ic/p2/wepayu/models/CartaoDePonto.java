package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.utils.ValorMonetarioUtils;

/**
 * Classe que representa um cartão de ponto de um empregado horista.
 * 
 * <p>Esta classe armazena as informações de um cartão de ponto,
 * incluindo a data e o número de horas trabalhadas. As horas
 * são automaticamente truncadas para 2 casas decimais.</p>
 * 
 * <p>Informações armazenadas:</p>
 * <ul>
 *   <li>Data do cartão de ponto</li>
 *   <li>Número de horas trabalhadas</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class CartaoDePonto {
    private String data;
    private Double horas;

    public CartaoDePonto() {}

    public CartaoDePonto(String data, String horas) {
        this.data = data;
        // Trunca as horas para 2 casas decimais usando ValorMonetarioUtils
        this.horas = ValorMonetarioUtils.truncarValorMonetario(horas.replace(',', '.'));
    }

    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    public Double getHoras() {
        return horas;
    }
    public void setHoras(Double horas) {
        this.horas = horas;
    }
}
