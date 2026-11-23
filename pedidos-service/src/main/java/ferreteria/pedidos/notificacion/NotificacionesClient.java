package ferreteria.pedidos.notificacion;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class NotificacionesClient {
    private final RestClient restClient;

    public NotificacionesClient(RestClient.Builder loadBalancedRestClientBuilder) {
        this.restClient = loadBalancedRestClientBuilder.baseUrl("http://notificaciones-service").build();
    }

    public NotificacionResponse registrar(NotificacionRequest request) {
        return restClient.post()
                .uri("/api/notificaciones")
                .body(request)
                .retrieve()
                .body(NotificacionResponse.class);
    }
}
