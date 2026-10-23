package com.example.hotel.exception;

/**
 * Se lanza cuando la operación choca con el estado actual de los datos
 * (número de habitación repetido, fechas cruzadas, habitación no disponible).
 * Se responde con 409.
 */
public class ConflictoException extends RuntimeException {

	public ConflictoException(String mensaje) {
		super(mensaje);
	}
}
