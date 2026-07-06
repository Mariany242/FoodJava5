package model;

public class Auditorio extends Espaco {

    public Auditorio(Long id, String nome, int capacidade, double preco) {
        super(id, nome, capacidade, preco);
    }

    @Override
    public double calcularCustoReserva(double horas, Object... extras) {
        double custo = precoPorHora * horas;

        custo += 100; // taxa fixa de evento

        return custo;
    }
}
