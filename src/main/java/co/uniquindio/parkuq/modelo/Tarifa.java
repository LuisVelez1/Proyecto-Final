package co.uniquindio.parkuq.modelo;

import co.uniquindio.parkuq.enums.TipoVehiculo;

public class Tarifa {

    private TipoVehiculo tipoVehiculo;
    private double valorPorHora;
    private double descuento;

    public Tarifa(TipoVehiculo tipoVehiculo, double valorPorHora, double descuento) {
        this.tipoVehiculo = tipoVehiculo;
        this.valorPorHora = valorPorHora;
        this.descuento = descuento;
    }

    public TipoVehiculo getTipoVehiculo() { return tipoVehiculo; }
    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) { this.tipoVehiculo = tipoVehiculo; }

    public double getValorPorHora() { return valorPorHora; }
    public void setValorPorHora(double valorPorHora) { this.valorPorHora = valorPorHora; }

    public double getDescuento() { return descuento; }
    public void setDescuento(double descuento) { this.descuento = descuento; }

    public double calcularValor(double horas) {
        double subtotal = valorPorHora * horas;
        return subtotal - (subtotal * (descuento / 100));
    }

    @Override
    public String toString() {
        return "Tarifa{tipo=" + tipoVehiculo + ", valorHora=" + valorPorHora +
                ", descuento=" + descuento + "%}";
    }
}
