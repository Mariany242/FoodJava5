package view;

import model.Espaco;
import model.Reserva;
import model.exceptions.PagamentoException;
import model.exceptions.ReservaException;
import service.EspacoService;
import service.PagamentoService;
import service.ReservaService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuPrincipal {

    private EspacoService espacoService = new EspacoService();
    private ReservaService reservaService = new ReservaService();
    private PagamentoService pagamentoService = new PagamentoService();

    private Scanner sc = new Scanner(System.in);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");

    public void exibir() {
        int opc = -1;

        while (opc != 0) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1 - Cadastrar espaço");
            System.out.println("2 - Listar espaços");
            System.out.println("3 - Criar reserva");
            System.out.println("4 - Listar reservas");
            System.out.println("5 - Registrar pagamento");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            try {
                opc = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Digite um número válido para a opção!");
                sc.nextLine(); 
                continue;
            }

            switch (opc) {
                case 1 -> cadastrarEspaco();
                case 2 -> listarEspacos();
                case 3 -> criarReserva();
                case 4 -> listarReservas();
                case 5 -> registrarPagamento();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida!");
            }
        }
    }

   
    private void cadastrarEspaco() {
        System.out.println("\nTipo do espaço:");
        System.out.println("1 - Sala de Reunião");
        System.out.println("2 - Cabine Individual");
        System.out.println("3 - Auditório");

        int tipo = lerIntComValidacao("Tipo do espaço: ");

        sc.nextLine();
        System.out.print("Nome: ");
        String nome = sc.nextLine();

        int capacidade = lerIntComValidacao("Capacidade: ");
        double preco = lerDoubleComValidacao("Preço por hora: ");

        Espaco espaco = espacoService.criarEspaco(tipo, nome, capacidade, preco);

        System.out.println("\nEspaço cadastrado com sucesso!");
        System.out.println("ID: " + espaco.getId() + " | Nome: " + espaco.getNome());
    }

   
    private void listarEspacos() {
        System.out.println("\n=== ESPAÇOS CADASTRADOS ===");
        for (Espaco e : espacoService.listar()) {
            System.out.println("ID: " + e.getId()
                    + " | Nome: " + e.getNome()
                    + " | Cap.: " + e.getCapacidade()
                    + " | Disponível: " + e.isDisponivel());
        }
    }

   
    private void criarReserva() {
        listarEspacos();
        long id = lerLongComValidacao("\nID do espaço: ");
        Espaco espaco = espacoService.buscarPorId(id);

        if (espaco == null) {
            System.out.println("Espaço não encontrado!");
            return;
        }

        try {
            System.out.print("Início (AAAA-MM-DD HH): ");
            String inicioStr = sc.next() + " " + sc.next();
            LocalDateTime inicio = LocalDateTime.parse(inicioStr, formatter);

            System.out.print("Fim (AAAA-MM-DD HH): ");
            String fimStr = sc.next() + " " + sc.next();
            LocalDateTime fim = LocalDateTime.parse(fimStr, formatter);

            Reserva r = reservaService.criarReserva(espaco, inicio, fim);
            System.out.println("\nReserva criada com sucesso!");
            System.out.println("ID: " + r.getId() + " | Valor total: R$ " + r.getValor());

        } catch (ReservaException e) {
            System.out.println("\nErro ao criar reserva: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("\nFormato de data/hora inválido! Use AAAA-MM-DD HH");
        }
    }

  
    private void listarReservas() {
        System.out.println("\n=== RESERVAS ===");
        for (Reserva r : reservaService.listar()) {
            System.out.println("ID: " + r.getId()
                    + " | Espaço: " + r.getEspaco().getNome()
                    + " | Início: " + r.getInicio()
                    + " | Fim: " + r.getFim()
                    + " | Valor: R$ " + r.getValor());
        }
    }

   
    private void registrarPagamento() {
        listarReservas();
        long id = lerLongComValidacao("\nID da reserva: ");
        Reserva reserva = reservaService.buscarPorId(id);

        if (reserva == null) {
            System.out.println("Reserva não encontrada!");
            return;
        }

        double valor = lerDoubleComValidacao("Valor pago: ");
        System.out.print("Método (Pix/Cartão/Dinheiro): ");
        String metodo = sc.next();

        try {
            pagamentoService.registrar(reserva, valor, metodo);
            System.out.println("\nPagamento registrado com sucesso!");
        } catch (PagamentoException e) {
         
            if (e.getMessage().contains("Não é permitido")) {
                System.out.println("\nAtenção: pagamento já efetuado para esta reserva!");
            } else {
                System.out.println("\nErro ao registrar pagamento: " + e.getMessage());
            }
        }
    }

    
    private int lerIntComValidacao(String mensagem) {
        int valor = 0;
        boolean valido = false;
        while (!valido) {
            System.out.print(mensagem);
            try {
                valor = sc.nextInt();
                valido = true;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Digite um número inteiro.");
                sc.nextLine(); 
            }
        }
        return valor;
    }

    private long lerLongComValidacao(String mensagem) {
        long valor = 0;
        boolean valido = false;
        while (!valido) {
            System.out.print(mensagem);
            try {
                valor = sc.nextLong();
                valido = true;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Digite um número inteiro válido.");
                sc.nextLine();
            }
        }
        return valor;
    }

    private double lerDoubleComValidacao(String mensagem) {
        double valor = 0;
        boolean valido = false;
        while (!valido) {
            System.out.print(mensagem);
            try {
                valor = sc.nextDouble();
                valido = true;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Digite um número válido.");
                sc.nextLine();
            }
        }
        return valor;
    }
}

