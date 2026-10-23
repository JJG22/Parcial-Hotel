package com.example.hotel.exception;

/** Se lanza cuando no existe el registro pedido. Se responde con 404. */
public class RecursoNoEncontradoException extends RuntimeException {

	public RecursoNoEncontradoException(String recurso, Long id) {
		super(recurso + " con id " + id + " no encontrada");
	}
}
