package co.uniquindio.parkuq.modelo;

import co.uniquindio.parkuq.enums.TipoUsuario;

public class Usuario {

    private String nombre;
    private String identificacion;
    private TipoUsuario tipoUsuario;

    public Usuario(String nombre, String identificacion, TipoUsuario tipoUsuario) {
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.tipoUsuario = tipoUsuario;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }

    public TipoUsuario getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(TipoUsuario tipoUsuario) { this.tipoUsuario = tipoUsuario; }

    @Override
    public String toString() {
        return "Usuario{nombre='" + nombre + "', id='" + identificacion +
                "', tipo=" + tipoUsuario + "}";
    }
}
