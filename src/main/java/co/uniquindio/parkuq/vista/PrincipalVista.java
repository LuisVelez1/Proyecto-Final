package co.uniquindio.parkuq.vista;

import co.uniquindio.parkuq.enums.Rol;
import co.uniquindio.parkuq.modelo.Cuenta;
import co.uniquindio.parkuq.servicio.ParqueaderoServicio;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class PrincipalVista {

    private Stage stage;
    private ParqueaderoServicio servicio;
    private Cuenta cuentaActual;
    private BorderPane root;
    private VBox contenidoCentral;

    public PrincipalVista(Stage stage, ParqueaderoServicio servicio, Cuenta cuentaActual) {
        this.stage = stage;
        this.servicio = servicio;
        this.cuentaActual = cuentaActual;
    }

    public void mostrar() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #4caf50;");

        root.setTop(crearEncabezado());
        root.setLeft(crearMenuLateral());

        contenidoCentral = new VBox(20);
        contenidoCentral.setPadding(new Insets(30));
        contenidoCentral.setStyle("-fx-background-color: #4caf50;");
        mostrarBienvenida();
        root.setCenter(contenidoCentral);

        Scene scene = new Scene(root, 1000, 650);
        stage.setTitle("ParkUQ - " + cuentaActual.getRol().toString());
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    private HBox crearEncabezado() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 25, 15, 25));
        header.setStyle("-fx-background-color: #4caf50; -fx-border-color: #ffffff; -fx-border-width: 0 0 2 0;");

        Label titulo = new Label("PARKUQ");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        titulo.setTextFill(Color.web("#ffffff"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblRol = new Label("● " + cuentaActual.getRol() + " - " + cuentaActual.getNombreUsuario());
        lblRol.setTextFill(Color.web("#ffffff"));

        Button btnSalir = new Button("Cerrar Sesión");
        btnSalir.setStyle("-fx-background-color: #ff0002; -fx-text-fill: #ffffff; " +
                "-fx-border-color: #ffffff; -fx-border-radius: 4; -fx-cursor: hand;");
        btnSalir.setOnAction(e -> {
            LoginVista login = new LoginVista(stage);
            login.mostrar();
        });

        header.getChildren().addAll(titulo, spacer, lblRol, new Label("  "), btnSalir);
        return header;
    }

    private VBox crearMenuLateral() {
        VBox menu = new VBox(5);
        menu.setPadding(new Insets(20, 10, 20, 10));
        menu.setPrefWidth(200);
        menu.setStyle("-fx-background-color: #4caf50");

        Label lblOperador = crearLabelSeccion("OPERADOR");
        menu.getChildren().add(lblOperador);

        menu.getChildren().add(crearBotonMenu("Registrar Ingreso", () -> {
            OperadorVista vista = new OperadorVista(servicio);
            vista.mostrarRegistroIngreso(contenidoCentral);
        }));

        menu.getChildren().add(crearBotonMenu("Registrar Salida", () -> {
            OperadorVista vista = new OperadorVista(servicio);
            vista.mostrarRegistroSalida(contenidoCentral);
        }));

        menu.getChildren().add(crearBotonMenu("Vehículos Dentro", () -> {
            OperadorVista vista = new OperadorVista(servicio);
            vista.mostrarVehiculosDentro(contenidoCentral);
        }));

        menu.getChildren().add(crearBotonMenu("Espacios", () -> {
            OperadorVista vista = new OperadorVista(servicio);
            vista.mostrarEspacios(contenidoCentral);
        }));

        menu.getChildren().add(crearBotonMenu("Reportes", () -> {
            OperadorVista vista = new OperadorVista(servicio);
            vista.mostrarReportes(contenidoCentral);
        }));

        if (cuentaActual.getRol() == Rol.ADMINISTRADOR) {
            menu.getChildren().add(new Separator());
            Label lblAdmin = crearLabelSeccion("ADMINISTRADOR");
            menu.getChildren().add(lblAdmin);

            menu.getChildren().add(crearBotonMenu("Gestionar Espacios", () -> {
                AdminVista vista = new AdminVista(servicio);
                vista.mostrarGestionEspacios(contenidoCentral);
            }));

            menu.getChildren().add(crearBotonMenu("Gestionar Tarifas", () -> {
                AdminVista vista = new AdminVista(servicio);
                vista.mostrarGestionTarifas(contenidoCentral);
            }));

            menu.getChildren().add(crearBotonMenu("Gestionar Usuarios", () -> {
                AdminVista vista = new AdminVista(servicio);
                vista.mostrarGestionUsuarios(contenidoCentral);
            }));
        }

        return menu;
    }

    private Label crearLabelSeccion(String texto) {
        Label lbl = new Label(texto);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        lbl.setTextFill(Color.web("#ffffff"));
        lbl.setPadding(new Insets(10, 5, 5, 5));
        return lbl;
    }

    private Button crearBotonMenu(String texto, Runnable accion) {
        Button btn = new Button(texto);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #ffffff; " +
                "-fx-padding: 8 10; -fx-cursor: hand; -fx-background-radius: 6;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #4caf50;" +
                "-fx-padding: 8 10; -fx-cursor: hand; -fx-background-radius: 6;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #ffffff; " +
                "-fx-padding: 8 10; -fx-cursor: hand; -fx-background-radius: 6;"));
        btn.setOnAction(e -> accion.run());
        return btn;
    }

    private void mostrarBienvenida() {
        contenidoCentral.getChildren().clear();

        Label bienvenida = new Label("Bienvenido, " + cuentaActual.getNombreUsuario());
        bienvenida.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        bienvenida.setTextFill(Color.web("#ffffff"));

        Label info = new Label("Selecciona una opción del menú lateral para comenzar.");
        info.setTextFill(Color.web("#ffffff"));

        HBox stats = new HBox(20);
        stats.setPadding(new Insets(20, 0, 0, 0));

        stats.getChildren().addAll(
                crearTarjetaStat("Vehículos Dentro", String.valueOf(servicio.getVehiculosDentro().size()), "#ffa700"),
                crearTarjetaStat("Espacios Disponibles", String.valueOf(servicio.getEspaciosDisponibles()), "#26ff0f"),
                crearTarjetaStat("Ingresos Hoy", "$ " + String.format("%.0f", servicio.getTotalIngresosHoy()), "#2196f3")
        );

        contenidoCentral.getChildren().addAll(bienvenida, info, stats);
    }

    private VBox crearTarjetaStat(String titulo, String valor, String color) {
        VBox tarjeta = new VBox(5);
        tarjeta.setPadding(new Insets(20));
        tarjeta.setMinWidth(180);
        tarjeta.setStyle("-fx-background-color: #00a635; -fx-background-radius: 10; " +
                "-fx-border-color: #ffffff" + color + "; -fx-border-width: 0 0 0 4; -fx-border-radius: 0 0 0 10;");

        Label lblTitulo = new Label(titulo);
        lblTitulo.setTextFill(Color.web("#ffffff"));
        lblTitulo.setFont(Font.font("Arial", 12));

        Label lblValor = new Label(valor);
        lblValor.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        lblValor.setTextFill(Color.web(color));

        tarjeta.getChildren().addAll(lblTitulo, lblValor);
        return tarjeta;
    }
}
