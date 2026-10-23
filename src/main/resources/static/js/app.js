// Interfaz web de la API REST Hotel: consume /api/habitaciones y /api/reservas con fetch.
'use strict';

const estado = {
	habitaciones: [],
	reservas: []
};

const $ = (selector) => document.querySelector(selector);

const TIPOS = { SENCILLA: 'Sencilla', DOBLE: 'Doble', SUITE: 'Suite' };

// ---------- Utilidades ----------

/** Escapa texto para insertarlo en HTML sin riesgo (los nombres vienen de usuarios). */
function esc(valor) {
	return String(valor ?? '').replace(/[&<>"']/g, (c) => ({
		'&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
	})[c]);
}

const formatoMoneda = new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 });
const moneda = (n) => formatoMoneda.format(Number(n) || 0);

/** Convierte "yyyy-MM-dd" a Date local (new Date("yyyy-MM-dd") usaría UTC y mostraría el día anterior). */
function aFecha(iso) {
	const [a, m, d] = iso.split('-').map(Number);
	return new Date(a, m - 1, d);
}

const formatoFecha = new Intl.DateTimeFormat('es-CO', { day: '2-digit', month: 'short', year: 'numeric' });
const fecha = (iso) => formatoFecha.format(aFecha(iso));

function nochesEntre(entrada, salida) {
	const [a1, m1, d1] = entrada.split('-').map(Number);
	const [a2, m2, d2] = salida.split('-').map(Number);
	return Math.round((Date.UTC(a2, m2 - 1, d2) - Date.UTC(a1, m1 - 1, d1)) / 86400000);
}

// ---------- Acceso a la API ----------

class ApiError extends Error {
	constructor(status, cuerpo) {
		super(cuerpo?.mensaje || `Error ${status}`);
		this.status = status;
		this.errores = cuerpo?.errores || null;
	}
}

async function api(ruta, opciones = {}) {
	const config = { ...opciones, headers: { Accept: 'application/json' } };
	if (opciones.body !== undefined) {
		config.headers['Content-Type'] = 'application/json';
		config.body = JSON.stringify(opciones.body);
	}
	const respuesta = await fetch(ruta, config);
	if (respuesta.status === 204) {
		return null;
	}
	const cuerpo = await respuesta.json().catch(() => null);
	if (!respuesta.ok) {
		throw new ApiError(respuesta.status, cuerpo);
	}
	return cuerpo;
}

async function obtenerDatos() {
	const [habitaciones, reservas] = await Promise.all([api('/api/habitaciones'), api('/api/reservas')]);
	estado.habitaciones = habitaciones.sort((a, b) => a.numero.localeCompare(b.numero, 'es', { numeric: true }));
	estado.reservas = reservas.sort((a, b) => a.fechaEntrada.localeCompare(b.fechaEntrada));
	renderizar();
}

async function cargarInicial() {
	$('#estadoCarga').hidden = false;
	$('#errorCarga').hidden = true;
	$('#contenido').hidden = true;
	const aviso = setTimeout(() => { $('#avisoDespertar').hidden = false; }, 2500);
	try {
		await obtenerDatos();
		$('#contenido').hidden = false;
	} catch (error) {
		$('#errorCargaDetalle').textContent = error.message || '';
		$('#errorCarga').hidden = false;
	} finally {
		clearTimeout(aviso);
		$('#estadoCarga').hidden = true;
	}
}

/** Recarga los datos después de un cambio, avisando si falla. */
async function refrescar() {
	try {
		await obtenerDatos();
	} catch (error) {
		notificar('danger', 'No se pudieron recargar los datos', error.message);
	}
}

// ---------- Renderizado ----------

function renderizar() {
	renderizarIndicadores();
	renderizarHabitaciones();
	renderizarFiltroHabitacion();
	renderizarReservas();
}

function renderizarIndicadores() {
	const disponibles = estado.habitaciones.filter((h) => h.disponible).length;
	const total = estado.reservas.reduce((suma, r) => suma + Number(r.total), 0);
	$('#kpiHabitaciones').textContent = estado.habitaciones.length;
	$('#kpiDisponibles').textContent = disponibles;
	$('#kpiReservas').textContent = estado.reservas.length;
	$('#kpiIngresos').textContent = moneda(total);
}

