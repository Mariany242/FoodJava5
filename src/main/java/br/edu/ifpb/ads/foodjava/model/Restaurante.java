package br.edu.ifpb.ads.foodjava.model;

public class Restaurante {
    private String nomeFantasia;
    private String cnpj;
    private String endereco;
    private String telefone;
    private String categoriaCulinaria;
    private String logotipoPath;
    private Gerente gerente;

    public Restaurante() {
    }

    public Restaurante(String nomeFantasia, String cnpj, String endereco, String telefone, String categoriaCulinaria,
                       String logotipoPath, Gerente gerente) {
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
        this.endereco = endereco;
        this.telefone = telefone;
        this.categoriaCulinaria = categoriaCulinaria;
        this.logotipoPath = logotipoPath;
        this.gerente = gerente;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCategoriaCulinaria() {
        return categoriaCulinaria;
    }

    public String getLogotipoPath() {
        return logotipoPath;
    }

    public Gerente getGerente() {
        return gerente;
    }
}
