package br.ufal.ic.p2.wepayu.commands;

import br.ufal.ic.p2.wepayu.models.MembroSindicato;
import br.ufal.ic.p2.wepayu.Exception.ErroCriacaoMembroSindicatoException;
import java.util.Map;

/**
 * Comando para criar um novo membro do sindicato no sistema WePayU.
 * 
 * <p>Este comando implementa o padrão Command para criar membros
 * do sindicato, permitindo que a operação seja desfeita através
 * do sistema de undo/redo.</p>
 * 
 * <p>Funcionalidades:</p>
 * <ul>
 *   <li>Criação de membros do sindicato</li>
 *   <li>Armazenamento do ID do membro para desfazer</li>
 *   <li>Remoção do membro ao desfazer</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class CriarMembroSindicatoCommand implements Command {
    private MembroSindicato membro;
    private Map<String, MembroSindicato> membrosSindicato;
    
    public CriarMembroSindicatoCommand(MembroSindicato membro, Map<String, MembroSindicato> membrosSindicato) {
        this.membro = membro;
        this.membrosSindicato = membrosSindicato;
    }
    
    @Override
    public void executar() {
        try {
            membrosSindicato.put(membro.getIdMembro(), membro);
        } catch (Exception e) {
            throw new ErroCriacaoMembroSindicatoException("Erro ao criar membro do sindicato: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void desfazer() {
        membrosSindicato.remove(membro.getIdMembro());
    }
}


