package br.ufal.ic.p2.wepayu.services.impl;

import br.ufal.ic.p2.wepayu.services.FolhaPagamentoService;
import br.ufal.ic.p2.wepayu.models.*;
import br.ufal.ic.p2.wepayu.Exception.*;
import br.ufal.ic.p2.wepayu.utils.ValorMonetarioUtils;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * Implementação do serviço de folha de pagamento no sistema WePayU.
 * 
 * <p>Esta classe implementa todas as operações relacionadas ao processamento
 * e cálculo da folha de pagamento, incluindo cálculo de totais e geração
 * de arquivos de folha para diferentes tipos de empregados.</p>
 * 
 * <p>Funcionalidades implementadas:</p>
 * <ul>
 *   <li>Cálculo do total da folha de pagamento</li>
 *   <li>Processamento de folha para empregados assalariados</li>
 *   <li>Processamento de folha para empregados horistas com sistema de dívida sindical</li>
 *   <li>Processamento de folha para empregados comissionados</li>
 *   <li>Geração de arquivos de folha de pagamento</li>
 * </ul>
 * 
 * <p><strong>Sistema de Dívida Sindical para Horistas:</strong></p>
 * <p>Os empregados horistas possuem um sistema especial de acumulação de dívida sindical:</p>
 * <ul>
 *   <li>A cada pagamento semanal, adiciona-se 7 dias de taxa sindical à dívida</li>
 *   <li>Os descontos incluem a dívida sindical acumulada + taxas de serviço do período</li>
 *   <li>Se o salário líquido for negativo, a dívida é ajustada e os descontos limitados ao salário bruto</li>
 *   <li>Se o empregado consegue pagar toda a dívida, ela é zerada</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.1
 * @since 2025
 */
public class FolhaPagamentoServiceImpl implements FolhaPagamentoService {
    
    private Map<String, Empregado> empregados;
    private Map<String, MembroSindicato> membrosSindicato;
    
    public FolhaPagamentoServiceImpl(Map<String, Empregado> empregados, Map<String, MembroSindicato> membrosSindicato) {
        this.empregados = empregados;
        this.membrosSindicato = membrosSindicato;
    }
    
    @Override
    public String totalFolha(String data) throws DataInvalidaException {
        try {
            LocalDate dataFolha = LocalDate.parse(data, DateTimeFormatter.ofPattern("d/M/yyyy"));
            BigDecimal total = BigDecimal.ZERO;

            for (Empregado empregado : empregados.values()) {
                if (deveReceberNaData(empregado, dataFolha)) {
                    total = total.add(calcularSalario(empregado, dataFolha)); // acumula com máxima precisão
                }
            }

            // Usa ValorMonetarioUtils para formatação e truncamento com DOWN
            return ValorMonetarioUtils.formatarValorMonetario(total);
        } catch (Exception e) {
            return "0,00";
        }
    }
    
    @Override
    public void rodaFolha(String data, String arquivo) throws DataInvalidaException {
        try {
            LocalDate dataFolha = LocalDate.parse(data, DateTimeFormatter.ofPattern("d/M/yyyy"));
            
            // Coleta empregados que devem receber na data
            List<Empregado> empregadosHoristas = new ArrayList<>();
            List<Empregado> empregadosAssalariados = new ArrayList<>();
            List<Empregado> empregadosComissionados = new ArrayList<>();
            
            for (Empregado empregado : empregados.values()) {
                if (deveReceberNaData(empregado, dataFolha)) {
                    switch (empregado.getTipo()) {
                        case "horista":
                            empregadosHoristas.add(empregado);
                            break;
                        case "assalariado":
                            empregadosAssalariados.add(empregado);
                            break;
                        case "comissionado":
                            empregadosComissionados.add(empregado);
                            break;
                    }
                }
            }
            
            // Ordena os empregados por nome
            empregadosHoristas.sort((e1, e2) -> e1.getNome().compareTo(e2.getNome()));
            empregadosAssalariados.sort((e1, e2) -> e1.getNome().compareTo(e2.getNome()));
            empregadosComissionados.sort((e1, e2) -> e1.getNome().compareTo(e2.getNome()));
            
            // Gera o arquivo da folha
            gerarArquivoFolha(dataFolha, arquivo, empregadosHoristas, empregadosAssalariados, empregadosComissionados);
            
        } catch (Exception e) {
            throw new DataInvalidaException("Data invalida.");
        }
    }

