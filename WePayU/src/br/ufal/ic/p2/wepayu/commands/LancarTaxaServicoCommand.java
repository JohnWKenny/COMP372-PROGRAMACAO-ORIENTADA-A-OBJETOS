package br.ufal.ic.p2.wepayu.commands;

import br.ufal.ic.p2.wepayu.models.MembroSindicato;
import br.ufal.ic.p2.wepayu.models.TaxaServico;
import br.ufal.ic.p2.wepayu.Exception.MembroSindicatoNaoEncontradoException;
import br.ufal.ic.p2.wepayu.Exception.ErroLancamentoTaxaServicoException;
import java.util.Map;

/**
 * Comando para lançar uma taxa de serviço no sistema WePayU.
 * 
 * <p>Este comando implementa o padrão Command para lançar taxas
 * de serviço para membros do sindicato, permitindo que a operação
 * seja desfeita através do sistema de undo/redo.</p>
 * 
 * <p>Funcionalidades:</p>
 * <ul>
 *   <li>Lançamento de taxas de serviço para membros do sindicato</li>
 *   <li>Validação de existência do membro</li>
 *   <li>Backup do estado anterior para desfazer</li>
 *   <li>Restauração do estado anterior ao desfazer</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class LancarTaxaServicoCommand implements Command {
    private String membroId;
    private String data;
    private String valor;
    private Map<String, MembroSindicato> membrosSindicato;
    private TaxaServicoMemento memento;
    
    public LancarTaxaServicoCommand(String membroId, String data, String valor, Map<String, MembroSindicato> membrosSindicato) {
        this.membroId = membroId;
        this.data = data;
        this.valor = valor;
        this.membrosSindicato = membrosSindicato;
    }
    
    @Override
    public void executar() {
        try {
            MembroSindicato membro = membrosSindicato.get(membroId);
            if (membro == null) {
                throw new MembroSindicatoNaoEncontradoException("Membro nao existe.");
            }
            
            // Cria o memento ANTES de fazer a alteração para capturar o estado anterior
            memento = new TaxaServicoMemento(membroId, membrosSindicato);
            
            TaxaServico taxaServico = new TaxaServico(data, valor);
            membro.getTaxasDeServicos().add(taxaServico);
        } catch (MembroSindicatoNaoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new ErroLancamentoTaxaServicoException("Erro ao lançar taxa de serviço: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void desfazer() {
        if (memento != null) {
            memento.restaurar();
        }
    }
}


