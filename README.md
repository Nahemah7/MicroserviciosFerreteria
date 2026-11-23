# Backend Ferretería Los Maestros

Backend en Java/Spring Boot con los servicios `usuarios`, `tienda-service` (productos, stock, carrito y comentarios), `pedidos-service` y `notificaciones-service`, además de Eureka y Gateway. Este paquete no incluye archivos del frontend.

## Requisitos

- Java 21
- MySQL en `localhost:3306`
- Maven Wrapper incluido en `usuarios`

Por defecto, cada servicio usa su propia base MySQL (`ferreteria_usuarios`, `ferreteria_tienda`, `ferreteria_pedidos` y `ferreteria_notificaciones`). Los servicios nuevos usan la misma cuenta local `root` sin contraseña; se pueden configurar con `DB_URL`, `DB_USER` y `DB_PASSWORD`. Antes de iniciar usuarios y tienda, define el mismo `JWT_SECRET` de al menos 32 bytes en ambas terminales; no guardes esa clave en el repositorio.

## Ejecutar en Windows

Desde la carpeta raíz, abre una terminal por aplicación. Inicia Eureka antes de los clientes; Gateway puede arrancar después:

En cada terminal de PowerShell de usuarios y tienda, configura `JWT_SECRET` con la misma clave aleatoria de al menos 32 bytes antes de ejecutar Maven:

```powershell
$env:JWT_SECRET = "<tu-clave-aleatoria-de-32-o-mas-bytes>"
```

```powershell
.\usuarios\mvnw.cmd -f .\eureka-server\pom.xml spring-boot:run
.\usuarios\mvnw.cmd -f .\usuarios\pom.xml spring-boot:run
.\usuarios\mvnw.cmd -f .\tienda\pom.xml spring-boot:run
.\usuarios\mvnw.cmd -f .\pedidos-service\pom.xml spring-boot:run
.\usuarios\mvnw.cmd -f .\notificaciones-service\pom.xml spring-boot:run
.\usuarios\mvnw.cmd -f .\gateway\pom.xml spring-boot:run
```

Maven descargará las dependencias en la primera ejecución.

| Servicio | Puerto | Dirección |
|---|---:|---|
| Gateway | 8080 | `http://localhost:8080` |
| Usuarios | 8081 | `http://localhost:8081` |
| Tienda | 8082 | `http://localhost:8082` |
| Pedidos | 8083 | `http://localhost:8083` |
| Notificaciones | 8084 | `http://localhost:8084` |
| Eureka | 8761 | `http://localhost:8761` |

Swagger del Gateway: `http://localhost:8080/swagger-ui.html`. Ahí se puede elegir Usuarios, Tienda, Pedidos o Notificaciones API.

Al arrancar tienda se insertan ocho productos de demostración únicamente si la tabla está vacía; no se sobrescriben stock ni productos existentes.
Pedidos y notificaciones usan sus propias bases de datos (`ferreteria_pedidos` y `ferreteria_notificaciones`). Notificaciones registra avisos en la base de datos; no envía correos porque no hay servidor SMTP configurado.

## Endpoints

El Gateway publica las APIs en `http://localhost:8080`.

- Usuarios: `POST /api/usuarios/login`, `POST /api/usuarios/registro`, y gestión administrativa bajo `/api/usuarios/**`.
- Catálogo: `GET /api/productos`, `GET /api/productos/{codigo}` y búsqueda opcional `?q=martillo&categoria=Herramientas`.
- Stock/productos (Administrador): `GET /api/productos/stock-bajo`, `POST /api/productos`, `PUT /api/productos/{codigo}`, `PATCH /api/productos/{codigo}/stock`, `DELETE /api/productos/{codigo}`.
- Carrito sin cuenta: `POST /api/carrito`, `GET /api/carrito/{id}`, `PUT /api/carrito/{id}/items/{codigo}` con `{"cantidad":2}`, `DELETE /api/carrito/{id}/items/{codigo}`, `DELETE /api/carrito/{id}/items`.
- Confirmación: `POST /api/carrito/{id}/confirmar` valida stock y descuenta existencias dentro de una transacción. No procesa pagos ni registra una orden.
- Pedidos: `POST /api/pedidos` con `{"carritoId":"<id>","nombreCliente":"Ana","correoCliente":"ana@example.com"}` crea el pedido, confirma el carrito a través de tienda y registra una notificación; `GET /api/pedidos/{id}` consulta el pedido y `POST /api/pedidos/{id}/notificar` reintenta registrar su notificación.
- Notificaciones: `GET /api/notificaciones/pedido/{pedidoId}` consulta el aviso registrado. Los avisos se crean desde pedidos y son idempotentes por número de pedido.
- Comentarios públicos: `GET /api/productos/{codigo}/comentarios`, `POST /api/productos/{codigo}/comentarios` con `{"nombre":"Ana","texto":"Buen producto"}`.
- Moderación (Administrador): `GET /api/comentarios`, `PATCH /api/comentarios/{id}/visibilidad` con `{"visible":false}`, `DELETE /api/comentarios/{id}`.

Las rutas administrativas requieren `Authorization: Bearer <JWT>` emitido por usuarios y con rol `Administrador`. Login y registro de usuarios no requieren token.

El servicio de pedidos guarda una copia de los artículos del carrito antes de solicitar su confirmación a tienda. Como los servicios conservan bases de datos independientes, una interrupción de red durante esa confirmación puede dejar el pedido en `PENDIENTE_CONFIRMACION`; consulta el estado antes de reintentar.
