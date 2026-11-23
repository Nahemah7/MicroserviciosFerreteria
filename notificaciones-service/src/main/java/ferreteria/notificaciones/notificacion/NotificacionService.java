package ferreteria.notificaciones.notificacion;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class NotificacionService {
    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public NotificacionResponse registrar(NotificacionRequest request) {
        Notificacion notificacion = notificacionRepository.findByPedidoId(request.pedidoId())
                .orElseGet(() -> notificacionRepository.save(new Notificacion(request)));
        return respuesta(notificacion);
    }

    public NotificacionResponse obtenerPorPedido(Long pedidoId) {
        Notificacion notificacion = notificacionRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notificación no encontrada"));
        return respuesta(notificacion);
    }

    private NotificacionResponse respuesta(Notificacion notificacion) {
        return new NotificacionResponse(
                notificacion.getId(),
                notificacion.getPedidoId(),
                notificacion.getEstado(),
                notificacion.getCreadoEn());
    }
}
