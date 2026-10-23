package com.example.hotel.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

/** Información general que muestra Swagger UI en /swagger-ui.html. */
@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI hotelOpenApi() {
		return new OpenAPI()
				.info(new Info()
						.title("API REST Hotel")
						.version("1.0")
						.description("Gestión de habitaciones y reservas de un hotel. "
								+ "Relación 1 habitación → N reservas, con validaciones y manejo de errores."))
				// URL relativa: así "Try it out" usa el mismo dominio y protocolo (https en Render)
				.servers(List.of(new Server().url("/")));
	}
}
