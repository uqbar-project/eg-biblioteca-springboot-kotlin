package org.uqbar.biblioteca.domain

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BusinessException(mensaje: String) : RuntimeException(mensaje) {}


@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFoundException(mensaje: String) : RuntimeException(mensaje) {}