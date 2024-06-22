# Ejemplo de biblioteca RESTful en Spring Boot - Kotlin

[![Build](https://github.com/uqbar-project/eg-biblioteca-springboot-kotlin/actions/workflows/build.yml/badge.svg)](https://github.com/uqbar-project/eg-biblioteca-springboot/actions/workflows/build.yml) [![codecov](https://codecov.io/gh/uqbar-project/eg-biblioteca-springboot-kotlin/branch/master/graph/badge.svg?token=0ZNNS99PJP)](https://codecov.io/gh/uqbar-project/eg-biblioteca-springboot-kotlin)

Demostración de uso de [Spring Boot](https://spring.io/projects/spring-boot) sobre cómo declarar y probar una API REST con operaciones CRUD y búsqueda.


## Implementación

| package | descripción |
| --- | --- |
| `org.uqbar.biblioteca.domain`      | Modelo de dominio (Biblioteca y Libro) |
| `org.uqbar.biblioteca.controller`       | Definición Spring Boot de la API REST (detalles [más abajo](#api-rest-en-ejemplos)) |
| `org.uqbar.biblioteca.BibliotecaApplication`        | El `main` que levanta un servidor HTTP y inicializa un modelo con libros de prueba |


## API REST en ejemplos

| operation                 | request                   | response status | response description | 
| --- | --- | --- | --- |
| Obtener todos los libros  | `GET /libros`             | 200 OK          | Lista de todos los libros |
| | | | |
| Obtener un libro por id   | `GET /libros/7`           | 200 OK          | Un libro con el id indicado (`7`) |
|                           | `GET /libros/88888`       | 404 Not Found   | No hay libro con el id indicado (`88888`) |
|                           | `GET /libros/Ficc`        | 400 Bad Request | Id mal formado (`Ficc` no es un entero) |
| | | | |
| Buscar libros por título  | `GET /libros?string=Ficc` | 200 OK          | Lista de libros que contengan `ficc` (ignorando mayúsculas/minúsculas) |
| | | | |
| Crear libro     | `POST /libros` (BODY bien)| 200 OK          | El libro recibido en el BODY (formato JSON) ahora pertenece a la biblioteca |
|                           | `POST /libros` (BODY mal) | 400 Bad Request | No pudo leerse al BODY como instancia de `org.uqbar.biblioteca.domain.Libro` |
|                           | `POST /libros` (BODY con un id existente) | 400 Bad Request | Ya existe un libro con ese id |
|                           | `POST /libros` (BODY sin título) | 400 Bad Request | Debe ingresar un título |
| Modificar libro     | `PUT /libros` (BODY bien)| 200 OK          | El libro recibido en el BODY (formato JSON) se actualiza correctamente |
|      | `PUT /libros` (BODY de un id inexistente)| 400 Bad Request          | No existe el libro a actualizar |
|      | `PUT /libros` (BODY sin título)| 400 Bad Request | Debe ingresar un título |
| | | | |
| Borrar libro              | `DELETE /libros/7`        | 200 OK          | Borra el libro con id `7` |
|                           | `DELETE /libros/88888`    | 404 Not Found | No hay libro con id `88888` |
|                           | `DELETE /libros/Ficc`     | 400 Bad Request | Id mal formado (`Ficc` no es un entero) |

**Atención**: La implementación usa formato JSON en el BODY, tanto en request como en response.


## Modo de uso

### Cómo levantar

#### Opción A: Desde IntelliJ IDEA

1. Importar este proyecto en Eclipse como **Gradle project**.
2. Ejecutar `org.uqbar.biblioteca.BibliotecaApplication`, que levanta el servidor en el puerto 8080.

#### Opción B: Desde línea de comandos

1. Compilar y ejecutar el proyecto con el wrapper de Gradle: `./gradlew bootRun`

Esta opción requiere menos recursos de sistema porque no es necesario ejecutar IntelliJ.

### Cómo probar

Probar los [ejemplos de API REST](#api-rest-en-ejemplos)

* en [Postman](https://www.getpostman.com/), importar [este archivo](Biblioteca.postman_collection.json) que provee varios ejemplos de request listos para usar.
* en [Insomnia](https://insomnia.rest/download), importar [este archivo](Biblioteca.insomnia_collection.json)

Si querés ver una demo de cómo probarlo, podés chequear [este link](https://github.com/uqbar-project/eg-tareas-springboot-kotlin).
