package com.example.hotel.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.example.hotel.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

/** Convierte las excepciones de toda la API en respuestas JSON con el mismo formato. */
@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(RecursoNoEncontradoException.class)
	public ResponseEntity<ErrorResponse> manejarNoEncontrado(RecursoNoEncontradoException ex,
			HttpServletRequest request) {
		return respuesta(HttpStatus.NOT_FOUND, ex.getMessage(), request);
	}

	@ExceptionHandler(ConflictoException.class)
	public ResponseEntity<ErrorResponse> manejarConflicto(ConflictoException ex, HttpServletRequest request) {
		return respuesta(HttpStatus.CONFLICT, ex.getMessage(), request);
	}

	// Errores de Jakarta Validation (@NotBlank, @NotNull, @AssertTrue, ...) en el cuerpo de la petición
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> manejarValidacion(MethodArgumentNotValidException ex,
			HttpServletRequest request) {
		Map<String, String> errores = new LinkedHashMap<>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errores.putIfAbsent(error.getField(), error.getDefaultMessage());
		}
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ErrorResponse cuerpo = new ErrorResponse(status.value(), status.getReasonPhrase(),
				"La petición tiene campos inválidos", request.getRequestURI(), errores);
		return ResponseEntity.status(status).body(cuerpo);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> manejarJsonInvalido(HttpMessageNotReadableException ex,
			HttpServletRequest request) {
		return respuesta(HttpStatus.BAD_REQUEST,
				"El cuerpo no es un JSON válido o tiene valores con formato incorrecto "
						+ "(fechas yyyy-MM-dd, tipo SENCILLA/DOBLE/SUITE, números sin comillas)",
				request);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> manejarTipoInvalido(MethodArgumentTypeMismatchException ex,
			HttpServletRequest request) {
		return respuesta(HttpStatus.BAD_REQUEST,
				"El parámetro '" + ex.getName() + "' tiene un valor inválido: " + ex.getValue(), request);
	}

	// Respaldo por si dos peticiones simultáneas pasan la validación del servicio
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> manejarIntegridad(DataIntegrityViolationException ex,
			HttpServletRequest request) {
		return respuesta(HttpStatus.CONFLICT, "La operación viola una restricción de la base de datos", request);
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ErrorResponse> manejarRutaInexistente(NoResourceFoundException ex,
			HttpServletRequest request) {
		return respuesta(HttpStatus.NOT_FOUND, "La ruta no existe", request);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> manejarMetodoNoPermitido(HttpRequestMethodNotSupportedException ex,
			HttpServletRequest request) {
		return respuesta(HttpStatus.METHOD_NOT_ALLOWED,
				"El método " + ex.getMethod() + " no está permitido en esta ruta", request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> manejarGeneral(Exception ex, HttpServletRequest request) {
		log.error("Error no controlado en {}", request.getRequestURI(), ex);
		return respuesta(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error interno en el servidor", request);
	}

	private ResponseEntity<ErrorResponse> respuesta(HttpStatus status, String mensaje, HttpServletRequest request) {
		ErrorResponse cuerpo = new ErrorResponse(status.value(), status.getReasonPhrase(), mensaje,
				request.getRequestURI());
		return ResponseEntity.status(status).body(cuerpo);
	}
}
