package com.example.hotel.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservas")
public class Reserva {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nombre_cliente", nullable = false, length = 100)
	private String nombreCliente;

	@Column(name = "fecha_entrada", nullable = false)
	private LocalDate fechaEntrada;

	@Column(name = "fecha_salida", nullable = false)
	private LocalDate fechaSalida;

	// Muchas reservas pertenecen a una habitación
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "habitacion_id", nullable = false)
	private Habitacion habitacion;

	protected Reserva() {
	}

	public Reserva(String nombreCliente, LocalDate fechaEntrada, LocalDate fechaSalida, Habitacion habitacion) {
		this.nombreCliente = nombreCliente;
		this.fechaEntrada = fechaEntrada;
		this.fechaSalida = fechaSalida;
		this.habitacion = habitacion;
	}

	public Long getId() {
		return id;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public LocalDate getFechaEntrada() {
		return fechaEntrada;
	}

	public void setFechaEntrada(LocalDate fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}

	public LocalDate getFechaSalida() {
		return fechaSalida;
	}

	public void setFechaSalida(LocalDate fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	public Habitacion getHabitacion() {
		return habitacion;
	}

	public void setHabitacion(Habitacion habitacion) {
		this.habitacion = habitacion;
	}
}
