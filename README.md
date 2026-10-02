# Clínica Veterinaria

Aplicación web para la gestión de una clínica veterinaria.

| Carpeta | Tecnología | Puerto |
|---|---|---|
| [frontend26-83681](frontend26-83681) | Angular 19 (plantilla Datta Able) | 4200 |
| [backend-83681](backend-83681) | Spring Boot 4 + Java 21 + JPA | 8080 (8081 en el PC del laboratorio) |
| [backend-83681/database](backend-83681/database) | Scripts SQL para MySQL / MariaDB | 3306 (3307 con XAMPP) |

## Módulos

Gestión de usuarios, clientes y mascotas (visibles en el menú), además de razas, médicos y especializaciones
(ocultos en el menú, se activan quitando `hidden: true` en `navigation.ts`). Cada módulo permite listar,
buscar, crear, editar y eliminar, y valida que no se repitan registros:

- **Usuarios:** nombre de usuario y correo únicos.
- **Clientes:** tipo + número de documento únicos.
- **Mascotas:** un cliente no puede tener dos mascotas con el mismo nombre.
- **Razas:** nombre + especie únicos.
- **Médicos:** documento y registro profesional únicos.
- **Especializaciones:** código y nombre únicos.

No se puede eliminar un registro que esté en uso (por ejemplo, una raza con mascotas).

La lupa del menú superior busca en todos los datos (cédula, nombre, correo, teléfono, mascota…) y abre
el módulo con la tabla filtrada.

## Cómo ejecutarlo

### 1. Base de datos

Ejecutar en MySQL, en este orden:

```
backend-83681/database/clinica.sql        -- crea la base "clinica" y sus tablas
backend-83681/database/datos-prueba.sql   -- datos de ejemplo (opcional)
```

Los usuarios de prueba tienen la contraseña `Clinica123`.

### 2. Backend

Requiere Java 21. Por defecto se conecta a `localhost:3306/clinica` con `root` / `1234`; se puede cambiar
con variables de entorno:

```
DB_URL=jdbc:mysql://localhost:3307/clinica?serverTimezone=America/Bogota
DB_USERNAME=root
DB_PASSWORD=
SERVER_PORT=8081
```

```
cd backend-83681
mvnw spring-boot:run
```

### 3. Frontend

```
cd frontend26-83681
npm install
npm start
```

Abrir http://localhost:4200. La URL del backend se configura en
`frontend26-83681/src/environments/environment.ts`.

### Atajo en el PC del laboratorio

`iniciar-clinica.bat` arranca la base de datos de XAMPP (3307), el backend (8081) y el frontend (4200).
En ese PC el puerto 8080 está ocupado por Oracle, por eso el backend usa el 8081.
