# ParkUQ - Sistema de Gestión de Parqueadero
## Universidad del Quindío

---

## Estructura del Proyecto

```
parkuq/
├── pom.xml
└── src/
    ├── main/java/co/uniquindio/parkuq/
    │   ├── Main.java                        <- Punto de entrada JavaFX
    │   ├── module-info.java
    │   ├── enums/
    │   │   ├── TipoVehiculo.java            <- CARRO, MOTOCICLETA, BICICLETA
    │   │   ├── TipoUsuario.java             <- ESTUDIANTE, DOCENTE, ADMINISTRATIVO, VISITANTE
    │   │   ├── EstadoVehiculo.java          <- DENTRO, SALIO
    │   │   ├── EstadoEspacio.java           <- DISPONIBLE, OCUPADO, FUERA_DE_SERVICIO
    │   │   └── Rol.java                     <- ADMINISTRADOR, OPERADOR
    │   ├── modelo/
    │   │   ├── Vehiculo.java
    │   │   ├── EspacioParqueadero.java
    │   │   ├── Tarifa.java
    │   │   ├── Usuario.java
    │   │   ├── Cuenta.java
    │   │   └── RegistroSalida.java
    │   ├── excepciones/
    │   │   ├── PlacaDuplicadaException.java
    │   │   ├── SinEspaciosDisponiblesException.java
    │   │   ├── VehiculoNoEncontradoException.java
    │   │   ├── EspacioDuplicadoException.java
    │   │   ├── UsuarioDuplicadoException.java
    │   │   └── CredencialesInvalidasException.java
    │   ├── servicio/
    │   │   └── ParqueaderoServicio.java      <- Toda la lógica del negocio
    │   └── vista/
    │       ├── LoginVista.java
    │       ├── PrincipalVista.java
    │       ├── OperadorVista.java
    │       └── AdminVista.java
    └── test/java/co/uniquindio/parkuq/
        └── ParqueaderoServicioTest.java      <- 23 pruebas JUnit 5
```

---

## Cómo ejecutar

### Requisitos
- Java 21 LTS
- Maven 3.8+

### Ejecutar la aplicación JavaFX
```bash
mvn javafx:run
```

### Ejecutar pruebas
```bash
mvn test
```

### Compilar
```bash
mvn compile
```

---

## Credenciales por defecto

| Usuario    | Contraseña | Rol            |
|------------|------------|----------------|
| admin      | admin123   | ADMINISTRADOR  |
| operador   | op123      | OPERADOR       |

---

## Espacios predefinidos

| Código | Tipo         | Cantidad |
|--------|--------------|----------|
| C-01 a C-05 | CARRO   | 5        |
| M-01 a M-03 | MOTOCICLETA | 3   |
| B-01 a B-02 | BICICLETA | 2      |

---

## Tarifas por defecto

| Tipo         | Valor/Hora |
|--------------|------------|
| Carro        | $3.000     |
| Motocicleta  | $1.500     |
| Bicicleta    | $500       |

---

## Descuentos para usuarios autorizados

| Tipo Usuario  | Descuento |
|---------------|-----------|
| Estudiante    | 10%       |
| Docente       | 20%       |
| Administrativo| 15%       |
| Visitante     | 0%        |
