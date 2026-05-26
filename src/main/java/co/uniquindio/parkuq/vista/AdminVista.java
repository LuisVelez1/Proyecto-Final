package co.uniquindio.parkuq.vista;

import co.uniquindio.parkuq.enums.EstadoEspacio;
import co.uniquindio.parkuq.enums.TipoUsuario;
import co.uniquindio.parkuq.enums.TipoVehiculo;
import co.uniquindio.parkuq.excepciones.EspacioDuplicadoException;
import co.uniquindio.parkuq.excepciones.UsuarioDuplicadoException;
import co.uniquindio.parkuq.excepciones.VehiculoNoEncontradoException;
import co.uniquindio.parkuq.modelo.EspacioParqueadero;
import co.uniquindio.parkuq.modelo.Tarifa;
import co.uniquindio.parkuq.modelo.Usuario;
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

public class AdminVista {

    private ParqueaderoServicio servicio;

    public AdminVista(ParqueaderoServicio servicio) {
        this.servicio = servicio;
    }

    public void mostrarGestionEspacios(VBox contenedor) {
        contenedor.getChildren().clear();
        contenedor.getChildren().add(crearTitulo("Gestión de Espacios"));

        VBox formBox = new VBox(10);
        formBox.setPadding(new Insets(15));
        formBox.setMaxWidth(450);
        formBox.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10;");

        Label lblNuevo = new Label("Registrar nuevo espacio");
        lblNuevo.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        lblNuevo.setTextFill(Color.web("#2196f3"));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);

        TextField txtCodigo = crearTextField("Ej: C-06");
        ComboBox<TipoVehiculo> cbTipo = new ComboBox<>(FXCollections.observableArrayList(TipoVehiculo.values()));
        cbTipo.setPromptText("Tipo de espacio");
        cbTipo.setStyle("-fx-background-color: #ffffff;");
        cbTipo.setMaxWidth(Double.MAX_VALUE);

        form.add(crearLabel("Código:"), 0, 0);
        form.add(txtCodigo, 1, 0);
        form.add(crearLabel("Tipo:"), 0, 1);
        form.add(cbTipo, 1, 1);

        Label lblMsg = new Label();
        lblMsg.setWrapText(true);

        Button btnAgregar = crearBoton("Agregar Espacio", "#4caf50");
        btnAgregar.setOnAction(e -> {
            String codigo = txtCodigo.getText().trim();
            TipoVehiculo tipo = cbTipo.getValue();
            if (codigo.isEmpty() || tipo == null) {
                lblMsg.setTextFill(Color.web("#e94560"));
                lblMsg.setText("Complete todos los campos.");
                return;
            }
            try {
                servicio.registrarEspacio(codigo, tipo);
                lblMsg.setTextFill(Color.web("#4caf50"));
                lblMsg.setText("Espacio " + codigo.toUpperCase() + " registrado.");
                txtCodigo.clear();
                cbTipo.setValue(null);
                actualizarTablaEspacios(tablaEspaciosRef, contenedor);
            } catch (EspacioDuplicadoException ex) {
                lblMsg.setTextFill(Color.web("#e94560"));
                lblMsg.setText("X " + ex.getMessage());
            }
        });

        Separator sep = new Separator();
        sep.setPadding(new Insets(5, 0, 5, 0));

        Label lblCambiarEstado = new Label("Cambiar estado de espacio");
        lblCambiarEstado.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        lblCambiarEstado.setTextFill(Color.web("#2196f3"));

        HBox filaEstado = new HBox(10);
        filaEstado.setAlignment(Pos.CENTER_LEFT);
        TextField txtCodigoEstado = crearTextField("Código del espacio");
        txtCodigoEstado.setMaxWidth(120);
        ComboBox<EstadoEspacio> cbEstado = new ComboBox<>(
                FXCollections.observableArrayList(EstadoEspacio.values()));
        cbEstado.setPromptText("Nuevo estado");
        cbEstado.setStyle("-fx-background-color: #ffffff;");
        Button btnCambiar = crearBoton("Cambiar", "#ff9800");

        Label lblMsgEstado = new Label();
        lblMsgEstado.setWrapText(true);

        btnCambiar.setOnAction(e -> {
            String cod = txtCodigoEstado.getText().trim();
            EstadoEspacio nuevoEstado = cbEstado.getValue();
            if (cod.isEmpty() || nuevoEstado == null) {
                lblMsgEstado.setTextFill(Color.web("#e94560"));
                lblMsgEstado.setText("Complete los campos.");
                return;
            }
            try {
                servicio.modificarEstadoEspacio(cod, nuevoEstado);
                lblMsgEstado.setTextFill(Color.web("#4caf50"));
                lblMsgEstado.setText("Estado actualizado.");
            } catch (VehiculoNoEncontradoException ex) {
                lblMsgEstado.setTextFill(Color.web("#e94560"));
                lblMsgEstado.setText("Espacio no encontrado.");
            }
        });

