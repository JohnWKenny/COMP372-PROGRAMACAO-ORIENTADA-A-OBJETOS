package br.ufal.ic.p2.wepayu.commands;

import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.EmpregadoHorista;
import br.ufal.ic.p2.wepayu.models.CartaoDePonto;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEncontradoException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhHoristaException;
import br.ufal.ic.p2.wepayu.Exception.ErroLancamentoCartaoException;
import java.util.Map;

/**
 * Comando para lançar um cartão de ponto no sistema WePayU.
 * 
 * <p>Este comando implementa o padrão Command para lançar cartões
 * de ponto para empregados horistas, permitindo que a operação
 * seja desfeita através do sistema de undo/redo.</p>
 * 
 * <p>Funcionalidades:</p>
 * <ul>
 *   <li>Lançamento de cartões de ponto para empregados horistas</li>
 *   <li>Validação de tipo de empregado</li>
 *   <li>Backup do estado anterior para desfazer</li>
 *   <li>Restauração do estado anterior ao desfazer</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class LancarCartaoCommand implements Command {
    private String empId;
    private String data;
    private String horas;
    private Map<String, Empregado> empregados;
    private CartaoMemento memento;
    
    public LancarCartaoCommand(String empId, String data, String horas, Map<String, Empregado> empregados) {
        this.empId = empId;
        this.data = data;
        this.horas = horas;
        this.empregados = empregados;
    }
    
    @Override
    public void executar() {
        try {
            Empregado empregado = empregados.get(empId);
            if (empregado == null) {
                throw new EmpregadoNaoEncontradoException("Empregado não encontrado.");
            }
            
            if (!(empregado instanceof EmpregadoHorista)) {
                throw new EmpregadoNaoEhHoristaException("Empregado nao eh horista.");
            }
            
            EmpregadoHorista horista = (EmpregadoHorista) empregado;
            
            // Cria o memento ANTES de fazer a alteração para capturar o estado anterior
            memento = new CartaoMemento(empId, empregados);
            
            CartaoDePonto cartao = new CartaoDePonto(data, horas);
            horista.getCartoes().add(cartao);
        } catch (EmpregadoNaoEncontradoException | EmpregadoNaoEhHoristaException e) {
            throw e;
        } catch (Exception e) {
            throw new ErroLancamentoCartaoException("Erro ao lançar cartão: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void desfazer() {
        if (memento != null) {
            memento.restaurar();
        }
    }
}


