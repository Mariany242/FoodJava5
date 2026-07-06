package br.edu.ifpb.ads.foodjava.model;

public class Gerente extends Usuario {
    public Gerente() {
    }

    public Gerente(String nome, String email, String senhaHash) {
        super(nome, email, senhaHash);
    }

    @Override
    public String getTipo() {
        return "GERENTE";
    }
}
