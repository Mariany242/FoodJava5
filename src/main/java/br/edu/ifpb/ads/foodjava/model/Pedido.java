package br.edu.ifpb.ads.foodjava.model;

import br.edu.ifpb.ads.foodjava.exception.CancelamentoNaoPermitidoException;
import br.edu.ifpb.ads.foodjava.exception.StatusInvalidoException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Pedido implements Identificavel {
    private String id = UUID.randomUUID().toString();
    private String clienteEmail;
    private LocalDateTime dataHora = LocalDateTime.now();
    private List<ItemPedido> itens = new ArrayList<>();
    private double valorTotal;
    private StatusPedido status = StatusPedido.AGUARDANDO_CONFIRMACAO;

    public Pedido() {
    }

    public Pedido(String clienteEmail, List<ItemPedido> itens) {
        this.clienteEmail = clienteEmail;
        this.itens = new ArrayList<>(itens);
        recalcularTotal();
    }

    @Override
    public String getId() {
        return id;
    }

    public String getClienteEmail() {
        return clienteEmail;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void avancarStatus() throws StatusInvalidoException {
        status = status.proximo();
    }

    public void cancelar() throws CancelamentoNaoPermitidoException {
        if (status != StatusPedido.AGUARDANDO_CONFIRMACAO) {
            throw new CancelamentoNaoPermitidoException("Pedido só pode ser cancelado enquanto aguarda confirmação.");
        }
        status = StatusPedido.CANCELADO;
    }

    public boolean estaAberto() {
        return status != StatusPedido.ENTREGUE && status != StatusPedido.CANCELADO;
    }

    private void recalcularTotal() {
        valorTotal = itens.stream().mapToDouble(ItemPedido::getSubtotal).sum();
    }

    @Override
    public String toString() {
        return "#" + id.substring(0, 8) + " | " + clienteEmail + " | " + status + " | R$ " + String.format("%.2f", valorTotal);
    }
}