function renderizarHabitaciones() {
	const texto = $('#filtroNumero').value.trim().toLowerCase();
	const tipo = $('#filtroTipo').value;
	const disponible = $('#filtroDisponible').value;

	const lista = estado.habitaciones.filter((h) =>
		(!texto || h.numero.toLowerCase().includes(texto))
		&& (!tipo || h.tipo === tipo)
		&& (!disponible || String(h.disponible) === disponible));

	$('#gridHabitaciones').innerHTML = lista.map((h) => {
		const cantidad = estado.reservas.filter((r) => r.habitacionId === h.id).length;
		return `
		<div class="col-sm-6 col-lg-4 col-xl-3">
			<article class="card habitacion h-100 ${h.disponible ? '' : 'no-disponible'}">
				<div class="franja tipo-${esc(h.tipo)}"></div>
				<div class="card-body">
					<div class="d-flex justify-content-between align-items-start mb-2">
						<div>
							<div class="small text-body-secondary">Habitación</div>
							<div class="habitacion-numero">${esc(h.numero)}</div>
						</div>
						<span class="badge rounded-pill tipo-${esc(h.tipo)}">${esc(TIPOS[h.tipo] || h.tipo)}</span>
					</div>
					<div class="habitacion-precio">${moneda(h.precioPorNoche)} <span class="small fw-normal text-body-secondary">/ noche</span></div>
					<div class="d-flex flex-wrap gap-2 mt-3 small">
						${h.disponible
							? '<span class="badge bg-success-subtle text-success-emphasis border border-success-subtle"><i class="bi bi-check-circle me-1"></i>Disponible</span>'
							: '<span class="badge bg-danger-subtle text-danger-emphasis border border-danger-subtle"><i class="bi bi-x-circle me-1"></i>No disponible</span>'}
						<span class="badge bg-body-tertiary text-body border"><i class="bi bi-calendar-event me-1"></i>${cantidad} ${cantidad === 1 ? 'reserva' : 'reservas'}</span>
					</div>
				</div>
				<div class="card-footer bg-transparent d-flex gap-2">
					<button class="btn btn-sm btn-outline-primary flex-grow-1" data-accion="ver-reservas" data-id="${h.id}" type="button">
						<i class="bi bi-calendar3 me-1"></i>Reservas
					</button>
					<button class="btn btn-sm btn-outline-secondary" data-accion="editar-habitacion" data-id="${h.id}" type="button" title="Editar" aria-label="Editar habitación ${esc(h.numero)}">
						<i class="bi bi-pencil"></i>
					</button>
					<button class="btn btn-sm btn-outline-danger" data-accion="eliminar-habitacion" data-id="${h.id}" type="button" title="Eliminar" aria-label="Eliminar habitación ${esc(h.numero)}">
						<i class="bi bi-trash3"></i>
					</button>
				</div>
			</article>
		</div>`;
	}).join('');

	$('#vacioHabitaciones').hidden = lista.length > 0;
}

function renderizarFiltroHabitacion() {
	const select = $('#filtroHabitacion');
	const actual = select.value;
	select.innerHTML = '<option value="">Todas las habitaciones</option>'
		+ estado.habitaciones.map((h) => `<option value="${h.id}">Habitación ${esc(h.numero)}</option>`).join('');
	// Conserva el filtro si la habitación sigue existiendo
	select.value = estado.habitaciones.some((h) => String(h.id) === actual) ? actual : '';
}

function renderizarReservas() {
	const texto = $('#filtroCliente').value.trim().toLowerCase();
	const habitacionId = $('#filtroHabitacion').value;

	const lista = estado.reservas.filter((r) =>
		(!texto || r.nombreCliente.toLowerCase().includes(texto))
		&& (!habitacionId || String(r.habitacionId) === habitacionId));

	$('#tablaReservas').innerHTML = lista.map((r) => `
		<tr>
			<td class="fw-medium">${esc(r.nombreCliente)}</td>
			<td><span class="badge bg-body-tertiary text-body border">${esc(r.numeroHabitacion)}</span></td>
			<td class="text-nowrap">${fecha(r.fechaEntrada)}</td>
			<td class="text-nowrap">${fecha(r.fechaSalida)}</td>
			<td class="num">${r.noches}</td>
			<td class="num fw-semibold">${moneda(r.total)}</td>
			<td class="text-end text-nowrap">
				<button class="btn btn-sm btn-outline-secondary" data-accion="editar-reserva" data-id="${r.id}" type="button" title="Editar" aria-label="Editar reserva de ${esc(r.nombreCliente)}">
					<i class="bi bi-pencil"></i>
				</button>
				<button class="btn btn-sm btn-outline-danger" data-accion="eliminar-reserva" data-id="${r.id}" type="button" title="Eliminar" aria-label="Eliminar reserva de ${esc(r.nombreCliente)}">
					<i class="bi bi-trash3"></i>
				</button>
			</td>
		</tr>`).join('');

	const vacio = lista.length === 0;
	$('#vacioReservas').hidden = !vacio;
	$('#tablaReservas').closest('.card').hidden = vacio;
}

