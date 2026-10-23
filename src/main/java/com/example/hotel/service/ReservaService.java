package com.example.hotel.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hotel.dto.ReservaRequest;
import com.example.hotel.dto.ReservaResponse;
import com.example.hotel.entity.Habitacion;
import com.example.hotel.entity.Reserva;
import com.example.hotel.exception.ConflictoException;
import com.example.hotel.exception.RecursoNoEncontradoException;
import com.example.hotel.repository.ReservaRepository;

@Service
@Transactional(readOnly = true)
public class ReservaService {

	// Valor que nunca coincide con un id real; se usa al crear para no excluir ninguna reserva
	private static final Long SIN_EXCLUIR = -1L;

	private final ReservaRepository reservaRepository;
	private final HabitacionService habitacionService;

	public ReservaService(ReservaRepository reservaRepository, HabitacionService habitacionService) {
		this.reservaRepository = reservaRepository;
		this.habitacionService = habitacionService;
	}

	public List<ReservaResponse> listar() {
		return reservaRepository.findAll().stream()
				.map(ReservaResponse::from)
				.toList();
	}

	public ReservaResponse obtener(Long id) {
		return ReservaResponse.from(buscar(id));
	}

	@Transactional
	public ReservaResponse crear(ReservaRequest request) {
		Habitacion habitacion = validarHabitacion(request, SIN_EXCLUIR);
		Reserva reserva = new Reserva(
				request.nombreCliente(), request.fechaEntrada(), request.fechaSalida(), habitacion);
		return ReservaResponse.from(reservaRepository.save(reserva));
	}

	@Transactional
	public ReservaResponse actualizar(Long id, ReservaRequest request) {
		Reserva reserva = buscar(id);
		Habitacion habitacion = validarHabitacion(request, id);
		reserva.setNombreCliente(request.nombreCliente());
		reserva.setFechaEntrada(request.fechaEntrada());
		reserva.setFechaSalida(request.fechaSalida());
		reserva.setHabitacion(habitacion);
		return ReservaResponse.from(reserva);
	}

	@Transactional
	public void eliminar(Long id) {
		reservaRepository.delete(buscar(id));
	}

	/** Comprueba que la habitación exista, esté disponible y no tenga reservas cruzadas en esas fechas. */
	private Habitacion validarHabitacion(ReservaRequest request, Long reservaExcluida) {
		Habitacion habitacion = habitacionService.buscar(request.habitacionId());
		if (!habitacion.isDisponible()) {
			throw new ConflictoException("La habitación " + habitacion.getNumero() + " no está disponible");
		}
		if (reservaRepository.existeCruce(habitacion.getId(), request.fechaEntrada(), request.fechaSalida(),
				reservaExcluida)) {
			throw new ConflictoException("La habitación " + habitacion.getNumero()
					+ " ya tiene una reserva que se cruza con esas fechas");
		}
		return habitacion;
	}

	private Reserva buscar(Long id) {
		return reservaRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Reserva", id));
	}
}
