package ferreteria.pedidos.pedido;

import java.math.BigDecimal;

public record PedidoItemResponse(String codigo, String nombre, int cantidad, BigDecimal precioUnitario) {
}
