package ferreteria.notificaciones.notificacion;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {
    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public NotificacionResponse registrar(@Valid @RequestBody NotificacionRequest request) {
        return notificacionService.registrar(request);
    }

    @GetMapping("/pedido/{pedidoId}")
    public NotificacionResponse obtenerPorPedido(@PathVariable Long pedidoId) {
        return notificacionService.obtenerPorPedido(pedidoId);
    }

    @GetMapping
    public List<NotificacionResponse> listar() {
        return notificacionService.listar();
    }
}
