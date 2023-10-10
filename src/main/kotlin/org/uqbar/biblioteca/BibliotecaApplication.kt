package org.uqbar.biblioteca

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.uqbar.biblioteca.domain.Biblioteca
import net.datafaker.Faker
import java.util.Locale
import java.util.Random

@SpringBootApplication
class BibliotecaApplication {

    @Bean
    fun initializeBiblioteca(): Biblioteca {
//        // Utilizados para opciones 2 y 3 mas abajo, con faker.
//        val randomizer = Random(2022)
//        val faker = Faker(Locale("es", "MX"), randomizer)


        return Biblioteca().apply {
//        // Opción 1: Generar libros manualmente
            addLibro(1, "Juancito y los clonosaurios")
            addLibro(5, "Ficciones")
            addLibro(7, "El Aleph")
            addLibro(11, "Historia universal de la infamia")
            addLibro(13, "El informe de Brodie")
            addLibro(17, "El libro de arena")
//        // Opción 2: Generar secuencialmente
//            repeat (22) { n -> addLibro(n+1, faker.book().title()) }
//        // Opción 3: Poner ids aleatorios como en el ejemplo inicial
//            val usedIds = (1..50).shuffled(randomizer).take(22).toList()
//            usedIds.forEach{ id -> addLibro(id, faker.book().title()) }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<BibliotecaApplication>(*args)
}

