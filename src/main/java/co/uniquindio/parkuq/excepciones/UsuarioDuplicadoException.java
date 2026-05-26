package co.uniquindio.parkuq.excepciones;

public class UsuarioDuplicadoException extends Exception {

    public UsuarioDuplicadoException(String identificacion) {
        super("Ya existe un usuario con la identificación '" + identificacion + "'.");
    }
}
