package com.example.hotel.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "habitaciones")
public class Habitacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 10)
	private String numero;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private TipoHabitacion tipo;

	@Column(name = "precio_por_noche", nullable = false, precision = 10, scale = 2)
	private BigDecimal precioPorNoche;

	@Column(nullable = false)
	private boolean disponible = true;

	// Una habitación tiene muchas reservas; al borrar la habitación se borran sus reservas
	@OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Reserva> reservas = new ArrayList<>();

	protected Habitacion() {
	}

	public Habitacion(String numero, TipoHabitacion tipo, BigDecimal precioPorNoche, boolean disponible) {
		this.numero = numero;
		this.tipo = tipo;
		this.precioPorNoche = precioPorNoche;
		this.disponible = disponible;
	}

	public Long getId() {
		return id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public TipoHabitacion getTipo() {
		return tipo;
	}

	public void setTipo(TipoHabitacion tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getPrecioPorNoche() {
		return precioPorNoche;
	}

	public void setPrecioPorNoche(BigDecimal precioPorNoche) {
		this.precioPorNoche = precioPorNoche;
	}

	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}

	public List<Reserva> getReservas() {
		return reservas;
	}
}
