package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.models.MembroSindicato;
import br.ufal.ic.p2.wepayu.Exception.*;

/**
 * Interface para operações relacionadas ao sindicato no sistema WePayU.
 * 
 * <p>Esta interface define os contratos para operações relacionadas
 * ao sindicato, incluindo criação de membros, lançamento de taxas
 * de serviço e consulta de informações sindicais.</p>
 * 
 * <p>Funcionalidades principais:</p>
 * <ul>
 *   <li>Criação e remoção de membros do sindicato</li>
 *   <li>Lançamento de taxas de serviço</li>
 *   <li>Consulta de informações de membros</li>
 *   <li>Consulta de taxas de serviço</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public interface SindicatoService {
    MembroSindicato criarMembro(String id, String taxa) 
            throws IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, ValorMonetarioInvalidoException, 
                   ValorDeveSerNumericoException, ValorDeveSerNaoNegativoException;
    
    void removerMembro(String id) 
            throws IdentificacaoSindicatoNaoPodeSerNulaException, MembroSindicatoNaoEncontradoException;
    
    MembroSindicato getMembro(String id) 
            throws IdentificacaoSindicatoNaoPodeSerNulaException, MembroSindicatoNaoEncontradoException;
    
    void lancaTaxaServico(String membro, String data, String valor) 
            throws IdentificacaoSindicatoNaoPodeSerNulaException, DataNaoPodeSerNulaException, ValorNaoPodeSerNuloException, DataInvalidaException, ValorMonetarioInvalidoException, 
                   MembroSindicatoNaoEncontradoException, ValorDeveSerPositivoException;
    
    String getTaxasServico(String empregado, String dataInicial, String dataFinal) 
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, DataNaoPodeSerNulaException, DataInvalidaException,
                   EmpregadoNaoEhSindicalizadoException, DataInicialPosteriorDataFinalException;
}
