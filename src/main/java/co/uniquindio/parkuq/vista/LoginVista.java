package co.uniquindio.parkuq.vista;

import co.uniquindio.parkuq.excepciones.CredencialesInvalidasException;
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

public class



LoginVista {

    private Stage stage;
    private ParqueaderoServicio servicio;

    public LoginVista(Stage stage) {
        this.stage = stage;
        this.servicio = new ParqueaderoServicio();
    }

    public void mostrar() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #072354;");

        Label titulo = new Label("PARKUQ");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titulo.setTextFill(Color.web("#ffffff"));

        Label subtitulo = new Label("Sistema de Gestión de Parqueadero");
        subtitulo.setFont(Font.font("Arial", 14));
        subtitulo.setTextFill(Color.web("#ffffff"));

        VBox formBox = new VBox(10);
        formBox.setMaxWidth(320);
        formBox.setStyle("-fx-background-color: #014d6e; -fx-padding: 25; -fx-background-radius: 10;");

        Label lblUsuario = new Label("Usuario");
        lblUsuario.setTextFill(Color.web("#ffffff"));

        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Ingrese su usuario");
        txtUsuario.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black; " +
                "-fx-prompt-text-fill: #5a6a8a; -fx-border-color: #00ff45; " +
                "-fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 8;");

        Label lblContrasena = new Label("Contraseña");
        lblContrasena.setTextFill(Color.web("#ffffff"));

        PasswordField txtContrasena = new PasswordField();
        txtContrasena.setPromptText("Ingrese su contraseña");
        txtContrasena.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black; " +
                "-fx-prompt-text-fill: #5a6a8a; -fx-border-color: #00ff45; " +
                "-fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 8;");

        Label lblError = new Label();
        lblError.setTextFill(Color.web("#e94560"));
        lblError.setVisible(false);

        Button btnLogin = new Button("Iniciar Sesión");
        btnLogin.setMaxWidth(Double.MAX_VALUE);
        btnLogin.setStyle("-fx-background-color: #27c150; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 10; -fx-background-radius: 5; " +
                "-fx-cursor: hand;");

        btnLogin.setOnAction(e -> {
            String usuario = txtUsuario.getText().trim();
            String contrasena = txtContrasena.getText().trim();

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                lblError.setText("Por favor complete todos los campos.");
                lblError.setVisible(true);
                return;
            }

            try {
                Cuenta cuenta = servicio.iniciarSesion(usuario, contrasena);
                lblError.setVisible(false);
                PrincipalVista principal = new PrincipalVista(stage, servicio, cuenta);
                principal.mostrar();
            } catch (CredencialesInvalidasException ex) {
                lblError.setText(ex.getMessage());
                lblError.setVisible(true);
                txtContrasena.clear();
            }
        });

        txtContrasena.setOnAction(e -> btnLogin.fire());

        Label credenciales = new Label("Admin: admin/admin123  |  Operador: operador/op123");
        credenciales.setFont(Font.font("Arial", 10));
        credenciales.setTextFill(Color.web("#ffffff"));

        formBox.getChildren().addAll(lblUsuario, txtUsuario, lblContrasena,
                txtContrasena, lblError, btnLogin);

        root.getChildren().addAll(titulo, subtitulo, formBox, credenciales);

        Scene scene = new Scene(root, 450, 420);
        stage.setTitle("ParkUQ - Iniciar Sesión");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
