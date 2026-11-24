package ferreteria.notificaciones.notificacion;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

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

    public List<NotificacionResponse> listar() {
        return notificacionRepository.findAllByOrderByCreadoEnDesc().stream()
                .map(this::respuesta)
                .toList();
    }

    private NotificacionResponse respuesta(Notificacion notificacion) {
        return new NotificacionResponse(
                notificacion.getId(),
                notificacion.getPedidoId(),
                notificacion.getDestinatario(),
                notificacion.getMensaje(),
                notificacion.getEstado(),
                notificacion.getCreadoEn());
    }
}
