package br.ufal.ic.p2.wepayu.commands;

import br.ufal.ic.p2.wepayu.models.EmpregadoHorista;
import br.ufal.ic.p2.wepayu.models.CartaoDePonto;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Memento para operações de cartão de ponto no sistema WePayU.
 * 
 * <p>Esta classe implementa o padrão Memento para salvar e restaurar
 * o estado dos cartões de ponto de um empregado horista, permitindo
 * desfazer operações de lançamento de cartões.</p>
 * 
 * <p>Funcionalidades:</p>
 * <ul>
 *   <li>Backup da lista de cartões de ponto</li>
 *   <li>Restauração do estado anterior</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class CartaoMemento implements Memento {
    private final String empId;
    private final Map<String, br.ufal.ic.p2.wepayu.models.Empregado> empregados;
    private final List<CartaoDePonto> cartoesAnteriores;
    
    /**
     * Construtor do memento de cartão de ponto.
     * 
     * @param empId ID do empregado horista
     * @param empregados Mapa de empregados do sistema
     */
    public CartaoMemento(String empId, Map<String, br.ufal.ic.p2.wepayu.models.Empregado> empregados) {
        this.empId = empId;
        this.empregados = empregados;
        
        // Faz backup dos cartões atuais
        br.ufal.ic.p2.wepayu.models.Empregado empregado = empregados.get(empId);
        if (empregado instanceof EmpregadoHorista) {
            EmpregadoHorista horista = (EmpregadoHorista) empregado;
            this.cartoesAnteriores = new ArrayList<>(horista.getCartoes());
        } else {
            this.cartoesAnteriores = new ArrayList<>();
        }
    }
    
    /**
     * Restaura o estado anterior dos cartões de ponto.
     * 
     * <p>Este método restaura a lista de cartões de ponto do empregado
     * horista para o estado anterior ao lançamento do cartão.</p>
     */
    @Override
    public void restaurar() {
        br.ufal.ic.p2.wepayu.models.Empregado empregado = empregados.get(empId);
        if (empregado instanceof EmpregadoHorista) {
            EmpregadoHorista horista = (EmpregadoHorista) empregado;
            horista.getCartoes().clear();
            horista.getCartoes().addAll(cartoesAnteriores);
        }
    }
}
