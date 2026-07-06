package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.exception.DocumentoInvalidoException;
import br.edu.ifpb.ads.foodjava.exception.SenhaInvalidaException;
import br.edu.ifpb.ads.foodjava.model.Gerente;
import br.edu.ifpb.ads.foodjava.model.Restaurante;
import br.edu.ifpb.ads.foodjava.repository.AppDatabase;
import br.edu.ifpb.ads.foodjava.util.DocumentoValidator;
import br.edu.ifpb.ads.foodjava.util.SenhaUtil;

public class RestauranteController {
    private final AppDatabase db;

    public RestauranteController(AppDatabase db) {
        this.db = db;
    }

    public boolean configurado() {
        return db.restauranteConfigurado();
    }

    public Restaurante carregar() {
        return db.carregarRestaurante();
    }

    public Restaurante configurar(String nome, String cnpj, String endereco, String telefone, String categoria,
                                  String logotipoPath, String emailGerente, String senhaGerente)
            throws DocumentoInvalidoException, SenhaInvalidaException {
        if (!DocumentoValidator.cnpjValido(cnpj)) {
            throw new DocumentoInvalidoException("CNPJ inválido.");
        }
        SenhaUtil.validar(senhaGerente);
        Restaurante restaurante = new Restaurante(nome, DocumentoValidator.apenasDigitos(cnpj), endereco, telefone, categoria,
                logotipoPath, new Gerente(nome, emailGerente, SenhaUtil.hash(senhaGerente)));
        db.salvarRestaurante(restaurante);
        return restaurante;
    }
}
