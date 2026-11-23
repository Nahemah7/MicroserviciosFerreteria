package ferreteria.pedidos.carrito;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class TiendaClient {
    private final RestClient restClient;

    public TiendaClient(RestClient.Builder loadBalancedRestClientBuilder) {
        this.restClient = loadBalancedRestClientBuilder.baseUrl("http://tienda-service").build();
    }

    public CarritoResponse obtenerCarrito(String carritoId) {
        return restClient.get()
                .uri("/api/carrito/{id}", carritoId)
                .retrieve()
                .body(CarritoResponse.class);
    }

    public CompraResponse confirmarCarrito(String carritoId) {
        return restClient.post()
                .uri("/api/carrito/{id}/confirmar", carritoId)
                .retrieve()
                .body(CompraResponse.class);
    }
}
