package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoExisteException;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.EmpregadoAssalariado;
import br.ufal.ic.p2.wepayu.models.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.EmpregadoHorista;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Facade {
    public List<Empregado> empregados = new ArrayList<>();
    public int id = 0;

    public final void zerarSistema(){
        empregados.clear();
        id = 0;
    }

    // Primeira User Story
    public final String criarEmpregado(String nome, String endereco, String tipo, String salario) throws EmpregadoNaoExisteException {
        Empregado emp = switch (tipo) {
            case "assalariado" -> new EmpregadoAssalariado(nome, endereco, salario);
            case "horista" -> new EmpregadoHorista(nome, endereco, salario);
            default -> throw new EmpregadoNaoExisteException();
        };

        empregados.add(emp);

        return String.valueOf(id++);
    }

    public final String criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao) throws EmpregadoNaoExisteException {
        Empregado emp = switch (tipo) {
            case "comissionado" -> new EmpregadoComissionado(nome, endereco, salario, comissao);
            default -> throw new EmpregadoNaoExisteException();
        };

        empregados.add(emp);

        return String.valueOf(id);
    }

    public final String getAtributoEmpregado(String emp, String atributo) {
        Empregado empregado = empregados.get(Integer.parseInt(emp));

        return switch (atributo) {
            case "nome" -> empregado.getNome();
            case "endereco" -> empregado.getEndereco();
            case "tipo" -> empregado.getTipo();
            case "salario" -> empregado.getSalario();
            case "sindicalizado" -> empregado.ehSindicalizado();
            case "comissao" -> empregado.getTaxaDeComissao();
            default -> null;
        };

    }

    public final void encerrarSistema() {}
    public final void quit() {}

    //public String getEmpregadoPorNome() {}


    /*
    // Segunda User Story
    public static void removerEmpregado() {}

    // Terceira User Story
    public static void lancaCartao() {}

    // Quarta User Story
    public static void lancaVenda() {}

    // Quinta User Story
    public static void lancaTaxaServico() {}

    // Sexta User Story
    public static void alterarEmpregado(String emp, String atributo, String valor){}

    // Setima User Story
    public static String totalFolha() {}
    public static void rodaFolha() {}

    // Oitava User Story
    public static void undo() {}

    public static void redo() {}*/
}
