package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.exception.ArquivoImportacaoException;
import br.edu.ifpb.ads.foodjava.exception.ItemVinculadoException;
import br.edu.ifpb.ads.foodjava.exception.PrecoInvalidoException;
import br.edu.ifpb.ads.foodjava.model.Categoria;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.model.Pedido;
import br.edu.ifpb.ads.foodjava.repository.AppDatabase;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CardapioController {
    private final AppDatabase db;

    public CardapioController(AppDatabase db) {
        this.db = db;
    }

    public List<ItemCardapio> listarTodos() {
        return db.cardapio().listar().stream()
                .sorted(Comparator.comparing(ItemCardapio::getCategoria).thenComparing(ItemCardapio::getNome))
                .toList();
    }

    public List<ItemCardapio> listarDisponiveis() {
        return listarTodos().stream().filter(ItemCardapio::isDisponivel).toList();
    }

    public ItemCardapio salvar(ItemCardapio item) throws PrecoInvalidoException {
        validarPreco(item.getPreco());
        if (item.getCategoria() == null) {
            throw new IllegalArgumentException("Categoria é obrigatória.");
        }
        List<ItemCardapio> itens = db.cardapio().listar();
        itens.add(item);
        db.cardapio().salvarTodos(itens);
        return item;
    }

    public void editar(ItemCardapio alvo, String nome, String descricao, double preco, Categoria categoria,
                       boolean disponivel, String imagemPath) throws PrecoInvalidoException {
        validarPreco(preco);
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria é obrigatória.");
        }
        List<ItemCardapio> itens = db.cardapio().listar();
        itens.stream().filter(i -> i.getId().equals(alvo.getId())).findFirst()
                .ifPresent(i -> i.atualizar(nome, descricao, preco, categoria, disponivel, imagemPath));
        db.cardapio().salvarTodos(itens);
    }

    public void excluir(ItemCardapio item) throws ItemVinculadoException {
        boolean vinculadoAberto = db.pedidos().listar().stream()
                .filter(Pedido::estaAberto)
                .flatMap(p -> p.getItens().stream())
                .anyMatch(i -> i.getItemId().equals(item.getId()));
        if (vinculadoAberto) {
            throw new ItemVinculadoException("Item vinculado a pedido aberto não pode ser excluído.");
        }
        List<ItemCardapio> itens = db.cardapio().listar();
        itens.removeIf(i -> i.getId().equals(item.getId()));
        db.cardapio().salvarTodos(itens);
    }

    public List<String> importarJson(Path arquivo) throws ArquivoImportacaoException {
        if (arquivo == null || Files.notExists(arquivo)) {
            throw new ArquivoImportacaoException("Arquivo de importação ausente.");
        }
        try {
            String json = Files.readString(arquivo);
            if (json.isBlank()) {
                throw new ArquivoImportacaoException("Arquivo de importação vazio.");
            }
            JsonObject raiz = JsonParser.parseString(json).getAsJsonObject();
            JsonArray array = raiz.getAsJsonArray("cardapio");
            if (array == null) {
                throw new ArquivoImportacaoException("Estrutura inválida: campo cardapio ausente.");
            }
            List<ItemCardapio> itens = db.cardapio().listar();
            List<String> relatorio = new ArrayList<>();
            int linha = 1;
            for (JsonElement element : array) {
                try {
                    JsonObject o = element.getAsJsonObject();
                    String nome = obrigatorio(o, "nome");
                    String descricao = obrigatorio(o, "descricao");
                    double preco = o.get("preco").getAsDouble();
                    validarPreco(preco);
                    Categoria categoria = Categoria.valueOf(obrigatorio(o, "categoria"));
                    boolean disponivel = !o.has("disponivel") || o.get("disponivel").getAsBoolean();
                    String imagem = o.has("imagemPath") ? o.get("imagemPath").getAsString() : null;
                    itens.add(new ItemCardapio(nome, descricao, preco, categoria, disponivel, imagem));
                    relatorio.add("Registro " + linha + ": importado.");
                } catch (Exception e) {
                    relatorio.add("Registro " + linha + ": " + e.getMessage());
                }
                linha++;
            }
            db.cardapio().salvarTodos(itens);
            return relatorio;
        } catch (IOException | IllegalStateException e) {
            throw new ArquivoImportacaoException("Erro ao ler JSON: " + e.getMessage());
        }
    }

    private String obrigatorio(JsonObject object, String campo) {
        if (!object.has(campo) || object.get(campo).isJsonNull() || object.get(campo).getAsString().isBlank()) {
            throw new IllegalArgumentException("Campo obrigatório ausente: " + campo);
        }
        return object.get(campo).getAsString();
    }

    private void validarPreco(double preco) throws PrecoInvalidoException {
        if (preco <= 0) {
            throw new PrecoInvalidoException("Preço deve ser maior que zero.");
        }
    }
}
