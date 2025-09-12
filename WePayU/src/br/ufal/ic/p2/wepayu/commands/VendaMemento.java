package br.ufal.ic.p2.wepayu.commands;

import br.ufal.ic.p2.wepayu.models.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.ResultadoDeVenda;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Memento para operações de venda no sistema WePayU.
 * 
 * <p>Esta classe implementa o padrão Memento para salvar e restaurar
 * o estado das vendas de um empregado comissionado, permitindo
 * desfazer operações de lançamento de vendas.</p>
 * 
 * <p>Funcionalidades:</p>
 * <ul>
 *   <li>Backup da lista de vendas</li>
 *   <li>Restauração do estado anterior</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class VendaMemento implements Memento {
    private final String empId;
    private final Map<String, br.ufal.ic.p2.wepayu.models.Empregado> empregados;
    private final List<ResultadoDeVenda> vendasAnteriores;
    
    public VendaMemento(String empId, Map<String, br.ufal.ic.p2.wepayu.models.Empregado> empregados) {
        this.empId = empId;
        this.empregados = empregados;
        
        // Faz backup das vendas atuais
        br.ufal.ic.p2.wepayu.models.Empregado empregado = empregados.get(empId);
        if (empregado instanceof EmpregadoComissionado) {
            EmpregadoComissionado comissionado = (EmpregadoComissionado) empregado;
            this.vendasAnteriores = new ArrayList<>(comissionado.getResultadoDeVenda());
        } else {
            this.vendasAnteriores = new ArrayList<>();
        }
    }
    
    @Override
    public void restaurar() {
        br.ufal.ic.p2.wepayu.models.Empregado empregado = empregados.get(empId);
        if (empregado instanceof EmpregadoComissionado) {
            EmpregadoComissionado comissionado = (EmpregadoComissionado) empregado;
            comissionado.getResultadoDeVenda().clear();
            comissionado.getResultadoDeVenda().addAll(vendasAnteriores);
        }
    }
}
