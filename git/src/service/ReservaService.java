package service;

import model.*;
import model.exceptions.ReservaException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaService {

    private List<Reserva> reservas = new ArrayList<>();
    private Long ultimoId = 1L;

    public Reserva criarReserva(Espaco espaco, LocalDateTime inicio, LocalDateTime fim)
            throws ReservaException {

        if (espaco == null)
            throw new ReservaException("Espaço não encontrado.");

        if (inicio.isAfter(fim)) {
            throw new ReservaException("A data inicial deve ser antes da final.");
        }

        if (!espaco.isDisponivel()) {
            throw new ReservaException("Este espaço já está reservado.");
        }

        
        for (Reserva r : reservas) {
            if (!r.isCancelada() && r.getEspaco().equals(espaco)) {

                boolean sobrepoe =
                        !(fim.isBefore(r.getInicio()) ||
                        inicio.isAfter(r.getFim()));

                if (sobrepoe) {
                    throw new ReservaException("Existe uma reserva sobreposta.");
                }
            }
        }

        double horas = java.time.Duration.between(inicio, fim).toHours();
        double valor = espaco.calcularCustoReserva(horas);

        Reserva r = new Reserva(ultimoId++, espaco, inicio, fim, valor);

        espaco.setDisponivel(false);
        reservas.add(r);

        return r;
    }

    public List<Reserva> listar() {
        return reservas;
    }

    public Reserva buscarPorId(Long id) {
        for (Reserva r : reservas) {
            if (r.getId().equals(id)) return r;
        }
        return null;
    }
}

