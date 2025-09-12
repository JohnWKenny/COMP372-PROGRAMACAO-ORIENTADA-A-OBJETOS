package br.ufal.ic.p2.wepayu.factories;

import br.ufal.ic.p2.wepayu.Exception.*;
import br.ufal.ic.p2.wepayu.models.*;
import br.ufal.ic.p2.wepayu.utils.ValorMonetarioUtils;

/**
 * Factory para criação de empregados no sistema WePayU.
 * 
 * <p>Esta classe implementa o padrão Factory para criar instâncias
 * de diferentes tipos de empregados (assalariados, horistas, comissionados)
 * com validações apropriadas para cada tipo.</p>
 * 
 * <p>Tipos de empregados suportados:</p>
 * <ul>
 *   <li><strong>assalariado:</strong> Empregado com salário fixo mensal</li>
 *   <li><strong>horista:</strong> Empregado que recebe por horas trabalhadas</li>
 *   <li><strong>comissionado:</strong> Empregado que recebe comissão sobre vendas</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class EmpregadoFactory {
    
    public static Empregado criarEmpregado(String tipo, String nome, String endereco, String salario) 
            throws TipoInvalidoException, SalarioNaoPodeSerNuloException, SalarioDeveSerNumericoException, 
                   SalarioDeveSerNaoNegativoException {
        
        // Validações de salário
        if (salario == null || salario.isBlank()) {
            throw new SalarioNaoPodeSerNuloException("Salario nao pode ser nulo.");
        }
        
        double salarioNumerico;
        try {
            salarioNumerico = Double.parseDouble(salario.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new SalarioDeveSerNumericoException("Salario deve ser numerico.");
        }
        
        if (salarioNumerico < 0) {
            throw new SalarioDeveSerNaoNegativoException("Salario deve ser nao-negativo.");
        }
        
        // Trunca o salário para 2 casas decimais
        salarioNumerico = ValorMonetarioUtils.truncarValorMonetario(salarioNumerico);
        
        return switch (tipo) {
            case "assalariado" -> new EmpregadoAssalariado(nome, endereco, salarioNumerico);
            case "horista" -> new EmpregadoHorista(nome, endereco, salarioNumerico);
            case "comissionado" -> throw new TipoInvalidoException("Tipo nao aplicavel.");
            default -> throw new TipoInvalidoException("Tipo invalido.");
        };
    }
    
    public static Empregado criarEmpregado(String tipo, String nome, String endereco, String salario, String comissao) 
            throws TipoInvalidoException, SalarioNaoPodeSerNuloException, SalarioDeveSerNumericoException, 
                   SalarioDeveSerNaoNegativoException, ComissaoNaoPodeSerNulaException, 
                   ComissaoDeveSerNumericaException, ComissaoDeveSerNaoNegativaException {
        
        // Validações de salário
        if (salario == null || salario.isBlank()) {
            throw new SalarioNaoPodeSerNuloException("Salario nao pode ser nulo.");
        }
        
        double salarioNumerico;
        try {
            salarioNumerico = Double.parseDouble(salario.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new SalarioDeveSerNumericoException("Salario deve ser numerico.");
        }
        
        if (salarioNumerico < 0) {
            throw new SalarioDeveSerNaoNegativoException("Salario deve ser nao-negativo.");
        }
        
        // Validações de comissão
        if (comissao == null || comissao.isBlank()) {
            throw new ComissaoNaoPodeSerNulaException("Comissao nao pode ser nula.");
        }
        
        double comissaoNumerica;
        try {
            comissaoNumerica = Double.parseDouble(comissao.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new ComissaoDeveSerNumericaException("Comissao deve ser numerica.");
        }
        
        if (comissaoNumerica < 0) {
            throw new ComissaoDeveSerNaoNegativaException("Comissao deve ser nao-negativa.");
        }
        
        // Trunca os valores para 2 casas decimais
        salarioNumerico = ValorMonetarioUtils.truncarValorMonetario(salarioNumerico);
        comissaoNumerica = ValorMonetarioUtils.truncarValorMonetario(comissaoNumerica);
        
        return switch (tipo) {
            case "assalariado", "horista" -> throw new TipoInvalidoException("Tipo nao aplicavel.");
            case "comissionado" -> new EmpregadoComissionado(nome, endereco, salarioNumerico, comissaoNumerica);
            default -> throw new TipoInvalidoException("Tipo invalido.");
        };
    }
}