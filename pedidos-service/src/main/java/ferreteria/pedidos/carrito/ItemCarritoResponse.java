package ferreteria.pedidos.carrito;

import java.math.BigDecimal;

public record ItemCarritoResponse(String codigo, String nombre, int cantidad, BigDecimal precioUnitario, int stock) {
}
