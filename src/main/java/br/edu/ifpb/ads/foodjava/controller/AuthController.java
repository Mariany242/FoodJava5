package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.exception.DocumentoInvalidoException;
import br.edu.ifpb.ads.foodjava.exception.SenhaInvalidaException;
import br.edu.ifpb.ads.foodjava.exception.UsuarioDuplicadoException;
import br.edu.ifpb.ads.foodjava.model.Cliente;
import br.edu.ifpb.ads.foodjava.model.Gerente;
import br.edu.ifpb.ads.foodjava.model.Restaurante;
import br.edu.ifpb.ads.foodjava.model.Usuario;
import br.edu.ifpb.ads.foodjava.repository.AppDatabase;
import br.edu.ifpb.ads.foodjava.util.DocumentoValidator;
import br.edu.ifpb.ads.foodjava.util.SenhaUtil;

import java.util.List;
import java.util.Optional;

public class AuthController {
    private final AppDatabase db;

    public AuthController(AppDatabase db) {
        this.db = db;
    }

    public Cliente cadastrarCliente(String nome, String email, String senha, String cpf, String telefone, String endereco)
            throws UsuarioDuplicadoException, SenhaInvalidaException, DocumentoInvalidoException {
        SenhaUtil.validar(senha);
        if (!DocumentoValidator.cpfValido(cpf)) {
            throw new DocumentoInvalidoException("CPF inválido.");
        }
        List<Cliente> clientes = db.clientes().listar();
        boolean duplicado = clientes.stream().anyMatch(c -> c.getEmail().equalsIgnoreCase(email)
                || DocumentoValidator.apenasDigitos(c.getCpf()).equals(DocumentoValidator.apenasDigitos(cpf)));
        if (duplicado) {
            throw new UsuarioDuplicadoException("E-mail ou CPF já cadastrado.");
        }
        Cliente cliente = new Cliente(nome, email, SenhaUtil.hash(senha), DocumentoValidator.apenasDigitos(cpf), telefone, endereco);
        clientes.add(cliente);
        db.clientes().salvarTodos(clientes);
        return cliente;
    }

    public Usuario login(String email, String senha) {
        String hash = SenhaUtil.hash(senha);
        Restaurante restaurante = db.carregarRestaurante();
        if (restaurante != null && restaurante.getGerente() != null) {
            Gerente gerente = restaurante.getGerente();
            if (gerente.getEmail().equalsIgnoreCase(email) && gerente.getSenhaHash().equals(hash)) {
                return gerente;
            }
        }
        Optional<Cliente> cliente = db.clientes().listar().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email) && c.getSenhaHash().equals(hash))
                .findFirst();
        return cliente.orElse(null);
    }
}
