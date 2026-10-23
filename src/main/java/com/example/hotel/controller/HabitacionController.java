package com.example.hotel.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hotel.dto.HabitacionRequest;
import com.example.hotel.dto.HabitacionResponse;
import com.example.hotel.dto.ReservaResponse;
import com.example.hotel.service.HabitacionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/habitaciones")
public class HabitacionController {

	private final HabitacionService habitacionService;

	public HabitacionController(HabitacionService habitacionService) {
		this.habitacionService = habitacionService;
	}

	@GetMapping
	public List<HabitacionResponse> listar() {
		return habitacionService.listar();
	}

	@GetMapping("/{id}")
	public HabitacionResponse obtener(@PathVariable Long id) {
		return habitacionService.obtener(id);
	}

	@GetMapping("/{id}/reservas")
	public List<ReservaResponse> listarReservas(@PathVariable Long id) {
		return habitacionService.listarReservas(id);
	}

	@PostMapping
	public ResponseEntity<HabitacionResponse> crear(@Valid @RequestBody HabitacionRequest request) {
		HabitacionResponse creada = habitacionService.crear(request);
		return ResponseEntity.created(URI.create("/api/habitaciones/" + creada.id())).body(creada);
	}

	@PutMapping("/{id}")
	public HabitacionResponse actualizar(@PathVariable Long id, @Valid @RequestBody HabitacionRequest request) {
		return habitacionService.actualizar(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		habitacionService.eliminar(id);
		return ResponseEntity.noContent().build();
	}
}
