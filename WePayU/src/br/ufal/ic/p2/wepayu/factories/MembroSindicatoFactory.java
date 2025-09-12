package br.ufal.ic.p2.wepayu.factories;

import br.ufal.ic.p2.wepayu.Exception.*;
import br.ufal.ic.p2.wepayu.models.MembroSindicato;
import br.ufal.ic.p2.wepayu.utils.ValorMonetarioUtils;

/**
 * Factory para criação de membros do sindicato no sistema WePayU.
 * 
 * <p>Esta classe implementa o padrão Factory para criar instâncias
 * de membros do sindicato com validações apropriadas dos dados
 * fornecidos.</p>
 * 
 * <p>Funcionalidades:</p>
 * <ul>
 *   <li>Criação de membros do sindicato</li>
 *   <li>Validação de dados de entrada</li>
 *   <li>Tratamento de valores monetários</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class MembroSindicatoFactory {
    
    /**
     * Cria um novo membro do sindicato
     * @param idMembro ID do membro
     * @param taxaSindical Taxa sindical como String
     * @return MembroSindicato criado
     * @throws IdentificacaoSindicatoNaoPodeSerNulaException se ID for nulo
     * @throws TaxaSindicalNaoPodeSerNulaException se taxa for nula
     * @throws ValorDeveSerNumericoException se taxa não for numérica
     * @throws ValorDeveSerNaoNegativoException se taxa for negativa
     */
    public static MembroSindicato criarMembro(String idMembro, String taxaSindical) 
            throws IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, ValorDeveSerNumericoException, 
                   ValorDeveSerNaoNegativoException {
        
        // Validações de ID
        if (idMembro == null || idMembro.isBlank()) {
            throw new IdentificacaoSindicatoNaoPodeSerNulaException("Identificacao do membro nao pode ser nula.");
        }
        
        // Validações de taxa sindical
        if (taxaSindical == null || taxaSindical.isBlank()) {
            throw new TaxaSindicalNaoPodeSerNulaException("Taxa sindical nao pode ser nula.");
        }
        
        double taxaNumerica;
        try {
            taxaNumerica = Double.parseDouble(taxaSindical.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new ValorDeveSerNumericoException("Taxa sindical deve ser numerica.");
        }
        
        if (taxaNumerica < 0) {
            throw new ValorDeveSerNaoNegativoException("Taxa sindical deve ser nao-negativa.");
        }
        
        // Trunca a taxa para 2 casas decimais
        taxaNumerica = ValorMonetarioUtils.truncarValorMonetario(taxaNumerica);
        
        return new MembroSindicato(idMembro, String.valueOf(taxaNumerica));
    }
    
    /**
     * Cria um novo membro do sindicato com taxa como double
     * @param idMembro ID do membro
     * @param taxaSindical Taxa sindical como double
     * @return MembroSindicato criado
     * @throws IdentificacaoSindicatoNaoPodeSerNulaException se ID for nulo
     * @throws ValorDeveSerNaoNegativoException se taxa for negativa
     */
    public static MembroSindicato criarMembro(String idMembro, double taxaSindical) 
            throws IdentificacaoSindicatoNaoPodeSerNulaException, ValorDeveSerNaoNegativoException {
        
        // Validações de ID
        if (idMembro == null || idMembro.isBlank()) {
            throw new IdentificacaoSindicatoNaoPodeSerNulaException("Identificacao do membro nao pode ser nula.");
        }
        
        if (taxaSindical < 0) {
            throw new ValorDeveSerNaoNegativoException("Taxa sindical deve ser nao-negativa.");
        }
        
        // Trunca a taxa para 2 casas decimais
        taxaSindical = ValorMonetarioUtils.truncarValorMonetario(taxaSindical);
        
        return new MembroSindicato(idMembro, String.valueOf(taxaSindical));
    }
}