    private boolean deveReceberNaData(Empregado empregado, LocalDate data) {
        // Usa a agenda de pagamento do empregado
        String dataString = data.format(DateTimeFormatter.ofPattern("d/M/yyyy"));
        return empregado.getAgendaPagamento().devePagarNaData(dataString);
    }

    private boolean deveReceberHorista(EmpregadoHorista empregado, LocalDate data) {
        // Horistas são pagos toda sexta-feira
        return data.getDayOfWeek() == DayOfWeek.FRIDAY;
    }

    private boolean deveReceberAssalariado(LocalDate data) {
        // Assalariados são pagos no último dia do mês
        return data.equals(data.withDayOfMonth(data.lengthOfMonth()));
    }

    private boolean deveReceberComissionado(LocalDate data) {
        // Comissionados são pagos quinzenalmente a partir de 1/1/2005
        LocalDate primeiroPagamento = LocalDate.of(2005, 1, 14); // Primeira sexta-feira da segunda semana

        if (data.isBefore(primeiroPagamento)) {
            return false;
        }

        // Verifica se é um dia de pagamento quinzenal
        long diasEntre = ChronoUnit.DAYS.between(primeiroPagamento, data);
        return diasEntre % 14 == 0;
    }

    private BigDecimal calcularSalario(Empregado empregado, LocalDate data) {
        String tipo = empregado.getTipo();
        String agenda = empregado.getAgendaPagamento().getAgenda();
        
        // Se a agenda é a padrão para o tipo, usa o cálculo original
        if (agenda.equals(br.ufal.ic.p2.wepayu.models.AgendaPagamento.getAgendaPadrao(tipo))) {
            switch (tipo) {
                case "horista":
                    return calcularSalarioHorista((EmpregadoHorista) empregado, data);
                case "assalariado":
                    return calcularSalarioAssalariado((EmpregadoAssalariado) empregado, data);
                case "comissionado":
                    return calcularSalarioComissionado((EmpregadoComissionado) empregado, data);
                default:
                    return BigDecimal.ZERO;
            }
        } else {
            // Para agendas não-padrão, usa o novo cálculo
            String dataString = data.format(DateTimeFormatter.ofPattern("d/M/yyyy"));
            String dataInicial = calcularDataInicialPeriodo(empregado, data);
            String dataFinal = dataString;
            
            double valorPagamento = empregado.getAgendaPagamento().calcularValorPagamento(empregado, dataInicial, dataFinal);
            return BigDecimal.valueOf(valorPagamento);
        }
    }
    
    private String calcularDataInicialPeriodo(Empregado empregado, LocalDate data) {
        String agenda = empregado.getAgendaPagamento().getAgenda();
        
        switch (agenda) {
            case "semanal 5":
                // Período de 1 semana
                return data.minusDays(6).format(DateTimeFormatter.ofPattern("d/M/yyyy"));
            case "semanal 2 5":
                // Período de 2 semanas
                return data.minusDays(13).format(DateTimeFormatter.ofPattern("d/M/yyyy"));
            case "mensal $":
                // Período de 1 mês
                return data.withDayOfMonth(1).format(DateTimeFormatter.ofPattern("d/M/yyyy"));
            default:
                return data.minusDays(6).format(DateTimeFormatter.ofPattern("d/M/yyyy"));
        }
    }

    private BigDecimal calcularSalarioHorista(EmpregadoHorista empregado, LocalDate data) {
        LocalDate inicioSemana = data.minusDays(6); // semana de 7 dias
        BigDecimal horasNormais = BigDecimal.ZERO;
        BigDecimal horasExtras = BigDecimal.ZERO;

        for (CartaoDePonto cartao : empregado.getCartoes()) {
            LocalDate dataCartao = LocalDate.parse(cartao.getData(), DateTimeFormatter.ofPattern("d/M/yyyy"));
            if (!dataCartao.isBefore(inicioSemana) && !dataCartao.isAfter(data)) {
                BigDecimal horas = BigDecimal.valueOf(cartao.getHoras());
                horasNormais = horasNormais.add(horas.min(BigDecimal.valueOf(8.0)));
                horasExtras = horasExtras.add(horas.subtract(BigDecimal.valueOf(8.0)).max(BigDecimal.ZERO));
            }
        }

        BigDecimal salarioPorHora = BigDecimal.valueOf(empregado.getSalarioPorHora());
        BigDecimal salarioBruto = horasNormais.multiply(salarioPorHora)
                .add(horasExtras.multiply(salarioPorHora).multiply(BigDecimal.valueOf(1.5)));

        return salarioBruto; // sem setScale aqui
    }

