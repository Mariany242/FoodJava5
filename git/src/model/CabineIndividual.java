package model;

public class CabineIndividual extends Espaco {

    public CabineIndividual(Long id, String nome, int capacidade, double preco) {
        super(id, nome, capacidade, preco);
    }

    @Override
    public double calcularCustoReserva(double horas, Object... extras) {
        double custo = precoPorHora * horas;

        if (horas > 4) {
            custo = custo * 0.9; // 10% de desconto
        }

        return custo;
    }
}
