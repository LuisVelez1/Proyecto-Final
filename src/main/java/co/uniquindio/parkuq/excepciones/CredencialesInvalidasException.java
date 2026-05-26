package co.uniquindio.parkuq.excepciones;

public class CredencialesInvalidasException extends Exception {

    public CredencialesInvalidasException() {
        super("Usuario o contraseña incorrectos.");
    }
}
