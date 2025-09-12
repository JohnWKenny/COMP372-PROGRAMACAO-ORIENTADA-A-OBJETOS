package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.Exception.*;

/**
 * Interface para operações de lançamento de atividades no sistema WePayU.
 * 
 * <p>Esta interface define os contratos para operações relacionadas
 * ao lançamento de atividades dos empregados, incluindo cartões de ponto,
 * vendas e consulta de informações de lançamentos.</p>
 * 
 * <p>Funcionalidades principais:</p>
 * <ul>
 *   <li>Lançamento de cartões de ponto para empregados horistas</li>
 *   <li>Lançamento de vendas para empregados comissionados</li>
 *   <li>Consulta de horas trabalhadas</li>
 *   <li>Consulta de vendas realizadas</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public interface LancamentoService {
    void lancaCartao(String emp, String data, String horas) 
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException, DataInvalidaException, 
                   EmpregadoNaoEhTipoEsperadoException, ValorDeveSerPositivoException;
    
    void lancaVenda(String emp, String data, String valor) 
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, DataNaoPodeSerNulaException, ValorNaoPodeSerNuloException, DataInvalidaException, 
                   ValorMonetarioInvalidoException, EmpregadoNaoEhTipoEsperadoException, 
                   ValorDeveSerPositivoException;
    
    String getHorasNormaisTrabalhadas(String emp, String dataInicial, String dataFinal) 
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, DataNaoPodeSerNulaException, DataInvalidaException,
                   EmpregadoNaoEhTipoEsperadoException, DataInicialPosteriorDataFinalException;
    
    String getHorasExtrasTrabalhadas(String emp, String dataInicial, String dataFinal) 
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, DataNaoPodeSerNulaException, DataInvalidaException,
                   EmpregadoNaoEhTipoEsperadoException, DataInicialPosteriorDataFinalException;
    
    String getVendasRealizadas(String emp, String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, DataNaoPodeSerNulaException, DataInvalidaException,
                   EmpregadoNaoEhTipoEsperadoException, DataInicialPosteriorDataFinalException;
}