    private BigDecimal calcularSalarioAssalariado(EmpregadoAssalariado empregado, LocalDate data) {
        return BigDecimal.valueOf(empregado.getSalarioMensal()); // bruto sem arredondar ainda
    }

    private BigDecimal calcularSalarioComissionado(EmpregadoComissionado empregado, LocalDate data) {
        BigDecimal salarioMensal = BigDecimal.valueOf(empregado.getSalarioMensal());
        BigDecimal salarioBase = salarioMensal.multiply(BigDecimal.valueOf(12))
                .divide(BigDecimal.valueOf(26), 10, RoundingMode.DOWN); // 10 casas decimais para precisão
        // Trunca o salário base para 2 casas decimais
        salarioBase = salarioBase.setScale(2, RoundingMode.DOWN);

        LocalDate inicioPeriodo = data.minusDays(14);
        BigDecimal totalVendas = BigDecimal.ZERO;

        for (ResultadoDeVenda venda : empregado.getResultadoDeVenda()) {
            LocalDate dataVenda = LocalDate.parse(venda.getData(), DateTimeFormatter.ofPattern("d/M/yyyy"));
            if (!dataVenda.isBefore(inicioPeriodo) && !dataVenda.isAfter(data)) {
                totalVendas = totalVendas.add(BigDecimal.valueOf(venda.getValor()));
            }
        }

        BigDecimal comissao = totalVendas.multiply(BigDecimal.valueOf(empregado.getTaxaDeComissao()));
        // Trunca a comissão para 2 casas decimais antes de somar
        comissao = comissao.setScale(2, RoundingMode.DOWN);
        BigDecimal salarioBruto = salarioBase.add(comissao);

        return salarioBruto; // sem arredondar ainda
    }

