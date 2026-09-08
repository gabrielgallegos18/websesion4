# API de empleados

Proyecto Spring Boot para gestionar empleados mediante endpoints REST y respuestas JSON.

## Datos del proyecto

- Group: `ni.edu.uam`
- Artifact: `api-empleados`
- Dependencias: Spring Web y Validation
- Entidad trabajada: Empleado
- Cliente de prueba: Postman

## Estructura

```text
src/main/java/ni/edu/uam/api_empleados
├── controllers
│   └── EmpleadoController.java
├── dto
│   └── EmpleadoDTO.java
├── exceptions
│   └── GlobalExceptionHandler.java
├── services
│   └── EmpleadoService.java
└── ApiEmpleadosApplication.java
```

## Ejecutar

```bash
mvn spring-boot:run
```

La API queda disponible en:

```text
http://localhost:8080
```

## Endpoints

| Metodo | Ruta | Accion | Respuesta esperada |
| --- | --- | --- | --- |
| GET | `/api/empleados` | Listar | `200 OK` |
| GET | `/api/empleados/{id}` | Buscar | `200 OK` o `404 Not Found` |
| POST | `/api/empleados` | Registrar | `201 Created` o `400 Bad Request` |
| PUT | `/api/empleados/{id}` | Actualizar | `200 OK`, `400 Bad Request` o `404 Not Found` |
| DELETE | `/api/empleados/{id}` | Eliminar | `200 OK` o `404 Not Found` |

## JSON valido para POST y PUT

```json
{
  "nombres": "Ana Maria",
  "apellidos": "Lopez Perez",
  "cargo": "Analista de sistemas",
  "salario": 18500.00
}
```

## Prueba de validacion

```json
{
  "nombres": "",
  "apellidos": "Perez",
  "cargo": "",
  "salario": 0
}
```

La API responde `400 Bad Request` porque `nombres` y `cargo` son obligatorios, y `salario` debe ser mayor que cero.

## Orden de prueba en Postman

1. `POST /api/empleados` para registrar un empleado.
2. `GET /api/empleados` para listar empleados.
3. `GET /api/empleados/{id}` usando el id devuelto por el POST.
4. `PUT /api/empleados/{id}` para actualizar el empleado.
5. `DELETE /api/empleados/{id}` para eliminarlo.
6. `GET /api/empleados/{id}` para comprobar el `404 Not Found`.
7. `POST /api/empleados` con datos invalidos para comprobar el `400 Bad Request`.

Tambien se incluye una coleccion importable en:

```text
postman/api-empleados.postman_collection.json
```

## Preguntas de cierre

- `@Controller` puede devolver vistas; `@RestController` devuelve datos directamente, normalmente JSON.
- `@RequestBody` convierte el JSON recibido en un objeto Java.
- `@Valid` ejecuta las reglas de validacion definidas en el DTO.
- Un registro creado correctamente debe devolver `201 Created`.
- Jackson convierte automaticamente JSON a objetos Java y objetos Java a JSON.
