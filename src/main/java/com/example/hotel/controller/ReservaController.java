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

import com.example.hotel.dto.ReservaRequest;
import com.example.hotel.dto.ReservaResponse;
import com.example.hotel.service.ReservaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

	private final ReservaService reservaService;

	public ReservaController(ReservaService reservaService) {
		this.reservaService = reservaService;
	}

	@GetMapping
	public List<ReservaResponse> listar() {
		return reservaService.listar();
	}

	@GetMapping("/{id}")
	public ReservaResponse obtener(@PathVariable Long id) {
		return reservaService.obtener(id);
	}

	@PostMapping
	public ResponseEntity<ReservaResponse> crear(@Valid @RequestBody ReservaRequest request) {
		ReservaResponse creada = reservaService.crear(request);
		return ResponseEntity.created(URI.create("/api/reservas/" + creada.id())).body(creada);
	}

	@PutMapping("/{id}")
	public ReservaResponse actualizar(@PathVariable Long id, @Valid @RequestBody ReservaRequest request) {
		return reservaService.actualizar(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		reservaService.eliminar(id);
		return ResponseEntity.noContent().build();
	}
}