        filaEstado.getChildren().addAll(txtCodigoEstado, cbEstado, btnCambiar);
        formBox.getChildren().addAll(lblNuevo, form, btnAgregar, lblMsg,
                sep, lblCambiarEstado, filaEstado, lblMsgEstado);

        TableView<EspacioParqueadero> tabla = crearTablaEspacios();
        contenedor.getChildren().addAll(formBox, tabla);
    }

    private TableView<EspacioParqueadero> tablaEspaciosRef;

    private TableView<EspacioParqueadero> crearTablaEspacios() {
        TableView<EspacioParqueadero> tabla = new TableView<>();
        tabla.setMaxHeight(250);

        TableColumn<EspacioParqueadero, String> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colCodigo.setPrefWidth(100);

        TableColumn<EspacioParqueadero, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoEspacio"));
        colTipo.setPrefWidth(130);

        TableColumn<EspacioParqueadero, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEstado.setPrefWidth(150);

        tabla.getColumns().addAll(colCodigo, colTipo, colEstado);
        tabla.setItems(FXCollections.observableArrayList(servicio.getEspacios()));
        tablaEspaciosRef = tabla;
        return tabla;
    }

    private void actualizarTablaEspacios(TableView<EspacioParqueadero> tabla, VBox contenedor) {
        if (tabla != null) {
            tabla.setItems(FXCollections.observableArrayList(servicio.getEspacios()));
        }
    }

    public void mostrarGestionTarifas(VBox contenedor) {
        contenedor.getChildren().clear();
        contenedor.getChildren().add(crearTitulo("Gestión de Tarifas"));

        VBox formBox = new VBox(12);
        formBox.setPadding(new Insets(20));
        formBox.setMaxWidth(420);
        formBox.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10;");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(10);

        ComboBox<TipoVehiculo> cbTipo = new ComboBox<>(FXCollections.observableArrayList(TipoVehiculo.values()));
        cbTipo.setPromptText("Tipo vehículo");
        cbTipo.setStyle("-fx-background-color: #ffffff;");
        cbTipo.setMaxWidth(Double.MAX_VALUE);

        TextField txtValor = crearTextField("Valor por hora ($)");
        TextField txtDescuento = crearTextField("Descuento (%) - 0 si no aplica");

        form.add(crearLabel("Tipo Vehículo:"), 0, 0);
        form.add(cbTipo, 1, 0);
        form.add(crearLabel("Valor/Hora:"), 0, 1);
        form.add(txtValor, 1, 1);
        form.add(crearLabel("Descuento %:"), 0, 2);
        form.add(txtDescuento, 1, 2);

        Label lblMsg = new Label();
        lblMsg.setWrapText(true);

        Button btnActualizar = crearBoton("Actualizar Tarifa", "#2196f3");
        btnActualizar.setOnAction(e -> {
            try {
                TipoVehiculo tipo = cbTipo.getValue();
                if (tipo == null || txtValor.getText().isEmpty()) {
                    lblMsg.setTextFill(Color.web("#e94560"));
                    lblMsg.setText("⚠ Complete todos los campos.");
                    return;
                }
                double valor = Double.parseDouble(txtValor.getText().trim());
                double descuento = txtDescuento.getText().isEmpty() ? 0
                        : Double.parseDouble(txtDescuento.getText().trim());
                servicio.actualizarTarifa(tipo, valor, descuento);
                lblMsg.setTextFill(Color.web("#4caf50"));
                lblMsg.setText("Tarifa actualizada correctamente.");
                cbTipo.setValue(null);
                txtValor.clear();
                txtDescuento.clear();
            } catch (NumberFormatException ex) {
                lblMsg.setTextFill(Color.web("#e94560"));
                lblMsg.setText("Ingrese valores numéricos válidos.");
            }
        });

        formBox.getChildren().addAll(form, btnActualizar, lblMsg);

        TableView<Tarifa> tabla = new TableView<>();
        tabla.setMaxHeight(200);

        TableColumn<Tarifa, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoVehiculo"));
        colTipo.setPrefWidth(130);

        TableColumn<Tarifa, Double> colValor = new TableColumn<>("Valor/Hora ($)");
        colValor.setCellValueFactory(new PropertyValueFactory<>("valorPorHora"));
        colValor.setPrefWidth(130);

        TableColumn<Tarifa, Double> colDescuento = new TableColumn<>("Descuento (%)");
        colDescuento.setCellValueFactory(new PropertyValueFactory<>("descuento"));
        colDescuento.setPrefWidth(130);

        tabla.getColumns().addAll(colTipo, colValor, colDescuento);
        tabla.setItems(FXCollections.observableArrayList(servicio.getTarifas()));

        Label lblTarifas = new Label("Tarifas actuales:");
        lblTarifas.setTextFill(Color.web("#a8b2d8"));
        lblTarifas.setPadding(new Insets(10, 0, 5, 0));

        contenedor.getChildren().addAll(formBox, lblTarifas, tabla);
    }

    public void mostrarGestionUsuarios(VBox contenedor) {
        contenedor.getChildren().clear();
        contenedor.getChildren().add(crearTitulo("Gestión de Usuarios Autorizados"));

        VBox formBox = new VBox(10);
        formBox.setPadding(new Insets(15));
        formBox.setMaxWidth(450);
        formBox.setStyle("-fx-background-color: #ffffff ; -fx-background-radius: 10;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);

        TextField txtNombre = crearTextField("Nombre completo");
        TextField txtIdentificacion = crearTextField("Número de identificación");
        ComboBox<TipoUsuario> cbTipo = new ComboBox<>(
                FXCollections.observableArrayList(TipoUsuario.values()));
        cbTipo.setPromptText("Tipo de usuario");
        cbTipo.setStyle("-fx-background-color: #ffffff;");
        cbTipo.setMaxWidth(Double.MAX_VALUE);

        form.add(crearLabel("Nombre:"), 0, 0);
        form.add(txtNombre, 1, 0);
        form.add(crearLabel("Identificación:"), 0, 1);
        form.add(txtIdentificacion, 1, 1);
        form.add(crearLabel("Tipo:"), 0, 2);
        form.add(cbTipo, 1, 2);

        Label lblMsg = new Label();
        lblMsg.setWrapText(true);

        Button btnRegistrar = crearBoton("Registrar Usuario", "#4caf50");
        btnRegistrar.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            String id = txtIdentificacion.getText().trim();
            TipoUsuario tipo = cbTipo.getValue();

            if (nombre.isEmpty() || id.isEmpty() || tipo == null) {
                lblMsg.setTextFill(Color.web("#e94560"));
                lblMsg.setText("⚠ Complete todos los campos.");
                return;
            }

            try {
                servicio.registrarUsuario(nombre, id, tipo);
                lblMsg.setTextFill(Color.web("#4caf50"));
                lblMsg.setText("Usuario " + nombre + " registrado.");
                txtNombre.clear();
                txtIdentificacion.clear();
                cbTipo.setValue(null);
            } catch (UsuarioDuplicadoException ex) {
                lblMsg.setTextFill(Color.web("#e94560"));
                lblMsg.setText("X " + ex.getMessage());
            }
        });

        Label descuentoInfo = new Label(
                "ℹ Descuentos automáticos: Estudiante 10% | Docente 20% | Administrativo 15%");
        descuentoInfo.setTextFill(Color.web("#5a6a8a"));
        descuentoInfo.setFont(Font.font("Arial", 11));
        descuentoInfo.setWrapText(true);

        formBox.getChildren().addAll(form, descuentoInfo, btnRegistrar, lblMsg);

        TableView<Usuario> tabla = new TableView<>();
        tabla.setMaxHeight(250);

        TableColumn<Usuario, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setPrefWidth(180);

        TableColumn<Usuario, String> colId = new TableColumn<>("Identificación");
        colId.setCellValueFactory(new PropertyValueFactory<>("identificacion"));
        colId.setPrefWidth(150);

        TableColumn<Usuario, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoUsuario"));
        colTipo.setPrefWidth(130);

        tabla.getColumns().addAll(colNombre, colId, colTipo);
        tabla.setItems(FXCollections.observableArrayList(servicio.getUsuarios()));

        Label lblUsuarios = new Label("Usuarios registrados:");
        lblUsuarios.setTextFill(Color.web("#a8b2d8"));
        lblUsuarios.setPadding(new Insets(10, 0, 5, 0));

        contenedor.getChildren().addAll(formBox, lblUsuarios, tabla);
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
        lbl.setTextFill(Color.web("#4caf50"));
        return lbl;
    }

    private TextField crearTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; " +
                "-fx-prompt-text-fill: #ffffff; -fx-padding: 5; -fx-background-radius: 5;");
        return tf;
    }

    private Button crearBoton(String texto, String color) {
        Button btn = new Button(texto);
        btn.setStyle("-fx-background-color: #4caf50" + color + "; -fx-text-fill: white; " +
                "-fx-padding: 8 18; -fx-background-radius: 5; -fx-cursor: hand;");
        return btn;
    }
}
