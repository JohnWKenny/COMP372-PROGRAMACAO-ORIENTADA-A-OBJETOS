package br.ufal.ic.p2.wepayu.models;

/**
 * Classe que representa uma agenda de pagamento no sistema WePayU.
 * 
 * <p>Uma agenda de pagamento define quando e como um empregado deve ser pago.
 * Existem diferentes tipos de agendas suportadas pelo sistema:</p>
 * 
 * <ul>
 *   <li><strong>semanal X:</strong> Pagamento semanal em um dia específico da semana</li>
 *   <li><strong>semanal X Y:</strong> Pagamento a cada X semanas em um dia específico</li>
 *   <li><strong>mensal $:</strong> Pagamento mensal no último dia útil do mês</li>
 * </ul>
 * 
 * <p>Agendas padrão por tipo de empregado:</p>
 * <ul>
 *   <li><strong>Horista:</strong> "semanal 5" (toda sexta-feira)</li>
 *   <li><strong>Assalariado:</strong> "mensal $" (último dia útil do mês)</li>
 *   <li><strong>Comissionado:</strong> "semanal 2 5" (a cada duas semanas na sexta-feira)</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
 * @since 2025
 */
public class AgendaPagamento {
    private String agenda;
    
    // Agendas válidas disponíveis no sistema
    public static final String SEMANAL_5 = "semanal 5";
    public static final String SEMANAL_2_5 = "semanal 2 5";
    public static final String MENSAL_DOLAR = "mensal $";
    
    /**
     * Construtor padrão da classe AgendaPagamento.
     */
    public AgendaPagamento() {
        this.agenda = SEMANAL_5; // Padrão
    }
    
    /**
     * Construtor da classe AgendaPagamento com agenda específica.
     * 
     * @param agenda String representando a agenda de pagamento
     */
    public AgendaPagamento(String agenda) {
        this.agenda = agenda;
    }
    
    /**
     * Obtém a agenda de pagamento.
     * 
     * @return String representando a agenda de pagamento
     */
    public String getAgenda() {
        return agenda;
    }
    
