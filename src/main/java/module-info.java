module co.uniquindio.parkuq {
    requires javafx.controls;
    requires javafx.fxml;
    opens co.uniquindio.parkuq to javafx.fxml;
    opens co.uniquindio.parkuq.modelo to javafx.base;
    exports co.uniquindio.parkuq;
    exports co.uniquindio.parkuq.modelo;
    exports co.uniquindio.parkuq.enums;
    exports co.uniquindio.parkuq.excepciones;
    exports co.uniquindio.parkuq.servicio;
    exports co.uniquindio.parkuq.vista;
}
