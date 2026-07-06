package br.edu.ifpb.ads.foodjava.view;

import br.edu.ifpb.ads.foodjava.controller.AuthController;
import br.edu.ifpb.ads.foodjava.controller.CardapioController;
import br.edu.ifpb.ads.foodjava.controller.PedidoController;
import br.edu.ifpb.ads.foodjava.controller.RestauranteController;
import br.edu.ifpb.ads.foodjava.exception.FoodJavaException;
import br.edu.ifpb.ads.foodjava.model.Categoria;
import br.edu.ifpb.ads.foodjava.model.Cliente;
import br.edu.ifpb.ads.foodjava.model.Gerente;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.model.ItemPedido;
import br.edu.ifpb.ads.foodjava.model.Pedido;
import br.edu.ifpb.ads.foodjava.model.StatusPedido;
import br.edu.ifpb.ads.foodjava.model.Usuario;
import br.edu.ifpb.ads.foodjava.repository.AppDatabase;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.util.List;

public class FoodJavaApp extends Application {
    private final AppDatabase db = new AppDatabase();
    private final RestauranteController restauranteController = new RestauranteController(db);
    private final AuthController authController = new AuthController(db);
    private final CardapioController cardapioController = new CardapioController(db);
    private final PedidoController pedidoController = new PedidoController(db);
    private Stage stage;
    private Usuario usuarioLogado;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("FoodJava");
        if (restauranteController.configurado()) {
            mostrarLogin();
        } else {
            mostrarConfiguracaoInicial();
        }
        stage.show();
    }

    private void mostrarConfiguracaoInicial() {
        TextField nome = campo("Nome fantasia");
        TextField cnpj = campo("CNPJ");
        TextField endereco = campo("Endereço completo");
        TextField telefone = campo("Telefone");
        TextField categoria = campo("Categoria culinária");
        TextField email = campo("E-mail do gerente");
        PasswordField senha = senha("Senha do gerente");
        TextField logo = campo("Logotipo path opcional");

        Button salvar = new Button("Salvar configuração");
        salvar.setOnAction(e -> executar(() -> {
            restauranteController.configurar(nome.getText(), cnpj.getText(), endereco.getText(), telefone.getText(),
                    categoria.getText(), logo.getText(), email.getText(), senha.getText());
            mostrarLogin();
        }));

        stage.setScene(cena(pagina("Configuração inicial do restaurante",
                formulario(List.of(nome, cnpj, endereco, telefone, categoria, email, senha, logo), salvar))));
    }

    private void mostrarLogin() {
        TextField email = campo("E-mail");
        PasswordField senha = senha("Senha");
        Button entrar = new Button("Entrar");
        Button cadastro = new Button("Cadastrar cliente");

        entrar.setOnAction(e -> {
            Usuario usuario = authController.login(email.getText(), senha.getText());
            if (usuario == null) {
                erro("Login inválido.");
                return;
            }
            usuarioLogado = usuario;
            if (usuario instanceof Gerente) {
                mostrarPainelGerente();
            } else {
                mostrarAreaCliente();
            }
        });
        cadastro.setOnAction(e -> mostrarCadastroCliente());

        HBox botoes = new HBox(10, entrar, cadastro);
        botoes.setAlignment(Pos.CENTER_RIGHT);
        stage.setScene(cena(pagina("Entrar no FoodJava", formulario(List.of(email, senha), botoes))));
    }

    private void mostrarCadastroCliente() {
        TextField nome = campo("Nome");
        TextField email = campo("E-mail");
        PasswordField senha = senha("Senha");
        TextField cpf = campo("CPF");
        TextField telefone = campo("Telefone");
        TextField endereco = campo("Endereço");
        Button salvar = new Button("Criar conta");
        Button voltar = new Button("Voltar");

        salvar.setOnAction(e -> executar(() -> {
            authController.cadastrarCliente(nome.getText(), email.getText(), senha.getText(), cpf.getText(),
                    telefone.getText(), endereco.getText());
            info("Cliente cadastrado. Faça login para continuar.");
            mostrarLogin();
        }));
        voltar.setOnAction(e -> mostrarLogin());

        HBox botoes = new HBox(10, voltar, salvar);
        botoes.setAlignment(Pos.CENTER_RIGHT);
        stage.setScene(cena(pagina("Cadastro do cliente", formulario(List.of(nome, email, senha, cpf, telefone, endereco), botoes))));
    }

    private void mostrarAreaCliente() {
        TabPane tabs = new TabPane();
        tabs.getTabs().add(tab("Cardápio", clienteCardapio()));
        tabs.getTabs().add(tab("Carrinho", clienteCarrinho()));
        tabs.getTabs().add(tab("Histórico", clienteHistorico()));
        stage.setScene(cena(comCabecalho("Área do cliente", tabs)));
    }

    private VBox clienteCardapio() {
        VBox raiz = new VBox(12);
        raiz.setPadding(new Insets(16));
        for (Categoria categoria : Categoria.values()) {
            Label titulo = new Label(categoria.getRotulo());
            titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            raiz.getChildren().add(titulo);
            cardapioController.listarDisponiveis().stream()
                    .filter(i -> i.getCategoria() == categoria)
                    .forEach(item -> raiz.getChildren().add(cardItemCliente(item)));
        }
        return raiz;
    }

    private HBox cardItemCliente(ItemCardapio item) {
        Label dados = new Label(item.getNome() + "\n" + item.getDescricao() + "\nR$ " + dinheiro(item.getPreco())
                + "\nImagem: " + item.getImagemPath());
        dados.setWrapText(true);
        Button adicionar = new Button("Adicionar");
        adicionar.setOnAction(e -> {
            pedidoController.adicionarAoCarrinho(item);
            info("Item adicionado ao carrinho.");
        });
        HBox box = new HBox(12, dados, adicionar);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-border-color: #ddd; -fx-background-color: white;");
        HBox.setHgrow(dados, Priority.ALWAYS);
        return box;
    }

    private VBox clienteCarrinho() {
        ListView<ItemPedido> lista = new ListView<>();
        Runnable atualizar = () -> lista.setItems(FXCollections.observableArrayList(pedidoController.getCarrinho()));
        atualizar.run();
        Label total = new Label();
        Runnable atualizarTotal = () -> total.setText("Total: R$ " + dinheiro(pedidoController.getCarrinho().stream()
                .mapToDouble(ItemPedido::getSubtotal).sum()));
        atualizarTotal.run();

        Button remover = new Button("Remover selecionado");
        remover.setOnAction(e -> {
            ItemPedido item = lista.getSelectionModel().getSelectedItem();
            if (item != null) {
                pedidoController.removerDoCarrinho(item);
                atualizar.run();
                atualizarTotal.run();
            }
        });
        Button confirmar = new Button("Confirmar pedido");
        confirmar.setOnAction(e -> executar(() -> {
            Pedido pedido = pedidoController.confirmarPedido(usuarioLogado.getEmail());

            info("Pedido confirmado!\n\n"
                    + "Valor total: R$ " + dinheiro(pedido.getValorTotal()));

            mostrarAreaCliente();

        }));
        return painelComLista("Carrinho", lista, total, new HBox(10, remover, confirmar));
    }

    private VBox clienteHistorico() {
        ListView<Pedido> lista = new ListView<>(FXCollections.observableArrayList(
                pedidoController.listarDoCliente(usuarioLogado.getEmail())));
        Button atualizar = new Button("Atualizar");
        atualizar.setOnAction(e -> lista.setItems(FXCollections.observableArrayList(
                pedidoController.listarDoCliente(usuarioLogado.getEmail()))));
        return painelComLista("Meus pedidos", lista, new Label("Status atualizado pelo gerente."), atualizar);
    }

    private void mostrarPainelGerente() {
        TabPane tabs = new TabPane();
        tabs.getTabs().add(tab("Pedidos", gerentePedidos()));
        tabs.getTabs().add(tab("Cardápio", gerenteCardapio()));
        tabs.getTabs().add(tab("Restaurante", gerenteRestaurante()));
        stage.setScene(cena(comCabecalho("Painel do gerente", tabs)));
    }

    private VBox gerentePedidos() {
        ComboBox<StatusPedido> filtro = new ComboBox<>(FXCollections.observableArrayList(StatusPedido.values()));
        filtro.setPromptText("Filtrar por status");
        ListView<Pedido> lista = new ListView<>();
        Runnable atualizar = () -> lista.setItems(FXCollections.observableArrayList(pedidoController.listarTodos(filtro.getValue())));
        atualizar.run();
        filtro.setOnAction(e -> atualizar.run());
        Label resumo = new Label(resumoDia());

        Button avancar = new Button("Avançar status");
        avancar.setOnAction(e -> executar(() -> {
            Pedido pedido = lista.getSelectionModel().getSelectedItem();
            if (pedido == null) {
                throw new IllegalArgumentException("Selecione um pedido.");
            }
            pedidoController.avancar(pedido);
            resumo.setText(resumoDia());
            atualizar.run();
        }));
        Button cancelar = new Button("Cancelar");
        cancelar.setOnAction(e -> executar(() -> {
            Pedido pedido = lista.getSelectionModel().getSelectedItem();
            if (pedido == null) {
                throw new IllegalArgumentException("Selecione um pedido.");
            }
            pedidoController.cancelar(pedido);
            resumo.setText(resumoDia());
            atualizar.run();
        }));
        return painelComLista("Pedidos recebidos", lista, resumo, new HBox(10, filtro, avancar, cancelar));
    }

    private VBox gerenteCardapio() {
        ListView<ItemCardapio> lista = new ListView<>();
        Runnable atualizar = () -> lista.setItems(FXCollections.observableArrayList(cardapioController.listarTodos()));
        atualizar.run();

        TextField nome = campo("Nome");
        TextArea descricao = new TextArea();
        descricao.setPromptText("Descrição");
        descricao.setPrefRowCount(3);
        TextField preco = campo("Preço");
        ComboBox<Categoria> categoria = new ComboBox<>(FXCollections.observableArrayList(Categoria.values()));
        categoria.setPromptText("Categoria");
        CheckBox disponivel = new CheckBox("Disponível");
        disponivel.setSelected(true);
        TextField imagem = campo("Imagem path opcional");

        lista.getSelectionModel().selectedItemProperty().addListener((obs, antigo, item) -> {
            if (item != null) {
                nome.setText(item.getNome());
                descricao.setText(item.getDescricao());
                preco.setText(String.valueOf(item.getPreco()));
                categoria.setValue(item.getCategoria());
                disponivel.setSelected(item.isDisponivel());
                imagem.setText(item.getImagemPath());
            }
        });

        Button novo = new Button("Cadastrar");
        novo.setOnAction(e -> executar(() -> {
            cardapioController.salvar(new ItemCardapio(nome.getText(), descricao.getText(),
                    Double.parseDouble(preco.getText().replace(",", ".")), categoria.getValue(), disponivel.isSelected(), imagem.getText()));
            atualizar.run();
        }));
        Button editar = new Button("Editar selecionado");
        editar.setOnAction(e -> executar(() -> {
            ItemCardapio item = lista.getSelectionModel().getSelectedItem();
            if (item == null) {
                throw new IllegalArgumentException("Selecione um item.");
            }
            cardapioController.editar(item, nome.getText(), descricao.getText(), Double.parseDouble(preco.getText().replace(",", ".")),
                    categoria.getValue(), disponivel.isSelected(), imagem.getText());
            atualizar.run();
        }));
        Button excluir = new Button("Excluir");
        excluir.setOnAction(e -> executar(() -> {
            ItemCardapio item = lista.getSelectionModel().getSelectedItem();
            if (item == null) {
                throw new IllegalArgumentException("Selecione um item.");
            }
            cardapioController.excluir(item);
            atualizar.run();
        }));
        Button importar = new Button("Importar JSON");
        importar.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Importar cardápio");
            var arquivo = chooser.showOpenDialog(stage);
            if (arquivo != null) {
                executar(() -> {
                    List<String> relatorio = cardapioController.importarJson(Path.of(arquivo.toURI()));
                    atualizar.run();
                    info(String.join("\n", relatorio));
                });
            }
        });

        VBox form = new VBox(8, nome, descricao, preco, categoria, disponivel, imagem,
                new HBox(10, novo, editar, excluir, importar));
        return painelComLista("Gerenciamento do cardápio", lista, form, new Label("Categorias obrigatórias: Entrada, Prato Principal, Sobremesa e Bebidas."));
    }

    private VBox gerenteRestaurante() {
        var r = restauranteController.carregar();
        Label dados = new Label("Nome: " + r.getNomeFantasia()
                + "\nCNPJ: " + r.getCnpj()
                + "\nEndereço: " + r.getEndereco()
                + "\nTelefone: " + r.getTelefone()
                + "\nCategoria: " + r.getCategoriaCulinaria()
                + "\nLogotipo: " + r.getLogotipoPath()
                + "\nGerente: " + r.getGerente().getEmail());
        dados.setWrapText(true);
        return new VBox(12, new Label("Configurações do restaurante"), dados);
    }

    private BorderPane comCabecalho(String titulo, javafx.scene.Node conteudo) {
        Label label = new Label(titulo + " - " + usuarioLogado.getNome());
        label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Button sair = new Button("Sair");
        sair.setOnAction(e -> {
            usuarioLogado = null;
            mostrarLogin();
        });
        HBox topo = new HBox(12, label, sair);
        topo.setPadding(new Insets(14));
        topo.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(label, Priority.ALWAYS);
        BorderPane pane = new BorderPane();
        pane.setTop(topo);
        pane.setCenter(conteudo);
        return pane;
    }

    private VBox painelComLista(String titulo, ListView<?> lista, javafx.scene.Node rodape, javafx.scene.Node acoes) {
        Label label = new Label(titulo);
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        VBox box = new VBox(10, label, lista, rodape, acoes);
        box.setPadding(new Insets(16));
        VBox.setVgrow(lista, Priority.ALWAYS);
        return box;
    }

    private Tab tab(String titulo, javafx.scene.Node node) {
        Tab tab = new Tab(titulo, new ScrollPane(node));
        tab.setClosable(false);
        return tab;
    }

    private VBox pagina(String titulo, javafx.scene.Node conteudo) {
        Label label = new Label(titulo);
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        VBox box = new VBox(18, label, conteudo);
        box.setPadding(new Insets(32));
        box.setAlignment(Pos.TOP_CENTER);
        return box;
    }

    private javafx.scene.Node formulario(List<javafx.scene.Node> campos, javafx.scene.Node botoes) {
        VBox box = new VBox(10);
        box.setMaxWidth(520);
        box.getChildren().addAll(campos);
        box.getChildren().add(botoes);
        return box;
    }

    private Scene cena(Parent node) {
        return new Scene(node, 980, 720);
    }

    private TextField campo(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        return field;
    }

    private PasswordField senha(String prompt) {
        PasswordField field = new PasswordField();
        field.setPromptText(prompt);
        return field;
    }

    private String dinheiro(double valor) {
        return String.format("%.2f", valor);
    }

    private String resumoDia() {
        return "Hoje: " + pedidoController.totalPedidosHoje() + " pedidos | Faturamento: R$ " + dinheiro(pedidoController.faturamentoHoje());
    }

    private void executar(Operacao operacao) {
        try {
            operacao.executar();
        } catch (FoodJavaException | IllegalArgumentException e) {
            erro(e.getMessage());
        } catch (RuntimeException e) {
            Throwable causa = e.getCause();
            erro(causa == null ? e.getMessage() : causa.getMessage());
        }
    }

    private void erro(String mensagem) {
        new Alert(Alert.AlertType.ERROR, mensagem).showAndWait();
    }

    private void info(String mensagem) {
        new Alert(Alert.AlertType.INFORMATION, mensagem).showAndWait();
    }

    @FunctionalInterface
    private interface Operacao {
        void executar() throws FoodJavaException;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
