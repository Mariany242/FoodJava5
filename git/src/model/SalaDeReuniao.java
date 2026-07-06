package model;

public class SalaDeReuniao extends Espaco {

    public SalaDeReuniao(Long id, String nome, int capacidade, double preco) {
        super(id, nome, capacidade, preco);
    }

    @Override
    public double calcularCustoReserva(double horas, Object... extras) {
        boolean usarProjetor = false;

        if (extras.length > 0 && extras[0] instanceof Boolean) {
            usarProjetor = (Boolean) extras[0];
        }

        double custo = precoPorHora * horas;

        if (usarProjetor) {
            custo += 15;   // taxa fixa
        }

        return custo;
    }
}
