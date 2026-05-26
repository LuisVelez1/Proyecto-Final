package co.uniquindio.parkuq.excepciones;

public class VehiculoNoEncontradoException extends Exception {

    public VehiculoNoEncontradoException(String placa) {
        super("No se encontró ningún vehículo activo con la placa '" + placa + "'.");
    }
}
