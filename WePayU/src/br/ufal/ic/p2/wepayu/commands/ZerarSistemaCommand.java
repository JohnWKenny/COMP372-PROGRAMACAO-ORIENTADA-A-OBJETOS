package br.ufal.ic.p2.wepayu.commands;

import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.MembroSindicato;
import java.util.Map;
import java.util.HashMap;

/**
 * Comando para zerar todo o sistema WePayU.
 * 
 * <p>Este comando implementa o padrão Command para limpar todos os dados
 * do sistema, incluindo empregados e membros do sindicato, permitindo
 * que a operação seja desfeita através do sistema de undo/redo.</p>
 * 
 * <p>Funcionalidades:</p>
 * <ul>
 *   <li>Limpeza de todos os empregados do sistema</li>
 *   <li>Limpeza de todos os membros do sindicato</li>
 *   <li>Backup dos dados para permitir desfazer</li>
 *   <li>Restauração completa dos dados ao desfazer</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class ZerarSistemaCommand implements Command {
    private Map<String, Empregado> empregados;
    private Map<String, MembroSindicato> membrosSindicato;
    private Map<String, Empregado> empregadosBackup;
    private Map<String, MembroSindicato> membrosSindicatoBackup;
    
    /**
     * Construtor do comando de zerar sistema.
     * 
     * @param empregados Mapa de empregados do sistema
     * @param membrosSindicato Mapa de membros do sindicato
     */
    public ZerarSistemaCommand(Map<String, Empregado> empregados, Map<String, MembroSindicato> membrosSindicato) {
        this.empregados = empregados;
        this.membrosSindicato = membrosSindicato;
    }
    
    /**
     * Executa a limpeza do sistema.
     * 
     * <p>Este método faz backup de todos os dados atuais e depois
     * limpa completamente os mapas de empregados e membros do sindicato.</p>
     */
    @Override
    public void executar() {
        // Faz backup dos dados atuais
        empregadosBackup = new HashMap<>(empregados);
        membrosSindicatoBackup = new HashMap<>(membrosSindicato);
        
        // Limpa os dados
        empregados.clear();
        membrosSindicato.clear();
        br.ufal.ic.p2.wepayu.models.AgendaDePagamentos.limparAgendasCustomizadas();
    }
    
    /**
     * Desfaz a limpeza do sistema.
     * 
     * <p>Este método restaura todos os dados do backup criado
     * durante a execução, retornando o sistema ao estado anterior.</p>
     */
    @Override
    public void desfazer() {
        // Restaura os dados do backup
        empregados.clear();
        empregados.putAll(empregadosBackup);
        
        membrosSindicato.clear();
        membrosSindicato.putAll(membrosSindicatoBackup);
    }
}
