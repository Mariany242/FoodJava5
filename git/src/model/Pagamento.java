package model;

import java.time.LocalDateTime;

public class Pagamento {

    private Long id;
    private Long reservaId;
    private double valor;
    public Pagamento(Long id, Long reservaId, double valor, String metodo) {
        this.id = id;
        this.reservaId = reservaId;
        this.valor = valor;
        LocalDateTime.now();
    }


    public String toString() {
        return "Pagamento " + id + " - Reserva: " + reservaId + " - R$" + valor;
    }
}
