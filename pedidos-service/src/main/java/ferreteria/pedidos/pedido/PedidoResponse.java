package ferreteria.pedidos.pedido;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record PedidoResponse(
        Long id,
        String carritoId,
        String nombreCliente,
        String estado,
        BigDecimal totalCLP,
        Instant creadoEn,
        Instant confirmadoEn,
        List<PedidoItemResponse> items,
        Long notificacionId,
        String estadoNotificacion) {
}
