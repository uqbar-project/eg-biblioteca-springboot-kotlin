package org.uqbar.biblioteca

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.uqbar.biblioteca.controller.BibliotecaController
import org.uqbar.biblioteca.domain.Biblioteca

const val OK_MESSAGE = "ok"

@TestConfiguration
class TestConfig {

    @Bean
    fun initializeBiblioteca(): Biblioteca {
        return Biblioteca().apply {
            addLibro(1, "Juancito y los clonosaurios")
            addLibro(5, "Ficciones")
            addLibro(7, "El Aleph")
            addLibro(11, "Historia universal de la infamia")
            addLibro(13, "El informe de Brodie")
            addLibro(17, "El libro de arena")
        }
    }
}

@Import(TestConfig::class)
@AutoConfigureJsonTesters
@ContextConfiguration(classes = [BibliotecaController::class])
@WebMvcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Dado un controller de biblioteca")
class BibliotecaControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `Se pueden obtener todos los libros`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/libros"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(6))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].titulo").value("Juancito y los clonosaurios"))
    }

    @Test
    fun `Se pueden obtener libros filtrando por un titulo`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/libros?titulo=El"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].titulo").value("El Aleph"))
    }

    @Test
    fun `Se puede obtener un libro a partir de su ID`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/libros/7"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.titulo").value("El Aleph"))
    }

    @Test
    fun `Al intentar obtener un libro con un ID no numerico se obtiene un error`() {
        val errorMessage = mockMvc.perform(MockMvcRequestBuilders.get("/libros/asd"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andReturn().resolvedException?.message

        assertEquals(errorMessage, "El id debe ser un número entero")
    }

    @Test
    fun `Al intentar obtener un libro con un ID inexistente se obtiene un error`() {
        val errorMessage = mockMvc.perform(MockMvcRequestBuilders.get("/libros/800"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andReturn().resolvedException?.message

        assertEquals(errorMessage, "No existe el libro con el identificador 800")
    }

    @Test
    fun `Se pueden eliminar libros a partir de su ID`() {
        mockMvc.perform(MockMvcRequestBuilders.delete("/libros/7"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(OK_MESSAGE))
    }

    @Test
    fun `Al intentar eliminar un libro con un ID no numerico se obtiene un error`() {
        val errorMessage = mockMvc.perform(MockMvcRequestBuilders.delete("/libros/asd"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andReturn().resolvedException?.message

        assertEquals(errorMessage, "El id debe ser un número entero")
    }

    @Test
    fun `Al intentar eliminar un libro con un ID que no existe se obtiene un error`() {
        val errorMessage = mockMvc.perform(MockMvcRequestBuilders.delete("/libros/800"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andReturn().resolvedException?.message

        assertEquals(errorMessage, "No existe el libro con el identificador 800")
    }

    @Test
    fun `Se puede agregar un nuevo libro`() {
        val libroValido = """
            {
                "id": 100,
                "titulo": "Rayuela"
            }
        """.trimIndent()

        mockMvc.perform(MockMvcRequestBuilders.post("/libros").content(libroValido))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(OK_MESSAGE))
    }

    @Test
    fun `No se puede agregar un nuevo libro si el ID existe`() {
        val libroExistente = """
            {
                "id": 1,
                "titulo": "Rayuela"
            }
        """.trimIndent()

        val errorMessage = mockMvc.perform(MockMvcRequestBuilders.post("/libros").content(libroExistente))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andReturn().resolvedException?.message

        assertEquals(errorMessage, "Ya hay un libro con ese id")
    }

    @Test
    fun `Se puede actualizar un libro que ya existia`() {
        val libroActualizado = """
            {
                "id": 7,
                "titulo": "Los premios"
            }
        """.trimIndent()

        mockMvc.perform(MockMvcRequestBuilders.put("/libros").content(libroActualizado))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(OK_MESSAGE))
    }

    @Test
    fun `Al intentar agregar un libro sin titulo se obtiene un error`() {
        val libroInvalido = """
            {
                "id": 100
            }
        """.trimIndent()

        val errorMessage = mockMvc.perform(MockMvcRequestBuilders.post("/libros").content(libroInvalido))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andReturn().resolvedException?.message

        assertEquals(errorMessage, "Debe ingresar título")
    }


    @Test
    fun `Al intentar agregar un libro sin ID devuelve un error`() {
        val libroInvalido = """
            {
                "titulo": "Casa tomada"
            }
        """.trimIndent()

        val errorMessage = mockMvc.perform(MockMvcRequestBuilders.post("/libros").content(libroInvalido))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andReturn().resolvedException?.message

        assertEquals(errorMessage, "Debe ingresar identificador")
    }

    @Test
    fun `Al intentar agregar un libro con un JSON mal formado se obtiene un error`() {
        val body = """[ titulo="Mal formado", id:4]"""

        val errorMessage = mockMvc.perform(MockMvcRequestBuilders.post("/libros").content(body))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andReturn().resolvedException?.message

        assertEquals(errorMessage, "El body debe ser un Libro")
    }


}
