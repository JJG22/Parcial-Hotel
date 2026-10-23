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

import com.example.hotel.dto.ErrorResponse;
import com.example.hotel.dto.HabitacionRequest;
import com.example.hotel.dto.HabitacionResponse;
import com.example.hotel.dto.ReservaResponse;
import com.example.hotel.service.HabitacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/habitaciones")
@Tag(name = "Habitaciones", description = "CRUD de habitaciones del hotel")
public class HabitacionController {

	private final HabitacionService habitacionService;

	public HabitacionController(HabitacionService habitacionService) {
		this.habitacionService = habitacionService;
	}

	@GetMapping
	@Operation(summary = "Listar todas las habitaciones")
	@ApiResponse(responseCode = "200", description = "Lista de habitaciones")
	public List<HabitacionResponse> listar() {
		return habitacionService.listar();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Obtener una habitación por id")
	@ApiResponse(responseCode = "200", description = "Habitación encontrada")
	@ApiResponse(responseCode = "404", description = "La habitación no existe",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	public HabitacionResponse obtener(@PathVariable Long id) {
		return habitacionService.obtener(id);
	}

	@GetMapping("/{id}/reservas")
	@Operation(summary = "Listar las reservas de una habitación", description = "Ordenadas por fecha de entrada")
	@ApiResponse(responseCode = "200", description = "Reservas de la habitación")
	@ApiResponse(responseCode = "404", description = "La habitación no existe",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	public List<ReservaResponse> listarReservas(@PathVariable Long id) {
		return habitacionService.listarReservas(id);
	}

	@PostMapping
	@Operation(summary = "Crear una habitación")
	@ApiResponse(responseCode = "201", description = "Habitación creada")
	@ApiResponse(responseCode = "400", description = "Datos inválidos",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@ApiResponse(responseCode = "409", description = "Ya existe una habitación con ese número",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	public ResponseEntity<HabitacionResponse> crear(@Valid @RequestBody HabitacionRequest request) {
		HabitacionResponse creada = habitacionService.crear(request);
		return ResponseEntity.created(URI.create("/api/habitaciones/" + creada.id())).body(creada);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Actualizar una habitación")
	@ApiResponse(responseCode = "200", description = "Habitación actualizada")
	@ApiResponse(responseCode = "400", description = "Datos inválidos",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@ApiResponse(responseCode = "404", description = "La habitación no existe",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@ApiResponse(responseCode = "409", description = "Otra habitación ya tiene ese número",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	public HabitacionResponse actualizar(@PathVariable Long id, @Valid @RequestBody HabitacionRequest request) {
		return habitacionService.actualizar(id, request);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Eliminar una habitación", description = "También elimina todas sus reservas")
	@ApiResponse(responseCode = "204", description = "Habitación eliminada")
	@ApiResponse(responseCode = "404", description = "La habitación no existe",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		habitacionService.eliminar(id);
		return ResponseEntity.noContent().build();
	}
}
