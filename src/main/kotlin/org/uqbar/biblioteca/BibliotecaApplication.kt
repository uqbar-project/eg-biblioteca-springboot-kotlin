package org.uqbar.biblioteca

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.uqbar.biblioteca.domain.Biblioteca

@SpringBootApplication
class BibliotecaApplication {

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

fun main(args: Array<String>) {
    runApplication<BibliotecaApplication>(*args)
}

