package br.ufal.ic.p2.wepayu.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Classe utilitária para operações com valores monetários no sistema WePayU.
 * 
 * <p>Esta classe fornece métodos estáticos para manipulação de valores monetários,
 * incluindo truncamento, formatação e conversão entre diferentes tipos de dados.
 * Todos os valores monetários são tratados com 2 casas decimais e vírgula
 * como separador decimal, seguindo o padrão brasileiro.</p>
 * 
 * <p>Funcionalidades principais:</p>
 * <ul>
 *   <li>Truncamento de valores para 2 casas decimais (sem arredondamento)</li>
 *   <li>Formatação para exibição com vírgula como separador decimal</li>
 *   <li>Conversão entre diferentes tipos numéricos</li>
 *   <li>Tratamento de valores nulos e inválidos</li>
 * </ul>
 * 
 * <p>Exemplo de uso:</p>
 * <pre>
 * // Truncar valor monetário
 * double valor = ValorMonetarioUtils.truncarValorMonetario(123.456);
 * // resultado: 123.45
 * 
 * // Formatar para exibição
 * String formatado = ValorMonetarioUtils.formatarValorMonetario(123.45);
 * // resultado: "123,45"
 * </pre>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class ValorMonetarioUtils {
    
    /**
     * Trunca um valor monetário para 2 casas decimais sem arredondamento.
     * 
     * <p>Este método converte um valor double para BigDecimal, aplica
     * truncamento para 2 casas decimais usando RoundingMode.DOWN,
     * e retorna o resultado como double.</p>
     * 
     * @param valor O valor a ser truncado
     * @return O valor truncado para 2 casas decimais
     */
    public static double truncarValorMonetario(double valor) {
        BigDecimal bd = BigDecimal.valueOf(valor);
        bd = bd.setScale(2, RoundingMode.DOWN);
        return bd.doubleValue();
    }
    
    /**
     * Trunca um valor monetário para 2 casas decimais a partir de uma String.
     * 
     * <p>Este método aceita valores monetários em formato String, que podem
     * conter vírgula como separador decimal. Valores nulos ou inválidos
     * retornam 0.0.</p>
     * 
     * @param valorStr O valor como String (pode conter vírgula como separador decimal)
     * @return O valor truncado para 2 casas decimais
     */
    public static double truncarValorMonetario(String valorStr) {
        if (valorStr == null || valorStr.isBlank()) {
            return 0.0;
        }
        
        // Substitui vírgula por ponto para parsing
        String valorNormalizado = valorStr.replace(",", ".");
        
        try {
            double valor = Double.parseDouble(valorNormalizado);
            return truncarValorMonetario(valor);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    /**
     * Formata um valor monetário truncado para exibição com vírgula como separador decimal.
     * 
     * <p>Este método converte um valor double para formato de exibição brasileiro,
     * sempre mostrando 2 casas decimais e usando vírgula como separador decimal.</p>
     * 
     * @param valor O valor a ser formatado
     * @return O valor formatado como String (ex: "123,45")
     */
    public static String formatarValorMonetario(double valor) {
        double valorTruncado = truncarValorMonetario(valor);
        
        // Sempre mostra 2 casas decimais para valores monetários
        return String.format("%.2f", valorTruncado).replace(".", ",");
    }
    
    /**
     * Formata um valor monetário truncado para exibição a partir de uma String.
     * 
     * <p>Este método aceita valores monetários em formato String e os converte
     * para formato de exibição brasileiro com vírgula como separador decimal.</p>
     * 
     * @param valorStr O valor como String
     * @return O valor formatado como String (ex: "123,45")
     */
    public static String formatarValorMonetario(String valorStr) {
        double valor = truncarValorMonetario(valorStr);
        return formatarValorMonetario(valor);
    }
    
    /**
     * Trunca um valor monetário BigDecimal para 2 casas decimais sem arredondamento.
     * 
     * <p>Este método aplica truncamento para 2 casas decimais usando RoundingMode.DOWN
     * em um valor BigDecimal. Valores nulos retornam BigDecimal.ZERO.</p>
     * 
     * @param valor O valor BigDecimal a ser truncado
     * @return O valor BigDecimal truncado para 2 casas decimais
     */
    public static BigDecimal truncarValorMonetario(BigDecimal valor) {
        if (valor == null) {
            return BigDecimal.ZERO;
        }
        return valor.setScale(2, RoundingMode.DOWN);
    }
    
    /**
     * Formata um valor monetário BigDecimal truncado para exibição com vírgula como separador decimal.
     * 
     * <p>Este método converte um valor BigDecimal para formato de exibição brasileiro,
     * sempre mostrando 2 casas decimais e usando vírgula como separador decimal.
     * Valores nulos retornam "0,00".</p>
     * 
     * @param valor O valor BigDecimal a ser formatado
     * @return O valor formatado como String (ex: "123,45")
     */
    public static String formatarValorMonetario(BigDecimal valor) {
        if (valor == null) {
            return "0,00";
        }
        
        BigDecimal valorTruncado = truncarValorMonetario(valor);
        
        // Sempre mostra 2 casas decimais para valores monetários
        return String.format("%.2f", valorTruncado).replace(".", ",");
    }
    
    /**
     * Converte um BigDecimal para double truncado para 2 casas decimais.
     * 
     * <p>Este método converte um valor BigDecimal para double, aplicando
     * truncamento para 2 casas decimais. Valores nulos retornam 0.0.</p>
     * 
     * @param valor O valor BigDecimal a ser convertido
     * @return O valor como double truncado para 2 casas decimais
     */
    public static double converterParaDouble(BigDecimal valor) {
        if (valor == null) {
            return 0.0;
        }
        return truncarValorMonetario(valor).doubleValue();
    }
}
