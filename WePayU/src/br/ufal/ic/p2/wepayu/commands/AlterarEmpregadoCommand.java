package br.ufal.ic.p2.wepayu.commands;

import br.ufal.ic.p2.wepayu.Exception.AtributoNaoExisteException;
import br.ufal.ic.p2.wepayu.Exception.NomeNaoPodeSerNuloException;
import br.ufal.ic.p2.wepayu.Exception.EnderecoNaoPodeSerNuloException;
import br.ufal.ic.p2.wepayu.Exception.SalarioNaoPodeSerNuloException;
import br.ufal.ic.p2.wepayu.Exception.SalarioDeveSerNumericoException;
import br.ufal.ic.p2.wepayu.Exception.SalarioDeveSerNaoNegativoException;
import br.ufal.ic.p2.wepayu.Exception.ComissaoNaoPodeSerNulaException;
import br.ufal.ic.p2.wepayu.Exception.ComissaoDeveSerNumericaException;
import br.ufal.ic.p2.wepayu.Exception.ComissaoDeveSerNaoNegativaException;
import br.ufal.ic.p2.wepayu.Exception.MetodoPagamentoInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.BancoNaoPodeSerNuloException;
import br.ufal.ic.p2.wepayu.Exception.AgenciaNaoPodeSerNulaException;
import br.ufal.ic.p2.wepayu.Exception.ContaCorrenteNaoPodeSerNulaException;
import br.ufal.ic.p2.wepayu.Exception.ValorDeveSerTrueOuFalseException;
import br.ufal.ic.p2.wepayu.Exception.IdentificacaoSindicatoNaoPodeSerNulaException;
import br.ufal.ic.p2.wepayu.Exception.TaxaSindicalNaoPodeSerNulaException;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.MembroSindicato;
import br.ufal.ic.p2.wepayu.models.MetodoPagamento;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEncontradoException;
import br.ufal.ic.p2.wepayu.Exception.ErroAlteracaoEmpregadoException;
import br.ufal.ic.p2.wepayu.Exception.IdentificacaoSindicatoJaExisteException;
import br.ufal.ic.p2.wepayu.factories.MembroSindicatoFactory;
import br.ufal.ic.p2.wepayu.factories.EmpregadoFactory;
import java.util.Map;

