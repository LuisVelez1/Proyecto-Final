package co.uniquindio.parkuq;

import co.uniquindio.parkuq.vista.LoginVista;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginVista loginVista = new LoginVista(primaryStage);
        loginVista.mostrar();
    }

    public static void main(String[] args) {
        launch(args);
    }
}