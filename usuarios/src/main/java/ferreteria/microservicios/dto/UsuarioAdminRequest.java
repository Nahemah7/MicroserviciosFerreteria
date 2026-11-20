package ferreteria.microservicios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioAdminRequest(
        @NotBlank String run,
        @NotBlank String nombre,
        @NotBlank String apellidos,
        @NotBlank @Email String mail,
        @NotBlank @Size(min = 6) String clave,
        @NotBlank @Pattern(regexp = "Administrador|Contratista|Cliente") String tipoUsuario,
        String region,
        String comuna,
        String direccion,
        Boolean activo
) {
    
}