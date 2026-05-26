package co.uniquindio.parkuq.excepciones;

public class EspacioDuplicadoException extends Exception {

    public EspacioDuplicadoException(String codigo) {
        super("Ya existe un espacio con el código '" + codigo + "'.");
    }
}
