package model;

public abstract class Espaco {

    protected Long id;
    protected String nome;
    protected int capacidade;
    protected boolean disponivel = true;
    protected double precoPorHora;

    public Espaco(Long id, String nome, int capacidade, double precoPorHora) {
        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
        this.precoPorHora = precoPorHora;
    }

    // cada tipo calcula o custo do seu jeito
    public abstract double calcularCustoReserva(double horas, Object... extras);

    // Getters simples
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public int getCapacidade() { return capacidade; }
    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }
    public double getPrecoPorHora() { return precoPorHora; }

 
    public String toString() {
        return id + " - " + nome + " (capacidade: " + capacidade + ")";
    }
}
