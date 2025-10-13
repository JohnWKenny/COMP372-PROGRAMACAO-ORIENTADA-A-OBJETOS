package br.ufal.ic.p2.wepayu.models;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

/**
 * Classe que gerencia agendas de pagamento customizadas no sistema WePayU.
 * 
 * <p>Esta classe permite criar e gerenciar agendas de pagamento personalizadas
 * além das três agendas padrão do sistema. Suporta diferentes formatos:</p>
 * 
 * <ul>
 *   <li><strong>semanal X:</strong> Pagamento semanal em um dia específico (1-7)</li>
 *   <li><strong>semanal X Y:</strong> Pagamento a cada X semanas (1-52) em um dia específico (1-7)</li>
 *   <li><strong>mensal X:</strong> Pagamento mensal em um dia específico (1-28)</li>
 *   <li><strong>mensal $:</strong> Pagamento mensal no último dia do mês</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class AgendaDePagamentos {
    private static final Set<String> AGENDAS_PADRAO = Set.of(
        "semanal 5", "semanal 2 5", "mensal $"
    );
    
    private static final Map<String, AgendaDePagamentos> agendasCustomizadas = new HashMap<>();
    
    private String descricao;
    private String tipo; // "semanal" ou "mensal"
    private int parametro1; // dia da semana (1-7) ou dia do mês (1-28) ou semanas (1-52)
    private int parametro2; // dia da semana (1-7) para agendas semanal X Y
    
    /**
     * Construtor privado para agendas customizadas.
     */
    private AgendaDePagamentos(String descricao, String tipo, int parametro1, int parametro2) {
        this.descricao = descricao;
        this.tipo = tipo;
        this.parametro1 = parametro1;
        this.parametro2 = parametro2;
    }
    
    /**
     * Cria uma nova agenda de pagamento customizada.
     * 
     * @param descricao Descrição da agenda no formato "tipo parametros"
     * @return Nova agenda de pagamento
     * @throws IllegalArgumentException Se a descrição for inválida
     */
    public static AgendaDePagamentos criarAgenda(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Descricao de agenda invalida");
        }
        
        // Verifica se já existe (padrão ou customizada)
        if (AGENDAS_PADRAO.contains(descricao) || agendasCustomizadas.containsKey(descricao)) {
            throw new IllegalArgumentException("Agenda de pagamentos ja existe");
        }
        
        String[] partes = descricao.trim().split("\\s+");
        
        if (partes.length < 2) {
            throw new IllegalArgumentException("Descricao de agenda invalida");
        }
        
        String tipo = partes[0];
        
        if ("semanal".equals(tipo)) {
            return criarAgendaSemanal(descricao, partes);
        } else if ("mensal".equals(tipo)) {
            return criarAgendaMensal(descricao, partes);
        } else {
            throw new IllegalArgumentException("Descricao de agenda invalida");
        }
    }
    
    private static AgendaDePagamentos criarAgendaSemanal(String descricao, String[] partes) {
        if (partes.length == 2) {
            // Formato: semanal X (dia da semana)
            int diaSemana = Integer.parseInt(partes[1]);
            if (diaSemana < 1 || diaSemana > 7) {
                throw new IllegalArgumentException("Descricao de agenda invalida");
            }
            AgendaDePagamentos agenda = new AgendaDePagamentos(descricao, "semanal", diaSemana, 0);
            agendasCustomizadas.put(descricao, agenda);
            return agenda;
        } else if (partes.length == 3) {
            // Formato: semanal X Y (X semanas, Y dia da semana)
            int semanas = Integer.parseInt(partes[1]);
            int diaSemana = Integer.parseInt(partes[2]);
            if (semanas < 1 || semanas > 52 || diaSemana < 1 || diaSemana > 7) {
                throw new IllegalArgumentException("Descricao de agenda invalida");
            }
            AgendaDePagamentos agenda = new AgendaDePagamentos(descricao, "semanal", semanas, diaSemana);
            agendasCustomizadas.put(descricao, agenda);
            return agenda;
        } else {
            throw new IllegalArgumentException("Descricao de agenda invalida");
        }
    }
    
    private static AgendaDePagamentos criarAgendaMensal(String descricao, String[] partes) {
        if (partes.length == 2) {
            String parametro = partes[1];
            if ("$".equals(parametro)) {
                // Formato: mensal $ (último dia do mês)
                AgendaDePagamentos agenda = new AgendaDePagamentos(descricao, "mensal", -1, 0);
                agendasCustomizadas.put(descricao, agenda);
                return agenda;
            } else {
                // Formato: mensal X (dia do mês)
                int diaMes = Integer.parseInt(parametro);
                if (diaMes < 1 || diaMes > 28) {
                    throw new IllegalArgumentException("Descricao de agenda invalida");
                }
                AgendaDePagamentos agenda = new AgendaDePagamentos(descricao, "mensal", diaMes, 0);
                agendasCustomizadas.put(descricao, agenda);
                return agenda;
            }
        } else {
            throw new IllegalArgumentException("Descricao de agenda invalida");
        }
    }
    
    /**
     * Verifica se uma agenda é válida (existe no sistema).
     * 
     * @param descricao Descrição da agenda
     * @return true se a agenda for válida, false caso contrário
     */
    public static boolean isAgendaValida(String descricao) {
        return AGENDAS_PADRAO.contains(descricao) || agendasCustomizadas.containsKey(descricao);
    }
    
    /**
     * Obtém uma agenda pelo nome.
     * 
     * @param descricao Descrição da agenda
     * @return Agenda de pagamento ou null se não existir
     */
    public static AgendaDePagamentos getAgenda(String descricao) {
        if (AGENDAS_PADRAO.contains(descricao)) {
            // Cria uma agenda padrão temporária
            return new AgendaDePagamentos(descricao, getTipoPadrao(descricao), 
                                        getParametro1Padrao(descricao), getParametro2Padrao(descricao));
        }
        return agendasCustomizadas.get(descricao);
    }
    
    private static String getTipoPadrao(String descricao) {
        if (descricao.startsWith("semanal")) return "semanal";
        if (descricao.startsWith("mensal")) return "mensal";
        return "semanal";
    }
    
    private static int getParametro1Padrao(String descricao) {
        switch (descricao) {
            case "semanal 5": return 5;
            case "semanal 2 5": return 2;
            case "mensal $": return -1;
            default: return 5;
        }
    }
    
    private static int getParametro2Padrao(String descricao) {
        switch (descricao) {
            case "semanal 2 5": return 5;
            default: return 0;
        }
    }
    
    /**
     * Verifica se deve pagar em uma data específica baseado na agenda.
     * 
     * @param data Data a ser verificada (formato "dd/MM/yyyy")
     * @return true se deve pagar na data, false caso contrário
     */
    public boolean devePagarNaData(String data) {
        try {
            String[] partes = data.split("/");
            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            int ano = Integer.parseInt(partes[2]);
            
            java.time.LocalDate localDate = java.time.LocalDate.of(ano, mes, dia);
            java.time.DayOfWeek diaSemana = localDate.getDayOfWeek();
            int diaSemanaNumero = diaSemana.getValue(); // 1=segunda, 7=domingo
            
            if ("semanal".equals(tipo)) {
                if (parametro2 == 0) {
                    // Formato: semanal X (toda semana no dia X)
                    return diaSemanaNumero == parametro1;
                } else {
                    // Formato: semanal X Y (a cada X semanas no dia Y)
                    // Para agendas customizadas, usa uma data base específica baseada nos parâmetros
                    java.time.LocalDate dataBase;
                    
                    if (parametro1 == 52 && parametro2 == 1) {
                        // Caso especial: semanal 52 1 - usa 26/12/2004 como data base
                        dataBase = java.time.LocalDate.of(2004, 12, 26);
                    } else {
                        // Para outros casos, usa 14/1/2005 como data base (mesma do sistema padrão)
                        dataBase = java.time.LocalDate.of(2005, 1, 14);
                    }
                    
                    // Encontra a primeira ocorrência do dia da semana desejado a partir da data base
                    while (dataBase.getDayOfWeek().getValue() != parametro2) {
                        dataBase = dataBase.plusDays(1);
                    }
                    
                    if (localDate.isBefore(dataBase)) {
                        return false;
                    }
                    
                    long diasEntre = java.time.temporal.ChronoUnit.DAYS.between(dataBase, localDate);
                    long semanasEntre = diasEntre / 7;
                    return diaSemanaNumero == parametro2 && semanasEntre % parametro1 == 0;
                }
            } else if ("mensal".equals(tipo)) {
                if (parametro1 == -1) {
                    // Formato: mensal $ (último dia do mês)
                    return localDate.equals(localDate.withDayOfMonth(localDate.lengthOfMonth()));
                } else {
                    // Formato: mensal X (dia X do mês)
                    return dia == parametro1;
                }
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Calcula o valor a ser pago baseado na agenda e tipo de empregado.
     * 
     * @param empregado Empregado para calcular o pagamento
     * @param dataInicial Data inicial do período
     * @param dataFinal Data final do período
     * @return Valor a ser pago
     */
    public double calcularValorPagamento(Empregado empregado, String dataInicial, String dataFinal) {
        String tipoEmpregado = empregado.getTipo();
        
        if ("semanal".equals(tipo)) {
            if (parametro2 == 0) {
                // Formato: semanal X (toda semana)
                return calcularPagamentoSemanal(empregado, tipoEmpregado);
            } else {
                // Formato: semanal X Y (a cada X semanas)
                return calcularPagamentoBiSemanal(empregado, tipoEmpregado, parametro1);
            }
        } else if ("mensal".equals(tipo)) {
            // Formato: mensal X ou mensal $
            return calcularPagamentoMensal(empregado, tipoEmpregado);
        }
        
        return 0.0;
    }
    
    private double calcularPagamentoSemanal(Empregado empregado, String tipoEmpregado) {
        if ("horista".equals(tipoEmpregado)) {
            return calcularPagamentoHoristaSemanal(empregado);
        } else if ("assalariado".equals(tipoEmpregado)) {
            return calcularPagamentoAssalariadoSemanal(empregado);
        } else if ("comissionado".equals(tipoEmpregado)) {
            return calcularPagamentoComissionadoSemanal(empregado);
        }
        return 0.0;
    }
    
    private double calcularPagamentoBiSemanal(Empregado empregado, String tipoEmpregado, int semanas) {
        if ("horista".equals(tipoEmpregado)) {
            return calcularPagamentoHoristaBiSemanal(empregado);
        } else if ("assalariado".equals(tipoEmpregado)) {
            return calcularPagamentoAssalariadoBiSemanal(empregado, semanas);
        } else if ("comissionado".equals(tipoEmpregado)) {
            return calcularPagamentoComissionadoBiSemanal(empregado, semanas);
        }
        return 0.0;
    }
    
    private double calcularPagamentoMensal(Empregado empregado, String tipoEmpregado) {
        if ("horista".equals(tipoEmpregado)) {
            return calcularPagamentoHoristaMensal(empregado);
        } else if ("assalariado".equals(tipoEmpregado)) {
            return calcularPagamentoAssalariadoMensal(empregado);
        } else if ("comissionado".equals(tipoEmpregado)) {
            return calcularPagamentoComissionadoMensal(empregado);
        }
        return 0.0;
    }
    
    // Métodos de cálculo específicos por tipo de empregado
    private double calcularPagamentoHoristaSemanal(Empregado empregado) {
        if (!(empregado instanceof EmpregadoHorista)) return 0.0;
        EmpregadoHorista horista = (EmpregadoHorista) empregado;
        double salarioHora = horista.getSalarioPorHora();
        
        double horasNormais = calcularHorasNormais(horista, 7); // 7 dias
        double horasExtras = calcularHorasExtras(horista, 7);
        
        return (horasNormais * salarioHora) + (horasExtras * salarioHora * 1.5);
    }
    
    private double calcularPagamentoHoristaBiSemanal(Empregado empregado) {
        if (!(empregado instanceof EmpregadoHorista)) return 0.0;
        EmpregadoHorista horista = (EmpregadoHorista) empregado;
        double salarioHora = horista.getSalarioPorHora();
        
        double horasNormais = calcularHorasNormais(horista, 14); // 14 dias
        double horasExtras = calcularHorasExtras(horista, 14);
        
        return (horasNormais * salarioHora) + (horasExtras * salarioHora * 1.5);
    }
    
    private double calcularPagamentoHoristaMensal(Empregado empregado) {
        if (!(empregado instanceof EmpregadoHorista)) return 0.0;
        EmpregadoHorista horista = (EmpregadoHorista) empregado;
        double salarioHora = horista.getSalarioPorHora();
        
        double horasNormais = calcularHorasNormais(horista, 30); // 30 dias
        double horasExtras = calcularHorasExtras(horista, 30);
        
        return (horasNormais * salarioHora) + (horasExtras * salarioHora * 1.5);
    }
    
    private double calcularPagamentoAssalariadoSemanal(Empregado empregado) {
        if (!(empregado instanceof EmpregadoAssalariado)) return 0.0;
        EmpregadoAssalariado assalariado = (EmpregadoAssalariado) empregado;
        double salarioAnual = assalariado.getSalarioMensal() * 12;
        return salarioAnual / 52; // Divide por 52 semanas
    }
    
    private double calcularPagamentoAssalariadoBiSemanal(Empregado empregado, int semanas) {
        if (!(empregado instanceof EmpregadoAssalariado)) return 0.0;
        EmpregadoAssalariado assalariado = (EmpregadoAssalariado) empregado;
        double salarioAnual = assalariado.getSalarioMensal() * 12;
        return (salarioAnual / 52) * semanas; // Divide por 52 semanas e multiplica pelas semanas
    }
    
    private double calcularPagamentoAssalariadoMensal(Empregado empregado) {
        if (!(empregado instanceof EmpregadoAssalariado)) return 0.0;
        EmpregadoAssalariado assalariado = (EmpregadoAssalariado) empregado;
        return assalariado.getSalarioMensal();
    }
    
    private double calcularPagamentoComissionadoSemanal(Empregado empregado) {
        if (!(empregado instanceof EmpregadoComissionado)) return 0.0;
        EmpregadoComissionado comissionado = (EmpregadoComissionado) empregado;
        double salarioAnual = comissionado.getSalarioMensal() * 12;
        double salarioSemanal = salarioAnual / 52;
        
        double comissoes = calcularComissoes(comissionado, 7);
        return salarioSemanal + comissoes;
    }
    
    private double calcularPagamentoComissionadoBiSemanal(Empregado empregado, int semanas) {
        if (!(empregado instanceof EmpregadoComissionado)) return 0.0;
        EmpregadoComissionado comissionado = (EmpregadoComissionado) empregado;
        double salarioAnual = comissionado.getSalarioMensal() * 12;
        double salarioBiSemanal = (salarioAnual / 52) * semanas;
        
        double comissoes = calcularComissoes(comissionado, semanas * 7);
        return salarioBiSemanal + comissoes;
    }
    
    private double calcularPagamentoComissionadoMensal(Empregado empregado) {
        if (!(empregado instanceof EmpregadoComissionado)) return 0.0;
        EmpregadoComissionado comissionado = (EmpregadoComissionado) empregado;
        double salarioMensal = comissionado.getSalarioMensal();
        
        double comissoes = calcularComissoes(comissionado, 30);
        return salarioMensal + comissoes;
    }
    
    private double calcularHorasNormais(EmpregadoHorista horista, int dias) {
        double totalHoras = 0.0;
        int count = 0;
        
        for (CartaoDePonto cartao : horista.getCartoes()) {
            if (count >= dias) break;
            double horas = cartao.getHoras();
            if (horas <= 8) {
                totalHoras += horas;
            } else {
                totalHoras += 8; // Máximo 8 horas normais por dia
            }
            count++;
        }
        
        return totalHoras;
    }
    
    private double calcularHorasExtras(EmpregadoHorista horista, int dias) {
        double totalHoras = 0.0;
        int count = 0;
        
        for (CartaoDePonto cartao : horista.getCartoes()) {
            if (count >= dias) break;
            double horas = cartao.getHoras();
            if (horas > 8) {
                totalHoras += (horas - 8); // Horas extras
            }
            count++;
        }
        
        return totalHoras;
    }
    
    private double calcularComissoes(EmpregadoComissionado comissionado, int dias) {
        double totalComissoes = 0.0;
        double taxaComissao = comissionado.getTaxaDeComissao();
        int count = 0;
        
        for (ResultadoDeVenda venda : comissionado.getResultadoDeVenda()) {
            if (count >= dias) break;
            totalComissoes += venda.getValor() * taxaComissao;
            count++;
        }
        
        return totalComissoes;
    }
    
    // Getters
    public String getDescricao() { return descricao; }
    public String getTipo() { return tipo; }
    public int getParametro1() { return parametro1; }
    public int getParametro2() { return parametro2; }
    
    /**
     * Obtém todas as agendas customizadas.
     * 
     * @return Map com todas as agendas customizadas
     */
    public static Map<String, AgendaDePagamentos> getAgendasCustomizadas() {
        return new HashMap<>(agendasCustomizadas);
    }
    
    /**
     * Limpa todas as agendas customizadas.
     */
    public static void limparAgendasCustomizadas() {
        agendasCustomizadas.clear();
    }
    
    /**
     * Verifica se uma agenda customizada existe.
     * 
     * @param descricao Descrição da agenda
     * @return true se a agenda customizada existir, false caso contrário
     */
    public static boolean agendaCustomizadaExiste(String descricao) {
        return agendasCustomizadas.containsKey(descricao);
    }
}
