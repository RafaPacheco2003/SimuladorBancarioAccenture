# Guía de demo en vivo — Simulador Bancario API

Esta guía sigue la hoja de evaluación **NovaBank_Evaluacion_Demo** (17 requisitos funcionales, RF-01 a RF-17). Para cada requisito indica qué mostrar en Swagger, qué resultado esperar y qué riesgo hay. La demo completa dura unos 20 minutos.

**Resultado esperado: 16 de 17.** RF-11 (comprobante) no cumple todavía; ver [Brechas conocidas](#brechas-conocidas).

## Antes de empezar (obligatorio)

| # | Qué | Por qué |
|---|---|---|
| 1 | La rama evaluada es `develop`. Debe incluir el PR #36 (commits MVP y seed). Sin él, fallan también RF-10, RF-11 y RF-17. | El documento evalúa `develop` |
| 2 | `.env` con `JWT_EXPIRATION=86400000` | RF-02 pide 24 h; `.env.example` trae 1 h |
| 3 | `.env` con `SEED_DEMO_DATA=true`, `DB_PASSWORD` y `JWT_SECRET` (`openssl rand -base64 32`) | Sin `DB_PASSWORD` o `JWT_SECRET` válido la app no arranca |
| 4 | `ALPHA_VANTAGE_API_KEY` válida | RF-14. El plan gratuito permite ~25 peticiones al día y cada arranque consume 5 |
| 5 | Internet disponible | RF-09 y RF-12 consultan Frankfurter en vivo |
| 6 | Base limpia: `docker compose down -v` y luego `docker compose up -d --build` | Deja los datos del seed intactos (saldos y contraseñas originales). **`down -v` borra todos los datos de Postgres.** |
| 7 | Esperar 1–2 minutos y verificar el login de Ana | Maven compila al arrancar |

URLs:

- Swagger: http://localhost:8080/swagger-ui/index.html
- Mailpit (correos capturados): http://localhost:8025

> **Límite global de 50 peticiones por minuto.** Está activo para toda la app (un solo contador para todos los usuarios, no por usuario). Al pasarlo, cualquier endpoint responde **429** durante el resto del minuto. Cargar Swagger también cuenta. Durante la demo: un solo navegador, sin recargar Swagger de más y sin que los evaluadores llamen a la API al mismo tiempo. Si aparece un 429, espera un minuto.

## Datos de demo (seed)

Contraseña de ambos usuarios: **`Demo1234!`**

| Usuario | Cuenta | Divisa | Saldo inicial | Estado |
|---|---|---|---|---|
| Ana (`demo.ana@example.com`) | `3141000000000001` | MXN | 50,000.00 | ACTIVE |
| Ana | `3141000000000002` | USD | 3,000.00 | ACTIVE, con 5 AAPL (precio promedio 185.50) |
| Luis (`demo.luis@example.com`) | `3141000000000003` | MXN | 20,000.00 | ACTIVE |
| Luis | `3141000000000004` | MXN | 1,000.00 | **BLOCKED** |

Acciones: AAPL, MSFT, TSLA, AMZN y WMT. El seed solo crea las que no existen y no sobrescribe precios.

El seed corre en cada arranque, pero solo crea lo que falta: **no reinicia saldos ni contraseñas**. Para volver al estado inicial hay que repetir el paso 6.

En Swagger, después de cada login, pulsa **Authorize** (candado) y pega solo el token, sin `Bearer`.

## Guion por requisito

Regla común de la hoja: toda operación sobre una cuenta ajena responde **403**. Se muestra en el bloque de movimientos.

### Autenticación (RF-01 a RF-04)

| RF | Paso | Resultado esperado |
|---|---|---|
| RF-01 | `POST /auth/register` con nombre, CURP, email, contraseña de 8+ caracteres y teléfono | 201 |
| RF-01 | Repetir el mismo registro | 409 (CURP o email duplicado) |
| RF-01 | Registro con contraseña de 5 caracteres | 400 |
| RF-02 | `POST /auth/login` con `demo.ana@example.com` / `Demo1234!` | 200 + `token` |
| RF-02 | Pegar el token en jwt.io | Claims `id` y `fullName`; `exp` − `iat` = 86400 (24 h) |
| RF-02 | Login con contraseña incorrecta | 401 |
| RF-03 | `POST /auth/forgot-password` `{"email":"demo.luis@example.com"}` | 200 |
| RF-03 | Abrir Mailpit, correo **"Recupera tu cuenta"** | Correo con plantilla HTML (Thymeleaf) y la clave temporal |
| RF-03 | Repetir forgot-password | Llega una clave nueva; la anterior deja de servir (una clave activa por usuario) |
| RF-04 | `POST /auth/reset-password` `{"temporalKey":"<última clave>","newPassword":"NuevaClave123"}` | 200 |
| RF-04 | Reusar la misma clave | 400 (un solo uso) |
| RF-04 | Login de Luis con `NuevaClave123` | 200 |

La caducidad de 15 minutos no se puede mostrar en vivo; está en `ConfigurationConstants.TOKEN_EXPIRATION_MINUTES = 15`.

> Se usa a **Luis** para la recuperación para que Ana conserve `Demo1234!` el resto de la demo. Desde aquí, Luis usa `NuevaClave123`.

### Cuentas (RF-05, RF-06) — token de Ana

| RF | Paso | Resultado esperado |
|---|---|---|
| RF-05 | `POST /account/create` `{"accountType":"Checking","currency":"USD"}` | 200, texto con un número de 16 dígitos que empieza con `3141` |
| RF-05 | `GET /account/<número nuevo>/balance` | Saldo 0, divisa USD |
| RF-06 | `GET /account/3141000000000001/balance` | 200, saldo y divisa MXN |

El estado inicial ACTIVE no aparece en la respuesta; se demuestra al depositar en la cuenta nueva (solo se permite en cuentas activas).

### Movimientos (RF-07 a RF-11)

| RF | Paso (token de Ana salvo que se indique) | Resultado esperado |
|---|---|---|
| RF-07 | `POST /transactions/deposits` `{"account":"3141000000000001","amount":1500,"concept":"Depósito demo"}` | 201 |
| RF-07 | Depósito con `amount` 0 | 400 |
| RF-07 | Token de Luis: depósito a `3141000000000004` (bloqueada) | 400 "La cuenta no está activa o se encuentra bloqueada." |
| RF-08 | `POST /transactions/withdrawals` `{"account":"3141000000000001","amount":500,"concept":"Retiro demo"}` | 201 |
| RF-08 | Retiro con `amount` 99999999 | 422 "Saldo insuficiente" |
| RF-09 | `POST /transactions/transfers` `{"originAccount":"3141000000000001","destinationAccount":"3141000000000003","amount":500,"concept":"Pago demo"}` | 201 (MXN → MXN, tasa 1) |
| RF-09 / RF-12 | Token de Luis: saldo de `3141000000000003` antes y después de una transferencia de Ana desde `3141000000000002` (USD) por 10 | Luis recibe ~185 MXN, convertidos con la tasa del día |
| RF-09 | Transferencia de `3141000000000001` a `3141000000000002` (ambas de Ana) | 400 (el destino debe ser de otro usuario) |
| Regla 403 | Token de Luis: `GET /account/3141000000000001/balance` | 403 |
| RF-10 | `GET /transactions/3141000000000001` | Entradas y salidas, de la más reciente a la más antigua |
| RF-10 | Token de Luis: `GET /transactions/3141000000000003` | Aparecen las transferencias recibidas de Ana |
| RF-11 | Respuesta de cualquier depósito o transferencia | `id`, cuentas, `amount`, `transactionStatus`, `concept`, `date` |

> **RF-11 no cumple.** El comprobante no trae el monto destino, las divisas ni el tipo de cambio. Los datos se guardan en la base, pero la respuesta no los expone. Decirlo antes de que lo pregunten.

### Tipo de cambio (RF-12)

Se demuestra con la transferencia USD → MXN del bloque anterior: el monto recibido por Luis es igual a 10 por la tasa de Frankfurter. Con divisas iguales la tasa es 1 (la transferencia MXN → MXN llega completa).

La tasa no aparece en ninguna respuesta. Si se pide verla, consultar https://api.frankfurter.dev/v1/latest?from=USD&to=MXN.

### Mercado (RF-13, RF-14) — token de Ana

| RF | Paso | Resultado esperado |
|---|---|---|
| RF-13 | `GET /stocks/consulta` | Lista con `ticker`, `companyName`, `currentPrice` y `updatedAt` |
| RF-14 | `POST /batch/quotes` | 200 "Job iniciado". Tarda unos segundos (corre síncrono) |
| RF-14 | `GET /stocks/consulta` otra vez | `currentPrice` y `updatedAt` actualizados desde Alpha Vantage |

Las ejecuciones diaria (02:00 UTC) y al arrancar se explican con `DailyQuotesScheduler`, porque no se pueden esperar en vivo.

> **Riesgo RF-13.** Cuando el batch actualiza una acción, escribe el ticker en `companyName` (por ejemplo, `AAPL` en lugar de `Apple Inc.`). Con API key válida esto ya pasa al arrancar. Si el evaluador lo nota, es la brecha #8.

### Trading y portafolio (RF-15 a RF-17) — cuenta USD de Ana `3141000000000002`

| RF | Paso | Resultado esperado |
|---|---|---|
| RF-17 | `GET /cuentas/3141000000000002/portafolio` | 5 AAPL con precio promedio, precio actual y ganancia o pérdida |
| RF-15 | `POST /trading/account/3141000000000002/actions/buy` `{"ticker":"MSFT","quantity":2}` | 201 con precio de ejecución, total y nuevo saldo |
| RF-15 | Compra con `quantity` 0 | 400 |
| RF-15 | Compra de 1 AAPL y volver al portafolio | El precio promedio de AAPL se recalcula |
| RF-15 | Compra en la cuenta MXN `3141000000000001` | Descuenta el precio convertido de USD a MXN |
| RF-16 | `POST .../actions/sell` `{"ticker":"MSFT","quantity":100}` | 400, acciones insuficientes |
| RF-16 | `POST .../actions/sell` `{"ticker":"MSFT","quantity":2}` | 201, abona el importe |
| RF-17 | Portafolio otra vez | MSFT ya no aparece (se ocultan las posiciones en 0) |
| Regla 403 | Token de Luis: portafolio de `3141000000000002` | 403 |

## Brechas conocidas

Para responder con honestidad si el evaluador pregunta.

| RF | Brecha | ¿Se ve en la demo? |
|---|---|---|
| RF-11 | El comprobante no expone el monto destino, las divisas ni el tipo de cambio | **Sí**: único requisito que no cumple |
| RF-13 | `companyName` igual al ticker cuando lo carga el batch | Sí, si el batch ya corrió |
| RF-02 | La vigencia depende de `JWT_EXPIRATION`; `.env.example` trae 1 h | Solo si no se configuró 24 h |
| RF-12 | Las tasas entre divisas distintas no se redondean a 6 decimales | No |
| RF-14 | No hay espera de 1.5 s entre símbolos | No |
| RF-15 | La compra no valida que la cuenta esté activa ni guarda la orden (el criterio no lo exige) | No |
| RF-05 | Sin `accountType` o `currency` responde 500 en lugar de 400 | Solo si se envía el body incompleto |
| — | Logout solo del lado del cliente; el token vale hasta que expira | No |

Correcciones a la hoja de evaluación:

- Sección 5: además de `/auth`, también Swagger es público.
- Sección 6: el SMTP local es Mailpit, sin STARTTLS. STARTTLS aplica con un servidor SMTP real.

## Checklist antes de presentar

- [ ] `develop` incluye el PR #36.
- [ ] `.env`: `JWT_EXPIRATION=86400000`, `SEED_DEMO_DATA=true`, `ALPHA_VANTAGE_API_KEY` válida.
- [ ] Base reiniciada con `docker compose down -v` y `up -d --build`.
- [ ] `docker compose ps` muestra `nova_bank_backend`, `postgres_db` y `mailpit` arriba.
- [ ] El login de Ana con `Demo1234!` responde 200.
- [ ] Mailpit abre en el puerto 8025.
- [ ] Un solo navegador con Swagger, para no pasar el límite de 50 peticiones por minuto.
