package com.example.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hotel.entity.Habitacion;

public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

	boolean existsByNumero(String numero);

	boolean existsByNumeroAndIdNot(String numero, Long id);
}
