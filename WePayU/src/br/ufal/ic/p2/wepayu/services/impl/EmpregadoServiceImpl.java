package br.ufal.ic.p2.wepayu.services.impl;

import br.ufal.ic.p2.wepayu.services.EmpregadoService;
import br.ufal.ic.p2.wepayu.models.*;
import br.ufal.ic.p2.wepayu.factories.EmpregadoFactory;
import br.ufal.ic.p2.wepayu.commands.*;
import br.ufal.ic.p2.wepayu.commands.CommandManagerInterface;
import br.ufal.ic.p2.wepayu.Exception.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Implementação do serviço de empregados no sistema WePayU.
 * 
 * <p>Esta classe implementa todas as operações relacionadas ao gerenciamento
 * de empregados, incluindo criação, alteração, remoção e consulta de informações.
 * Utiliza o padrão Command para operações que podem ser desfeitas.</p>
 * 
 * <p>Funcionalidades implementadas:</p>
 * <ul>
 *   <li>Criação de empregados (assalariados, horistas, comissionados)</li>
 *   <li>Alteração de atributos de empregados</li>
 *   <li>Remoção de empregados</li>
 *   <li>Consulta de informações de empregados</li>
 *   <li>Integração com sistema de sindicato</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class EmpregadoServiceImpl implements EmpregadoService {
    
    private Map<String, Empregado> empregados;
    private Map<String, MembroSindicato> membrosSindicato;
    private int id;
    private CommandManagerInterface commandManager;
    
    public EmpregadoServiceImpl(Map<String, Empregado> empregados, 
                               Map<String, MembroSindicato> membrosSindicato,
                               int id, 
                               CommandManagerInterface commandManager) {
        this.empregados = empregados;
        this.membrosSindicato = membrosSindicato;
        this.id = id;
        this.commandManager = commandManager;
    }
    
    @Override
    public String criarEmpregado(String nome, String endereco, String tipo, String salario) 
            throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, TipoInvalidoException, SalarioNaoPodeSerNuloException, 
                   SalarioDeveSerNumericoException, SalarioDeveSerNaoNegativoException {
        
        if(nome == null || nome.isBlank()) throw new NomeNaoPodeSerNuloException("Nome nao pode ser nulo.");
        if(endereco == null || endereco.isBlank()) throw new EnderecoNaoPodeSerNuloException("Endereco nao pode ser nulo.");
        if(tipo == null || tipo.isBlank()) throw new TipoNaoPodeSerNuloException("Tipo nao pode ser nulo.");

        Empregado empregado = EmpregadoFactory.criarEmpregado(tipo, nome, endereco, salario);
        String idEmpregado = String.valueOf(id++);
        empregado.setId(idEmpregado);

        CriarEmpregadoCommand command = new CriarEmpregadoCommand(empregado, empregados);
        commandManager.executar(command);

        return idEmpregado;
    }
    
    @Override
    public String criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao) 
            throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, TipoInvalidoException, SalarioNaoPodeSerNuloException, 
                   SalarioDeveSerNumericoException, SalarioDeveSerNaoNegativoException, 
                   ComissaoNaoPodeSerNulaException, ComissaoDeveSerNumericaException, 
                   ComissaoDeveSerNaoNegativaException {
        
        if(nome == null || nome.isBlank()) throw new NomeNaoPodeSerNuloException("Nome nao pode ser nulo.");
        if(endereco == null || endereco.isBlank()) throw new EnderecoNaoPodeSerNuloException("Endereco nao pode ser nulo.");
        if(tipo == null || tipo.isBlank()) throw new TipoNaoPodeSerNuloException("Tipo nao pode ser nulo.");

        Empregado empregado = EmpregadoFactory.criarEmpregado(tipo, nome, endereco, salario, comissao);
        String idEmpregado = String.valueOf(id++);
        empregado.setId(idEmpregado);

        CriarEmpregadoCommand command = new CriarEmpregadoCommand(empregado, empregados);
        commandManager.executar(command);

        return idEmpregado;
    }
    
    @Override
    public void alteraEmpregado(String emp, String atributo, String valor) 
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, TipoInvalidoException, 
                   AtributoNaoExisteException, ValorDeveSerNumericoException, ValorDeveSerNaoNegativoException,
                   MetodoPagamentoInvalidoException, ValorDeveSerTrueOuFalseException, 
                   IdentificacaoSindicatoJaExisteException {
        
        if(emp == null || emp.isBlank()) throw new IdentificacaoEmpregadoNaoPodeSerNulaException("Identificacao do empregado nao pode ser nula.");
        if(!empregados.containsKey(emp)) throw new EmpregadoNaoExisteException("Empregado nao existe.");
        
        Map<String, String> valores = Map.of("valor", valor);
        AlterarEmpregadoCommand command = new AlterarEmpregadoCommand(emp, atributo, valores, empregados, membrosSindicato);
        commandManager.executar(command);
    }
    
    @Override
    public void alteraEmpregado(String emp, String atributo, String valor, String comissao_salario) 
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, TipoInvalidoException, 
                   AtributoNaoExisteException, ValorDeveSerNumericoException, ValorDeveSerNaoNegativoException,
                   MetodoPagamentoInvalidoException, ValorDeveSerTrueOuFalseException, 
                   IdentificacaoSindicatoJaExisteException {
        
        if(emp == null || emp.isBlank()) throw new IdentificacaoEmpregadoNaoPodeSerNulaException("Identificacao do empregado nao pode ser nula.");
        if(!empregados.containsKey(emp)) throw new EmpregadoNaoExisteException("Empregado nao existe.");
        
        Map<String, String> valores;
        if ("tipo".equals(atributo)) {
            // Para alteração de tipo, verifica se é comissionado
            if ("comissionado".equals(valor)) {
                // Para comissionado, o parâmetro adicional é comissão
                valores = Map.of("valor", valor, "comissao", comissao_salario);
            } else {
                // Para outros tipos, o parâmetro adicional é salário
                valores = Map.of("valor", valor, "salario", comissao_salario);
            }
        } else if ("comissao".equals(atributo)) {
            // Para alteração de comissão, o parâmetro adicional é comissão
            valores = Map.of("valor", valor, "comissao", comissao_salario);
        } else {
            // Para outros casos, usa como valor simples
            valores = Map.of("valor", valor);
        }
        
        AlterarEmpregadoCommand command = new AlterarEmpregadoCommand(emp, atributo, valores, empregados, membrosSindicato);
        commandManager.executar(command);
    }
    
    @Override
    public void alteraEmpregado(String emp, String atributo, String valor1, String banco, String agencia, String contaCorrente) 
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, TipoInvalidoException, 
                   AtributoNaoExisteException, ValorDeveSerNumericoException, ValorDeveSerNaoNegativoException,
                   MetodoPagamentoInvalidoException, ValorDeveSerTrueOuFalseException, 
                   IdentificacaoSindicatoJaExisteException {
        
        if(emp == null || emp.isBlank()) throw new IdentificacaoEmpregadoNaoPodeSerNulaException("Identificacao do empregado nao pode ser nula.");
        if(!empregados.containsKey(emp)) throw new EmpregadoNaoExisteException("Empregado nao existe.");
        
        Map<String, String> valores = Map.of("valor1", valor1, "banco", banco, "agencia", agencia, "contaCorrente", contaCorrente);
        AlterarEmpregadoCommand command = new AlterarEmpregadoCommand(emp, atributo, valores, empregados, membrosSindicato);
        commandManager.executar(command);
    }
    
    @Override
    public void alteraEmpregado(String emp, String atributo, String valor, String idSindicato, String taxaSindical) 
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, TipoInvalidoException, 
                   AtributoNaoExisteException, ValorDeveSerNumericoException, ValorDeveSerNaoNegativoException,
                   MetodoPagamentoInvalidoException, ValorDeveSerTrueOuFalseException, 
                   IdentificacaoSindicatoJaExisteException {
        
        if(emp == null || emp.isBlank()) throw new IdentificacaoEmpregadoNaoPodeSerNulaException("Identificacao do empregado nao pode ser nula.");
        if(!empregados.containsKey(emp)) throw new EmpregadoNaoExisteException("Empregado nao existe.");
        
        Map<String, String> valores = Map.of("valor", valor, "idSindicato", idSindicato, "taxaSindical", taxaSindical);
        AlterarEmpregadoCommand command = new AlterarEmpregadoCommand(emp, atributo, valores, empregados, membrosSindicato);
        commandManager.executar(command);
    }
    
    @Override
    public void alteraEmpregado(String emp, String atributo, String valor1, String banco, String agencia, String contaCorrente, String comissao) 
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, TipoInvalidoException, 
                   AtributoNaoExisteException, ValorDeveSerNumericoException, ValorDeveSerNaoNegativoException,
                   MetodoPagamentoInvalidoException, ValorDeveSerTrueOuFalseException, 
                   IdentificacaoSindicatoJaExisteException {
        
        if(emp == null || emp.isBlank()) throw new IdentificacaoEmpregadoNaoPodeSerNulaException("Identificacao do empregado nao pode ser nula.");
        if(!empregados.containsKey(emp)) throw new EmpregadoNaoExisteException("Empregado nao existe.");
        
        Map<String, String> valores = Map.of("valor1", valor1, "banco", banco, "agencia", agencia, "contaCorrente", contaCorrente, "comissao", comissao);
        AlterarEmpregadoCommand command = new AlterarEmpregadoCommand(emp, atributo, valores, empregados, membrosSindicato);
        commandManager.executar(command);
    }
    
    @Override
    public void removerEmpregado(String emp) throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException {
        if(emp == null || emp.isBlank()) throw new IdentificacaoEmpregadoNaoPodeSerNulaException("Identificacao do empregado nao pode ser nula.");
        if(!empregados.containsKey(emp)) throw new EmpregadoNaoExisteException("Empregado nao existe.");
        
        RemoverEmpregadoCommand command = new RemoverEmpregadoCommand(emp, empregados);
        commandManager.executar(command);
    }
    
    @Override
    public String getEmpregadoPorNome(String emp, String indice) throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, IndiceNaoPodeSerNuloException {
        if(emp == null || emp.isBlank()) throw new IdentificacaoEmpregadoNaoPodeSerNulaException("Identificacao do empregado nao pode ser nula.");

        // Converte o índice para int
        int indiceInt;
        try {
            indiceInt = Integer.parseInt(indice);
        } catch (NumberFormatException e) {
            throw new IndiceNaoPodeSerNuloException("Indice deve ser numerico.");
        }

        // Filtra todos os empregados que têm esse nome
        List<Empregado> encontrados = empregados.values().stream()
                .filter(empregado -> empregado.getNome().equals(emp))
                .toList();

        // Se não encontrou nenhum
        if (encontrados.isEmpty())
            throw new EmpregadoNaoExisteException("Nao ha empregado com esse nome.");

        // Se pediu um índice maior do que existe
        if (indiceInt < 1 || indiceInt > encontrados.size())
            throw new EmpregadoNaoExisteException("Nao ha empregado com esse nome.");

        // Pega o empregado correspondente
        Empregado escolhido = encontrados.get(indiceInt - 1);

        return escolhido.getId();
    }
    
    @Override
    public String getAtributoEmpregado(String emp, String atributo) 
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, AtributoNaoExisteException,
                   EmpregadoNaoEhComissionadoException, EmpregadoNaoEhSindicalizadoException, 
                   EmpregadoNaoRecebeEmBancoException {
        
        if(emp == null || emp.isBlank()) throw new IdentificacaoEmpregadoNaoPodeSerNulaException("Identificacao do empregado nao pode ser nula.");
        if(atributo == null || atributo.isBlank()) throw new AtributoNaoPodeSerNuloException("Atributo nao pode ser nulo.");
        if(!empregados.containsKey(emp)) throw new EmpregadoNaoExisteException("Empregado nao existe.");
        
        Empregado empregado = empregados.get(emp);
        
        switch (atributo) {
            case "nome":
                return empregado.getNome();
            case "endereco":
                return empregado.getEndereco();
            case "tipo":
                return empregado.getTipo();
            case "salario":
                return empregado.getSalario();
            case "sindicalizado":
                return empregado.getSindicato() != null ? "true" : "false";
            case "idSindicato":
                if (empregado.getSindicato() == null) {
                    throw new br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhSindicalizadoException("Empregado nao eh sindicalizado.");
                }
                return empregado.getSindicato().getIdMembro();
            case "taxaSindical":
                if (empregado.getSindicato() == null) {
                    throw new br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhSindicalizadoException("Empregado nao eh sindicalizado.");
                }
                return br.ufal.ic.p2.wepayu.utils.ValorMonetarioUtils.formatarValorMonetario(empregado.getSindicato().getTaxaSindical());
            case "metodoPagamento":
                return empregado.getMetodoPagamento().getMetodoPagamento();
            case "banco":
                if (empregado.getMetodoPagamento() instanceof Banco) {
                    return ((Banco) empregado.getMetodoPagamento()).getBanco();
                }
                throw new br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoRecebeEmBancoException("Empregado nao recebe em banco.");
            case "agencia":
                if (empregado.getMetodoPagamento() instanceof Banco) {
                    return ((Banco) empregado.getMetodoPagamento()).getAgencia();
                }
                throw new br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoRecebeEmBancoException("Empregado nao recebe em banco.");
            case "contaCorrente":
                if (empregado.getMetodoPagamento() instanceof Banco) {
                    return ((Banco) empregado.getMetodoPagamento()).getContaCorrente();
                }
                throw new br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoRecebeEmBancoException("Empregado nao recebe em banco.");
            case "comissao":
                if (empregado instanceof EmpregadoComissionado) {
                    return String.valueOf(((EmpregadoComissionado) empregado).getTaxaDeComissao()).replace('.', ',');
                }
                throw new br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhComissionadoException("Empregado nao eh comissionado.");
            case "agendaPagamento":
                return empregado.getAgendaPagamento().getAgenda();
            default:
                throw new AtributoNaoExisteException("Atributo nao existe.");
        }
    }
}