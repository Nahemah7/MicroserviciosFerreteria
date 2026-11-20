package ferreteria.microservicios.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank String mail,
    @NotBlank String clave
) {}
