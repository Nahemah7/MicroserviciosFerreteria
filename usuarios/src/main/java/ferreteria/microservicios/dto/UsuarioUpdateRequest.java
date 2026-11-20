package ferreteria.microservicios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UsuarioUpdateRequest(
        @NotBlank String nombre,
        @NotBlank String apellidos,
        @NotBlank @Email String mail,
        @Pattern(regexp = "^$|.{6,}", message = "La clave debe tener al menos 6 caracteres") String clave,
        @NotBlank @Pattern(regexp = "Administrador|Contratista|Cliente") String tipoUsuario,
        String region,
        String comuna,
        String direccion,
        Boolean activo
) {
    
}