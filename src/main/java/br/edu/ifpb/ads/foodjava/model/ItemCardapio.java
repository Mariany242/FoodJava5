package br.edu.ifpb.ads.foodjava.model;

import java.util.UUID;

public class ItemCardapio implements Identificavel {
    private String id = UUID.randomUUID().toString();
    private String nome;
    private String descricao;
    private double preco;
    private Categoria categoria;
    private boolean disponivel;
    private String imagemPath;

    public ItemCardapio() {
    }

    public ItemCardapio(String nome, String descricao, double preco, Categoria categoria, boolean disponivel, String imagemPath) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.categoria = categoria;
        this.disponivel = disponivel;
        this.imagemPath = imagemPath == null || imagemPath.isBlank() ? "images/placeholder.txt" : imagemPath;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPreco() {
        return preco;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public String getImagemPath() {
        return imagemPath == null || imagemPath.isBlank() ? "images/placeholder.txt" : imagemPath;
    }

    public void atualizar(String nome, String descricao, double preco, Categoria categoria, boolean disponivel, String imagemPath) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.categoria = categoria;
        this.disponivel = disponivel;
        this.imagemPath = imagemPath == null || imagemPath.isBlank() ? "images/placeholder.txt" : imagemPath;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    @Override
    public String toString() {
        return nome + " - R$ " + String.format("%.2f", preco);
    }
}