    /**
     * Define a agenda de pagamento.
     * 
     * @param agenda Nova agenda de pagamento
     */
    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }
    
    /**
     * Verifica se uma agenda é válida.
     * 
     * <p>Uma agenda é considerada válida se estiver entre as agendas
     * disponíveis no sistema (padrão ou customizadas).</p>
     * 
     * @param agenda Agenda a ser verificada
     * @return true se a agenda for válida, false caso contrário
     */
    public static boolean isAgendaValida(String agenda) {
        return SEMANAL_5.equals(agenda) || 
               SEMANAL_2_5.equals(agenda) || 
               MENSAL_DOLAR.equals(agenda) ||
               AgendaDePagamentos.isAgendaValida(agenda);
    }
    
    /**
     * Obtém a agenda padrão para um tipo de empregado.
     * 
     * @param tipo Tipo do empregado ("horista", "assalariado", "comissionado")
     * @return Agenda padrão para o tipo de empregado
     */
    public static String getAgendaPadrao(String tipo) {
        switch (tipo) {
            case "horista":
                return SEMANAL_5;
            case "assalariado":
                return MENSAL_DOLAR;
            case "comissionado":
                return SEMANAL_2_5;
            default:
                return SEMANAL_5; // Padrão geral
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
            // Se é uma agenda padrão, usa o cálculo original
            if (SEMANAL_5.equals(agenda) || SEMANAL_2_5.equals(agenda) || MENSAL_DOLAR.equals(agenda)) {
                return devePagarNaDataPadrao(data);
            } else {
                // Se é uma agenda customizada, delega para AgendaDePagamentos
                AgendaDePagamentos agendaCustomizada = AgendaDePagamentos.getAgenda(agenda);
                if (agendaCustomizada != null) {
                    return agendaCustomizada.devePagarNaData(data);
                }
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    
    private boolean devePagarNaDataPadrao(String data) {
        try {
            String[] partes = data.split("/");
            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            int ano = Integer.parseInt(partes[2]);
            
            java.time.LocalDate localDate = java.time.LocalDate.of(ano, mes, dia);
            java.time.DayOfWeek diaSemana = localDate.getDayOfWeek();
            
            switch (agenda) {
                case SEMANAL_5:
                    // Pagamento toda sexta-feira
                    return diaSemana == java.time.DayOfWeek.FRIDAY;
                    
                case SEMANAL_2_5:
                    // Pagamento a cada duas semanas na sexta-feira
                    // Primeira sexta-feira quinzenal é 14/1/2005
                    java.time.LocalDate primeiroPagamento = java.time.LocalDate.of(2005, 1, 14);
                    
                    if (localDate.isBefore(primeiroPagamento)) {
                        return false;
                    }
                    
                    // Verifica se é um dia de pagamento quinzenal
                    long diasEntre = java.time.temporal.ChronoUnit.DAYS.between(primeiroPagamento, localDate);
                    return diaSemana == java.time.DayOfWeek.FRIDAY && diasEntre % 14 == 0;
                    
                case MENSAL_DOLAR:
                    // Pagamento no último dia do mês
                    return localDate.equals(localDate.withDayOfMonth(localDate.lengthOfMonth()));
                    
                default:
                    return false;
            }
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
        // Se é uma agenda padrão, usa o cálculo original
        if (SEMANAL_5.equals(agenda) || SEMANAL_2_5.equals(agenda) || MENSAL_DOLAR.equals(agenda)) {
            return calcularValorPagamentoPadrao(empregado, dataInicial, dataFinal);
        } else {
            // Se é uma agenda customizada, delega para AgendaDePagamentos
            AgendaDePagamentos agendaCustomizada = AgendaDePagamentos.getAgenda(agenda);
            if (agendaCustomizada != null) {
                return agendaCustomizada.calcularValorPagamento(empregado, dataInicial, dataFinal);
            }
            return 0.0;
        }
    }
    
    private double calcularValorPagamentoPadrao(Empregado empregado, String dataInicial, String dataFinal) {
        String tipo = empregado.getTipo();
        
        switch (agenda) {
            case SEMANAL_5:
                // Para horistas: horas trabalhadas na semana
                // Para assalariados: salário anual / 52 semanas
                // Para comissionados: salário base anual / 52 + comissões da semana
                if ("horista".equals(tipo)) {
                    return calcularPagamentoHoristaSemanal(empregado, dataInicial, dataFinal);
                } else if ("assalariado".equals(tipo)) {
                    return calcularPagamentoAssalariadoSemanal(empregado);
                } else if ("comissionado".equals(tipo)) {
                    return calcularPagamentoComissionadoSemanal(empregado, dataInicial, dataFinal);
                }
                break;
                
            case SEMANAL_2_5:
                // Para horistas: horas trabalhadas nas duas semanas
                // Para assalariados: salário anual / 26 períodos
                // Para comissionados: salário base anual / 26 + comissões das duas semanas
                if ("horista".equals(tipo)) {
                    return calcularPagamentoHoristaBiSemanal(empregado, dataInicial, dataFinal);
                } else if ("assalariado".equals(tipo)) {
                    return calcularPagamentoAssalariadoBiSemanal(empregado);
                } else if ("comissionado".equals(tipo)) {
                    return calcularPagamentoComissionadoBiSemanal(empregado, dataInicial, dataFinal);
                }
                break;
                
            case MENSAL_DOLAR:
                // Para horistas: horas trabalhadas no mês
                // Para assalariados: salário mensal
                // Para comissionados: salário base mensal + comissões do mês
                if ("horista".equals(tipo)) {
                    return calcularPagamentoHoristaMensal(empregado, dataInicial, dataFinal);
                } else if ("assalariado".equals(tipo)) {
                    return calcularPagamentoAssalariadoMensal(empregado);
                } else if ("comissionado".equals(tipo)) {
                    return calcularPagamentoComissionadoMensal(empregado, dataInicial, dataFinal);
                }
                break;
        }
        
        return 0.0;
    }
    
    private double calcularPagamentoHoristaSemanal(Empregado empregado, String dataInicial, String dataFinal) {
        if (!(empregado instanceof EmpregadoHorista)) return 0.0;
        
        EmpregadoHorista horista = (EmpregadoHorista) empregado;
        double salarioHora = horista.getSalarioPorHora();
        
        // Calcula horas normais e extras da semana
        double horasNormais = calcularHorasNormais(horista, dataInicial, dataFinal);
        double horasExtras = calcularHorasExtras(horista, dataInicial, dataFinal);
        
        return (horasNormais * salarioHora) + (horasExtras * salarioHora * 1.5);
    }
    
    private double calcularPagamentoHoristaBiSemanal(Empregado empregado, String dataInicial, String dataFinal) {
        if (!(empregado instanceof EmpregadoHorista)) return 0.0;
        
        EmpregadoHorista horista = (EmpregadoHorista) empregado;
        double salarioHora = horista.getSalarioPorHora();
        
        // Calcula horas normais e extras das duas semanas
        double horasNormais = calcularHorasNormais(horista, dataInicial, dataFinal);
        double horasExtras = calcularHorasExtras(horista, dataInicial, dataFinal);
        
        return (horasNormais * salarioHora) + (horasExtras * salarioHora * 1.5);
    }
    
    private double calcularPagamentoHoristaMensal(Empregado empregado, String dataInicial, String dataFinal) {
        if (!(empregado instanceof EmpregadoHorista)) return 0.0;
        
        EmpregadoHorista horista = (EmpregadoHorista) empregado;
        double salarioHora = horista.getSalarioPorHora();
        
        // Calcula horas normais e extras do mês
        double horasNormais = calcularHorasNormais(horista, dataInicial, dataFinal);
        double horasExtras = calcularHorasExtras(horista, dataInicial, dataFinal);
        
        return (horasNormais * salarioHora) + (horasExtras * salarioHora * 1.5);
    }
    
    private double calcularPagamentoAssalariadoSemanal(Empregado empregado) {
        if (!(empregado instanceof EmpregadoAssalariado)) return 0.0;
        
        EmpregadoAssalariado assalariado = (EmpregadoAssalariado) empregado;
        double salarioAnual = assalariado.getSalarioMensal() * 12;
        return salarioAnual / 52; // Divide por 52 semanas
    }
    
    private double calcularPagamentoAssalariadoBiSemanal(Empregado empregado) {
        if (!(empregado instanceof EmpregadoAssalariado)) return 0.0;
        
        EmpregadoAssalariado assalariado = (EmpregadoAssalariado) empregado;
        double salarioAnual = assalariado.getSalarioMensal() * 12;
        return salarioAnual / 26; // Divide por 26 períodos de duas semanas
    }
    
    private double calcularPagamentoAssalariadoMensal(Empregado empregado) {
        if (!(empregado instanceof EmpregadoAssalariado)) return 0.0;
        
        EmpregadoAssalariado assalariado = (EmpregadoAssalariado) empregado;
        return assalariado.getSalarioMensal();
    }
    
    private double calcularPagamentoComissionadoSemanal(Empregado empregado, String dataInicial, String dataFinal) {
        if (!(empregado instanceof EmpregadoComissionado)) return 0.0;
        
        EmpregadoComissionado comissionado = (EmpregadoComissionado) empregado;
        double salarioAnual = comissionado.getSalarioMensal() * 12;
        double salarioSemanal = salarioAnual / 52;
        
        // Calcula comissões da semana
        double comissoes = calcularComissoes(comissionado, dataInicial, dataFinal);
        
        return salarioSemanal + comissoes;
    }
    
    private double calcularPagamentoComissionadoBiSemanal(Empregado empregado, String dataInicial, String dataFinal) {
        if (!(empregado instanceof EmpregadoComissionado)) return 0.0;
        
        EmpregadoComissionado comissionado = (EmpregadoComissionado) empregado;
        double salarioAnual = comissionado.getSalarioMensal() * 12;
        double salarioBiSemanal = salarioAnual / 26;
        
        // Calcula comissões das duas semanas
        double comissoes = calcularComissoes(comissionado, dataInicial, dataFinal);
        
        return salarioBiSemanal + comissoes;
    }
    
    private double calcularPagamentoComissionadoMensal(Empregado empregado, String dataInicial, String dataFinal) {
        if (!(empregado instanceof EmpregadoComissionado)) return 0.0;
        
        EmpregadoComissionado comissionado = (EmpregadoComissionado) empregado;
        double salarioMensal = comissionado.getSalarioMensal();
        
        // Calcula comissões do mês
        double comissoes = calcularComissoes(comissionado, dataInicial, dataFinal);
        
        return salarioMensal + comissoes;
    }
    
    private double calcularHorasNormais(EmpregadoHorista horista, String dataInicial, String dataFinal) {
        double totalHoras = 0.0;
        
        for (CartaoDePonto cartao : horista.getCartoes()) {
            if (estaNoPeriodo(cartao.getData(), dataInicial, dataFinal)) {
                double horas = cartao.getHoras();
                if (horas <= 8) {
                    totalHoras += horas;
                } else {
                    totalHoras += 8; // Máximo 8 horas normais por dia
                }
            }
        }
        
        return totalHoras;
    }
    
    private double calcularHorasExtras(EmpregadoHorista horista, String dataInicial, String dataFinal) {
        double totalHoras = 0.0;
        
        for (CartaoDePonto cartao : horista.getCartoes()) {
            if (estaNoPeriodo(cartao.getData(), dataInicial, dataFinal)) {
                double horas = cartao.getHoras();
                if (horas > 8) {
                    totalHoras += (horas - 8); // Horas extras
                }
            }
        }
        
        return totalHoras;
    }
    
    private double calcularComissoes(EmpregadoComissionado comissionado, String dataInicial, String dataFinal) {
        double totalComissoes = 0.0;
        double taxaComissao = comissionado.getTaxaDeComissao();
        
        for (ResultadoDeVenda venda : comissionado.getResultadoDeVenda()) {
            if (estaNoPeriodo(venda.getData(), dataInicial, dataFinal)) {
                totalComissoes += venda.getValor() * taxaComissao;
            }
        }
        
        return totalComissoes;
    }
    
    private boolean estaNoPeriodo(String data, String dataInicial, String dataFinal) {
        try {
            String[] partesData = data.split("/");
            String[] partesInicial = dataInicial.split("/");
            String[] partesFinal = dataFinal.split("/");
            
            int dia = Integer.parseInt(partesData[0]);
            int mes = Integer.parseInt(partesData[1]);
            int ano = Integer.parseInt(partesData[2]);
            
            int diaInicial = Integer.parseInt(partesInicial[0]);
            int mesInicial = Integer.parseInt(partesInicial[1]);
            int anoInicial = Integer.parseInt(partesInicial[2]);
            
            int diaFinal = Integer.parseInt(partesFinal[0]);
            int mesFinal = Integer.parseInt(partesFinal[1]);
            int anoFinal = Integer.parseInt(partesFinal[2]);
            
            java.time.LocalDate localDate = java.time.LocalDate.of(ano, mes, dia);
            java.time.LocalDate dataInicialLocal = java.time.LocalDate.of(anoInicial, mesInicial, diaInicial);
            java.time.LocalDate dataFinalLocal = java.time.LocalDate.of(anoFinal, mesFinal, diaFinal);
            
            return !localDate.isBefore(dataInicialLocal) && !localDate.isAfter(dataFinalLocal);
        } catch (Exception e) {
            return false;
        }
    }
}
