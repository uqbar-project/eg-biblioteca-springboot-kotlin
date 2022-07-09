package org.uqbar.biblioteca.controller

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.uqbar.biblioteca.domain.Biblioteca
import org.uqbar.biblioteca.domain.BusinessException
import org.uqbar.biblioteca.domain.Libro
import org.uqbar.biblioteca.domain.NotFoundException

@RestController
class BibliotecaController(val biblioteca: Biblioteca) {

    @GetMapping("/libros")
    @Operation(summary =
        """Permite buscar libros que contengan cierto string en su título, u obtener todos los libros.
           Atiende requests de la forma GET /libros y GET /libros?titulo=xxx."""
    )
    fun getLibros(@RequestParam(value = "titulo", required = false) titulo: String?): List<Libro> {
        return this.biblioteca.searchLibros(titulo)
    }

    @GetMapping("/libros/{id}")
    @Operation(summary =
        """Permite obtener un libro por su id.
           Atiende requests de la forma GET /libros/17."""
    )
    fun getLibroById(@PathVariable id: String): Libro {
        return getLibro(id)
    }

    @DeleteMapping("/libros/{id}")
    @Operation(summary =
        """Permite eliminar un libro por su id.
           Atiende requests de la forma DELETE /libros/7."""
    )
    fun deleteLibroById(@PathVariable id: String): ResponseEntity<String> {
        val libro = getLibro(id)
        this.biblioteca.eliminarLibro(libro.id)
        return ResponseEntity.ok("""{"message":"ok"}""")
    }

    @PostMapping("/libros")
    @Operation(summary =
        """Permite crear un libro.
           Atiende requests de la forma POST /libros con un libro en el body (en formato JSON)."""
    )
    fun createLibro(@RequestBody nuevoLibro: String): ResponseEntity<String> {
        val libro = validarLibro(nuevoLibro)
        if (this.biblioteca.getLibro(libro.id) !== null) {
            throw BusinessException("Ya hay un libro con ese id")
        }
        this.biblioteca.setLibro(libro)
        return ResponseEntity.ok("""{"message":"ok"}""")
    }

    @PutMapping("/libros")
    @Operation(summary =
        """Permite crear un libro.
           Atiende requests de la forma POST /libros con un libro en el body (en formato JSON)."""
    )
    fun updateLibro(@RequestBody nuevoLibro: String): ResponseEntity<String> {
        val libro = validarLibro(nuevoLibro)
        getLibro(libro.id.toString())
        this.biblioteca.setLibro(libro)
        return ResponseEntity.ok("""{"message":"ok"}""")
    }


    /**
     * **********************************************************************
     * DEFINICIONES INTERNAS
     * **********************************************************************
     */
    private fun validarLibro(nuevoLibro: String): Libro {
        try {
            return mapper().readValue(nuevoLibro, Libro::class.java)
        } catch (e: MismatchedInputException) {
            throw BusinessException("El body debe ser un Libro")
        }
    }

    private fun getLibro(idLibro: String): Libro {
        var libro: Libro? = null
        try {
            libro = this.biblioteca.getLibro(Integer.valueOf(idLibro))
        } catch (e: NumberFormatException) {
            throw BusinessException("El id debe ser un número entero")
        }
        if (libro === null) {
            throw NotFoundException("No existe el libro con el identificador $idLibro")
        }
        return libro
    }

    companion object {
        fun mapper(): ObjectMapper {
            return ObjectMapper().apply {
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                configure(SerializationFeature.INDENT_OUTPUT, true)
            }
        }
    }

}