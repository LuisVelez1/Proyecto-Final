package co.uniquindio.parkuq;

import co.uniquindio.parkuq.enums.EstadoEspacio;
import co.uniquindio.parkuq.enums.EstadoVehiculo;
import co.uniquindio.parkuq.enums.Rol;
import co.uniquindio.parkuq.enums.TipoUsuario;
import co.uniquindio.parkuq.enums.TipoVehiculo;
import co.uniquindio.parkuq.excepciones.*;
import co.uniquindio.parkuq.modelo.*;
import co.uniquindio.parkuq.servicio.ParqueaderoServicio;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ParqueaderoServicioTest {

    private static ParqueaderoServicio servicio;

    @BeforeAll
    static void setUp() {
        servicio = new ParqueaderoServicio();
    }

    @Test
    @Order(1)
    @DisplayName("Login exitoso como administrador")
    void testLoginAdminExitoso() {
        assertDoesNotThrow(() -> {
            Cuenta cuenta = servicio.iniciarSesion("admin", "admin123");
            assertNotNull(cuenta);
            assertEquals(Rol.ADMINISTRADOR, cuenta.getRol());
        });
    }

    @Test
    @Order(2)
    @DisplayName("Login fallido con credenciales incorrectas")
    void testLoginFallido() {
        assertThrows(CredencialesInvalidasException.class, () -> {
            servicio.iniciarSesion("admin", "clavemal");
        });
    }

    @Test
    @Order(3)
    @DisplayName("Login exitoso como operador")
    void testLoginOperadorExitoso() {
        assertDoesNotThrow(() -> {
            Cuenta cuenta = servicio.iniciarSesion("operador", "op123");
            assertEquals(Rol.OPERADOR, cuenta.getRol());
        });
    }

    @Test
    @Order(4)
    @DisplayName("Registrar ingreso de carro exitosamente")
    void testRegistrarIngresoExitoso() {
        assertDoesNotThrow(() -> {
            Vehiculo v = servicio.registrarIngreso("ABC123", TipoVehiculo.CARRO, "Luis Velez", "123456");
            assertNotNull(v);
            assertEquals("ABC123", v.getPlaca());
            assertEquals(EstadoVehiculo.DENTRO, v.getEstado());
            assertNotNull(v.getEspacioAsignado());
        });
    }

    @Test
    @Order(5)
    @DisplayName("No se puede ingresar vehículo con placa duplicada")
    void testPlacaDuplicada() {
        assertThrows(PlacaDuplicadaException.class, () -> {
            servicio.registrarIngreso("ABC123", TipoVehiculo.CARRO, "Otro Conductor", "999");
        });
    }

    @Test
    @Order(6)
    @DisplayName("La placa se normaliza a mayúsculas")
    void testPlacaMayusculas() {
        assertDoesNotThrow(() -> {
            Vehiculo v = servicio.registrarIngreso("xyz999", TipoVehiculo.MOTOCICLETA, "Pedro", "777");
            assertEquals("XYZ999", v.getPlaca());
        });
    }

    @Test
    @Order(7)
    @DisplayName("Registrar salida calcula valor correctamente")
    void testRegistrarSalida() {
        assertDoesNotThrow(() -> {
            RegistroSalida registro = servicio.registrarSalida("ABC123");
            assertNotNull(registro);
            assertTrue(registro.getValorPagado() >= 0);
            assertTrue(registro.getHorasTotales() >= 0);
        });
    }

    @Test
    @Order(8)
    @DisplayName("No se puede registrar salida de vehículo que no está dentro")
    void testSalidaVehiculoInexistente() {
        assertThrows(VehiculoNoEncontradoException.class, () -> {
            servicio.registrarSalida("NOEXISTE");
        });
    }

    @Test
    @Order(9)
    @DisplayName("No se puede ingresar si no hay espacios disponibles del tipo")
    void testSinEspaciosDisponibles() {
        assertDoesNotThrow(() -> {
            servicio.registrarIngreso("BIC001", TipoVehiculo.BICICLETA, "Daniel", "111");
            servicio.registrarIngreso("BIC002", TipoVehiculo.BICICLETA, "Luis", "222");
        });

        assertThrows(SinEspaciosDisponiblesException.class, () -> {
            servicio.registrarIngreso("BIC003", TipoVehiculo.BICICLETA, "Johan", "333");
        });
    }

    @Test
    @Order(10)
    @DisplayName("Registrar nuevo espacio exitosamente")
    void testRegistrarEspacio() {
        int totalAntes = servicio.getTotalEspacios();
        assertDoesNotThrow(() -> {
            servicio.registrarEspacio("C-99", TipoVehiculo.CARRO);
        });
        assertEquals(totalAntes + 1, servicio.getTotalEspacios());
    }

    @Test
    @Order(11)
    @DisplayName("No se puede registrar espacio con código duplicado")
    void testEspacioDuplicado() {
        assertThrows(EspacioDuplicadoException.class, () -> {
            servicio.registrarEspacio("C-99", TipoVehiculo.CARRO);
        });
    }

    @Test
    @Order(12)
    @DisplayName("Modificar estado de espacio a fuera de servicio")
    void testModificarEstadoEspacio() {
        assertDoesNotThrow(() -> {
            servicio.modificarEstadoEspacio("C-99", EstadoEspacio.FUERA_DE_SERVICIO);
            EspacioParqueadero espacio = servicio.getEspacios().stream()
                    .filter(e -> e.getCodigo().equals("C-99"))
                    .findFirst().orElse(null);
            assertNotNull(espacio);
            assertEquals(EstadoEspacio.FUERA_DE_SERVICIO, espacio.getEstado());
        });
    }

    @Test
    @Order(13)
    @DisplayName("Actualizar tarifa de motocicleta")
    void testActualizarTarifa() {
        servicio.actualizarTarifa(TipoVehiculo.MOTOCICLETA, 2000, 5);
        Tarifa tarifa = servicio.getTarifas().stream()
                .filter(t -> t.getTipoVehiculo() == TipoVehiculo.MOTOCICLETA)
                .findFirst().orElse(null);
        assertNotNull(tarifa);
        assertEquals(2000, tarifa.getValorPorHora());
        assertEquals(5, tarifa.getDescuento());
    }

    @Test
    @Order(14)
    @DisplayName("Tarifa calcula valor con descuento correctamente")
    void testCalculoTarifaConDescuento() {
        Tarifa tarifa = new Tarifa(TipoVehiculo.CARRO, 3000, 10);
        double valor = tarifa.calcularValor(2);
        assertEquals(5400, valor, 0.01);
    }

    @Test
    @Order(15)
    @DisplayName("Registrar usuario autorizado exitosamente")
    void testRegistrarUsuario() {
        assertDoesNotThrow(() -> {
            servicio.registrarUsuario("Daniel Monsalve", "555555", TipoUsuario.DOCENTE);
        });
        assertEquals(1, servicio.getUsuarios().size());
    }

    @Test
    @Order(16)
    @DisplayName("No se puede registrar usuario con identificación duplicada")
    void testUsuarioDuplicado() {
        assertThrows(UsuarioDuplicadoException.class, () -> {
            servicio.registrarUsuario("Otro Nombre", "555555", TipoUsuario.ESTUDIANTE);
        });
    }

    @Test
    @Order(17)
    @DisplayName("Consultar espacios disponibles y ocupados")
    void testConsultaEspacios() {
        int disponibles = servicio.getEspaciosDisponibles();
        int ocupados = servicio.getEspaciosOcupados();
        int total = servicio.getTotalEspacios();
        assertTrue(disponibles >= 0);
        assertTrue(ocupados >= 0);
        assertTrue(disponibles + ocupados <= total);
    }

    @Test
    @Order(18)
    @DisplayName("Buscar vehículo dentro del parqueadero")
    void testBuscarVehiculoDentro() {
        assertDoesNotThrow(() -> {
            servicio.registrarIngreso("TEST01", TipoVehiculo.CARRO, "Test", "000");
            Vehiculo v = servicio.buscarVehiculoDentro("TEST01");
            assertNotNull(v);
            assertEquals("TEST01", v.getPlaca());
        });
    }

    @Test
    @Order(19)
    @DisplayName("Reporte de vehículos con más de cierto tiempo")
    void testReporteVehiculosMasDe() {
        var lista = servicio.getVehiculosMasDe(100);
        assertNotNull(lista);
    }

    @Test
    @Order(20)
    @DisplayName("Tiempo promedio retorna 0 si no hay historial")
    void testTiempoPromedioSinHistorial() {
        ParqueaderoServicio servicioNuevo = new ParqueaderoServicio();
        assertEquals(0, servicioNuevo.getTiempoPromedioEstancia());
    }

    @Test
    @Order(21)
    @DisplayName("Cuenta verifica contraseña correctamente")
    void testCuentaVerificaContrasena() {
        Cuenta cuenta = new Cuenta("usuario1", "pass123", Rol.OPERADOR);
        assertTrue(cuenta.verificarContrasena("pass123"));
        assertFalse(cuenta.verificarContrasena("incorrecta"));
    }

    @Test
    @Order(22)
    @DisplayName("EspacioParqueadero reporta disponibilidad correctamente")
    void testEspacioDisponibilidad() {
        EspacioParqueadero espacio = new EspacioParqueadero("TEST-01", TipoVehiculo.CARRO);
        assertTrue(espacio.estaDisponible());
        espacio.setEstado(EstadoEspacio.OCUPADO);
        assertFalse(espacio.estaDisponible());
    }

    @Test
    @Order(23)
    @DisplayName("Vehículo se crea con estado DENTRO por defecto")
    void testVehiculoEstadoInicial() {
        Vehiculo v = new Vehiculo("ZZZ000", TipoVehiculo.BICICLETA, "Test", "000",
                java.time.LocalDateTime.now(), "B-01");
        assertEquals(EstadoVehiculo.DENTRO, v.getEstado());
    }
}
