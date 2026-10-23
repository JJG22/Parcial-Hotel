package com.example.hotel.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

/** Formato común de todas las respuestas de error de la API. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
		LocalDateTime timestamp,
		int status,
		String error,
		String mensaje,
		String ruta,
		Map<String, String> errores) {

	public ErrorResponse(int status, String error, String mensaje, String ruta) {
		this(LocalDateTime.now(), status, error, mensaje, ruta, null);
	}

	public ErrorResponse(int status, String error, String mensaje, String ruta, Map<String, String> errores) {
		this(LocalDateTime.now(), status, error, mensaje, ruta, errores);
	}
}
