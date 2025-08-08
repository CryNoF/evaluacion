# Evaluación - API de Gestión de Usuarios

API RESTful para la creacion de usuarios con autenticación JWT, desarrollada con Spring Boot 3.x.

## Características

- Registro de usuarios con validación de datos
- Autenticación mediante JWT (JSON Web Tokens)
- Validación de formato de email y contraseña
- Almacenamiento en base de datos H2 (en memoria)
- Documentación con OpenAPI 3.0 (Swagger UI)
- Manejo centralizado de excepciones
- Validación de datos de entrada
- Logging de operaciones

## Requisitos Previos

- Java 21 o superior
- Maven 3.6 o superior
- Spring Boot 3.5.4

## Configuración

1. Clonar el repositorio:
   ```bash
   git clone [URL_DEL_REPOSITORIO]
   cd evaluacion
   ```

2. Configuración de la aplicación:
   - La configuración principal se encuentra en `src/main/resources/application.yml`
   - La base de datos H2 se inicia automáticamente en memoria
   - La consola H2 está disponible en: http://localhost:8080/h2-console
     - JDBC URL: jdbc:h2:mem:testdb
     - Username: camilonavarrete
     - Password: evaluacion

## Ejecución

```bash
./gradlew bootRun
```

La aplicación estará disponible en: http://localhost:8080

## Documentación de la API

La documentación interactiva de la API está disponible en:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/camilonavarrete/evaluacion/
│   │       ├── configs/         # Configuraciones de la aplicación
│   │       ├── controllers/     # Controladores REST
│   │       ├── dto/            # Objetos de transferencia de datos
│   │       ├── entities/        # Entidades de la base de datos
│   │       ├── exceptions/      # Manejo de excepciones
│   │       ├── mappers/         # Mapeadores entre DTOs y entidades
│   │       ├── repository/      # Repositorios de datos
│   │       ├── services/        # Lógica de negocio
│   │       └── utils/           # Utilidades varias
│   └── resources/
│       └── application.yml      # Configuración de la aplicación
└── test/                       # Pruebas unitarias y de integración
```

## Endpoints Principales

### Registrar un nuevo usuario

```http
POST /users
Content-Type: application/json

{
  "name": "Nombre Apellido",
  "email": "usuario@ejemplo.com",
  "password": "Contraseña123",
  "phones": [
    {
      "number": "12345678",
      "citycode": "1",
      "contrycode": "57"
    }
  ]
}
```

### Respuesta exitosa

```json
{
  "id": "a0f8a8b8-78d2-46b8-a6a4-345b7d9d2e1c",
  "name": "Nombre Apellido",
  "email": "usuario@ejemplo.com",
  "phones": [
    {
      "number": "12345678",
      "citycode": "1",
      "contrycode": "57"
    }
  ],
  "created": "2025-08-08T12:00:00Z",
  "modified": "2025-08-08T12:00:00Z",
  "last_login": "2025-08-08T12:00:00Z",
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "isactive": true
}
```

## Validaciones

- **Email**: Debe tener un formato válido (ejemplo@dominio.com)
- **Contraseña**:
  - Mínimo 8 caracteres
  - Al menos una letra mayúscula
  - Al menos un número
  - Al menos un carácter especial
- **Teléfono**:
  - Número: Hasta 10 dígitos
  - Código de ciudad: Hasta 2 dígitos
  - Código de país: Hasta 3 dígitos

## Pruebas

Para ejecutar las pruebas unitarias:

```bash
./gradlew test
```

## Licencia

Este proyecto está bajo la licencia MIT. Ver el archivo [LICENSE](LICENSE) para más detalles.

## Autor

Camilo Navarrete  
[camilonavarrete@gmail.com](mailto:camilonavarrete@gmail.com)
