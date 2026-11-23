package ferreteria.pedidos.carrito;

import java.math.BigDecimal;
import java.time.Instant;

public record CompraResponse(String carritoId, String estado, BigDecimal totalCLP, Instant confirmadaEn) {
}
