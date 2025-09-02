package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoExisteException;

public abstract class Empregado {
    private String nome;
    private String endereco;

    public Empregado(String nome, String endereco) throws EmpregadoNaoExisteException {
        this.nome = nome;
        this.endereco = endereco;
    }

    public final String getNome() {
        return nome;
    }

    public final String getEndereco() {
        return endereco;
    }

    public abstract String getTipo();

    public abstract String getSalario();

    public final String ehSindicalizado(){
        return "false";
    }

    public String getTaxaDeComissao() { return ""; }
}