package co.uniquindio.parkuq.excepciones;

public class PlacaDuplicadaException extends Exception {

    public PlacaDuplicadaException(String placa) {
        super("El vehículo con placa '" + placa + "' ya se encuentra dentro del parqueadero.");
    }
}
