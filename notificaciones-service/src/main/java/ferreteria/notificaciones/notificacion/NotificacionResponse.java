package ferreteria.notificaciones.notificacion;

import java.time.Instant;

public record NotificacionResponse(Long id, Long pedidoId, String estado, Instant creadoEn) {
}
