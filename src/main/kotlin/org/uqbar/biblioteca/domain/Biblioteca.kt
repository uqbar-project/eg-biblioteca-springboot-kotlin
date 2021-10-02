package org.uqbar.biblioteca.domain

class Biblioteca {

    val libros = mutableListOf<Libro>()

    /**
     * Helper method para agregar un libro
     */
    fun addLibro(idLibro: Int, titulo: String) {
        this.setLibro(Libro(idLibro, titulo))
    }

    fun setLibro(libro: Libro) {
        libro.validar()
        val index = this.libros.indexOfFirst { libro.id == it.id }
        if (index >= 0) {
            this.libros.set(index, libro)
        } else {
            this.libros.add(libro)
        }
    }

    fun getLibro(id: Int): Libro? {
        return this.libros.find { it.id == id }
    }

    fun eliminarLibro(id: Int) {
        this.libros.removeIf { it.id == id }
    }

    fun searchLibros(substring: String?): List<Libro> {
        return if (substring === null) {
            this.libros
        } else {
            this.libros.filter { it.titulo.lowercase().contains(substring.lowercase()) }.toList()
        }
    }
}