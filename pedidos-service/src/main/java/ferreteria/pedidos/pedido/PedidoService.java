package ferreteria.pedidos.pedido;

import ferreteria.pedidos.carrito.CarritoResponse;
import ferreteria.pedidos.carrito.CompraResponse;
import ferreteria.pedidos.carrito.TiendaClient;
import ferreteria.pedidos.notificacion.NotificacionRequest;
import ferreteria.pedidos.notificacion.NotificacionResponse;
import ferreteria.pedidos.notificacion.NotificacionesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PedidoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PedidoService.class);

    private final PedidoRepository pedidoRepository;
    private final TiendaClient tiendaClient;
    private final NotificacionesClient notificacionesClient;

    public PedidoService(PedidoRepository pedidoRepository, TiendaClient tiendaClient,
                        NotificacionesClient notificacionesClient) {
        this.pedidoRepository = pedidoRepository;
        this.tiendaClient = tiendaClient;
        this.notificacionesClient = notificacionesClient;
    }

    public PedidoResponse crear(PedidoRequest request) {
        Pedido existente = pedidoRepository.findByCarritoId(request.carritoId()).orElse(null);
        if (existente != null) {
            return respuesta(existente);
        }

        CarritoResponse carrito = obtenerCarrito(request.carritoId());
        if (carrito == null || carrito.items() == null || carrito.items().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El carrito está vacío o no está disponible");
        }

        Pedido pedido = pedidoRepository.save(
                new Pedido(carrito.id(), request.nombreCliente(), request.correoCliente(), carrito));

        CompraResponse compra;
        try {
            compra = tiendaClient.confirmarCarrito(carrito.id());
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().is4xxClientError()) {
                pedido.rechazar();
                pedidoRepository.save(pedido);
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Tienda rechazó la confirmación del carrito");
            }
            pedido.marcarPendienteConfirmacion();
            pedidoRepository.save(pedido);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Tienda no pudo confirmar el resultado; consulta el estado antes de reintentar");
        } catch (ResourceAccessException exception) {
            pedido.marcarPendienteConfirmacion();
            pedidoRepository.save(pedido);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "No se pudo confirmar el carrito; consulta el estado del pedido antes de reintentar");
        }

        if (compra == null || !"CONFIRMADA".equals(compra.estado())) {
            pedido.marcarPendienteConfirmacion();
            pedidoRepository.save(pedido);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Tienda no devolvió una confirmación clara; consulta el estado antes de reintentar");
        }
        pedido.confirmar(compra);
        pedido = pedidoRepository.save(pedido);
        registrarNotificacion(pedido);
        return respuesta(pedido);
    }

    public PedidoResponse obtener(Long id) {
        return respuesta(buscar(id));
    }

    public PedidoResponse notificar(Long id) {
        Pedido pedido = buscar(id);
        if (!"CONFIRMADA".equals(pedido.getEstado())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Solo se pueden notificar pedidos confirmados");
        }
        registrarNotificacion(pedido);
        return respuesta(pedido);
    }

    private CarritoResponse obtenerCarrito(String carritoId) {
        try {
            return tiendaClient.obtenerCarrito(carritoId);
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().value() == HttpStatus.NOT_FOUND.value()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado");
            }
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Tienda no pudo entregar los datos del carrito");
        } catch (ResourceAccessException exception) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "No se pudo conectar con tienda para consultar el carrito");
        }
    }

    private void registrarNotificacion(Pedido pedido) {
        try {
            NotificacionResponse notificacion = notificacionesClient.registrar(new NotificacionRequest(
                    pedido.getId(),
                    pedido.getCorreoCliente(),
                    "Pedido " + pedido.getId() + " confirmado por $" + pedido.getTotalCLP() + " CLP"));
            if (notificacion == null) {
                pedido.marcarNotificacionPendiente();
                pedidoRepository.save(pedido);
                LOGGER.warn("Notificaciones no devolvió una respuesta para el pedido {}", pedido.getId());
                return;
            }
            if (notificacion.id() == null) {
                pedido.marcarNotificacionPendiente();
                pedidoRepository.save(pedido);
                LOGGER.warn("Notificaciones devolvió una respuesta sin identificador para el pedido {}",
                        pedido.getId());
                return;
            }
            pedido.registrarNotificacion(notificacion.id());
            pedidoRepository.save(pedido);
        } catch (RestClientResponseException | ResourceAccessException exception) {
            pedido.marcarNotificacionPendiente();
            pedidoRepository.save(pedido);
            LOGGER.warn("No se pudo registrar la notificación del pedido {}", pedido.getId(), exception);
        }
    }

    private Pedido buscar(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));
    }

    private PedidoResponse respuesta(Pedido pedido) {
        return new PedidoResponse(
                pedido.getId(),
                pedido.getCarritoId(),
                pedido.getNombreCliente(),
                pedido.getEstado(),
                pedido.getTotalCLP(),
                pedido.getCreadoEn(),
                pedido.getConfirmadoEn(),
                pedido.getItems().stream()
                        .map(item -> new PedidoItemResponse(
                                item.getCodigo(), item.getNombre(), item.getCantidad(), item.getPrecioUnitario()))
                        .toList(),
                pedido.getNotificacionId(),
                pedido.getEstadoNotificacion());
    }
}
