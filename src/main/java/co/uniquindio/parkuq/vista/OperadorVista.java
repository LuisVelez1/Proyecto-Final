package co.uniquindio.parkuq.vista;

import co.uniquindio.parkuq.enums.EstadoEspacio;
import co.uniquindio.parkuq.enums.TipoVehiculo;
import co.uniquindio.parkuq.excepciones.PlacaDuplicadaException;
import co.uniquindio.parkuq.excepciones.SinEspaciosDisponiblesException;
import co.uniquindio.parkuq.excepciones.VehiculoNoEncontradoException;
import co.uniquindio.parkuq.modelo.EspacioParqueadero;
import co.uniquindio.parkuq.modelo.RegistroSalida;
import co.uniquindio.parkuq.modelo.Vehiculo;
import co.uniquindio.parkuq.servicio.ParqueaderoServicio;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class OperadorVista {

    private ParqueaderoServicio servicio;
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public OperadorVista(ParqueaderoServicio servicio) {
        this.servicio = servicio;
    }

    public void mostrarRegistroIngreso(VBox contenedor) {
        contenedor.getChildren().clear();

        Label titulo = crearTitulo("Registrar Ingreso de Vehículo");
        contenedor.getChildren().add(titulo);

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(12);
        form.setPadding(new Insets(20));
        form.setMaxWidth(500);
        form.setStyle("-fx-background-color: #2c0478; -fx-background-radius: 10;");

        TextField txtPlaca = crearTextField("Ej: ABC123");
        TextField txtNombre = crearTextField("Nombre del conductor");
        TextField txtIdentificacion = crearTextField("Número de identificación");
        ComboBox<TipoVehiculo> cbTipo = new ComboBox<>(
                FXCollections.observableArrayList(TipoVehiculo.values()));
        cbTipo.setPromptText("Seleccione tipo");
        cbTipo.setMaxWidth(Double.MAX_VALUE);
        cbTipo.setStyle("-fx-background-color: #328fe6; -fx-text-fill: white;");

        form.add(crearLabel("Placa:"), 0, 0);
        form.add(txtPlaca, 1, 0);
        form.add(crearLabel("Tipo Vehículo:"), 0, 1);
        form.add(cbTipo, 1, 1);
        form.add(crearLabel("Conductor:"), 0, 2);
        form.add(txtNombre, 1, 2);
        form.add(crearLabel("Identificación:"), 0, 3);
        form.add(txtIdentificacion, 1, 3);

        Label lblMensaje = new Label();
        lblMensaje.setWrapText(true);

        Button btnRegistrar = crearBoton("Registrar Ingreso", "#00ff51");
        btnRegistrar.setOnAction(e -> {
            String placa = txtPlaca.getText().trim();
            String nombre = txtNombre.getText().trim();
            String identificacion = txtIdentificacion.getText().trim();
            TipoVehiculo tipo = cbTipo.getValue();

            if (placa.isEmpty() || nombre.isEmpty() || identificacion.isEmpty() || tipo == null) {
                lblMensaje.setTextFill(Color.web("#ff0000"));
                lblMensaje.setText("Por favor complete todos los campos.");
                return;
            }

            try {
                Vehiculo v = servicio.registrarIngreso(placa, tipo, nombre, identificacion);
                lblMensaje.setTextFill(Color.web("#4caf50"));
                lblMensaje.setText("Vehículo " + v.getPlaca() + " registrado en espacio " + v.getEspacioAsignado());
                txtPlaca.clear();
                txtNombre.clear();
                txtIdentificacion.clear();
                cbTipo.setValue(null);
            } catch (PlacaDuplicadaException | SinEspaciosDisponiblesException ex) {
                lblMensaje.setTextFill(Color.web("#e94560"));
                lblMensaje.setText("X" + ex.getMessage());
            }
        });

        contenedor.getChildren().addAll(form, btnRegistrar, lblMensaje);
    }

    public void mostrarRegistroSalida(VBox contenedor) {
        contenedor.getChildren().clear();

        Label titulo = crearTitulo("Registrar Salida de Vehículo");
        contenedor.getChildren().add(titulo);

        VBox formBox = new VBox(12);
        formBox.setPadding(new Insets(20));
        formBox.setMaxWidth(450);
        formBox.setStyle("-fx-background-color: #2c0478; -fx-background-radius: 10;");

        HBox filaPlaca = new HBox(10);
        filaPlaca.setAlignment(Pos.CENTER_LEFT);
        TextField txtPlaca = crearTextField("Placa del vehículo");
        txtPlaca.setMaxWidth(200);
        Button btnBuscar = crearBoton("Buscar", "#2196f3");
        filaPlaca.getChildren().addAll(crearLabel("Placa:"), txtPlaca, btnBuscar);

        VBox infoVehiculo = new VBox(5);
        infoVehiculo.setStyle("-fx-background-color: #2196f3; -fx-padding: 10; -fx-background-radius: 6;");
        infoVehiculo.setVisible(false);

        Label lblInfoPlaca = crearLabel("");
        Label lblInfoConductor = crearLabel("");
        Label lblInfoIngreso = crearLabel("");
        Label lblInfoTipo = crearLabel("");
        infoVehiculo.getChildren().addAll(lblInfoPlaca, lblInfoConductor, lblInfoIngreso, lblInfoTipo);

        Label lblMensaje = new Label();
        lblMensaje.setWrapText(true);

        Button btnRegistrarSalida = crearBoton("Registrar Salida y Calcular Cobro", "#e94560");
        btnRegistrarSalida.setVisible(false);

        btnBuscar.setOnAction(e -> {
            String placa = txtPlaca.getText().trim();
            if (placa.isEmpty()) {
                lblMensaje.setTextFill(Color.web("#e94560"));
                lblMensaje.setText("Ingrese una placa.");
                return;
            }
            try {
                Vehiculo v = servicio.buscarVehiculoDentro(placa);
                lblInfoPlaca.setText("Placa: " + v.getPlaca());
                lblInfoConductor.setText("Conductor: " + v.getNombreConductor());
                lblInfoIngreso.setText("Ingresó: " + v.getHoraIngreso().format(FORMATO));
                lblInfoTipo.setText("Tipo: " + v.getTipoVehiculo());
                infoVehiculo.setVisible(true);
                btnRegistrarSalida.setVisible(true);
                lblMensaje.setText("");
            } catch (VehiculoNoEncontradoException ex) {
                lblMensaje.setTextFill(Color.web("#e94560"));
                lblMensaje.setText("X " + ex.getMessage());
                infoVehiculo.setVisible(false);
                btnRegistrarSalida.setVisible(false);
            }
        });

        btnRegistrarSalida.setOnAction(e -> {
            try {
                RegistroSalida registro = servicio.registrarSalida(txtPlaca.getText().trim());
                lblMensaje.setTextFill(Color.web("#4caf50"));
                lblMensaje.setText(String.format(
                        "Salida registrada.\nTiempo: %.2f horas\nTotal a pagar: $%.0f",
                        registro.getHorasTotales(), registro.getValorPagado()));
                infoVehiculo.setVisible(false);
                btnRegistrarSalida.setVisible(false);
                txtPlaca.clear();
            } catch (VehiculoNoEncontradoException ex) {
                lblMensaje.setTextFill(Color.web("#e94560"));
                lblMensaje.setText("X " + ex.getMessage());
            }
        });

        formBox.getChildren().addAll(filaPlaca, infoVehiculo);
        contenedor.getChildren().addAll(formBox, btnRegistrarSalida, lblMensaje);
    }

    public void mostrarVehiculosDentro(VBox contenedor) {
        contenedor.getChildren().clear();
        contenedor.getChildren().add(crearTitulo("Vehículos Actualmente Dentro"));

        List<Vehiculo> vehiculos = servicio.getVehiculosDentro();

        if (vehiculos.isEmpty()) {
            Label vacio = new Label("No hay vehículos dentro del parqueadero.");
            vacio.setTextFill(Color.web("#a8b2d8"));
            contenedor.getChildren().add(vacio);
            return;
        }

        TableView<Vehiculo> tabla = new TableView<>();
        tabla.setStyle("-fx-background-color: #16213e; -fx-text-fill: white;");
        tabla.setMaxHeight(400);

        TableColumn<Vehiculo, String> colPlaca = new TableColumn<>("Placa");
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        colPlaca.setPrefWidth(100);

        TableColumn<Vehiculo, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoVehiculo"));
        colTipo.setPrefWidth(120);

        TableColumn<Vehiculo, String> colConductor = new TableColumn<>("Conductor");
        colConductor.setCellValueFactory(new PropertyValueFactory<>("nombreConductor"));
        colConductor.setPrefWidth(180);

        TableColumn<Vehiculo, String> colEspacio = new TableColumn<>("Espacio");
        colEspacio.setCellValueFactory(new PropertyValueFactory<>("espacioAsignado"));
        colEspacio.setPrefWidth(100);

        TableColumn<Vehiculo, String> colIngreso = new TableColumn<>("Hora Ingreso");
        colIngreso.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getHoraIngreso().format(FORMATO)));
        colIngreso.setPrefWidth(150);

        tabla.getColumns().addAll(colPlaca, colTipo, colConductor, colEspacio, colIngreso);
        tabla.setItems(FXCollections.observableArrayList(vehiculos));

        Label total = new Label("Total: " + vehiculos.size() + " vehículo(s)");
        total.setTextFill(Color.web("#a8b2d8"));

        contenedor.getChildren().addAll(tabla, total);
    }

    public void mostrarEspacios(VBox contenedor) {
        contenedor.getChildren().clear();
        contenedor.getChildren().add(crearTitulo("Estado de Espacios"));

        HBox resumen = new HBox(20);
        resumen.setPadding(new Insets(0, 0, 15, 0));

        Label lblTotal = new Label("Total: " + servicio.getTotalEspacios());
        lblTotal.setTextFill(Color.web("#a8b2d8"));
        Label lblOcupados = new Label("Ocupados: " + servicio.getEspaciosOcupados());
        lblOcupados.setTextFill(Color.web("#e94560"));
        Label lblDisponibles = new Label("Disponibles: " + servicio.getEspaciosDisponibles());
        lblDisponibles.setTextFill(Color.web("#4caf50"));

        resumen.getChildren().addAll(lblTotal, lblOcupados, lblDisponibles);

        TableView<EspacioParqueadero> tabla = new TableView<>();
        tabla.setMaxHeight(380);

        TableColumn<EspacioParqueadero, String> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colCodigo.setPrefWidth(100);

        TableColumn<EspacioParqueadero, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoEspacio"));
        colTipo.setPrefWidth(120);

        TableColumn<EspacioParqueadero, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEstado.setPrefWidth(150);

        colEstado.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.equals("DISPONIBLE")) setStyle("-fx-text-fill: #4caf50;");
                    else if (item.equals("OCUPADO")) setStyle("-fx-text-fill: #e94560;");
                    else setStyle("-fx-text-fill: #ff9800;");
                }
            }
        });

        TableColumn<EspacioParqueadero, String> colVehiculo = new TableColumn<>("Vehículo");
        colVehiculo.setCellValueFactory(new PropertyValueFactory<>("placaVehiculoAsignado"));
        colVehiculo.setPrefWidth(130);

        tabla.getColumns().addAll(colCodigo, colTipo, colEstado, colVehiculo);
        tabla.setItems(FXCollections.observableArrayList(servicio.getEspacios()));

        contenedor.getChildren().addAll(resumen, tabla);
    }

    public void mostrarReportes(VBox contenedor) {
        contenedor.getChildren().clear();
        contenedor.getChildren().add(crearTitulo("Reportes del Día"));

        VBox reporteBox = new VBox(10);
        reporteBox.setPadding(new Insets(20));
        reporteBox.setStyle("-fx-background-color: #2c0478; -fx-background-radius: 10;");
        reporteBox.setMaxWidth(500);

        reporteBox.getChildren().addAll(
                crearFilaReporte("Total vehículos ingresados hoy:", String.valueOf(servicio.getTotalVehiculosHoy())),
                crearFilaReporte("Ingresos generados hoy:", "$ " + String.format("%.0f", servicio.getTotalIngresosHoy())),
                crearFilaReporte("Tiempo promedio de estancia:", String.format("%.2f horas", servicio.getTiempoPromedioEstancia())),
                crearFilaReporte("Vehículos actualmente dentro:", String.valueOf(servicio.getVehiculosDentro().size()))
        );

        Separator sep = new Separator();
        sep.setPadding(new Insets(10, 0, 10, 0));

        Label lblFiltro = new Label("Vehículos con más de X horas:");
        lblFiltro.setTextFill(Color.web("#a8b2d8"));

        HBox filaFiltro = new HBox(10);
        filaFiltro.setAlignment(Pos.CENTER_LEFT);
        TextField txtHoras = crearTextField("Ej: 2");
        txtHoras.setMaxWidth(80);
        Button btnFiltrar = crearBoton("Filtrar", "#2196f3");

        Label lblResultado = new Label();
        lblResultado.setTextFill(Color.web("#a8b2d8"));
        lblResultado.setWrapText(true);

        btnFiltrar.setOnAction(e -> {
            try {
                double horas = Double.parseDouble(txtHoras.getText().trim());
                List<RegistroSalida> lista = servicio.getVehiculosMasDe(horas);
                if (lista.isEmpty()) {
                    lblResultado.setText("No hay vehículos con más de " + horas + " horas.");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (RegistroSalida r : lista) {
                        sb.append("• ").append(r.getPlaca())
                                .append(" - ").append(String.format("%.2f h", r.getHorasTotales()))
                                .append("\n");
                    }
                    lblResultado.setText(sb.toString());
                }
            } catch (NumberFormatException ex) {
                lblResultado.setText("Ingrese un número válido.");
            }
        });

        filaFiltro.getChildren().addAll(txtHoras, btnFiltrar);
        reporteBox.getChildren().addAll(sep, lblFiltro, filaFiltro, lblResultado);

        contenedor.getChildren().add(reporteBox);
    }

    private HBox crearFilaReporte(String etiqueta, String valor) {
        HBox fila = new HBox(15);
        fila.setAlignment(Pos.CENTER_LEFT);
        Label lbl = new Label(etiqueta);
        lbl.setTextFill(Color.web("#ffffff"));
        lbl.setMinWidth(280);
        Label val = new Label(valor);
        val.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        val.setTextFill(Color.web("#ffffff"));
        fila.getChildren().addAll(lbl, val);
        return fila;
    }

    private Label crearTitulo(String texto) {
        Label lbl = new Label(texto);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        lbl.setTextFill(Color.web("#ffffff"));
        lbl.setPadding(new Insets(0, 0, 15, 0));
        return lbl;
    }

    private Label crearLabel(String texto) {
        Label lbl = new Label(texto);
        lbl.setTextFill(Color.web("#ffffff"));
        return lbl;
    }

    private TextField crearTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-background-color: #000000; -fx-text-fill: white; " +
                "-fx-prompt-text-fill: #ffffff; -fx-padding: 7; -fx-background-radius: 5;");
        return tf;
    }

    private Button crearBoton(String texto, String color) {
        Button btn = new Button(texto);
        btn.setStyle("-fx-background-color: #2196f3" + color + "; -fx-text-fill: white; " +
                "-fx-padding: 8 18; -fx-background-radius: 5; -fx-cursor: hand;");
        return btn;
    }
}
