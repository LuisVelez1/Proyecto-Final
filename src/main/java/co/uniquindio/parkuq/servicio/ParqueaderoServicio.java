package co.uniquindio.parkuq.servicio;

import co.uniquindio.parkuq.enums.EstadoEspacio;
import co.uniquindio.parkuq.enums.EstadoVehiculo;
import co.uniquindio.parkuq.enums.Rol;
import co.uniquindio.parkuq.enums.TipoVehiculo;
import co.uniquindio.parkuq.excepciones.*;
import co.uniquindio.parkuq.modelo.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ParqueaderoServicio {

    private List<Vehiculo> vehiculos;
    private List<EspacioParqueadero> espacios;
    private List<Tarifa> tarifas;
    private List<Usuario> usuarios;
    private List<Cuenta> cuentas;
    private List<RegistroSalida> historialSalidas;

    public ParqueaderoServicio() {
        vehiculos = new ArrayList<>();
        espacios = new ArrayList<>();
        tarifas = new ArrayList<>();
        usuarios = new ArrayList<>();
        cuentas = new ArrayList<>();
        historialSalidas = new ArrayList<>();
        inicializarDatosPorDefecto();
    }

    private void inicializarDatosPorDefecto() {
        cuentas.add(new Cuenta("admin", "admin123", Rol.ADMINISTRADOR));
        cuentas.add(new Cuenta("operador", "op123", Rol.OPERADOR));

        tarifas.add(new Tarifa(TipoVehiculo.CARRO, 3000, 0));
        tarifas.add(new Tarifa(TipoVehiculo.MOTOCICLETA, 1500, 0));
        tarifas.add(new Tarifa(TipoVehiculo.BICICLETA, 500, 0));

        for (int i = 1; i <= 5; i++) {
            espacios.add(new EspacioParqueadero("C-" + String.format("%02d", i), TipoVehiculo.CARRO));
        }
        for (int i = 1; i <= 3; i++) {
            espacios.add(new EspacioParqueadero("M-" + String.format("%02d", i), TipoVehiculo.MOTOCICLETA));
        }
        for (int i = 1; i <= 2; i++) {
            espacios.add(new EspacioParqueadero("B-" + String.format("%02d", i), TipoVehiculo.BICICLETA));
        }
    }

    public Cuenta iniciarSesion(String nombreUsuario, String contrasena) throws CredencialesInvalidasException {
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getNombreUsuario().equals(nombreUsuario) && cuenta.verificarContrasena(contrasena)) {
                return cuenta;
            }
        }
        throw new CredencialesInvalidasException();
    }

    public Vehiculo registrarIngreso(String placa, TipoVehiculo tipo, String nombreConductor,
                                     String identificacion) throws PlacaDuplicadaException, SinEspaciosDisponiblesException {
        placa = placa.toUpperCase();

        for (Vehiculo v : vehiculos) {
            if (v.getPlaca().equals(placa) && v.getEstado() == EstadoVehiculo.DENTRO) {
                throw new PlacaDuplicadaException(placa);
            }
        }

        EspacioParqueadero espacio = buscarEspacioDisponible(tipo);
        if (espacio == null) {
            throw new SinEspaciosDisponiblesException(tipo);
        }

        Vehiculo vehiculo = new Vehiculo(placa, tipo, nombreConductor, identificacion,
                LocalDateTime.now(), espacio.getCodigo());

        espacio.setEstado(EstadoEspacio.OCUPADO);
        espacio.setPlacaVehiculoAsignado(placa);
        vehiculos.add(vehiculo);
        return vehiculo;
    }

    private EspacioParqueadero buscarEspacioDisponible(TipoVehiculo tipo) {
        for (EspacioParqueadero espacio : espacios) {
            if (espacio.getTipoEspacio() == tipo && espacio.estaDisponible()) {
                return espacio;
            }
        }
        return null;
    }

    public RegistroSalida registrarSalida(String placa) throws VehiculoNoEncontradoException {
        placa = placa.toUpperCase();
        Vehiculo vehiculo = buscarVehiculoDentro(placa);

        LocalDateTime horaSalida = LocalDateTime.now();
        long minutos = ChronoUnit.MINUTES.between(vehiculo.getHoraIngreso(), horaSalida);
        double horas = Math.max(minutos / 60.0, 0.0167);

        Tarifa tarifa = buscarTarifa(vehiculo.getTipoVehiculo());

        double descuentoUsuario = obtenerDescuentoConductor(vehiculo.getIdentificacionConductor());
        double tarifaConDescuento = tarifa.getValorPorHora() * (1 - descuentoUsuario / 100);
        double valorTotal = tarifaConDescuento * horas;

        vehiculo.setHoraSalida(horaSalida);
        vehiculo.setEstado(EstadoVehiculo.SALIO);

        for (EspacioParqueadero espacio : espacios) {
            if (espacio.getCodigo().equals(vehiculo.getEspacioAsignado())) {
                espacio.setEstado(EstadoEspacio.DISPONIBLE);
                espacio.setPlacaVehiculoAsignado(null);
                break;
            }
        }

        RegistroSalida registro = new RegistroSalida(placa, vehiculo.getNombreConductor(),
                vehiculo.getHoraIngreso(), horaSalida, horas, valorTotal);
        historialSalidas.add(registro);
        return registro;
    }

    private double obtenerDescuentoConductor(String identificacion) {
        for (Usuario usuario : usuarios) {
            if (usuario.getIdentificacion().equals(identificacion)) {
                return switch (usuario.getTipoUsuario()) {
                    case ESTUDIANTE -> 10;
                    case DOCENTE -> 20;
                    case ADMINISTRATIVO -> 15;
                    default -> 0;
                };
            }
        }
        return 0;
    }

    public Vehiculo buscarVehiculoDentro(String placa) throws VehiculoNoEncontradoException {
        placa = placa.toUpperCase();
        for (Vehiculo v : vehiculos) {
            if (v.getPlaca().equals(placa) && v.getEstado() == EstadoVehiculo.DENTRO) {
                return v;
            }
        }
        throw new VehiculoNoEncontradoException(placa);
    }

    private Tarifa buscarTarifa(TipoVehiculo tipo) {
        for (Tarifa t : tarifas) {
            if (t.getTipoVehiculo() == tipo) {
                return t;
            }
        }
        return new Tarifa(tipo, 1000, 0);
    }

    public List<Vehiculo> getVehiculosDentro() {
        List<Vehiculo> dentro = new ArrayList<>();
        for (Vehiculo v : vehiculos) {
            if (v.getEstado() == EstadoVehiculo.DENTRO) {
                dentro.add(v);
            }
        }
        return dentro;
    }

    public int getTotalEspacios() { return espacios.size(); }

    public int getEspaciosOcupados() {
        int count = 0;
        for (EspacioParqueadero e : espacios) {
            if (e.getEstado() == EstadoEspacio.OCUPADO) count++;
        }
        return count;
    }

    public int getEspaciosDisponibles() {
        int count = 0;
        for (EspacioParqueadero e : espacios) {
            if (e.estaDisponible()) count++;
        }
        return count;
    }

    public List<EspacioParqueadero> getEspacios() { return espacios; }
    public List<Tarifa> getTarifas() { return tarifas; }
    public List<Usuario> getUsuarios() { return usuarios; }
    public List<RegistroSalida> getHistorialSalidas() { return historialSalidas; }
    public List<Vehiculo> getTodosVehiculos() { return vehiculos; }

    public void registrarEspacio(String codigo, TipoVehiculo tipo) throws EspacioDuplicadoException {
        codigo = codigo.toUpperCase();
        for (EspacioParqueadero e : espacios) {
            if (e.getCodigo().equals(codigo)) {
                throw new EspacioDuplicadoException(codigo);
            }
        }
        espacios.add(new EspacioParqueadero(codigo, tipo));
    }

    public void modificarEstadoEspacio(String codigo, EstadoEspacio nuevoEstado) throws VehiculoNoEncontradoException {
        codigo = codigo.toUpperCase();
        for (EspacioParqueadero e : espacios) {
            if (e.getCodigo().equals(codigo)) {
                e.setEstado(nuevoEstado);
                return;
            }
        }
        throw new VehiculoNoEncontradoException(codigo);
    }

    public void actualizarTarifa(TipoVehiculo tipo, double valorPorHora, double descuento) {
        for (Tarifa t : tarifas) {
            if (t.getTipoVehiculo() == tipo) {
                t.setValorPorHora(valorPorHora);
                t.setDescuento(descuento);
                return;
            }
        }
        tarifas.add(new Tarifa(tipo, valorPorHora, descuento));
    }

    public void registrarUsuario(String nombre, String identificacion, co.uniquindio.parkuq.enums.TipoUsuario tipo)
            throws UsuarioDuplicadoException {
        for (Usuario u : usuarios) {
            if (u.getIdentificacion().equals(identificacion)) {
                throw new UsuarioDuplicadoException(identificacion);
            }
        }
        usuarios.add(new Usuario(nombre, identificacion, tipo));
    }

    public double getTotalIngresosHoy() {
        LocalDateTime hoy = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        double total = 0;
        for (RegistroSalida r : historialSalidas) {
            if (r.getHoraSalida().isAfter(hoy)) {
                total += r.getValorPagado();
            }
        }
        return total;
    }

    public int getTotalVehiculosHoy() {
        LocalDateTime hoy = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        int count = 0;
        for (RegistroSalida r : historialSalidas) {
            if (r.getHoraSalida().isAfter(hoy)) count++;
        }
        return count;
    }

    public double getTiempoPromedioEstancia() {
        if (historialSalidas.isEmpty()) return 0;
        double total = 0;
        for (RegistroSalida r : historialSalidas) {
            total += r.getHorasTotales();
        }
        return total / historialSalidas.size();
    }

    public List<RegistroSalida> getVehiculosMasDe(double horas) {
        List<RegistroSalida> resultado = new ArrayList<>();
        for (RegistroSalida r : historialSalidas) {
            if (r.getHorasTotales() > horas) {
                resultado.add(r);
            }
        }
        return resultado;
    }
}
