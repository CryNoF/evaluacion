# Evaluación - API de Creacion de usuarios.

API RESTful para la creacion de usuarios con autenticación JWT, desarrollada con Spring Boot 3.x.

## Características

- Registro de usuarios con validación de datos
- Autenticación mediante JWT (JSON Web Tokens)
- Validación de formato de email y contraseña
- Almacenamiento en base de datos H2 (en memoria)
- Documentación con OpenAPI 3.0 (Swagger UI)
- Manejo centralizado de excepciones
- Validación de datos de entrada

## Requisitos Previos

- Java 21 o superior
- Gradle 8.0 o superior
- Spring Boot 3.5.4

## Configuración

1. Clonar el repositorio:
   ```bash
   git clone git@github.com:CryNoF/evaluacion.git
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
## Diagrama de solucion

![Diagrama](diagrama.png)


## Endpoints Principales

### Registrar un nuevo usuario

```bash
curl --location 'localhost:8080/users' \
--header 'Content-Type: application/json' \
--data-raw '{
  "name": "Camilo Navarrete",
  "email": "camilonavarrete@gmail.com",
  "password": "Camilo123",
  "phones": [
    {
      "number": "000000",
      "citycode": "2",
      "contrycode": "32"
    }
  ]
}'
```

### Respuesta exitosa

```json
{
    "id": "d4fef125-8a16-46fe-a342-cf011fce26e0",
    "name": "Camilo Navarrete",
    "email": "camilonavarrete@gmail.com",
    "phones": [
        {
            "number": "000000",
            "citycode": "2",
            "contrycode": "32"
        }
    ],
    "created": "2025-08-08T16:37:30.096287",
    "modified": "2025-08-08T16:37:30.096307",
    "last_login": "2025-08-08T16:37:30.09171",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjYW1pbG9uYXZhcnJldGVAZ21haWwuY29tIiwiaWF0IjoxNzU0Njg1NDUwLCJleHAiOjE3NTQ3NzE4NTB9.X8J_Qy6vqLTROmAcMOYdfMHqUPTQtMBCv3mxBC93CEo",
    "isactive": true
}
```

## Validaciones

- **Email**: Debe tener un formato válido (ejemplo@dominio.com)
- **Contraseña**:
  - Mínimo 8 caracteres
  - Al menos una letra mayúscula
  - Al menos un número

## Pruebas

Para ejecutar las pruebas unitarias:

```bash
./gradlew test
```

## Licencia

Este proyecto está bajo la licencia MIT. Ver el archivo [LICENSE](LICENSE) para más detalles.

## Autor

Camilo Navarrete  
[camilonavarreteportino@gmail.com](mailto:camilonavarreteportino@gmail.com)
