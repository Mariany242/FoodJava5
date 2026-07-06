package service;

import model.Auditorio;
import model.CabineIndividual;
import model.Espaco;
import model.SalaDeReuniao;

import java.util.ArrayList;
import java.util.List;

public class EspacoService {

    private List<Espaco> espacos = new ArrayList<>();
    private Long ultimoId = 1L;

   
    public Espaco criarEspaco(int tipo, String nome, int capacidade, double precoPorHora) {
        Espaco espaco = null;

        switch (tipo) {
            case 1 -> espaco = new SalaDeReuniao(ultimoId++, nome, capacidade, precoPorHora);
            case 2 -> espaco = new CabineIndividual(ultimoId++, nome, capacidade, precoPorHora);
            case 3 -> espaco = new Auditorio(ultimoId++, nome, capacidade, precoPorHora);
            default -> System.out.println("Tipo inválido.");
        }

        if (espaco != null) {
            espacos.add(espaco);
        }

        return espaco;
    }

   
    public Espaco buscarPorId(Long id) {
        for (Espaco e : espacos) {
            if (e.getId().equals(id)) {
                return e;
            }
        }
        return null; 
    }

   
    public List<Espaco> listar() {
        return espacos;
    }

    public boolean remover(Long id) {
        Espaco e = buscarPorId(id);
        if (e != null) {
            espacos.remove(e);
            return true;
        }
        return false;
    }
}
