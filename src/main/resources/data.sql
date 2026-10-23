-- Datos de ejemplo que se cargan al iniciar la aplicación

INSERT INTO habitaciones (numero, tipo, precio_por_noche, disponible) VALUES
	('101', 'SENCILLA', 120000.00, TRUE),
	('102', 'DOBLE', 180000.00, TRUE),
	('201', 'DOBLE', 190000.00, TRUE),
	('301', 'SUITE', 350000.00, TRUE),
	('302', 'SUITE', 380000.00, FALSE);

INSERT INTO reservas (nombre_cliente, fecha_entrada, fecha_salida, habitacion_id) VALUES
	('Ana Torres', '2026-11-05', '2026-11-08', 1),
	('Carlos Pérez', '2026-11-10', '2026-11-12', 1),
	('Laura Gómez', '2026-11-15', '2026-11-20', 2),
	('Andrés Rojas', '2026-12-20', '2026-12-27', 4);
