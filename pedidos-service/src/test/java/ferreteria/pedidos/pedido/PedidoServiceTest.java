package ferreteria.pedidos.pedido;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import ferreteria.pedidos.carrito.CarritoResponse;
import ferreteria.pedidos.carrito.ItemCarritoResponse;
import ferreteria.pedidos.carrito.TiendaClient;
import ferreteria.pedidos.notificacion.NotificacionesClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {
    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private TiendaClient tiendaClient;

    @Mock
    private NotificacionesClient notificacionesClient;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void noConfirmaDosVecesUnCarritoQueYaTienePedido() {
        CarritoResponse carrito = new CarritoResponse(
                "carrito-1",
                List.of(new ItemCarritoResponse("P-1", "Martillo", 1, new BigDecimal("1000"), 5)),
                1,
                new BigDecimal("1000"));
        Pedido existente = new Pedido("carrito-1", "Ana", "ana@example.com", carrito);
        when(pedidoRepository.findByCarritoId("carrito-1")).thenReturn(Optional.of(existente));

        PedidoResponse respuesta = pedidoService.crear(
                new PedidoRequest("carrito-1", "Ana", "ana@example.com"));

        assertEquals("PENDIENTE_CONFIRMACION", respuesta.estado());
        verifyNoInteractions(tiendaClient, notificacionesClient);
    }
}