// ---------- Notificaciones y confirmación ----------

function notificar(tipo, titulo, mensaje = '') {
	const iconos = { success: 'bi-check-circle-fill', danger: 'bi-exclamation-octagon-fill', warning: 'bi-exclamation-triangle-fill' };
	const elemento = document.createElement('div');
	elemento.className = 'toast align-items-center border-0';
	elemento.setAttribute('role', tipo === 'danger' ? 'alert' : 'status');
	elemento.innerHTML = `
		<div class="d-flex">
			<div class="toast-body d-flex gap-2">
				<i class="bi ${iconos[tipo] || iconos.success} text-${tipo} fs-5"></i>
				<div><div class="fw-semibold">${esc(titulo)}</div>${mensaje ? `<div class="small text-body-secondary">${esc(mensaje)}</div>` : ''}</div>
			</div>
			<button type="button" class="btn-close me-2 m-auto" data-bs-dismiss="toast" aria-label="Cerrar"></button>
		</div>`;
	$('#contenedorToasts').appendChild(elemento);
	const toast = new bootstrap.Toast(elemento, { delay: tipo === 'danger' ? 7000 : 3500 });
	elemento.addEventListener('hidden.bs.toast', () => elemento.remove());
	toast.show();
}

const modalConfirmar = new bootstrap.Modal('#modalConfirmar');
let accionConfirmada = null;

function confirmar(titulo, texto, accion) {
	$('#tituloConfirmar').textContent = titulo;
	$('#textoConfirmar').textContent = texto;
	accionConfirmada = accion;
	modalConfirmar.show();
}

$('#btnConfirmar').addEventListener('click', async () => {
	const accion = accionConfirmada;
	accionConfirmada = null;
	modalConfirmar.hide();
	if (accion) {
		await accion();
	}
});

// ---------- Formularios: errores de validación de la API ----------

function limpiarErrores(form) {
	form.querySelectorAll('.is-invalid').forEach((campo) => campo.classList.remove('is-invalid'));
}

/** Marca en rojo los campos que la API rechazó (respuesta 400 con "errores"). Devuelve si marcó alguno. */
function marcarErrores(form, errores, campos) {
	let marcado = false;
	for (const [campo, mensaje] of Object.entries(errores || {})) {
		const input = campos[campo] && form.querySelector(campos[campo]);
		if (input) {
			input.classList.add('is-invalid');
			input.parentElement.querySelector('.invalid-feedback').textContent = mensaje;
			marcado = true;
		}
	}
	return marcado;
}

function ocupado(form, activo) {
	const boton = form.querySelector('[type="submit"]');
	boton.disabled = activo;
	boton.querySelector('.spinner-border').hidden = !activo;
}

function manejarErrorFormulario(form, error, campos, titulo) {
	const marcado = error instanceof ApiError && error.status === 400 && marcarErrores(form, error.errores, campos);
	if (marcado) {
		notificar('warning', 'Revisa los campos marcados', error.message);
	} else {
		notificar('danger', titulo, error.message);
	}
}

// ---------- Habitaciones ----------

const modalHabitacion = new bootstrap.Modal('#modalHabitacion');
const formHabitacion = $('#formHabitacion');
const CAMPOS_HABITACION = { numero: '#habNumero', tipo: '#habTipo', precioPorNoche: '#habPrecio' };

function abrirHabitacion(habitacion) {
	limpiarErrores(formHabitacion);
	$('#tituloModalHabitacion').textContent = habitacion ? `Editar habitación ${habitacion.numero}` : 'Nueva habitación';
	$('#habId').value = habitacion?.id ?? '';
	$('#habNumero').value = habitacion?.numero ?? '';
	$('#habTipo').value = habitacion?.tipo ?? 'SENCILLA';
	$('#habPrecio').value = habitacion?.precioPorNoche ?? '';
	$('#habDisponible').checked = habitacion?.disponible ?? true;
	modalHabitacion.show();
}

