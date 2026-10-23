package com.example.hotel.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/** Bienvenida de la API con la lista de endpoints disponibles. La raíz "/" muestra la interfaz web. */
@RestController
@Tag(name = "Inicio")
public class InicioController {

	@GetMapping("/api")
	@Operation(summary = "Bienvenida y lista de endpoints")
	public Map<String, Object> inicio() {
		Map<String, String> habitaciones = new LinkedHashMap<>();
		habitaciones.put("GET /api/habitaciones", "Lista todas las habitaciones");
		habitaciones.put("GET /api/habitaciones/{id}", "Obtiene una habitación");
		habitaciones.put("GET /api/habitaciones/{id}/reservas", "Lista las reservas de una habitación");
		habitaciones.put("POST /api/habitaciones", "Crea una habitación");
		habitaciones.put("PUT /api/habitaciones/{id}", "Actualiza una habitación");
		habitaciones.put("DELETE /api/habitaciones/{id}", "Elimina una habitación y sus reservas");

		Map<String, String> reservas = new LinkedHashMap<>();
		reservas.put("GET /api/reservas", "Lista todas las reservas");
		reservas.put("GET /api/reservas/{id}", "Obtiene una reserva");
		reservas.put("POST /api/reservas", "Crea una reserva");
		reservas.put("PUT /api/reservas/{id}", "Actualiza una reserva");
		reservas.put("DELETE /api/reservas/{id}", "Elimina una reserva");

		Map<String, Object> endpoints = new LinkedHashMap<>();
		endpoints.put("habitaciones", habitaciones);
		endpoints.put("reservas", reservas);

		Map<String, String> enlaces = new LinkedHashMap<>();
		enlaces.put("interfazWeb", "/");
		enlaces.put("swagger", "/swagger-ui.html");
		enlaces.put("openApi", "/v3/api-docs");

		Map<String, Object> respuesta = new LinkedHashMap<>();
		respuesta.put("mensaje", "Bienvenido a la API REST del Hotel");
		respuesta.put("estado", "API funcionando");
		respuesta.put("enlaces", enlaces);
		respuesta.put("endpoints", endpoints);
		return respuesta;
	}
}
