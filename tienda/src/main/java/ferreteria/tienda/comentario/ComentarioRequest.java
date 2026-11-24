package ferreteria.tienda.comentario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ComentarioRequest(
        @NotBlank @Size(max = 80) String nombre,
        @NotBlank @Email @Size(max = 254) String correo,
        @NotBlank @Size(max = 1000) String comentario) {
}
