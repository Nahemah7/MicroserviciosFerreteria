package ferreteria.pedidos.pedido;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PedidoRequest(
        @NotBlank String carritoId,
        @NotBlank String nombreCliente,
        @NotBlank @Email String correoCliente) {
}
