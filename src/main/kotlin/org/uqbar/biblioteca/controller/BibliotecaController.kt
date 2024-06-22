package org.uqbar.biblioteca.controller

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.uqbar.biblioteca.domain.Biblioteca
import org.uqbar.biblioteca.domain.BusinessException
import org.uqbar.biblioteca.domain.Libro
import org.uqbar.biblioteca.domain.NotFoundException

@RestController
class BibliotecaController(val biblioteca: Biblioteca) {

    @GetMapping("/libros")
    fun getLibros(@RequestParam(value = "titulo", required = false) titulo: String?): List<Libro> {
        return this.biblioteca.searchLibros(titulo)
    }

    @GetMapping("/libros/{id}")
    fun getLibroById(@PathVariable id: String): Libro {
        return getLibro(id)
    }

    @DeleteMapping("/libros/{id}")
    fun deleteLibroById(@PathVariable id: String): ResponseEntity<String> {
        val libro = getLibro(id)
        this.biblioteca.eliminarLibro(libro.id)
        return ResponseEntity.ok("""{"message":"ok"}""")
    }

    @PostMapping("/libros")
    fun createLibro(@RequestBody nuevoLibro: String): ResponseEntity<String> {
        val libro = validarLibro(nuevoLibro)
        if (this.biblioteca.getLibro(libro.id) !== null) {
            throw BusinessException("Ya hay un libro con ese id")
        }
        this.biblioteca.setLibro(libro)
        return ResponseEntity.ok("""{"message":"ok"}""")
    }

    @PutMapping("/libros")
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
        val libro: Libro?
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