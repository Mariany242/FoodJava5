package br.edu.ifpb.ads.foodjava.repository;

import br.edu.ifpb.ads.foodjava.model.Cliente;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.model.Pedido;
import br.edu.ifpb.ads.foodjava.model.Restaurante;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AppDatabase {
    private final Path dataDir = Path.of("src", "main", "resources", "data");
    private final JsonRepository<Cliente> clientes = new JsonRepository<>(dataDir.resolve("clientes.json"), Cliente.class);
    private final JsonRepository<ItemCardapio> cardapio = new JsonRepository<>(dataDir.resolve("cardapio.json"), ItemCardapio.class);
    private final JsonRepository<Pedido> pedidos = new JsonRepository<>(dataDir.resolve("pedidos.json"), Pedido.class);
    private final Path restaurantePath = dataDir.resolve("restaurante.json");

    public AppDatabase() {
        try {
            Files.createDirectories(dataDir);
            Files.createDirectories(Path.of("src", "main", "resources", "uploads"));
            Files.createDirectories(Path.of("src", "main", "resources", "images"));
            Path placeholder = Path.of("src", "main", "resources", "images", "placeholder.txt");
            if (Files.notExists(placeholder)) {
                Files.writeString(placeholder, "Imagem padrão do FoodJava.");
            }
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível preparar os diretórios do sistema.", e);
        }
    }

    public JsonRepository<Cliente> clientes() {
        return clientes;
    }

    public JsonRepository<ItemCardapio> cardapio() {
        return cardapio;
    }

    public JsonRepository<Pedido> pedidos() {
        return pedidos;
    }

    public boolean restauranteConfigurado() {
        try {
            return Files.exists(restaurantePath) && Files.size(restaurantePath) > 0;
        } catch (IOException e) {
            return false;
        }
    }

    public Restaurante carregarRestaurante() {
        try {
            if (!restauranteConfigurado()) {
                return null;
            }
            return JsonRepository.gson().fromJson(Files.readString(restaurantePath), Restaurante.class);
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível carregar restaurante.", e);
        }
    }

    public void salvarRestaurante(Restaurante restaurante) {
        try {
            Files.createDirectories(restaurantePath.getParent());
            Files.writeString(restaurantePath, JsonRepository.gson().toJson(restaurante));
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível salvar restaurante.", e);
        }
    }
}
