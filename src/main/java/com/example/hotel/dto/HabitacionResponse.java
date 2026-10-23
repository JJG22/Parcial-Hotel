package com.example.hotel.dto;

import java.math.BigDecimal;

import com.example.hotel.entity.Habitacion;
import com.example.hotel.entity.TipoHabitacion;

/** Habitación tal como la devuelve la API. */
public record HabitacionResponse(
		Long id,
		String numero,
		TipoHabitacion tipo,
		BigDecimal precioPorNoche,
		boolean disponible) {

	public static HabitacionResponse from(Habitacion habitacion) {
		return new HabitacionResponse(
				habitacion.getId(),
				habitacion.getNumero(),
				habitacion.getTipo(),
				habitacion.getPrecioPorNoche(),
				habitacion.isDisponible());
	}
}
