package br.edu.ifpb.ads.foodjava.model;

import java.util.UUID;

public abstract class Usuario implements Identificavel {
    private String id = UUID.randomUUID().toString();
    private String nome;
    private String email;
    private String senhaHash;

    protected Usuario() {
    }

    protected Usuario(String nome, String email, String senhaHash) {
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public abstract String getTipo();
}
