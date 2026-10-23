package com.example.hotel.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hotel.entity.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

	// Trae la habitación en la misma consulta para no hacer una consulta extra por cada reserva
	@Override
	@EntityGraph(attributePaths = "habitacion")
	List<Reserva> findAll();

	List<Reserva> findByHabitacionIdOrderByFechaEntradaAsc(Long habitacionId);

	/**
	 * Indica si la habitación ya tiene una reserva que se cruza con el rango dado.
	 * La fecha de salida no cuenta como noche ocupada, así que una reserva puede
	 * empezar el mismo día en que termina otra. {@code excluirId} permite ignorar
	 * la propia reserva al actualizarla (usar -1 al crear).
	 */
	@Query("""
			select count(r) > 0 from Reserva r
			where r.habitacion.id = :habitacionId
			  and r.id <> :excluirId
			  and r.fechaEntrada < :fechaSalida
			  and r.fechaSalida > :fechaEntrada
			""")
	boolean existeCruce(@Param("habitacionId") Long habitacionId,
			@Param("fechaEntrada") LocalDate fechaEntrada,
			@Param("fechaSalida") LocalDate fechaSalida,
			@Param("excluirId") Long excluirId);
}
