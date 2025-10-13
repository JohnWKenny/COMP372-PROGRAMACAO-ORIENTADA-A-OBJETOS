package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.services.*;
import br.ufal.ic.p2.wepayu.services.impl.*;
import br.ufal.ic.p2.wepayu.models.*;
import br.ufal.ic.p2.wepayu.commands.*;
import br.ufal.ic.p2.wepayu.Exception.*;
import java.util.Map;
import java.util.HashMap;

/**
 * Facade que simplifica a interface complexa do sistema de folha de pagamento WePayU.
 * Este é o ponto de entrada principal para todas as operações do sistema, fornecendo
 * uma interface unificada para gerenciamento de empregados, sindicato, lançamentos,
 * folha de pagamento e persistência de dados.
 * 
 * <p>O sistema WePayU suporta três tipos de empregados:</p>
 * <ul>
 *   <li><strong>Assalariado:</strong> Recebe salário fixo mensal</li>
 *   <li><strong>Horista:</strong> Recebe por horas trabalhadas</li>
 *   <li><strong>Comissionado:</strong> Recebe comissão sobre vendas realizadas</li>
 * </ul>
 * 
 * <p>Funcionalidades principais:</p>
 * <ul>
 *   <li>Criação e gerenciamento de empregados</li>
 *   <li>Controle de sindicalização e taxas sindicais</li>
 *   <li>Lançamento de cartões de ponto e vendas</li>
 *   <li>Geração de folha de pagamento</li>
 *   <li>Persistência automática de dados</li>
 *   <li>Sistema de undo/redo para operações</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class Facade {

    // Serviços que encapsulam a lógica de negócio
    private final EmpregadoService empregadoService;
    private final SindicatoService sindicatoService;
    private final LancamentoService lancamentoService;
    private final FolhaPagamentoService folhaPagamentoService;
    private final PersistenciaService persistenciaService;

    // Dados compartilhados entre serviços
    private final Map<String, Empregado> empregados;
    private final Map<String, MembroSindicato> membrosSindicato;
    private int id;
    private final CommandManager commandManager;
    private boolean sistemaEncerrado = false;

    /**
     * Construtor da classe Facade que inicializa todos os serviços necessários
     * para o funcionamento do sistema WePayU.
     * 
     * <p>Durante a inicialização:</p>
     * <ul>
     *   <li>Cria as estruturas de dados para empregados e membros do sindicato</li>
     *   <li>Inicializa o gerenciador de comandos para undo/redo</li>
     *   <li>Configura todos os serviços do sistema</li>
     *   <li>Carrega dados existentes do sistema de persistência</li>
     * </ul>
     */
    public Facade() {
        // Inicialização dos dados compartilhados
        this.empregados = new HashMap<>();
        this.membrosSindicato = new HashMap<>();
        this.id = 0;
        this.commandManager = new CommandManager();

        // Inicialização dos serviços
        this.empregadoService = new EmpregadoServiceImpl(empregados, membrosSindicato, id, commandManager);
        this.sindicatoService = new SindicatoServiceImpl(membrosSindicato, empregados, commandManager);
        this.lancamentoService = new LancamentoServiceImpl(empregados, commandManager);
        this.folhaPagamentoService = new FolhaPagamentoServiceImpl(empregados, membrosSindicato);
        this.persistenciaService = new PersistenciaServiceImpl(empregados, membrosSindicato, id);

        // Carrega dados existentes
        this.persistenciaService.carregarSistema();
    }

    // ========== OPERAÇÕES DE EMPREGADOS ==========

    /**
     * Cria um novo empregado no sistema com os dados básicos fornecidos.
     * 
     * <p>Este método cria empregados dos tipos:</p>
     * <ul>
     *   <li><strong>assalariado:</strong> Empregado com salário fixo mensal</li>
     *   <li><strong>horista:</strong> Empregado que recebe por horas trabalhadas</li>
     * </ul>
     * 
     * @param nome Nome completo do empregado (não pode ser nulo ou vazio)
     * @param endereco Endereço residencial do empregado (não pode ser nulo ou vazio)
     * @param tipo Tipo do empregado: "assalariado" ou "horista"
     * @param salario Valor do salário em formato monetário (ex: "1000,50")
     * @return ID único gerado para o empregado criado
     * @throws NomeNaoPodeSerNuloException Se nome for nulo/vazio
     * @throws EnderecoNaoPodeSerNuloException Se endereco for nulo/vazio  
     * @throws TipoNaoPodeSerNuloException Se tipo for nulo/vazio
     * @throws TipoInvalidoException Se o tipo não for "assalariado" ou "horista"
     * @throws SalarioNaoPodeSerNuloException Se o salário for nulo ou vazio
     * @throws SalarioDeveSerNumericoException Se o salário não for um número válido
     * @throws SalarioDeveSerNaoNegativoException Se o salário for negativo
     */
    public String criarEmpregado(String nome, String endereco, String tipo, String salario)
            throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, TipoInvalidoException, SalarioNaoPodeSerNuloException, SalarioDeveSerNumericoException, SalarioDeveSerNaoNegativoException {
        String id = empregadoService.criarEmpregado(nome, endereco, tipo, salario);
        salvarSistema(); // Salva automaticamente após criar empregado
        return id;
    }

    /**
     * Cria um novo empregado comissionado no sistema.
     * 
     * <p>Este método cria especificamente empregados do tipo "comissionado",
     * que recebem uma comissão sobre as vendas realizadas.</p>
     * 
     * @param nome Nome completo do empregado (não pode ser nulo ou vazio)
     * @param endereco Endereço residencial do empregado (não pode ser nulo ou vazio)
     * @param tipo Deve ser "comissionado"
     * @param salario Salário base em formato monetário (ex: "1000,50")
     * @param comissao Taxa de comissão em formato monetário (ex: "5,00")
     * @return ID único gerado para o empregado criado
     * @throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException Se qualquer parâmetro for nulo ou vazio
     * @throws TipoInvalidoException Se o tipo não for "comissionado"
     * @throws SalarioNaoPodeSerNuloException Se o salário for nulo ou vazio
     * @throws SalarioDeveSerNumericoException Se o salário não for um número válido
     * @throws SalarioDeveSerNaoNegativoException Se o salário for negativo
     * @throws ComissaoNaoPodeSerNulaException Se a comissão for nula ou vazia
     * @throws ComissaoDeveSerNumericaException Se a comissão não for um número válido
     * @throws ComissaoDeveSerNaoNegativaException Se a comissão for negativa
     */
    public String criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao)
            throws Exception {
        String id = empregadoService.criarEmpregado(nome, endereco, tipo, salario, comissao);
        salvarSistema(); // Salva automaticamente após criar empregado
        return id;
    }

    /**
     * Altera um atributo específico de um empregado existente.
     * 
     * <p>Atributos que podem ser alterados:</p>
     * <ul>
     *   <li><strong>nome:</strong> Nome completo do empregado</li>
     *   <li><strong>endereco:</strong> Endereço residencial</li>
     *   <li><strong>tipo:</strong> Tipo do empregado (assalariado, horista, comissionado)</li>
     *   <li><strong>salario:</strong> Valor do salário</li>
     *   <li><strong>metodoPagamento:</strong> Método de pagamento (emMaos, correios, banco)</li>
     *   <li><strong>sindicalizado:</strong> true ou false para sindicalização</li>
     * </ul>
     * 
     * @param emp ID do empregado a ser alterado
     * @param atributo Nome do atributo a ser modificado
     * @param valor Novo valor para o atributo
     * @throws EmpregadoNaoExisteException Se o empregado não existir
     * @throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException Se emp, atributo ou valor forem nulos/vazios
     * @throws TipoInvalidoException Se o tipo for inválido
     * @throws AtributoNaoExisteException Se o atributo não existir
     * @throws ValorDeveSerNumericoException Se o valor deveria ser numérico mas não é
     * @throws ValorDeveSerNaoNegativoException Se o valor for negativo quando deveria ser positivo
     * @throws MetodoPagamentoInvalidoException Se o método de pagamento for inválido
     * @throws ValorDeveSerTrueOuFalseException Se o valor deveria ser true/false mas não é
     * @throws IdentificacaoSindicatoJaExisteException Se a identificação do sindicato já existir
     */
    public void alteraEmpregado(String emp, String atributo, String valor)
            throws Exception {
        empregadoService.alteraEmpregado(emp, atributo, valor);
        salvarSistema(); // Salva automaticamente após alterar empregado
    }

    /**
     * Altera um atributo específico de um empregado comissionado.
     * 
     * <p>Este método é usado especificamente para alterar atributos de empregados
     * comissionados, incluindo a taxa de comissão.</p>
     * 
     * @param emp ID do empregado a ser alterado
     * @param atributo Nome do atributo a ser modificado
     * @param valor Novo valor para o atributo
     * @param comissao_salario Nova taxa de comissão ou salário
     * @throws EmpregadoNaoExisteException Se o empregado não existir
     * @throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException Se qualquer parâmetro for nulo/vazio
     * @throws TipoInvalidoException Se o tipo for inválido
     * @throws AtributoNaoExisteException Se o atributo não existir
     * @throws ValorDeveSerNumericoException Se o valor deveria ser numérico mas não é
     * @throws ValorDeveSerNaoNegativoException Se o valor for negativo quando deveria ser positivo
     * @throws MetodoPagamentoInvalidoException Se o método de pagamento for inválido
     * @throws ValorDeveSerTrueOuFalseException Se o valor deveria ser true/false mas não é
     * @throws IdentificacaoSindicatoJaExisteException Se a identificação do sindicato já existir
     */
    public void alteraEmpregado(String emp, String atributo, String valor, String comissao_salario)
            throws Exception {
        empregadoService.alteraEmpregado(emp, atributo, valor, comissao_salario);
        salvarSistema(); // Salva automaticamente após alterar empregado
    }

    /**
     * Altera o método de pagamento de um empregado para depósito bancário.
     * 
     * <p>Este método configura o pagamento via depósito bancário, requerendo
     * as informações completas da conta bancária.</p>
     * 
     * @param emp ID do empregado
     * @param atributo Deve ser "metodoPagamento"
     * @param valor1 Deve ser "banco"
     * @param banco Nome do banco
     * @param agencia Número da agência
     * @param contaCorrente Número da conta corrente
     * @throws EmpregadoNaoExisteException Se o empregado não existir
     * @throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException Se qualquer parâmetro for nulo/vazio
     * @throws TipoInvalidoException Se o tipo for inválido
     * @throws AtributoNaoExisteException Se o atributo não existir
     * @throws ValorDeveSerNumericoException Se algum valor deveria ser numérico mas não é
     * @throws ValorDeveSerNaoNegativoException Se algum valor for negativo quando deveria ser positivo
     * @throws MetodoPagamentoInvalidoException Se o método de pagamento for inválido
     * @throws ValorDeveSerTrueOuFalseException Se o valor deveria ser true/false mas não é
     * @throws IdentificacaoSindicatoJaExisteException Se a identificação do sindicato já existir
     */
    public void alteraEmpregado(String emp, String atributo, String valor1, String banco, String agencia, String contaCorrente)
            throws Exception {
        empregadoService.alteraEmpregado(emp, atributo, valor1, banco, agencia, contaCorrente);
        salvarSistema(); // Salva automaticamente após alterar empregado
    }

    /**
     * Altera informações relacionadas ao sindicato de um empregado.
     * 
     * <p>Este método é usado para configurar ou alterar a sindicalização
     * de um empregado, incluindo ID do sindicato e taxa sindical.</p>
     * 
     * @param emp ID do empregado
     * @param atributo Atributo a ser alterado (geralmente "sindicalizado")
     * @param valor Valor do atributo (geralmente "true")
     * @param idSindicato ID único do sindicato
     * @param taxaSindical Taxa sindical em formato monetário
     * @throws EmpregadoNaoExisteException Se o empregado não existir
     * @throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException Se qualquer parâmetro for nulo/vazio
     * @throws TipoInvalidoException Se o tipo for inválido
     * @throws AtributoNaoExisteException Se o atributo não existir
     * @throws ValorDeveSerNumericoException Se algum valor deveria ser numérico mas não é
     * @throws ValorDeveSerNaoNegativoException Se algum valor for negativo quando deveria ser positivo
     * @throws MetodoPagamentoInvalidoException Se o método de pagamento for inválido
     * @throws ValorDeveSerTrueOuFalseException Se o valor deveria ser true/false mas não é
     * @throws IdentificacaoSindicatoJaExisteException Se a identificação do sindicato já existir
     */
    public void alteraEmpregado(String emp, String atributo, String valor, String idSindicato, String taxaSindical)
            throws Exception {
        empregadoService.alteraEmpregado(emp, atributo, valor, idSindicato, taxaSindical);
        salvarSistema(); // Salva automaticamente após alterar empregado
    }

    /**
     * Altera informações de um empregado comissionado incluindo dados bancários.
     * 
     * <p>Este método combina alteração de método de pagamento bancário
     * com alteração de taxa de comissão para empregados comissionados.</p>
     * 
     * @param emp ID do empregado
     * @param atributo Atributo a ser alterado
     * @param valor1 Valor do atributo
     * @param banco Nome do banco
     * @param agencia Número da agência
     * @param contaCorrente Número da conta corrente
     * @param comissao Nova taxa de comissão
     * @throws EmpregadoNaoExisteException Se o empregado não existir
     * @throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException Se qualquer parâmetro for nulo/vazio
     * @throws TipoInvalidoException Se o tipo for inválido
     * @throws AtributoNaoExisteException Se o atributo não existir
     * @throws ValorDeveSerNumericoException Se algum valor deveria ser numérico mas não é
     * @throws ValorDeveSerNaoNegativoException Se algum valor for negativo quando deveria ser positivo
     * @throws MetodoPagamentoInvalidoException Se o método de pagamento for inválido
     * @throws ValorDeveSerTrueOuFalseException Se o valor deveria ser true/false mas não é
     * @throws IdentificacaoSindicatoJaExisteException Se a identificação do sindicato já existir
     */
    public void alteraEmpregado(String emp, String atributo, String valor1, String banco, String agencia, String contaCorrente, String comissao)
            throws Exception {
        empregadoService.alteraEmpregado(emp, atributo, valor1, banco, agencia, contaCorrente, comissao);
        salvarSistema(); // Salva automaticamente após alterar empregado
    }

    /**
     * Remove um empregado do sistema.
     * 
     * <p>Este método remove permanentemente um empregado do sistema,
     * incluindo todos os seus dados associados.</p>
     * 
     * @param emp ID do empregado a ser removido
     * @throws EmpregadoNaoExisteException Se o empregado não existir
     * @throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException Se o ID do empregado for nulo ou vazio
     */
    public void removerEmpregado(String emp) throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException{
        empregadoService.removerEmpregado(emp);
        salvarSistema(); // Salva automaticamente após remover empregado
    }

    /**
     * Busca um empregado pelo nome e retorna seu ID.
     * 
     * <p>Este método permite encontrar empregados pelo nome quando há
     * múltiplos empregados com o mesmo nome.</p>
     * 
     * @param emp Nome do empregado a ser buscado
     * @param indice Índice do empregado (começando em 1) quando há múltiplos com o mesmo nome
     * @return ID do empregado encontrado
     * @throws EmpregadoNaoExisteException Se o empregado não existir
     * @throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException Se nome ou índice forem nulos/vazios
     */
    public String getEmpregadoPorNome(String emp, String indice) throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, IndiceNaoPodeSerNuloException {
        return empregadoService.getEmpregadoPorNome(emp, indice);
    }

    /**
     * Obtém o valor de um atributo específico de um empregado.
     * 
     * <p>Atributos que podem ser consultados:</p>
     * <ul>
     *   <li><strong>nome:</strong> Nome completo do empregado</li>
     *   <li><strong>endereco:</strong> Endereço residencial</li>
     *   <li><strong>tipo:</strong> Tipo do empregado</li>
     *   <li><strong>salario:</strong> Valor do salário</li>
     *   <li><strong>comissao:</strong> Taxa de comissão (apenas para comissionados)</li>
     *   <li><strong>metodoPagamento:</strong> Método de pagamento</li>
     *   <li><strong>sindicalizado:</strong> Status de sindicalização</li>
     *   <li><strong>banco:</strong> Nome do banco (apenas para pagamento bancário)</li>
     *   <li><strong>agencia:</strong> Número da agência (apenas para pagamento bancário)</li>
     *   <li><strong>contaCorrente:</strong> Número da conta (apenas para pagamento bancário)</li>
     * </ul>
     * 
     * @param emp ID do empregado
     * @param atributo Nome do atributo a ser consultado
     * @return Valor do atributo solicitado
     * @throws EmpregadoNaoExisteException Se o empregado não existir
     * @throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException Se emp ou atributo forem nulos/vazios
     * @throws AtributoNaoExisteException Se o atributo não existir
     * @throws EmpregadoNaoEhComissionadoException Se tentar acessar comissão de não-comissionado
     * @throws EmpregadoNaoEhSindicalizadoException Se tentar acessar dados sindicais de não-sindicalizado
     * @throws EmpregadoNaoRecebeEmBancoException Se tentar acessar dados bancários de quem não recebe em banco
     */
    public String getAtributoEmpregado(String emp, String atributo)
            throws EmpregadoNaoExisteException, NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException, AtributoNaoExisteException,
                   EmpregadoNaoEhComissionadoException, EmpregadoNaoEhSindicalizadoException, 
                   EmpregadoNaoRecebeEmBancoException {
        return empregadoService.getAtributoEmpregado(emp, atributo);
    }

    /**
     * Retorna o número total de empregados cadastrados no sistema.
     * 
     * @return Número total de empregados
     */
    public int getNumeroDeEmpregados() {
        return empregados.size();
    }

    // ========== OPERAÇÕES DE SINDICATO ==========

    /**
     * Cria um novo membro do sindicato associado a um empregado.
     * 
     * <p>Este método cria a associação entre um empregado e o sindicato,
     * definindo a taxa sindical que será descontada.</p>
     * 
     * @param id ID do empregado que será associado ao sindicato
     * @param taxa Taxa sindical em formato monetário (ex: "50,00")
     * @return Objeto MembroSindicato criado
     * @throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException Se ID ou taxa forem nulos/vazios
     * @throws EmpregadoNaoExisteException Se o empregado não existir
     * @throws IdentificacaoSindicatoJaExisteException Se o empregado já for sindicalizado
     * @throws TaxaDeveSerNumericaException Se a taxa não for um número válido
     * @throws TaxaDeveSerNaoNegativaException Se a taxa for negativa
     */
    public MembroSindicato criarMembro(String id, String taxa)
            throws Exception {
        MembroSindicato membro = sindicatoService.criarMembro(id, taxa);
        salvarSistema(); // Salva automaticamente após criar membro
        return membro;
    }

    /**
     * Lança uma taxa de serviço para um membro do sindicato.
     * 
     * <p>Este método registra uma taxa de serviço adicional que será
     * descontada do salário do empregado sindicalizado.</p>
     * 
     * @param membro ID do empregado sindicalizado
     * @param data Data da taxa de serviço no formato "dd/MM/yyyy"
     * @param valor Valor da taxa de serviço em formato monetário
     * @throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException Se qualquer parâmetro for nulo/vazio
     * @throws EmpregadoNaoExisteException Se o empregado não existir
     * @throws EmpregadoNaoEhSindicalizadoException Se o empregado não for sindicalizado
     * @throws DataInvalidaException Se a data for inválida
     * @throws ValorDeveSerNumericoException Se o valor não for um número válido
     * @throws ValorDeveSerNaoNegativoException Se o valor for negativo
     */
    public void lancaTaxaServico(String membro, String data, String valor)
            throws Exception {
        sindicatoService.lancaTaxaServico(membro, data, valor);
        salvarSistema(); // Salva automaticamente após lançar taxa de serviço
    }

    /**
     * Obtém o total de taxas de serviço de um empregado em um período.
     * 
     * <p>Este método calcula a soma de todas as taxas de serviço lançadas
     * para um empregado sindicalizado dentro do período especificado.</p>
     * 
     * @param empregado ID do empregado
     * @param dataInicial Data inicial do período no formato "dd/MM/yyyy"
     * @param dataFinal Data final do período no formato "dd/MM/yyyy"
     * @return Total das taxas de serviço em formato monetário
     * @throws EmpregadoNaoExisteException Se o empregado não existir
     * @throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException Se qualquer parâmetro for nulo/vazio
     * @throws DataInvalidaException Se alguma data for inválida
     */
    public String getTaxasServico(String empregado, String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException, DataInvalidaException {
        return sindicatoService.getTaxasServico(empregado, dataInicial, dataFinal);
    }

    // ========== OPERAÇÕES DE LANÇAMENTO ==========

    /**
     * Lança um cartão de ponto para um empregado horista.
     * 
     * <p>Este método registra as horas trabalhadas por um empregado horista
     * em uma data específica. As horas são divididas em normais (até 8h) e
     * extras (acima de 8h).</p>
     * 
     * @param emp ID do empregado horista
     * @param data Data do cartão no formato "dd/MM/yyyy"
     * @param horas Número de horas trabalhadas (formato decimal, ex: "8,5")
     * @throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException Se qualquer parâmetro for nulo/vazio
     * @throws EmpregadoNaoExisteException Se o empregado não existir
     * @throws EmpregadoNaoEhHoristaException Se o empregado não for horista
     * @throws DataInvalidaException Se a data for inválida
     * @throws ValorDeveSerNumericoException Se as horas não forem um número válido
     * @throws ValorDeveSerNaoNegativoException Se as horas forem negativas
     */
    public void lancaCartao(String emp, String data, String horas)
            throws Exception {
        lancamentoService.lancaCartao(emp, data, horas);
        salvarSistema(); // Salva automaticamente após lançar cartão
    }

    /**
     * Lança uma venda realizada por um empregado comissionado.
     * 
     * <p>Este método registra uma venda realizada por um empregado comissionado
     * em uma data específica. O valor da venda será usado para calcular
     * a comissão do empregado.</p>
     * 
     * @param emp ID do empregado comissionado
     * @param data Data da venda no formato "dd/MM/yyyy"
     * @param valor Valor da venda em formato monetário
     * @throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException Se qualquer parâmetro for nulo/vazio
     * @throws EmpregadoNaoExisteException Se o empregado não existir
     * @throws EmpregadoNaoEhComissionadoException Se o empregado não for comissionado
     * @throws DataInvalidaException Se a data for inválida
     * @throws ValorDeveSerNumericoException Se o valor não for um número válido
     * @throws ValorDeveSerNaoNegativoException Se o valor for negativo
     */
    public void lancaVenda(String emp, String data, String valor)
            throws Exception {
        lancamentoService.lancaVenda(emp, data, valor);
        salvarSistema(); // Salva automaticamente após lançar venda
    }

    /**
     * Obtém o total de horas normais trabalhadas por um empregado em um período.
     * 
     * <p>Este método calcula a soma de todas as horas normais (até 8h por dia)
     * trabalhadas por um empregado horista dentro do período especificado.</p>
     * 
     * @param emp ID do empregado horista
     * @param dataInicial Data inicial do período no formato "dd/MM/yyyy"
     * @param dataFinal Data final do período no formato "dd/MM/yyyy"
     * @return Total de horas normais em formato decimal
     * @throws EmpregadoNaoExisteException Se o empregado não existir
     * @throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException Se qualquer parâmetro for nulo/vazio
     * @throws DataInvalidaException Se alguma data for inválida
     */
    public String getHorasNormaisTrabalhadas(String emp, String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException, DataInvalidaException {
        return lancamentoService.getHorasNormaisTrabalhadas(emp, dataInicial, dataFinal);
    }

    /**
     * Obtém o total de horas extras trabalhadas por um empregado em um período.
     * 
     * <p>Este método calcula a soma de todas as horas extras (acima de 8h por dia)
     * trabalhadas por um empregado horista dentro do período especificado.</p>
     * 
     * @param emp ID do empregado horista
     * @param dataInicial Data inicial do período no formato "dd/MM/yyyy"
     * @param dataFinal Data final do período no formato "dd/MM/yyyy"
     * @return Total de horas extras em formato decimal
     * @throws EmpregadoNaoExisteException Se o empregado não existir
     * @throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException Se qualquer parâmetro for nulo/vazio
     * @throws DataInvalidaException Se alguma data for inválida
     */
    public String getHorasExtrasTrabalhadas(String emp, String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException, DataInvalidaException {
        return lancamentoService.getHorasExtrasTrabalhadas(emp, dataInicial, dataFinal);
    }

    /**
     * Obtém o total de vendas realizadas por um empregado em um período.
     * 
     * <p>Este método calcula a soma de todas as vendas realizadas
     * por um empregado comissionado dentro do período especificado.</p>
     * 
     * @param emp ID do empregado comissionado
     * @param dataInicial Data inicial do período no formato "dd/MM/yyyy"
     * @param dataFinal Data final do período no formato "dd/MM/yyyy"
     * @return Total de vendas em formato monetário
     * @throws EmpregadoNaoExisteException Se o empregado não existir
     * @throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException Se qualquer parâmetro for nulo/vazio
     * @throws DataInvalidaException Se alguma data for inválida
     */
    public String getVendasRealizadas(String emp, String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException, DataInvalidaException {
        return lancamentoService.getVendasRealizadas(emp, dataInicial, dataFinal);
    }

    // ========== OPERAÇÕES DE FOLHA DE PAGAMENTO ==========

    /**
     * Calcula o total da folha de pagamento para uma data específica.
     * 
     * <p>Este método calcula a soma de todos os salários a serem pagos
     * em uma determinada data, considerando todos os tipos de empregados
     * e suas respectivas regras de cálculo.</p>
     * 
     * @param data Data da folha no formato "dd/MM/yyyy"
     * @return Total da folha de pagamento em formato monetário
     * @throws DataInvalidaException Se a data for inválida
     */
    public String totalFolha(String data) throws DataInvalidaException {
        return folhaPagamentoService.totalFolha(data);
    }

    /**
     * Processa a folha de pagamento e gera um arquivo com os dados.
     * 
     * <p>Este método executa o processamento completo da folha de pagamento
     * para uma data específica e salva os resultados em um arquivo.
     * A operação é registrada no sistema de comandos para permitir undo/redo.</p>
     * 
     * @param data Data da folha no formato "dd/MM/yyyy"
     * @param arquivo Nome do arquivo onde será salva a folha de pagamento
     * @throws DataInvalidaException Se a data for inválida
     */
    public void rodaFolha(String data, String arquivo) throws DataInvalidaException {
        RodaFolhaCommand command = new RodaFolhaCommand(data, arquivo, folhaPagamentoService);
        commandManager.executar(command);
    }

    // ========== OPERAÇÕES DE PERSISTÊNCIA ==========

    /**
     * Salva todos os dados do sistema em arquivos de persistência.
     * 
     * <p>Este método salva automaticamente todos os dados do sistema,
     * incluindo empregados e membros do sindicato, em arquivos XML.</p>
     */
    public void salvarSistema() {
        persistenciaService.salvarSistema();
    }

    /**
     * Carrega todos os dados do sistema a partir dos arquivos de persistência.
     * 
     * <p>Este método carrega automaticamente todos os dados salvos anteriormente,
     * incluindo empregados e membros do sindicato, dos arquivos XML.</p>
     */
    public void carregarSistema() {
        persistenciaService.carregarSistema();
    }

    /**
     * Remove todos os dados do sistema, reiniciando-o completamente.
     * 
     * <p>Este método limpa todos os empregados e membros do sindicato
     * do sistema. A operação é registrada no sistema de comandos
     * para permitir undo/redo.</p>
     */
    public void zerarSistema() {
        ZerarSistemaCommand command = new ZerarSistemaCommand(empregados, membrosSindicato);
        commandManager.executar(command);
    }

    /**
     * Encerra o sistema e salva todos os dados finais.
     * 
     * <p>Este método finaliza o sistema, salvando todos os dados
     * e marcando o sistema como encerrado. Após o encerramento,
     * não é possível executar comandos de undo/redo.</p>
     */
    public void encerrarSistema() { 
        persistenciaService.encerrarSistema();
        sistemaEncerrado = true;
    }

    // ========== OPERAÇÕES DE UNDO/REDO ==========

    /**
     * Desfaz a última operação executada no sistema.
     * 
     * <p>Este método permite desfazer a última operação que foi
     * registrada no sistema de comandos. Não é possível desfazer
     * operações após o sistema ter sido encerrado.</p>
     * 
     * @throws NaoPodeComandosAposEncerrarSistemaException Se o sistema já foi encerrado
     * @throws Exception Se não houver comandos para desfazer
     */
    public void undo() throws Exception {
        if (sistemaEncerrado) {
            throw new NaoPodeComandosAposEncerrarSistemaException("Nao pode dar comandos depois de encerrarSistema.");
        }
        commandManager.undo();
    }

    /**
     * Refaz a última operação que foi desfeita.
     * 
     * <p>Este método permite refazer uma operação que foi
     * anteriormente desfeita usando o comando undo.</p>
     * 
     * @throws Exception Se não houver comandos para refazer
     */
    public void redo() throws Exception {
        commandManager.redo();
    }

    // ========== OPERAÇÕES DE AGENDAS DE PAGAMENTO ==========

    /**
     * Cria uma nova agenda de pagamento customizada.
     * 
     * <p>Este método permite criar agendas de pagamento personalizadas
     * além das três agendas padrão do sistema.</p>
     * 
     * <p>Formatos suportados:</p>
     * <ul>
     *   <li><strong>semanal X:</strong> Pagamento semanal em um dia específico (1-7)</li>
     *   <li><strong>semanal X Y:</strong> Pagamento a cada X semanas (1-52) em um dia específico (1-7)</li>
     *   <li><strong>mensal X:</strong> Pagamento mensal em um dia específico (1-28)</li>
     *   <li><strong>mensal $:</strong> Pagamento mensal no último dia do mês</li>
     * </ul>
     * 
     * @param descricao Descrição da agenda no formato "tipo parametros"
     * @throws IllegalArgumentException Se a descrição for inválida ou a agenda já existir
     */
    public void criarAgendaDePagamentos(String descricao) throws IllegalArgumentException {
        br.ufal.ic.p2.wepayu.models.AgendaDePagamentos.criarAgenda(descricao);
        salvarSistema(); // Salva automaticamente após criar agenda
    }
}
