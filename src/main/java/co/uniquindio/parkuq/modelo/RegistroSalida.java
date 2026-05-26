package co.uniquindio.parkuq.modelo;

import java.time.LocalDateTime;

public class RegistroSalida {

    private String placa;
    private String nombreConductor;
    private LocalDateTime horaIngreso;
    private LocalDateTime horaSalida;
    private double horasTotales;
    private double valorPagado;

    public RegistroSalida(String placa, String nombreConductor, LocalDateTime horaIngreso,
                          LocalDateTime horaSalida, double horasTotales, double valorPagado) {
        this.placa = placa;
        this.nombreConductor = nombreConductor;
        this.horaIngreso = horaIngreso;
        this.horaSalida = horaSalida;
        this.horasTotales = horasTotales;
        this.valorPagado = valorPagado;
    }

    public String getPlaca() { return placa; }
    public String getNombreConductor() { return nombreConductor; }
    public LocalDateTime getHoraIngreso() { return horaIngreso; }
    public LocalDateTime getHoraSalida() { return horaSalida; }
    public double getHorasTotales() { return horasTotales; }
    public double getValorPagado() { return valorPagado; }

    @Override
    public String toString() {
        return "RegistroSalida{placa='" + placa + "', horas=" + horasTotales +
                ", valor=" + valorPagado + "}";
    }
}
