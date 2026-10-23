package com.example.hotel.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.example.hotel.entity.Habitacion;
import com.example.hotel.entity.Reserva;

/** Reserva tal como la devuelve la API, con las noches y el total calculados. */
public record ReservaResponse(
		Long id,
		String nombreCliente,
		LocalDate fechaEntrada,
		LocalDate fechaSalida,
		long noches,
		BigDecimal total,
		Long habitacionId,
		String numeroHabitacion) {

	public static ReservaResponse from(Reserva reserva) {
		Habitacion habitacion = reserva.getHabitacion();
		long noches = ChronoUnit.DAYS.between(reserva.getFechaEntrada(), reserva.getFechaSalida());
		return new ReservaResponse(
				reserva.getId(),
				reserva.getNombreCliente(),
				reserva.getFechaEntrada(),
				reserva.getFechaSalida(),
				noches,
				habitacion.getPrecioPorNoche().multiply(BigDecimal.valueOf(noches)),
				habitacion.getId(),
				habitacion.getNumero());
	}
}
