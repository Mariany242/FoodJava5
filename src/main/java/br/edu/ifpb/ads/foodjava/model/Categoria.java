package br.edu.ifpb.ads.foodjava.model;

public enum Categoria {
    ENTRADA("Entrada"),
    PRATO_PRINCIPAL("Prato Principal"),
    SOBREMESA("Sobremesa"),
    BEBIDAS("Bebidas");

    private final String rotulo;

    Categoria(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getRotulo() {
        return rotulo;
    }
}
