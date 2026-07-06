package br.edu.ifpb.ads.foodjava.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonRepository<T> {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .setPrettyPrinting()
            .create();

    private final Path arquivo;
    private final Type tipoLista;

    public JsonRepository(Path arquivo, Class<T> classe) {
        this.arquivo = arquivo;
        this.tipoLista = TypeToken.getParameterized(List.class, classe).getType();
    }

    public List<T> listar() {
        try {
            if (Files.notExists(arquivo) || Files.size(arquivo) == 0) {
                return new ArrayList<>();
            }
            String json = Files.readString(arquivo);
            List<T> lista = GSON.fromJson(json, tipoLista);
            return lista == null ? new ArrayList<>() : lista;
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível ler " + arquivo + ".", e);
        }
    }

    public void salvarTodos(List<T> itens) {
        try {
            Files.createDirectories(arquivo.getParent());
            Files.writeString(arquivo, GSON.toJson(itens, tipoLista));
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível salvar " + arquivo + ".", e);
        }
    }

    public static Gson gson() {
        return GSON;
    }
}