/**
 * Comando para alterar atributos de um empregado no sistema WePayU.
 * 
 * <p>Este comando implementa o padrão Command para alterar atributos
 * de empregados, permitindo que a operação seja desfeita através
 * do sistema de undo/redo.</p>
 * 
 * <p>Funcionalidades:</p>
 * <ul>
 *   <li>Alteração de atributos básicos (nome, endereço, salário)</li>
 *   <li>Alteração de método de pagamento</li>
 *   <li>Alteração de informações sindicais</li>
 *   <li>Backup do estado anterior para desfazer</li>
 *   <li>Restauração do estado anterior ao desfazer</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class AlterarEmpregadoCommand implements Command {
    private final String empId;
    private final String atributo;
    private final Map<String, String> valores;
    private final Map<String, Empregado> empregados;
    private final Map<String, MembroSindicato> membrosSindicato;
    
    // Estado anterior para desfazer
    private Object valorAnterior;

    public AlterarEmpregadoCommand(String empId, String atributo, Map<String, String> valores,
                                 Map<String, Empregado> empregados, 
                                 Map<String, MembroSindicato> membrosSindicato) {
        this.empId = empId;
        this.atributo = atributo;
        this.valores = valores;
        this.empregados = empregados;
        this.membrosSindicato = membrosSindicato;
    }

    @Override
    public void executar() {
        try {
            Empregado empregado = empregados.get(empId);
            if (empregado == null) {
                throw new EmpregadoNaoEncontradoException("Empregado não encontrado.");
            }
            
            // Salva o valor anterior para desfazer
            valorAnterior = obterValorAtual(empregado, atributo);
            
            // Aplica a alteração
            aplicarAlteracao(empregado, atributo, valores);
        } catch (EmpregadoNaoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new ErroAlteracaoEmpregadoException(e.getMessage());
        }
    }

    @Override
    public void desfazer() {
        Empregado empregado = empregados.get(empId);
        if (empregado != null) {
            if (valorAnterior != null) {
                restaurarValorAnterior(empregado, atributo, valorAnterior);
            } else {
                // Se valorAnterior é null, significa que o estado anterior era null
                restaurarValorAnterior(empregado, atributo, null);
            }
        }
    }
    
    private Object obterValorAtual(Empregado empregado, String atributo) {
        switch (atributo) {
            case "nome":
                return empregado.getNome();
            case "endereco":
                return empregado.getEndereco();
            case "tipo":
                return empregado.getTipo();
            case "salario":
                return empregado.getSalario();
            case "comissao":
                if (empregado instanceof br.ufal.ic.p2.wepayu.models.EmpregadoComissionado) {
                    return ((br.ufal.ic.p2.wepayu.models.EmpregadoComissionado) empregado).getTaxaDeComissao();
                }
                return null;
            case "sindicalizado":
                return empregado.getSindicato();
            case "idSindicato":
                return empregado.getSindicato() != null ? empregado.getSindicato().getIdMembro() : null;
            case "taxaSindical":
                return empregado.getSindicato() != null ? empregado.getSindicato().getTaxaSindical() : null;
            case "metodoPagamento":
                return empregado.getMetodoPagamento();
            case "banco":
                if (empregado.getMetodoPagamento() instanceof br.ufal.ic.p2.wepayu.models.Banco) {
                    return ((br.ufal.ic.p2.wepayu.models.Banco) empregado.getMetodoPagamento()).getBanco();
                }
                return null;
            case "agencia":
                if (empregado.getMetodoPagamento() instanceof br.ufal.ic.p2.wepayu.models.Banco) {
                    return ((br.ufal.ic.p2.wepayu.models.Banco) empregado.getMetodoPagamento()).getAgencia();
                }
                return null;
            case "contaCorrente":
                if (empregado.getMetodoPagamento() instanceof br.ufal.ic.p2.wepayu.models.Banco) {
                    return ((br.ufal.ic.p2.wepayu.models.Banco) empregado.getMetodoPagamento()).getContaCorrente();
                }
                return null;
            default:
                return null;
        }
    }
    
    private void aplicarAlteracao(Empregado empregado, String atributo, Map<String, String> valores) {
        String valor = valores.get("valor");
        String valor1 = valores.get("valor1");
        
        switch (atributo) {
            case "nome":
                if (valor == null || valor.isBlank()) {
                    throw new NomeNaoPodeSerNuloException("Nome nao pode ser nulo.");
                }
                empregado.setNome(valor);
                break;
            case "endereco":
                if (valor == null || valor.isBlank()) {
                    throw new EnderecoNaoPodeSerNuloException("Endereco nao pode ser nulo.");
                }
                empregado.setEndereco(valor);
                break;
            case "tipo":
                // Altera o tipo do empregado criando um novo empregado do tipo correto
                String novoSalario = valores.get("salario");
                if (novoSalario == null) {
                    // Se não foi passado salário, mantém o salário atual
                    novoSalario = empregado.getSalario().replace(",", ".");
                }
                
                String nome = empregado.getNome();
                String endereco = empregado.getEndereco();
                MembroSindicato sindicatoAnterior = empregado.getSindicato();
                MetodoPagamento metodoPagamentoAnterior = empregado.getMetodoPagamento();
                
                Empregado novoEmpregado;
                if ("comissionado".equals(valor)) {
                    String comissao = valores.get("comissao");
                    if (comissao == null) {
                        throw new ComissaoNaoPodeSerNulaException("Comissao nao pode ser nula.");
                    }
                    novoEmpregado = EmpregadoFactory.criarEmpregado(valor, nome, endereco, novoSalario, comissao);
                } else {
                    novoEmpregado = EmpregadoFactory.criarEmpregado(valor, nome, endereco, novoSalario);
                }
                
                // Preserva o ID e outros atributos
                novoEmpregado.setId(empregado.getId());
                novoEmpregado.setSindicato(sindicatoAnterior);
                novoEmpregado.setMetodoPagamento(metodoPagamentoAnterior);
                
                // Substitui o empregado no mapa
                empregados.put(empId, novoEmpregado);
                break;
            case "salario":
                // Validações de salário
                if (valor == null || valor.isBlank()) {
                    throw new SalarioNaoPodeSerNuloException("Salario nao pode ser nulo.");
                }
                
                double salarioNumerico;
                try {
                    salarioNumerico = Double.parseDouble(valor.replace(",", "."));
                } catch (NumberFormatException e) {
                    throw new SalarioDeveSerNumericoException("Salario deve ser numerico.");
                }
                
                if (salarioNumerico < 0) {
                    throw new SalarioDeveSerNaoNegativoException("Salario deve ser nao-negativo.");
                }
                
                // Altera o salário criando um novo empregado do mesmo tipo
                String tipoAtualSalario = empregado.getTipo();
                String nomeSalario = empregado.getNome();
                String enderecoSalario = empregado.getEndereco();
                MembroSindicato sindicatoAnteriorSalario = empregado.getSindicato();
                MetodoPagamento metodoPagamentoAnteriorSalario = empregado.getMetodoPagamento();
                
                Empregado novoEmpregadoSalario;
                if ("comissionado".equals(tipoAtualSalario)) {
                    // Para comissionado, precisa da comissão também
                    double comissaoAtual = ((br.ufal.ic.p2.wepayu.models.EmpregadoComissionado) empregado).getTaxaDeComissao();
                    novoEmpregadoSalario = EmpregadoFactory.criarEmpregado(tipoAtualSalario, nomeSalario, enderecoSalario, valor, String.valueOf(comissaoAtual));
                } else {
                    novoEmpregadoSalario = EmpregadoFactory.criarEmpregado(tipoAtualSalario, nomeSalario, enderecoSalario, valor);
                }
                
                // Preserva o ID e outros atributos
                novoEmpregadoSalario.setId(empregado.getId());
                novoEmpregadoSalario.setSindicato(sindicatoAnteriorSalario);
                novoEmpregadoSalario.setMetodoPagamento(metodoPagamentoAnteriorSalario);
                
                // Substitui o empregado no mapa
                empregados.put(empId, novoEmpregadoSalario);
                break;
            case "comissao":
                // Validações de comissão
                if (valor == null || valor.isBlank()) {
                    throw new ComissaoNaoPodeSerNulaException("Comissao nao pode ser nula.");
                }
                
                if (empregado instanceof br.ufal.ic.p2.wepayu.models.EmpregadoComissionado) {
                    double comissaoNumerica;
                    try {
                        comissaoNumerica = Double.parseDouble(valor.replace(",", "."));
                    } catch (NumberFormatException e) {
                        throw new ComissaoDeveSerNumericaException("Comissao deve ser numerica.");
                    }
                    
                    if (comissaoNumerica < 0) {
                        throw new ComissaoDeveSerNaoNegativaException("Comissao deve ser nao-negativa.");
                    }
                    
                    ((br.ufal.ic.p2.wepayu.models.EmpregadoComissionado) empregado).setTaxaDeComissao(comissaoNumerica);
                } else {
                    throw new br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhComissionadoException("Empregado nao eh comissionado.");
                }
                break;
            case "sindicalizado":
                // Validação para valor boolean
                if (!"true".equals(valor) && !"false".equals(valor)) {
                    throw new ValorDeveSerTrueOuFalseException("Valor deve ser true ou false.");
                }
                
                boolean sindicalizado = Boolean.parseBoolean(valor);
                if (sindicalizado) {
                    String idSindicato = valores.get("idSindicato");
                    String taxaSindical = valores.get("taxaSindical");
                    
                    // Validações para sindicalização
                    if (idSindicato == null || idSindicato.isBlank()) {
                        throw new IdentificacaoSindicatoNaoPodeSerNulaException("Identificacao do sindicato nao pode ser nula.");
                    }
                    
                    if (taxaSindical == null || taxaSindical.isBlank()) {
                        throw new TaxaSindicalNaoPodeSerNulaException("Taxa sindical nao pode ser nula.");
                    }

                    // Validações de taxa sindical antes de verificar ID duplicado
                    taxaSindical = taxaSindical.replace(",", ".");
                    double taxa;
                    try {
                        taxa = Double.parseDouble(taxaSindical);
                    } catch (NumberFormatException e) {
                        throw new br.ufal.ic.p2.wepayu.Exception.ValorMonetarioInvalidoException("Taxa sindical deve ser numerica.");
                    }
                    
                    if (taxa < 0) {
                        throw new br.ufal.ic.p2.wepayu.Exception.ValorMonetarioInvalidoException("Taxa sindical deve ser nao-negativa.");
                    }

                    if(membrosSindicato.containsKey(idSindicato)) { 
                        throw new IdentificacaoSindicatoJaExisteException("Ha outro empregado com esta identificacao de sindicato");
                    }

                    if (idSindicato != null && taxaSindical != null) {
                        br.ufal.ic.p2.wepayu.models.MembroSindicato membro = MembroSindicatoFactory.criarMembro(idSindicato, taxaSindical);
                        membro.setTaxaSindical(taxa);
                        empregado.setSindicato(membro);
                        // Adiciona o membro ao mapa de membros do sindicato
                        membrosSindicato.put(idSindicato, membro);
                    }
                } else {
                    empregado.setSindicato(null);
                }
                break;
            case "metodoPagamento":
                // Usa valor1 quando há banco/agencia/conta, senão usa valor
                String metodoValor = (valor1 != null) ? valor1 : valor;
                
                // Validação de método de pagamento
                if (!"banco".equals(metodoValor) && !"correios".equals(metodoValor) && !"emMaos".equals(metodoValor)) {
                    throw new MetodoPagamentoInvalidoException("Metodo de pagamento invalido.");
                }
                
                if ("banco".equals(metodoValor)) {
                    String banco = valores.get("banco");
                    String agencia = valores.get("agencia");
                    String contaCorrente = valores.get("contaCorrente");
                    
                    // Validações para banco
                    if (banco == null || banco.isBlank()) {
                        throw new BancoNaoPodeSerNuloException("Banco nao pode ser nulo.");
                    }
                    
                    if (agencia == null || agencia.isBlank()) {
                        throw new AgenciaNaoPodeSerNulaException("Agencia nao pode ser nulo.");
                    }
                    
                    if (contaCorrente == null || contaCorrente.isBlank()) {
                        throw new ContaCorrenteNaoPodeSerNulaException("Conta corrente nao pode ser nulo.");
                    }
                    
                    br.ufal.ic.p2.wepayu.models.Banco metodoBanco = new br.ufal.ic.p2.wepayu.models.Banco(banco, agencia, contaCorrente);
                    empregado.setMetodoPagamento(metodoBanco);
                } else if ("correios".equals(metodoValor)) {
                    br.ufal.ic.p2.wepayu.models.Correios metodoCorreios = new br.ufal.ic.p2.wepayu.models.Correios();
                    empregado.setMetodoPagamento(metodoCorreios);
                } else if ("emMaos".equals(metodoValor)) {
                    br.ufal.ic.p2.wepayu.models.EmMaos metodoEmMaos = new br.ufal.ic.p2.wepayu.models.EmMaos();
                    empregado.setMetodoPagamento(metodoEmMaos);
                }
                break;
            default:
                throw new AtributoNaoExisteException("Atributo nao existe.");
        }
    }
    
    private void restaurarValorAnterior(Empregado empregado, String atributo, Object valorAnterior) {
        
        switch (atributo) {
            case "nome":
                empregado.setNome((String) valorAnterior);
                break;
            case "endereco":
                empregado.setEndereco((String) valorAnterior);
                break;
            case "tipo":
                // Tipo não pode ser alterado após criação
                break;
            case "salario":
                // Salário não pode ser alterado diretamente na classe base
                break;
            case "comissao":
                if (empregado instanceof br.ufal.ic.p2.wepayu.models.EmpregadoComissionado) {
                    ((br.ufal.ic.p2.wepayu.models.EmpregadoComissionado) empregado).setTaxaDeComissao((Double) valorAnterior);
                }
                break;
            case "sindicalizado":
                MembroSindicato sindicatoAnterior = (MembroSindicato) valorAnterior;
                if (sindicatoAnterior != null) {
                    // Restaura o membro do sindicato
                    empregado.setSindicato(sindicatoAnterior);
                    // Adiciona de volta ao mapa se não estiver lá
                    if (!membrosSindicato.containsKey(sindicatoAnterior.getIdMembro())) {
                        membrosSindicato.put(sindicatoAnterior.getIdMembro(), sindicatoAnterior);
                    }
                } else {
                    // Remove o sindicato
                    if (empregado.getSindicato() != null) {
                        membrosSindicato.remove(empregado.getSindicato().getIdMembro());
                    }
                    empregado.setSindicato(null);
                }
                break;
            case "idSindicato":
                if (empregado.getSindicato() != null) {
                    empregado.getSindicato().setIdMembro((String) valorAnterior);
                }
                break;
            case "taxaSindical":
                if (empregado.getSindicato() != null) {
                    empregado.getSindicato().setTaxaSindical((Double) valorAnterior);
                }
                break;
            case "metodoPagamento":
                empregado.setMetodoPagamento((br.ufal.ic.p2.wepayu.models.MetodoPagamento) valorAnterior);
                break;
            case "banco":
                if (empregado.getMetodoPagamento() instanceof br.ufal.ic.p2.wepayu.models.Banco) {
                    ((br.ufal.ic.p2.wepayu.models.Banco) empregado.getMetodoPagamento()).setBanco((String) valorAnterior);
                }
                break;
            case "agencia":
                if (empregado.getMetodoPagamento() instanceof br.ufal.ic.p2.wepayu.models.Banco) {
                    ((br.ufal.ic.p2.wepayu.models.Banco) empregado.getMetodoPagamento()).setAgencia((String) valorAnterior);
                }
                break;
            case "contaCorrente":
                if (empregado.getMetodoPagamento() instanceof br.ufal.ic.p2.wepayu.models.Banco) {
                    ((br.ufal.ic.p2.wepayu.models.Banco) empregado.getMetodoPagamento()).setContaCorrente((String) valorAnterior);
                }
                break;
        }
    }
}

