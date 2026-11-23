package ferreteria.tienda.carrito;

import jakarta.validation.constraints.Min;

public record CantidadRequest(@Min(1) int cantidad) {
}
