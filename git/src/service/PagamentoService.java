package service;

import model.Pagamento;
import model.Reserva;
import model.exceptions.PagamentoException;

import java.util.ArrayList;
import java.util.List;

public class PagamentoService {

    private List<Pagamento> pagamentos = new ArrayList<>();
    private Long ultimoId = 1L;

    public Pagamento registrar(Reserva reserva, double valor, String metodo)
            throws PagamentoException {

       
        if (reserva == null) {
            throw new PagamentoException("Reserva não encontrada.");
        }

     
        for (Pagamento p : pagamentos) {
            if (reserva.getId().equals(p.getReservaId())) {
                throw new PagamentoException("Não é permitido realizar outro pagamento para esta reserva.");
            }
        }

       
        if (valor < reserva.getValor()) {
            throw new PagamentoException("O valor pago é menor que o valor da reserva.");
        }

       
        Pagamento pagamento = new Pagamento(
                ultimoId++,
                reserva.getId(),
                valor,
                metodo
        );

        pagamentos.add(pagamento);

        return pagamento;
    }

    public List<Pagamento> listar() {
        return pagamentos;
    }
}
