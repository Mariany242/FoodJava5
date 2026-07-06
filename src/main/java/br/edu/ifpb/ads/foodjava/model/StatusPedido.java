package br.edu.ifpb.ads.foodjava.model;

import br.edu.ifpb.ads.foodjava.exception.StatusInvalidoException;

public enum StatusPedido {
    AGUARDANDO_CONFIRMACAO,
    CONFIRMADO,
    EM_PREPARO,
    SAIU_PARA_ENTREGA,
    ENTREGUE,
    CANCELADO;

    public StatusPedido proximo() throws StatusInvalidoException {
        return switch (this) {
            case AGUARDANDO_CONFIRMACAO -> CONFIRMADO;
            case CONFIRMADO -> EM_PREPARO;
            case EM_PREPARO -> SAIU_PARA_ENTREGA;
            case SAIU_PARA_ENTREGA -> ENTREGUE;
            case ENTREGUE, CANCELADO -> throw new StatusInvalidoException("Não há próximo status para " + this + ".");
        };
    }
}
