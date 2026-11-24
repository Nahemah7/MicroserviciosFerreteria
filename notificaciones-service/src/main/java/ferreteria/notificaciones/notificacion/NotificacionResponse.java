package ferreteria.notificaciones.notificacion;

import java.time.Instant;

public record NotificacionResponse(
        Long id,
        Long pedidoId,
        String destinatario,
        String mensaje,
        String estado,
        Instant creadoEn) {
}
