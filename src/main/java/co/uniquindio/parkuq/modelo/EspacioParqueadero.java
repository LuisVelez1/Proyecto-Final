package co.uniquindio.parkuq.modelo;

import co.uniquindio.parkuq.enums.EstadoEspacio;
import co.uniquindio.parkuq.enums.TipoVehiculo;

public class EspacioParqueadero {

    private String codigo;
    private TipoVehiculo tipoEspacio;
    private EstadoEspacio estado;
    private String placaVehiculoAsignado;

    public EspacioParqueadero(String codigo, TipoVehiculo tipoEspacio) {
        this.codigo = codigo.toUpperCase();
        this.tipoEspacio = tipoEspacio;
        this.estado = EstadoEspacio.DISPONIBLE;
        this.placaVehiculoAsignado = null;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo.toUpperCase(); }

    public TipoVehiculo getTipoEspacio() { return tipoEspacio; }
    public void setTipoEspacio(TipoVehiculo tipoEspacio) { this.tipoEspacio = tipoEspacio; }

    public EstadoEspacio getEstado() { return estado; }
    public void setEstado(EstadoEspacio estado) { this.estado = estado; }

    public String getPlacaVehiculoAsignado() { return placaVehiculoAsignado; }
    public void setPlacaVehiculoAsignado(String placaVehiculoAsignado) { this.placaVehiculoAsignado = placaVehiculoAsignado; }

    public boolean estaDisponible() {
        return estado == EstadoEspacio.DISPONIBLE;
    }

    @Override
    public String toString() {
        return "EspacioParqueadero{codigo='" + codigo + "', tipo=" + tipoEspacio +
                ", estado=" + estado + ", vehiculo='" + placaVehiculoAsignado + "'}";
    }
}
