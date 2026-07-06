package br.edu.ifpb.ads.foodjava.model;

public class ItemPedido {
    private String itemId;
    private String nome;
    private double precoUnitario;
    private int quantidade;

    public ItemPedido() {
    }

    public ItemPedido(ItemCardapio item, int quantidade) {
        this.itemId = item.getId();
        this.nome = item.getNome();
        this.precoUnitario = item.getPreco();
        this.quantidade = quantidade;
    }

    public String getItemId() {
        return itemId;
    }

    public String getNome() {
        return nome;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void incrementar() {
        quantidade++;
    }

    public double getSubtotal() {
        return precoUnitario * quantidade;
    }

    @Override
    public String toString() {
        return quantidade + "x " + nome + " - R$ " + String.format("%.2f", getSubtotal());
    }
}
