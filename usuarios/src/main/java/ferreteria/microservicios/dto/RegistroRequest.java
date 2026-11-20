package ferreteria.microservicios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistroRequest(
    @NotBlank String run,
    @NotBlank String nombre,
    @NotBlank String apellidos,
    @NotBlank @Email String mail,
    @NotBlank @Size(min =6) String clave,
    @NotBlank String region,
    @NotBlank String comuna,
    @NotBlank String direccion
) {
    
}
