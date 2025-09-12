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
 *   <li>Processamento de folha para empregados horistas</li>
 *   <li>Processamento de folha para empregados comissionados</li>
 *   <li>Geração de arquivos de folha de pagamento</li>
 * </ul>
 * 
 * @author John Wallex
 * @version 1.0
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
        String tipo = empregado.getTipo();

        switch (tipo) {
            case "horista":
                return deveReceberHorista((EmpregadoHorista) empregado, data);
            case "assalariado":
                return deveReceberAssalariado(data);
            case "comissionado":
                return deveReceberComissionado(data);
            default:
                return false;
        }
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
                BigDecimal salarioBruto = calcularSalarioHorista(horista, dataFolha);
                BigDecimal descontos = BigDecimal.ZERO;
                if (salarioBruto.compareTo(BigDecimal.ZERO) > 0) {
                    descontos = calcularDescontos(horista, dataFolha);
                }
                BigDecimal salarioLiquido = salarioBruto.subtract(descontos).max(BigDecimal.ZERO);

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

    private BigDecimal calcularDescontos(Empregado empregado, LocalDate data) {
        if (empregado.getSindicato() == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.DOWN);
        }

        MembroSindicato sindicato = empregado.getSindicato();
        BigDecimal descontos = BigDecimal.ZERO;

        // Dias do período (7, mês ou 14)
        int diasPeriodo = calcularDiasPeriodo(empregado, data);
        System.out.println("Dias do período: " + diasPeriodo + " para o empregado " 
        + empregado.getNome() + " taxa sindical: " + sindicato.getTaxaSindical());

        // Taxa sindical: taxa diária * diasPeriodo (garantindo BigDecimal desde o início)
        BigDecimal taxaDiaria = new BigDecimal(String.valueOf(sindicato.getTaxaSindical()));
        BigDecimal taxaSindicalTotal = taxaDiaria.multiply(BigDecimal.valueOf(diasPeriodo))
                .setScale(2, RoundingMode.DOWN);
        descontos = descontos.add(taxaSindicalTotal);

        // Janela do período (inclui 1 dia a mais no início para pegar taxas na "fronteira")
        LocalDate inicioPeriodo = data.minusDays(diasPeriodo); // <-- alteração intencional

        // Soma taxas de serviço que caem dentro da janela [inicioPeriodo .. data]
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


    private int calcularDiasPeriodo(Empregado empregado, LocalDate data) {
        switch (empregado.getTipo()) {
            case "horista":
                return 7; // semana
            case "assalariado":
                return data.lengthOfMonth(); // mês completo
            case "comissionado":
                return 14; // quinzena
            default:
                return 1;
        }
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