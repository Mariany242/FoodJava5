package model;

import java.time.LocalDateTime;

public class Reserva {

    private Long id;
    private Espaco espaco;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private double valor;
    private boolean cancelada = false;

    public Reserva(Long id, Espaco espaco, LocalDateTime inicio, LocalDateTime fim, double valor) {
        this.id = id;
        this.espaco = espaco;
        this.inicio = inicio;
        this.fim = fim;
        this.valor = valor;
    }

    public Long getId() { return id; }
    public Espaco getEspaco() { return espaco; }
    public LocalDateTime getInicio() { return inicio; }
    public LocalDateTime getFim() { return fim; }
    public double getValor() { return valor; }
    public boolean isCancelada() { return cancelada; }

    public void cancelar() {
        this.cancelada = true;
        espaco.setDisponivel(true);
    }


    public String toString() {
        return "Reserva " + id + " - " + espaco.getNome() + " - Valor: R$" + valor;
    }
}
