package co.uniquindio.parkuq.modelo;

import co.uniquindio.parkuq.enums.EstadoVehiculo;
import co.uniquindio.parkuq.enums.TipoVehiculo;

import java.time.LocalDateTime;

public class Vehiculo {

    private String placa;
    private TipoVehiculo tipoVehiculo;
    private String nombreConductor;
    private String identificacionConductor;
    private LocalDateTime horaIngreso;
    private LocalDateTime horaSalida;
    private String espacioAsignado;
    private EstadoVehiculo estado;

    public Vehiculo(String placa, TipoVehiculo tipoVehiculo, String nombreConductor,
                    String identificacionConductor, LocalDateTime horaIngreso, String espacioAsignado) {
        this.placa = placa.toUpperCase();
        this.tipoVehiculo = tipoVehiculo;
        this.nombreConductor = nombreConductor;
        this.identificacionConductor = identificacionConductor;
        this.horaIngreso = horaIngreso;
        this.espacioAsignado = espacioAsignado;
        this.estado = EstadoVehiculo.DENTRO;
    }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa.toUpperCase(); }

    public TipoVehiculo getTipoVehiculo() { return tipoVehiculo; }
    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) { this.tipoVehiculo = tipoVehiculo; }

    public String getNombreConductor() { return nombreConductor; }
    public void setNombreConductor(String nombreConductor) { this.nombreConductor = nombreConductor; }

    public String getIdentificacionConductor() { return identificacionConductor; }
    public void setIdentificacionConductor(String identificacionConductor) { this.identificacionConductor = identificacionConductor; }

    public LocalDateTime getHoraIngreso() { return horaIngreso; }
    public void setHoraIngreso(LocalDateTime horaIngreso) { this.horaIngreso = horaIngreso; }

    public LocalDateTime getHoraSalida() { return horaSalida; }
    public void setHoraSalida(LocalDateTime horaSalida) { this.horaSalida = horaSalida; }

    public String getEspacioAsignado() { return espacioAsignado; }
    public void setEspacioAsignado(String espacioAsignado) { this.espacioAsignado = espacioAsignado; }

    public EstadoVehiculo getEstado() { return estado; }
    public void setEstado(EstadoVehiculo estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Vehiculo{placa='" + placa + "', tipo=" + tipoVehiculo +
                ", conductor='" + nombreConductor + "', espacio='" + espacioAsignado +
                "', ingreso=" + horaIngreso + ", estado=" + estado + "}";
    }
}
