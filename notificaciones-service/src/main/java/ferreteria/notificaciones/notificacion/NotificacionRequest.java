package ferreteria.notificaciones.notificacion;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record NotificacionRequest(
        @NotNull @Positive Long pedidoId,
        @NotBlank @Email String destinatario,
        @NotBlank String mensaje) {
}