formHabitacion.addEventListener('submit', async (evento) => {
	evento.preventDefault();
	limpiarErrores(formHabitacion);
	const id = $('#habId').value;
	const precio = $('#habPrecio').value;
	const datos = {
		numero: $('#habNumero').value.trim(),
		tipo: $('#habTipo').value,
		precioPorNoche: precio === '' ? null : Number(precio),
		disponible: $('#habDisponible').checked
	};
	ocupado(formHabitacion, true);
	try {
		const guardada = await api(id ? `/api/habitaciones/${id}` : '/api/habitaciones', { method: id ? 'PUT' : 'POST', body: datos });
		modalHabitacion.hide();
		notificar('success', id ? 'Habitación actualizada' : 'Habitación creada', `Habitación ${guardada.numero}`);
		await refrescar();
	} catch (error) {
		manejarErrorFormulario(formHabitacion, error, CAMPOS_HABITACION, 'No se pudo guardar la habitación');
	} finally {
		ocupado(formHabitacion, false);
	}
});

function eliminarHabitacion(habitacion) {
	const cantidad = estado.reservas.filter((r) => r.habitacionId === habitacion.id).length;
	const detalle = cantidad > 0
		? `También se eliminarán sus ${cantidad} ${cantidad === 1 ? 'reserva' : 'reservas'}.`
		: 'Esta acción no se puede deshacer.';
	confirmar(`¿Eliminar la habitación ${habitacion.numero}?`, detalle, async () => {
		try {
			await api(`/api/habitaciones/${habitacion.id}`, { method: 'DELETE' });
			notificar('success', 'Habitación eliminada', `Habitación ${habitacion.numero}`);
		} catch (error) {
			notificar('danger', 'No se pudo eliminar la habitación', error.message);
		}
		await refrescar();
	});
}

function verReservasDe(habitacion) {
	$('#filtroHabitacion').value = String(habitacion.id);
	$('#filtroCliente').value = '';
	renderizarReservas();
	bootstrap.Tab.getOrCreateInstance($('#tabReservas')).show();
}

// ---------- Reservas ----------

const modalReserva = new bootstrap.Modal('#modalReserva');
const formReserva = $('#formReserva');
const CAMPOS_RESERVA = {
	nombreCliente: '#resCliente',
	habitacionId: '#resHabitacion',
	fechaEntrada: '#resEntrada',
	fechaSalida: '#resSalida',
	rangoFechasValido: '#resSalida'
};

function llenarHabitacionesReserva(seleccionada) {
	$('#resHabitacion').innerHTML = '<option value="">Selecciona una habitación</option>'
		+ estado.habitaciones.map((h) => {
			// Una habitación no disponible solo se puede dejar si ya era la de esta reserva
			const bloqueada = !h.disponible && h.id !== seleccionada;
			const etiqueta = `${h.numero} · ${TIPOS[h.tipo] || h.tipo} · ${moneda(h.precioPorNoche)}/noche${h.disponible ? '' : ' (no disponible)'}`;
			return `<option value="${h.id}" ${bloqueada ? 'disabled' : ''}>${esc(etiqueta)}</option>`;
		}).join('');
	$('#resHabitacion').value = seleccionada ? String(seleccionada) : '';
}

function actualizarResumen() {
	const habitacion = estado.habitaciones.find((h) => String(h.id) === $('#resHabitacion').value);
	const entrada = $('#resEntrada').value;
	const salida = $('#resSalida').value;
	const resumen = $('#resumenReserva');
	if (!habitacion || !entrada || !salida) {
		resumen.textContent = 'Elige la habitación y las fechas para ver el total.';
		return;
	}
	const noches = nochesEntre(entrada, salida);
	if (noches <= 0) {
		resumen.innerHTML = '<i class="bi bi-exclamation-triangle text-warning me-1"></i>La fecha de salida debe ser posterior a la de entrada.';
		return;
	}
	resumen.innerHTML = `${noches} ${noches === 1 ? 'noche' : 'noches'} × ${moneda(habitacion.precioPorNoche)} = <strong>${moneda(noches * habitacion.precioPorNoche)}</strong>`;
}

['#resHabitacion', '#resEntrada', '#resSalida'].forEach((id) => $(id).addEventListener('input', actualizarResumen));

