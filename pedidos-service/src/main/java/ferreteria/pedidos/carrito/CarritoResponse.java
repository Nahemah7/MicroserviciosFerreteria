package ferreteria.pedidos.carrito;

import java.math.BigDecimal;
import java.util.List;

public record CarritoResponse(String id, List<ItemCarritoResponse> items, int cantidadTotal, BigDecimal totalCLP) {
}
