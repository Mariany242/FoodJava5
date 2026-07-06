package br.edu.ifpb.ads.foodjava.model;

public class Cliente extends Usuario {
    private String cpf;
    private String telefone;
    private String endereco;

    public Cliente() {
    }

    public Cliente(String nome, String email, String senhaHash, String cpf, String telefone, String endereco) {
        super(nome, email, senhaHash);
        this.cpf = cpf;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    @Override
    public String getTipo() {
        return "CLIENTE";
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEndereco() {
        return endereco;
    }
}
