package org.uqbar.biblioteca.domain

class Libro(val id: Int = 0, val titulo: String = "") {

    fun validar() {
        if (id == 0) {
            throw BusinessException("Debe ingresar identificador")
        }
        if (titulo.isBlank()) {
            throw BusinessException("Debe ingresar título")
        }
    }

}
