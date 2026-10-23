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
import com.example.hotel.dto.ReservaRequest;
import com.example.hotel.dto.ReservaResponse;
import com.example.hotel.service.ReservaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reservas")
@Tag(name = "Reservas", description = "CRUD de reservas; cada reserva pertenece a una habitación")
public class ReservaController {

	private final ReservaService reservaService;

	public ReservaController(ReservaService reservaService) {
		this.reservaService = reservaService;
	}

	@GetMapping
	@Operation(summary = "Listar todas las reservas")
	@ApiResponse(responseCode = "200", description = "Lista de reservas")
	public List<ReservaResponse> listar() {
		return reservaService.listar();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Obtener una reserva por id")
	@ApiResponse(responseCode = "200", description = "Reserva encontrada")
	@ApiResponse(responseCode = "404", description = "La reserva no existe",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	public ReservaResponse obtener(@PathVariable Long id) {
		return reservaService.obtener(id);
	}

	@PostMapping
	@Operation(summary = "Crear una reserva",
			description = "La habitación debe existir, estar disponible y no tener otra reserva en esas fechas")
	@ApiResponse(responseCode = "201", description = "Reserva creada, con noches y total calculados")
	@ApiResponse(responseCode = "400", description = "Datos inválidos o fecha de salida no posterior a la de entrada",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@ApiResponse(responseCode = "404", description = "La habitación no existe",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@ApiResponse(responseCode = "409", description = "Habitación no disponible o con fechas cruzadas",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	public ResponseEntity<ReservaResponse> crear(@Valid @RequestBody ReservaRequest request) {
		ReservaResponse creada = reservaService.crear(request);
		return ResponseEntity.created(URI.create("/api/reservas/" + creada.id())).body(creada);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Actualizar una reserva")
	@ApiResponse(responseCode = "200", description = "Reserva actualizada")
	@ApiResponse(responseCode = "400", description = "Datos inválidos",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@ApiResponse(responseCode = "404", description = "La reserva o la habitación no existe",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@ApiResponse(responseCode = "409", description = "Habitación no disponible o con fechas cruzadas",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	public ReservaResponse actualizar(@PathVariable Long id, @Valid @RequestBody ReservaRequest request) {
		return reservaService.actualizar(id, request);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Eliminar una reserva")
	@ApiResponse(responseCode = "204", description = "Reserva eliminada")
	@ApiResponse(responseCode = "404", description = "La reserva no existe",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		reservaService.eliminar(id);
		return ResponseEntity.noContent().build();
	}
}
