package com.example.hotel.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** Datos que el cliente envía para crear o actualizar una reserva. */
public record ReservaRequest(

		@NotBlank(message = "El nombre del cliente es obligatorio")
		@Size(max = 100, message = "El nombre del cliente debe tener máximo 100 caracteres")
		String nombreCliente,

		@NotNull(message = "La fecha de entrada es obligatoria (formato yyyy-MM-dd)")
		LocalDate fechaEntrada,

		@NotNull(message = "La fecha de salida es obligatoria (formato yyyy-MM-dd)")
		LocalDate fechaSalida,

		@NotNull(message = "El id de la habitación es obligatorio")
		@Positive(message = "El id de la habitación debe ser positivo")
		Long habitacionId) {

	// Si alguna fecha falta, ya lo reporta @NotNull; aquí solo se valida el orden
	@AssertTrue(message = "La fecha de salida debe ser posterior a la fecha de entrada")
	public boolean isRangoFechasValido() {
		return fechaEntrada == null || fechaSalida == null || fechaSalida.isAfter(fechaEntrada);
	}
}
