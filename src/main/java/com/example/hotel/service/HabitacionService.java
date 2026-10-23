package com.example.hotel.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hotel.dto.HabitacionRequest;
import com.example.hotel.dto.HabitacionResponse;
import com.example.hotel.dto.ReservaResponse;
import com.example.hotel.entity.Habitacion;
import com.example.hotel.exception.ConflictoException;
import com.example.hotel.exception.RecursoNoEncontradoException;
import com.example.hotel.repository.HabitacionRepository;
import com.example.hotel.repository.ReservaRepository;

@Service
@Transactional(readOnly = true)
public class HabitacionService {

	private final HabitacionRepository habitacionRepository;
	private final ReservaRepository reservaRepository;

	public HabitacionService(HabitacionRepository habitacionRepository, ReservaRepository reservaRepository) {
		this.habitacionRepository = habitacionRepository;
		this.reservaRepository = reservaRepository;
	}

	public List<HabitacionResponse> listar() {
		return habitacionRepository.findAll().stream()
				.map(HabitacionResponse::from)
				.toList();
	}

	public HabitacionResponse obtener(Long id) {
		return HabitacionResponse.from(buscar(id));
	}

	public List<ReservaResponse> listarReservas(Long id) {
		buscar(id);
		return reservaRepository.findByHabitacionIdOrderByFechaEntradaAsc(id).stream()
				.map(ReservaResponse::from)
				.toList();
	}

	@Transactional
	public HabitacionResponse crear(HabitacionRequest request) {
		if (habitacionRepository.existsByNumero(request.numero())) {
			throw new ConflictoException("Ya existe una habitación con el número " + request.numero());
		}
		Habitacion habitacion = new Habitacion(
				request.numero(), request.tipo(), request.precioPorNoche(), request.disponible());
		return HabitacionResponse.from(habitacionRepository.save(habitacion));
	}

	@Transactional
	public HabitacionResponse actualizar(Long id, HabitacionRequest request) {
		Habitacion habitacion = buscar(id);
		if (habitacionRepository.existsByNumeroAndIdNot(request.numero(), id)) {
			throw new ConflictoException("Ya existe otra habitación con el número " + request.numero());
		}
		habitacion.setNumero(request.numero());
		habitacion.setTipo(request.tipo());
		habitacion.setPrecioPorNoche(request.precioPorNoche());
		habitacion.setDisponible(request.disponible());
		return HabitacionResponse.from(habitacion);
	}

	@Transactional
	public void eliminar(Long id) {
		habitacionRepository.delete(buscar(id));
	}

	Habitacion buscar(Long id) {
		return habitacionRepository.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Habitación", id));
	}
}
