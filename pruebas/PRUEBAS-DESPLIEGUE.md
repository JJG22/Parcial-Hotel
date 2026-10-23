# Pruebas desde el despliegue — API REST Hotel

- **Despliegue:** https://parcial-hotel.onrender.com
- **Repositorio:** https://github.com/JJG22/Parcial-Hotel
- **Fecha de ejecución:** 2026-10-23 14:06:11 (hora de Colombia, UTC-5)
- **Resultado:** 25 de 25 pruebas exitosas

Todas las peticiones se hicieron contra la aplicación desplegada en Render (no en local). La misma API se puede probar desde la interfaz web (https://parcial-hotel.onrender.com) o desde Swagger (https://parcial-hotel.onrender.com/swagger-ui.html).
Las pruebas crean una habitación y una reserva temporales y al final las eliminan, así que los datos quedan como estaban.

## Resumen

| # | Prueba | Petición | Esperado | Obtenido | Resultado |
|---|---|---|---|---|---|
| 1 | Bienvenida y lista de rutas | `GET /api` | 200 | 200 | ✅ OK |
| 2 | Listar habitaciones | `GET /api/habitaciones` | 200 | 200 | ✅ OK |
| 3 | Obtener habitación por id | `GET /api/habitaciones/2` | 200 | 200 | ✅ OK |
| 4 | Listar reservas de una habitación | `GET /api/habitaciones/2/reservas` | 200 | 200 | ✅ OK |
| 5 | Listar reservas | `GET /api/reservas` | 200 | 200 | ✅ OK |
| 6 | Obtener reserva por id | `GET /api/reservas/1` | 200 | 200 | ✅ OK |
| 7 | Crear habitación | `POST /api/habitaciones` | 201 | 201 | ✅ OK |
| 8 | Actualizar habitación | `PUT /api/habitaciones/6` | 200 | 200 | ✅ OK |
| 9 | Crear reserva | `POST /api/reservas` | 201 | 201 | ✅ OK |
| 10 | Actualizar reserva | `PUT /api/reservas/5` | 200 | 200 | ✅ OK |
| 11 | Reservas de la habitación creada | `GET /api/habitaciones/6/reservas` | 200 | 200 | ✅ OK |
| 12 | Validación: campos obligatorios y precio | `POST /api/habitaciones` | 400 | 400 | ✅ OK |
| 13 | Validación: salida antes de la entrada | `POST /api/reservas` | 400 | 400 | ✅ OK |
| 14 | Conflicto: número de habitación repetido | `POST /api/habitaciones` | 409 | 409 | ✅ OK |
| 15 | Conflicto: reserva con fechas cruzadas | `POST /api/reservas` | 409 | 409 | ✅ OK |
| 16 | Conflicto: habitación no disponible | `POST /api/reservas` | 409 | 409 | ✅ OK |
| 17 | Habitación inexistente | `GET /api/habitaciones/9999` | 404 | 404 | ✅ OK |
| 18 | Reserva inexistente | `GET /api/reservas/9999` | 404 | 404 | ✅ OK |
| 19 | Reserva con habitación inexistente | `POST /api/reservas` | 404 | 404 | ✅ OK |
| 20 | Id no numérico | `GET /api/habitaciones/abc` | 400 | 400 | ✅ OK |
| 21 | Fecha con formato inválido | `POST /api/reservas` | 400 | 400 | ✅ OK |
| 22 | Eliminar reserva | `DELETE /api/reservas/5` | 204 | 204 | ✅ OK |
| 23 | Comprobar reserva eliminada | `GET /api/reservas/5` | 404 | 404 | ✅ OK |
| 24 | Eliminar habitación | `DELETE /api/habitaciones/6` | 204 | 204 | ✅ OK |
| 25 | Comprobar habitación eliminada | `GET /api/habitaciones/6` | 404 | 404 | ✅ OK |

## Detalle de cada prueba

### 1. Bienvenida y lista de rutas

- **Petición:** `GET https://parcial-hotel.onrender.com/api`
- **Esperado:** 200 — **Obtenido:** 200 — ✅ OK — 760 ms

**Respuesta:**

```json
{
  "mensaje": "Bienvenido a la API REST del Hotel",
  "estado": "API funcionando",
  "enlaces": {
    "interfazWeb": "/",
    "swagger": "/swagger-ui.html",
    "openApi": "/v3/api-docs"
  },
  "endpoints": {
    "habitaciones": {
      "GET /api/habitaciones": "Lista todas las habitaciones",
      "GET /api/habitaciones/{id}": "Obtiene una habitación",
      "GET /api/habitaciones/{id}/reservas": "Lista las reservas de una habitación",
      "POST /api/habitaciones": "Crea una habitación",
      "PUT /api/habitaciones/{id}": "Actualiza una habitación",
      "DELETE /api/habitaciones/{id}": "Elimina una habitación y sus reservas"
    },
    "reservas": {
      "GET /api/reservas": "Lista todas las reservas",
      "GET /api/reservas/{id}": "Obtiene una reserva",
      "POST /api/reservas": "Crea una reserva",
      "PUT /api/reservas/{id}": "Actualiza una reserva",
      "DELETE /api/reservas/{id}": "Elimina una reserva"
    }
  }
}
```

### 2. Listar habitaciones

- **Petición:** `GET https://parcial-hotel.onrender.com/api/habitaciones`
- **Esperado:** 200 — **Obtenido:** 200 — ✅ OK — 418 ms

**Respuesta:**

```json
[
  {
    "id": 1,
    "numero": "101",
    "tipo": "SENCILLA",
    "precioPorNoche": 120000.00,
    "disponible": true
  },
  {
    "id": 2,
    "numero": "102",
    "tipo": "DOBLE",
    "precioPorNoche": 180000.00,
    "disponible": true
  },
  {
    "id": 3,
    "numero": "201",
    "tipo": "DOBLE",
    "precioPorNoche": 190000.00,
    "disponible": true
  },
  {
    "id": 4,
    "numero": "301",
    "tipo": "SUITE",
    "precioPorNoche": 350000.00,
    "disponible": true
  },
  {
    "id": 5,
    "numero": "302",
    "tipo": "SUITE",
    "precioPorNoche": 380000.00,
    "disponible": false
  }
]
```

### 3. Obtener habitación por id

- **Petición:** `GET https://parcial-hotel.onrender.com/api/habitaciones/2`
- **Esperado:** 200 — **Obtenido:** 200 — ✅ OK — 462 ms

**Respuesta:**

```json
{
  "id": 2,
  "numero": "102",
  "tipo": "DOBLE",
  "precioPorNoche": 180000.00,
  "disponible": true
}
```

### 4. Listar reservas de una habitación

- **Petición:** `GET https://parcial-hotel.onrender.com/api/habitaciones/2/reservas`
- **Esperado:** 200 — **Obtenido:** 200 — ✅ OK — 771 ms

**Respuesta:**

```json
[
  {
    "id": 3,
    "nombreCliente": "Laura Gómez",
    "fechaEntrada": "2026-11-15",
    "fechaSalida": "2026-11-20",
    "noches": 5,
    "total": 900000.00,
    "habitacionId": 2,
    "numeroHabitacion": "102"
  }
]
```

### 5. Listar reservas

- **Petición:** `GET https://parcial-hotel.onrender.com/api/reservas`
- **Esperado:** 200 — **Obtenido:** 200 — ✅ OK — 471 ms

**Respuesta:**

```json
[
  {
    "id": 1,
    "nombreCliente": "Ana Torres",
    "fechaEntrada": "2026-11-05",
    "fechaSalida": "2026-11-08",
    "noches": 3,
    "total": 360000.00,
    "habitacionId": 1,
    "numeroHabitacion": "101"
  },
  {
    "id": 2,
    "nombreCliente": "Carlos Pérez",
    "fechaEntrada": "2026-11-10",
    "fechaSalida": "2026-11-12",
    "noches": 2,
    "total": 240000.00,
    "habitacionId": 1,
    "numeroHabitacion": "101"
  },
  {
    "id": 3,
    "nombreCliente": "Laura Gómez",
    "fechaEntrada": "2026-11-15",
    "fechaSalida": "2026-11-20",
    "noches": 5,
    "total": 900000.00,
    "habitacionId": 2,
    "numeroHabitacion": "102"
  },
  {
    "id": 4,
    "nombreCliente": "Andrés Rojas",
    "fechaEntrada": "2026-12-20",
    "fechaSalida": "2026-12-27",
    "noches": 7,
    "total": 2450000.00,
    "habitacionId": 4,
    "numeroHabitacion": "301"
  }
]
```

### 6. Obtener reserva por id

- **Petición:** `GET https://parcial-hotel.onrender.com/api/reservas/1`
- **Esperado:** 200 — **Obtenido:** 200 — ✅ OK — 497 ms

**Respuesta:**

```json
{
  "id": 1,
  "nombreCliente": "Ana Torres",
  "fechaEntrada": "2026-11-05",
  "fechaSalida": "2026-11-08",
  "noches": 3,
  "total": 360000.00,
  "habitacionId": 1,
  "numeroHabitacion": "101"
}
```

### 7. Crear habitación

- **Petición:** `POST https://parcial-hotel.onrender.com/api/habitaciones`
- **Esperado:** 201 — **Obtenido:** 201 — ✅ OK — 2276 ms

**Cuerpo enviado:**

```json
{
  "numero": "501",
  "tipo": "DOBLE",
  "precioPorNoche": 200000,
  "disponible": true
}
```

**Respuesta:**

```json
{
  "id": 6,
  "numero": "501",
  "tipo": "DOBLE",
  "precioPorNoche": 200000,
  "disponible": true
}
```

### 8. Actualizar habitación

- **Petición:** `PUT https://parcial-hotel.onrender.com/api/habitaciones/6`
- **Esperado:** 200 — **Obtenido:** 200 — ✅ OK — 653 ms

**Cuerpo enviado:**

```json
{
  "numero": "501",
  "tipo": "SUITE",
  "precioPorNoche": 310000,
  "disponible": true
}
```

**Respuesta:**

```json
{
  "id": 6,
  "numero": "501",
  "tipo": "SUITE",
  "precioPorNoche": 310000,
  "disponible": true
}
```

### 9. Crear reserva

- **Petición:** `POST https://parcial-hotel.onrender.com/api/reservas`
- **Esperado:** 201 — **Obtenido:** 201 — ✅ OK — 1320 ms

**Cuerpo enviado:**

```json
{
  "nombreCliente": "Jhon Garcia",
  "fechaEntrada": "2027-01-10",
  "fechaSalida": "2027-01-13",
  "habitacionId": 6
}
```

**Respuesta:**

```json
{
  "id": 5,
  "nombreCliente": "Jhon Garcia",
  "fechaEntrada": "2027-01-10",
  "fechaSalida": "2027-01-13",
  "noches": 3,
  "total": 930000.00,
  "habitacionId": 6,
  "numeroHabitacion": "501"
}
```

### 10. Actualizar reserva

- **Petición:** `PUT https://parcial-hotel.onrender.com/api/reservas/5`
- **Esperado:** 200 — **Obtenido:** 200 — ✅ OK — 443 ms

**Cuerpo enviado:**

```json
{
  "nombreCliente": "Jhon Garcia",
  "fechaEntrada": "2027-01-10",
  "fechaSalida": "2027-01-14",
  "habitacionId": 6
}
```

**Respuesta:**

```json
{
  "id": 5,
  "nombreCliente": "Jhon Garcia",
  "fechaEntrada": "2027-01-10",
  "fechaSalida": "2027-01-14",
  "noches": 4,
  "total": 1240000.00,
  "habitacionId": 6,
  "numeroHabitacion": "501"
}
```

### 11. Reservas de la habitación creada

- **Petición:** `GET https://parcial-hotel.onrender.com/api/habitaciones/6/reservas`
- **Esperado:** 200 — **Obtenido:** 200 — ✅ OK — 421 ms

**Respuesta:**

```json
[
  {
    "id": 5,
    "nombreCliente": "Jhon Garcia",
    "fechaEntrada": "2027-01-10",
    "fechaSalida": "2027-01-14",
    "noches": 4,
    "total": 1240000.00,
    "habitacionId": 6,
    "numeroHabitacion": "501"
  }
]
```

### 12. Validación: campos obligatorios y precio

- **Petición:** `POST https://parcial-hotel.onrender.com/api/habitaciones`
- **Esperado:** 400 — **Obtenido:** 400 — ✅ OK — 494 ms

**Cuerpo enviado:**

```json
{
  "numero": "",
  "precioPorNoche": -5
}
```

**Respuesta:**

```json
{
  "timestamp": "2026-10-23T19:06:11.690381484",
  "status": 400,
  "error": "Bad Request",
  "mensaje": "La petición tiene campos inválidos",
  "ruta": "/api/habitaciones",
  "errores": {
    "numero": "El número es obligatorio",
    "disponible": "El campo disponible es obligatorio",
    "precioPorNoche": "El precio por noche debe ser mayor que 0",
    "tipo": "El tipo es obligatorio (SENCILLA, DOBLE o SUITE)"
  }
}
```

### 13. Validación: salida antes de la entrada

- **Petición:** `POST https://parcial-hotel.onrender.com/api/reservas`
- **Esperado:** 400 — **Obtenido:** 400 — ✅ OK — 429 ms

**Cuerpo enviado:**

```json
{
  "nombreCliente": "Prueba",
  "fechaEntrada": "2027-02-10",
  "fechaSalida": "2027-02-08",
  "habitacionId": 6
}
```

**Respuesta:**

```json
{
  "timestamp": "2026-10-23T19:06:12.383769932",
  "status": 400,
  "error": "Bad Request",
  "mensaje": "La petición tiene campos inválidos",
  "ruta": "/api/reservas",
  "errores": {
    "rangoFechasValido": "La fecha de salida debe ser posterior a la fecha de entrada"
  }
}
```

### 14. Conflicto: número de habitación repetido

- **Petición:** `POST https://parcial-hotel.onrender.com/api/habitaciones`
- **Esperado:** 409 — **Obtenido:** 409 — ✅ OK — 405 ms

**Cuerpo enviado:**

```json
{
  "numero": "101",
  "tipo": "DOBLE",
  "precioPorNoche": 100000,
  "disponible": true
}
```

**Respuesta:**

```json
{
  "timestamp": "2026-10-23T19:06:12.952316444",
  "status": 409,
  "error": "Conflict",
  "mensaje": "Ya existe una habitación con el número 101",
  "ruta": "/api/habitaciones"
}
```

### 15. Conflicto: reserva con fechas cruzadas

- **Petición:** `POST https://parcial-hotel.onrender.com/api/reservas`
- **Esperado:** 409 — **Obtenido:** 409 — ✅ OK — 413 ms

**Cuerpo enviado:**

```json
{
  "nombreCliente": "Prueba",
  "fechaEntrada": "2027-01-12",
  "fechaSalida": "2027-01-15",
  "habitacionId": 6
}
```

**Respuesta:**

```json
{
  "timestamp": "2026-10-23T19:06:13.535243751",
  "status": 409,
  "error": "Conflict",
  "mensaje": "La habitación 501 ya tiene una reserva que se cruza con esas fechas",
  "ruta": "/api/reservas"
}
```

### 16. Conflicto: habitación no disponible

- **Petición:** `POST https://parcial-hotel.onrender.com/api/reservas`
- **Esperado:** 409 — **Obtenido:** 409 — ✅ OK — 416 ms

**Cuerpo enviado:**

```json
{
  "nombreCliente": "Prueba",
  "fechaEntrada": "2027-03-01",
  "fechaSalida": "2027-03-02",
  "habitacionId": 5
}
```

**Respuesta:**

```json
{
  "timestamp": "2026-10-23T19:06:14.110342585",
  "status": 409,
  "error": "Conflict",
  "mensaje": "La habitación 302 no está disponible",
  "ruta": "/api/reservas"
}
```

### 17. Habitación inexistente

- **Petición:** `GET https://parcial-hotel.onrender.com/api/habitaciones/9999`
- **Esperado:** 404 — **Obtenido:** 404 — ✅ OK — 463 ms

**Respuesta:**

```json
{
  "timestamp": "2026-10-23T19:06:14.713223128",
  "status": 404,
  "error": "Not Found",
  "mensaje": "Habitación con id 9999 no encontrada",
  "ruta": "/api/habitaciones/9999"
}
```

### 18. Reserva inexistente

- **Petición:** `GET https://parcial-hotel.onrender.com/api/reservas/9999`
- **Esperado:** 404 — **Obtenido:** 404 — ✅ OK — 416 ms

**Respuesta:**

```json
{
  "timestamp": "2026-10-23T19:06:15.247338775",
  "status": 404,
  "error": "Not Found",
  "mensaje": "Reserva con id 9999 no encontrada",
  "ruta": "/api/reservas/9999"
}
```

### 19. Reserva con habitación inexistente

- **Petición:** `POST https://parcial-hotel.onrender.com/api/reservas`
- **Esperado:** 404 — **Obtenido:** 404 — ✅ OK — 430 ms

**Cuerpo enviado:**

```json
{
  "nombreCliente": "Prueba",
  "fechaEntrada": "2027-03-01",
  "fechaSalida": "2027-03-02",
  "habitacionId": 9999
}
```

**Respuesta:**

```json
{
  "timestamp": "2026-10-23T19:06:15.802397292",
  "status": 404,
  "error": "Not Found",
  "mensaje": "Habitación con id 9999 no encontrada",
  "ruta": "/api/reservas"
}
```

### 20. Id no numérico

- **Petición:** `GET https://parcial-hotel.onrender.com/api/habitaciones/abc`
- **Esperado:** 400 — **Obtenido:** 400 — ✅ OK — 412 ms

**Respuesta:**

```json
{
  "timestamp": "2026-10-23T19:06:16.396392034",
  "status": 400,
  "error": "Bad Request",
  "mensaje": "El parámetro 'id' tiene un valor inválido: abc",
  "ruta": "/api/habitaciones/abc"
}
```

### 21. Fecha con formato inválido

- **Petición:** `POST https://parcial-hotel.onrender.com/api/reservas`
- **Esperado:** 400 — **Obtenido:** 400 — ✅ OK — 427 ms

**Cuerpo enviado:**

```json
{
  "nombreCliente": "Prueba",
  "fechaEntrada": "10/01/2027",
  "fechaSalida": "2027-01-13",
  "habitacionId": 6
}
```

**Respuesta:**

```json
{
  "timestamp": "2026-10-23T19:06:16.930248055",
  "status": 400,
  "error": "Bad Request",
  "mensaje": "El cuerpo no es un JSON válido o tiene valores con formato incorrecto (fechas yyyy-MM-dd, tipo SENCILLA/DOBLE/SUITE, números sin comillas)",
  "ruta": "/api/reservas"
}
```

### 22. Eliminar reserva

- **Petición:** `DELETE https://parcial-hotel.onrender.com/api/reservas/5`
- **Esperado:** 204 — **Obtenido:** 204 — ✅ OK — 431 ms

**Respuesta:** _(sin contenido)_

### 23. Comprobar reserva eliminada

- **Petición:** `GET https://parcial-hotel.onrender.com/api/reservas/5`
- **Esperado:** 404 — **Obtenido:** 404 — ✅ OK — 429 ms

**Respuesta:**

```json
{
  "timestamp": "2026-10-23T19:06:18.014552418",
  "status": 404,
  "error": "Not Found",
  "mensaje": "Reserva con id 5 no encontrada",
  "ruta": "/api/reservas/5"
}
```

### 24. Eliminar habitación

- **Petición:** `DELETE https://parcial-hotel.onrender.com/api/habitaciones/6`
- **Esperado:** 204 — **Obtenido:** 204 — ✅ OK — 628 ms

**Respuesta:** _(sin contenido)_

### 25. Comprobar habitación eliminada

- **Petición:** `GET https://parcial-hotel.onrender.com/api/habitaciones/6`
- **Esperado:** 404 — **Obtenido:** 404 — ✅ OK — 511 ms

**Respuesta:**

```json
{
  "timestamp": "2026-10-23T19:06:19.269574206",
  "status": 404,
  "error": "Not Found",
  "mensaje": "Habitación con id 6 no encontrada",
  "ruta": "/api/habitaciones/6"
}
```

---

Para repetir estas pruebas, importa `Hotel-API.postman_collection.json` (en esta misma carpeta) en Postman y ejecuta la colección con **Run collection**.
