package com.example.hotel.dto;

import java.math.BigDecimal;

import com.example.hotel.entity.TipoHabitacion;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Datos que el cliente envía para crear o actualizar una habitación. */
public record HabitacionRequest(

		@Schema(example = "401")
		@NotBlank(message = "El número es obligatorio")
		@Size(max = 10, message = "El número debe tener máximo 10 caracteres")
		String numero,

		@Schema(example = "SUITE")
		@NotNull(message = "El tipo es obligatorio (SENCILLA, DOBLE o SUITE)")
		TipoHabitacion tipo,

		@Schema(example = "420000")
		@NotNull(message = "El precio por noche es obligatorio")
		@DecimalMin(value = "0.0", inclusive = false, message = "El precio por noche debe ser mayor que 0")
		@Digits(integer = 8, fraction = 2, message = "El precio admite máximo 8 enteros y 2 decimales")
		BigDecimal precioPorNoche,

		@Schema(example = "true")
		@NotNull(message = "El campo disponible es obligatorio")
		Boolean disponible) {
}
