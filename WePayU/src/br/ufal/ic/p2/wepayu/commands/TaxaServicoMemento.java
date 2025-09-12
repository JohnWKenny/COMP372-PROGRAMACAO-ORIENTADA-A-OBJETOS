package br.ufal.ic.p2.wepayu.commands;

import br.ufal.ic.p2.wepayu.models.MembroSindicato;
import br.ufal.ic.p2.wepayu.models.TaxaServico;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Memento para operações de taxa de serviço no sistema WePayU.
 * 
 * <p>Esta classe implementa o padrão Memento para salvar e restaurar
 * o estado das taxas de serviço de um membro do sindicato, permitindo
 * desfazer operações de lançamento de taxas.</p>
 * 
 * <p>Funcionalidades:</p>
 * <ul>
 *   <li>Backup da lista de taxas de serviço</li>
 *   <li>Restauração do estado anterior</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class TaxaServicoMemento implements Memento {
    private final String membroId;
    private final Map<String, MembroSindicato> membrosSindicato;
    private final List<TaxaServico> taxasAnteriores;
    
    public TaxaServicoMemento(String membroId, Map<String, MembroSindicato> membrosSindicato) {
        this.membroId = membroId;
        this.membrosSindicato = membrosSindicato;
        
        // Faz backup das taxas atuais
        MembroSindicato membro = membrosSindicato.get(membroId);
        if (membro != null) {
            this.taxasAnteriores = new ArrayList<>(membro.getTaxasDeServicos());
        } else {
            this.taxasAnteriores = new ArrayList<>();
        }
    }
    
    @Override
    public void restaurar() {
        MembroSindicato membro = membrosSindicato.get(membroId);
        if (membro != null) {
            membro.getTaxasDeServicos().clear();
            membro.getTaxasDeServicos().addAll(taxasAnteriores);
        }
    }
}