    private void gerarArquivoFolha(LocalDate dataFolha, String arquivo,
                                   List<Empregado> horistas, List<Empregado> assalariados,
                                   List<Empregado> comissionados) throws IOException {

        try (FileWriter writer = new FileWriter(arquivo)) {
            // Cabeçalho idêntico ao do arquivo exemplo
            writer.write(String.format("FOLHA DE PAGAMENTO DO DIA %s\n", dataFolha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
            writer.write("====================================\n\n");

            // Seção Horistas (títulos e separadores exatamente iguais ao exemplo)
            writer.write("===============================================================================================================================\n");
            writer.write("===================== HORISTAS ================================================================================================\n");
            writer.write("===============================================================================================================================\n");
            writer.write("Nome                                 Horas Extra Salario Bruto Descontos Salario Liquido Metodo\n");
            writer.write("==================================== ===== ===== ============= ========= =============== ======================================\n");

            // Totais horistas
            BigDecimal totalHoristasBruto = BigDecimal.ZERO;
            BigDecimal totalHoristasDescontos = BigDecimal.ZERO;
            BigDecimal totalHoristasLiquido = BigDecimal.ZERO;
            int totalHorasNormais = 0;
            int totalHorasExtras = 0;

            // Colunas com larguras fixas conforme modelo:
            // name: 40, horas: 6, extra: 10, salarioBruto: 11, descontos: 15, salarioLiquido: 7, metodo: resto
            for (Empregado empregado : horistas) {
                EmpregadoHorista horista = (EmpregadoHorista) empregado;
                
                // Primeiro: acumula taxa sindical semanal (seguindo a lógica do WePayU - o)
                if (horista.getSindicato() != null) {
                    MembroSindicato sindicato = horista.getSindicato();
                    BigDecimal taxaSemanal = new BigDecimal(String.valueOf(sindicato.getTaxaSindical()))
                            .multiply(BigDecimal.valueOf(7))
                            .setScale(2, RoundingMode.DOWN);
                    BigDecimal dividaAtual = new BigDecimal(String.valueOf(sindicato.getDividaSindical()));
                    BigDecimal novaDivida = dividaAtual.add(taxaSemanal);
                    sindicato.setDividaSindical(novaDivida.doubleValue());
                }
                
                BigDecimal salarioBruto = calcularSalarioHorista(horista, dataFolha);
                BigDecimal descontos = BigDecimal.ZERO;
                BigDecimal salarioLiquido = BigDecimal.ZERO;
                
                if (salarioBruto.compareTo(BigDecimal.ZERO) > 0) {
                    descontos = calcularDescontos(horista, dataFolha);
                    salarioLiquido = salarioBruto.subtract(descontos);
                    
                    // Se o salário líquido for negativo, ajusta os descontos e atualiza a dívida sindical
                    if (salarioLiquido.compareTo(BigDecimal.ZERO) < 0) {
                        if (horista.getSindicato() != null) {
                            MembroSindicato sindicato = horista.getSindicato();
                            BigDecimal dividaRestante = descontos.subtract(salarioBruto);
                            sindicato.setDividaSindical(dividaRestante.doubleValue());
                            descontos = salarioBruto;
                            salarioLiquido = BigDecimal.ZERO;
                        }
                    } else {
                        // Se conseguiu pagar tudo, zera a dívida sindical
                        if (horista.getSindicato() != null) {
                            horista.getSindicato().setDividaSindical(0.0);
                        }
                    }
                }

                int[] horas = calcularHorasHorista(horista, dataFolha);
                totalHorasNormais += horas[0];
                totalHorasExtras += horas[1];
                String metodoPagamento = formatarMetodoPagamento(horista.getMetodoPagamento(), horista.getEndereco());

                writer.write(String.format("%-36s %5d %5d %13s %9s %15s %s\n",
                    horista.getNome(),
                    horas[0], // horas normais
                    horas[1], // horas extras
                    ValorMonetarioUtils.formatarValorMonetario(salarioBruto),
                    ValorMonetarioUtils.formatarValorMonetario(descontos),
                    ValorMonetarioUtils.formatarValorMonetario(salarioLiquido),
                    metodoPagamento
                ));

                totalHoristasBruto = totalHoristasBruto.add(salarioBruto);
                totalHoristasDescontos = totalHoristasDescontos.add(descontos);
                totalHoristasLiquido = totalHoristasLiquido.add(salarioLiquido);
            }

             writer.write(String.format("\nTOTAL HORISTAS                       %5d %5d %13s %9s %15s\n\n",
                 totalHorasNormais,
                 totalHorasExtras,
                 ValorMonetarioUtils.formatarValorMonetario(totalHoristasBruto),
                 ValorMonetarioUtils.formatarValorMonetario(totalHoristasDescontos),
                 ValorMonetarioUtils.formatarValorMonetario(totalHoristasLiquido)
             ));

            // Seção Assalariados (mesmo layout do exemplo)
            writer.write("===============================================================================================================================\n");
            writer.write("===================== ASSALARIADOS ============================================================================================\n");
            writer.write("===============================================================================================================================\n");
            writer.write("Nome                                             Salario Bruto Descontos Salario Liquido Metodo\n");
             writer.write("================================================ ============= ========= =============== ======================================\n");

             BigDecimal totalAssalariadosBruto = BigDecimal.ZERO;
             BigDecimal totalAssalariadosDescontos = BigDecimal.ZERO;
             BigDecimal totalAssalariadosLiquido = BigDecimal.ZERO;

             for (Empregado empregado : assalariados) {
                 EmpregadoAssalariado assalariado = (EmpregadoAssalariado) empregado;
                 BigDecimal salarioBruto = calcularSalarioAssalariado(assalariado, dataFolha);
                 BigDecimal descontos = calcularDescontos(assalariado, dataFolha);
                 BigDecimal salarioLiquido = salarioBruto.subtract(descontos).max(BigDecimal.ZERO);
                 String metodoPagamento = formatarMetodoPagamento(assalariado.getMetodoPagamento(), assalariado.getEndereco());

                 writer.write(String.format("%-48s %13s %9s %15s %s\n",
                     assalariado.getNome(),
                     ValorMonetarioUtils.formatarValorMonetario(salarioBruto),
                     ValorMonetarioUtils.formatarValorMonetario(descontos),
                     ValorMonetarioUtils.formatarValorMonetario(salarioLiquido),
                     metodoPagamento
                 ));

                 totalAssalariadosBruto = totalAssalariadosBruto.add(salarioBruto);
                 totalAssalariadosDescontos = totalAssalariadosDescontos.add(descontos);
                 totalAssalariadosLiquido = totalAssalariadosLiquido.add(salarioLiquido);
             }

             writer.write(String.format("\nTOTAL ASSALARIADOS                               %13s %9s %15s\n\n",
                 ValorMonetarioUtils.formatarValorMonetario(totalAssalariadosBruto),
                 ValorMonetarioUtils.formatarValorMonetario(totalAssalariadosDescontos),
                 ValorMonetarioUtils.formatarValorMonetario(totalAssalariadosLiquido)
             ));

            // Seção Comissionados (mesmo layout do exemplo)
            writer.write("===============================================================================================================================\n");
            writer.write("===================== COMISSIONADOS ===========================================================================================\n");
            writer.write("===============================================================================================================================\n");
            writer.write("Nome                  Fixo     Vendas   Comissao Salario Bruto Descontos Salario Liquido Metodo\n");
            writer.write("===================== ======== ======== ======== ============= ========= =============== ======================================\n");

            BigDecimal totalComissionadosFixo = BigDecimal.ZERO;
            BigDecimal totalComissionadosVendas = BigDecimal.ZERO;
            BigDecimal totalComissionadosComissao = BigDecimal.ZERO;
            BigDecimal totalComissionadosBruto = BigDecimal.ZERO;
            BigDecimal totalComissionadosDescontos = BigDecimal.ZERO;
            BigDecimal totalComissionadosLiquido = BigDecimal.ZERO;

            for (Empregado empregado : comissionados) {
                EmpregadoComissionado comissionado = (EmpregadoComissionado) empregado;
                BigDecimal[] valores = calcularValoresComissionado(comissionado, dataFolha);
                BigDecimal salarioBruto = valores[0];
                BigDecimal vendas = valores[1];
                BigDecimal comissao = valores[2];
                BigDecimal salarioBase = valores[3];
                BigDecimal descontos = calcularDescontos(comissionado, dataFolha);
                BigDecimal salarioLiquido = salarioBruto.subtract(descontos).max(BigDecimal.ZERO);
                String metodoPagamento = formatarMetodoPagamento(comissionado.getMetodoPagamento(), comissionado.getEndereco());

                writer.write(String.format("%-21s %8s %8s %8s %13s %9s %15s %s\n",
                    comissionado.getNome(),
                    ValorMonetarioUtils.formatarValorMonetario(salarioBase),
                    ValorMonetarioUtils.formatarValorMonetario(vendas),
                    ValorMonetarioUtils.formatarValorMonetario(comissao),
                    ValorMonetarioUtils.formatarValorMonetario(salarioBruto),
                    ValorMonetarioUtils.formatarValorMonetario(descontos),
                    ValorMonetarioUtils.formatarValorMonetario(salarioLiquido),
                    metodoPagamento
                ));

                totalComissionadosFixo = totalComissionadosFixo.add(salarioBase);
                totalComissionadosVendas = totalComissionadosVendas.add(vendas);
                totalComissionadosComissao = totalComissionadosComissao.add(comissao);
                totalComissionadosBruto = totalComissionadosBruto.add(salarioBruto);
                totalComissionadosDescontos = totalComissionadosDescontos.add(descontos);
                totalComissionadosLiquido = totalComissionadosLiquido.add(salarioLiquido);
            }

            writer.write(String.format("\nTOTAL COMISSIONADOS   %8s %8s %8s %13s %9s %15s\n",
                ValorMonetarioUtils.formatarValorMonetario(totalComissionadosFixo),
                ValorMonetarioUtils.formatarValorMonetario(totalComissionadosVendas),
                ValorMonetarioUtils.formatarValorMonetario(totalComissionadosComissao),
                ValorMonetarioUtils.formatarValorMonetario(totalComissionadosBruto),
                ValorMonetarioUtils.formatarValorMonetario(totalComissionadosDescontos),
                ValorMonetarioUtils.formatarValorMonetario(totalComissionadosLiquido)
            ));

            // Total da folha
            BigDecimal totalFolha = totalHoristasBruto.add(totalAssalariadosBruto).add(totalComissionadosBruto);
            writer.write(String.format("\nTOTAL FOLHA: %s\n", ValorMonetarioUtils.formatarValorMonetario(totalFolha)));
        }
    }

    /**
     * Calcula os descontos para um empregado baseado em seu tipo.
     * 
     * <p>Este método implementa diferentes estratégias de cálculo de descontos
     * dependendo do tipo do empregado:</p>
     * <ul>
     *   <li><strong>Horistas:</strong> Usa sistema de dívida sindical acumulada</li>
     *   <li><strong>Assalariados:</strong> Taxa sindical mensal + taxas de serviço do mês</li>
     *   <li><strong>Comissionados:</strong> Taxa sindical quinzenal + taxas de serviço da quinzena</li>
     * </ul>
     * 
     * @param empregado O empregado para calcular os descontos
     * @param data A data do pagamento
     * @return O valor total dos descontos para o empregado
     */
    private BigDecimal calcularDescontos(Empregado empregado, LocalDate data) {
        if (empregado.getSindicato() == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.DOWN);
        }

        MembroSindicato sindicato = empregado.getSindicato();
        BigDecimal descontos = BigDecimal.ZERO;

        // Para horistas, usa a lógica de dívida sindical acumulada
        if (empregado.getTipo().equals("horista")) {
            return calcularDescontosHorista(empregado, sindicato, data);
        }

        // Para outros tipos, usa a lógica original
        int diasPeriodo = calcularDiasPeriodo(empregado, data);
        BigDecimal taxaDiaria = new BigDecimal(String.valueOf(sindicato.getTaxaSindical()));
        BigDecimal taxaSindicalTotal = taxaDiaria.multiply(BigDecimal.valueOf(diasPeriodo))
                .setScale(2, RoundingMode.DOWN);
        descontos = descontos.add(taxaSindicalTotal);

        LocalDate inicioPeriodo = data.minusDays(diasPeriodo);

        for (TaxaServico taxa : sindicato.getTaxasDeServicos()) {
            LocalDate dataTaxa = LocalDate.parse(taxa.getData(), DateTimeFormatter.ofPattern("d/M/yyyy"));
            if (!dataTaxa.isBefore(inicioPeriodo) && !dataTaxa.isAfter(data)) {
                BigDecimal valorTaxa = new BigDecimal(String.valueOf(taxa.getValor()))
                        .setScale(2, RoundingMode.DOWN);
                descontos = descontos.add(valorTaxa);
            }
        }

        return descontos.setScale(2, RoundingMode.DOWN);
    }

    /**
     * Calcula os descontos específicos para empregados horistas.
     * 
     * <p>Para horistas, os descontos incluem:</p>
     * <ul>
     *   <li>Dívida sindical acumulada (gerenciada pelo sistema de dívida)</li>
     *   <li>Taxas de serviço do período atual (última semana)</li>
     * </ul>
     * 
     * @param empregado O empregado horista
     * @param sindicato O membro do sindicato associado
     * @param data A data do pagamento
     * @return O valor total dos descontos para o horista
     */
    private BigDecimal calcularDescontosHorista(Empregado empregado, MembroSindicato sindicato, LocalDate data) {
        // Calcula taxas de serviço do período (última semana)
        LocalDate inicioSemana = data.minusDays(6);
        BigDecimal taxasServico = BigDecimal.ZERO;
        
        for (TaxaServico taxa : sindicato.getTaxasDeServicos()) {
            LocalDate dataTaxa = LocalDate.parse(taxa.getData(), DateTimeFormatter.ofPattern("d/M/yyyy"));
            if (!dataTaxa.isBefore(inicioSemana) && !dataTaxa.isAfter(data)) {
                BigDecimal valorTaxa = new BigDecimal(String.valueOf(taxa.getValor()))
                        .setScale(2, RoundingMode.DOWN);
                taxasServico = taxasServico.add(valorTaxa);
            }
        }
        
        // Total de descontos = dívida sindical atual + taxas de serviço
        BigDecimal dividaAtual = new BigDecimal(String.valueOf(sindicato.getDividaSindical()));
        BigDecimal totalDescontos = dividaAtual.add(taxasServico);
        
        return totalDescontos.setScale(2, RoundingMode.DOWN);
    }


    private int calcularDiasPeriodo(Empregado empregado, LocalDate data) {
        switch (empregado.getTipo()) {
            case "horista":
                return calcularDiasPeriodoHorista(empregado, data);
            case "assalariado":
                return data.lengthOfMonth(); // mês completo
            case "comissionado":
                return 14; // quinzena
            default:
                return 1;
        }
    }

    private int calcularDiasPeriodoHorista(Empregado empregado, LocalDate data) {
        // Horistas são pagos toda sexta-feira
        // Calcula quantas semanas se passaram desde o último pagamento
        
        // Primeiro pagamento dos horistas é em 7/1/2005
        LocalDate primeiroPagamento = LocalDate.of(2005, 1, 7);
        
        if (data.isBefore(primeiroPagamento)) {
            return 0;
        }
        
        // Calcula o último pagamento (última sexta-feira antes da data atual)
        LocalDate ultimoPagamento = calcularUltimoPagamentoHorista(data);
        
        // Calcula quantas semanas se passaram desde o último pagamento
        long diasEntre = ChronoUnit.DAYS.between(ultimoPagamento, data);
        
        // Para horistas, cada semana corresponde a 7 dias de taxa sindical
        // Calcula quantas semanas completas se passaram
        int semanas = (int) (diasEntre / 7);
        
        return semanas * 7;
    }
    
    private LocalDate calcularUltimoPagamentoHorista(LocalDate data) {
        // Primeiro pagamento dos horistas é em 7/1/2005
        LocalDate primeiroPagamento = LocalDate.of(2005, 1, 7);
        
        if (data.isBefore(primeiroPagamento)) {
            return primeiroPagamento;
        }
        
        // Calcula quantas semanas se passaram desde o primeiro pagamento
        long diasEntre = ChronoUnit.DAYS.between(primeiroPagamento, data);
        int semanas = (int) (diasEntre / 7);
        
        // Retorna a data do último pagamento (semanas * 7 dias após o primeiro pagamento)
        return primeiroPagamento.plusDays(semanas * 7);
    }
    private int[] calcularHorasHorista(EmpregadoHorista empregado, LocalDate data) {
        LocalDate inicioSemana = data.minusDays(6);
        int horasNormais = 0;
        int horasExtras = 0;

        for (CartaoDePonto cartao : empregado.getCartoes()) {
            LocalDate dataCartao = LocalDate.parse(cartao.getData(), DateTimeFormatter.ofPattern("d/M/yyyy"));
            if (!dataCartao.isBefore(inicioSemana) && !dataCartao.isAfter(data)) {
                int horas = cartao.getHoras().intValue();
                horasNormais += Math.min(horas, 8);
                horasExtras += Math.max(horas - 8, 0);
            }
        }

        return new int[]{horasNormais, horasExtras};
    }

    private BigDecimal[] calcularValoresComissionado(EmpregadoComissionado empregado, LocalDate data) {
        BigDecimal salarioMensal = BigDecimal.valueOf(empregado.getSalarioMensal());
        BigDecimal salarioBase = salarioMensal.multiply(BigDecimal.valueOf(12))
                .divide(BigDecimal.valueOf(26), 10, RoundingMode.DOWN)
                .setScale(2, RoundingMode.DOWN);

        LocalDate inicioPeriodo = data.minusDays(14);
        BigDecimal totalVendas = BigDecimal.ZERO;

        for (ResultadoDeVenda venda : empregado.getResultadoDeVenda()) {
            LocalDate dataVenda = LocalDate.parse(venda.getData(), DateTimeFormatter.ofPattern("d/M/yyyy"));
            if (!dataVenda.isBefore(inicioPeriodo) && !dataVenda.isAfter(data)) {
                totalVendas = totalVendas.add(BigDecimal.valueOf(venda.getValor()));
            }
        }

        BigDecimal comissao = totalVendas.multiply(BigDecimal.valueOf(empregado.getTaxaDeComissao()))
                .setScale(2, RoundingMode.DOWN);
        BigDecimal salarioBruto = salarioBase.add(comissao);

        return new BigDecimal[]{salarioBruto, totalVendas, comissao, salarioBase};
    }

    private String formatarMetodoPagamento(MetodoPagamento metodo, String endereco) {
        if (metodo == null) {
            return "Em maos";
        }

        if (metodo instanceof EmMaos) {
            return "Em maos";
        } else if (metodo instanceof Banco) {
            Banco banco = (Banco) metodo;
            return String.format("Banco do Brasil, Ag. %s CC %s", banco.getAgencia(), banco.getContaCorrente());
        } else if (metodo instanceof Correios) {
            return String.format("Correios, %s", endereco);
        }

        return "Em maos";
    }
}