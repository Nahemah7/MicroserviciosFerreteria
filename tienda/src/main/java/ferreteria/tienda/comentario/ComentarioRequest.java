package ferreteria.tienda.comentario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ComentarioRequest(
        @Size(max = 80) String nombre,
        @NotBlank @Size(max = 1000) String texto) {
}
