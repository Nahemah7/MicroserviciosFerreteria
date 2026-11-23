package ferreteria.tienda.producto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductoRequest(
        @NotBlank @Size(max = 40) String codigo,
        @NotBlank @Size(max = 100) String categoria,
        @NotBlank @Size(max = 100) String subcategoria,
        @NotBlank @Size(max = 180) String nombre,
        @NotBlank @Size(max = 100) String marca,
        @NotBlank @Size(max = 40) String unidad,
        @NotNull @DecimalMin("0.0") BigDecimal pCompra,
        @NotNull @DecimalMin("0.0") BigDecimal pVenta,
        @NotNull @Min(0) Integer stock,
        @NotNull @Min(0) Integer stockMin) {
}
