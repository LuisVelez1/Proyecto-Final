package co.uniquindio.parkuq.modelo;

import co.uniquindio.parkuq.enums.Rol;

public class Cuenta {

    private String nombreUsuario;
    private String contrasena;
    private Rol rol;

    public Cuenta(String nombreUsuario, String contrasena, Rol rol) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public boolean verificarContrasena(String contrasena) {
        return this.contrasena.equals(contrasena);
    }

    @Override
    public String toString() {
        return "Cuenta{usuario='" + nombreUsuario + "', rol=" + rol + "}";
    }
}
