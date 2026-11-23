package ferreteria.pedidos.notificacion;

public record NotificacionRequest(Long pedidoId, String destinatario, String mensaje) {
}