function abrirReserva(reserva) {
	if (!reserva && !estado.habitaciones.some((h) => h.disponible)) {
		notificar('warning', 'No hay habitaciones disponibles', 'Crea una habitación o marca una como disponible.');
		return;
	}
	limpiarErrores(formReserva);
	$('#tituloModalReserva').textContent = reserva ? 'Editar reserva' : 'Nueva reserva';
	$('#resId').value = reserva?.id ?? '';
	$('#resCliente').value = reserva?.nombreCliente ?? '';
	$('#resEntrada').value = reserva?.fechaEntrada ?? '';
	$('#resSalida').value = reserva?.fechaSalida ?? '';
	// Al crear desde el filtro de una habitación, la deja preseleccionada
	const filtro = Number($('#filtroHabitacion').value) || null;
	llenarHabitacionesReserva(reserva?.habitacionId ?? filtro);
	actualizarResumen();
	modalReserva.show();
}

formReserva.addEventListener('submit', async (evento) => {
	evento.preventDefault();
	limpiarErrores(formReserva);
	const id = $('#resId').value;
	const datos = {
		nombreCliente: $('#resCliente').value.trim(),
		fechaEntrada: $('#resEntrada').value || null,
		fechaSalida: $('#resSalida').value || null,
		habitacionId: Number($('#resHabitacion').value) || null
	};
	ocupado(formReserva, true);
	try {
		const guardada = await api(id ? `/api/reservas/${id}` : '/api/reservas', { method: id ? 'PUT' : 'POST', body: datos });
		modalReserva.hide();
		notificar('success', id ? 'Reserva actualizada' : 'Reserva creada',
			`${guardada.nombreCliente} · Habitación ${guardada.numeroHabitacion} · ${moneda(guardada.total)}`);
		await refrescar();
	} catch (error) {
		manejarErrorFormulario(formReserva, error, CAMPOS_RESERVA, 'No se pudo guardar la reserva');
	} finally {
		ocupado(formReserva, false);
	}
});

function eliminarReserva(reserva) {
	confirmar('¿Eliminar la reserva?', `${reserva.nombreCliente} · Habitación ${reserva.numeroHabitacion} · ${fecha(reserva.fechaEntrada)}`, async () => {
		try {
			await api(`/api/reservas/${reserva.id}`, { method: 'DELETE' });
			notificar('success', 'Reserva eliminada', reserva.nombreCliente);
		} catch (error) {
			notificar('danger', 'No se pudo eliminar la reserva', error.message);
		}
		await refrescar();
	});
}

// ---------- Eventos ----------

$('#gridHabitaciones').addEventListener('click', (evento) => {
	const boton = evento.target.closest('[data-accion]');
	if (!boton) return;
	const habitacion = estado.habitaciones.find((h) => h.id === Number(boton.dataset.id));
	if (!habitacion) return;
	if (boton.dataset.accion === 'editar-habitacion') abrirHabitacion(habitacion);
	if (boton.dataset.accion === 'eliminar-habitacion') eliminarHabitacion(habitacion);
	if (boton.dataset.accion === 'ver-reservas') verReservasDe(habitacion);
});

$('#tablaReservas').addEventListener('click', (evento) => {
	const boton = evento.target.closest('[data-accion]');
	if (!boton) return;
	const reserva = estado.reservas.find((r) => r.id === Number(boton.dataset.id));
	if (!reserva) return;
	if (boton.dataset.accion === 'editar-reserva') abrirReserva(reserva);
	if (boton.dataset.accion === 'eliminar-reserva') eliminarReserva(reserva);
});

$('#btnNuevaHabitacion').addEventListener('click', () => abrirHabitacion(null));
$('#btnNuevaReserva').addEventListener('click', () => abrirReserva(null));
$('#btnReintentar').addEventListener('click', cargarInicial);

['#filtroNumero', '#filtroTipo', '#filtroDisponible'].forEach((id) => $(id).addEventListener('input', renderizarHabitaciones));
['#filtroCliente', '#filtroHabitacion'].forEach((id) => $(id).addEventListener('input', renderizarReservas));

// ---------- Tema claro / oscuro ----------

function aplicarTema(tema) {
	document.documentElement.setAttribute('data-bs-theme', tema);
	$('#btnTema i').className = tema === 'dark' ? 'bi bi-sun' : 'bi bi-moon-stars';
}

function temaGuardado() {
	try {
		return localStorage.getItem('tema');
	} catch {
		return null;
	}
}

$('#btnTema').addEventListener('click', () => {
	const nuevo = document.documentElement.getAttribute('data-bs-theme') === 'dark' ? 'light' : 'dark';
	aplicarTema(nuevo);
	try {
		localStorage.setItem('tema', nuevo);
	} catch {
		// Sin almacenamiento (modo privado): el tema solo dura esta visita
	}
});

aplicarTema(temaGuardado() || (matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'));
cargarInicial();
