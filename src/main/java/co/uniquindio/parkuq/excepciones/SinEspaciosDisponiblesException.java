package co.uniquindio.parkuq.excepciones;

import co.uniquindio.parkuq.enums.TipoVehiculo;

public class SinEspaciosDisponiblesException extends Exception {

    public SinEspaciosDisponiblesException(TipoVehiculo tipoVehiculo) {
        super("No hay espacios disponibles para vehículos de tipo: " + tipoVehiculo);
    }
}
