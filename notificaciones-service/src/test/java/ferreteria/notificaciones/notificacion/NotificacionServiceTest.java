package ferreteria.notificaciones.notificacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {
    @Mock
    private NotificacionRepository notificacionRepository;

    @InjectMocks
    private NotificacionService notificacionService;

    @Test
    void registrarPedidoExistenteNoDuplicaNotificacion() {
        Notificacion existente = new Notificacion(
                new NotificacionRequest(42L, "ana@example.com", "Pedido confirmado"));
        when(notificacionRepository.findByPedidoId(42L)).thenReturn(Optional.of(existente));

        NotificacionResponse respuesta = notificacionService.registrar(
                new NotificacionRequest(42L, "ana@example.com", "Pedido confirmado"));

        assertEquals(42L, respuesta.pedidoId());
        assertEquals("REGISTRADA", respuesta.estado());
        verify(notificacionRepository).findByPedidoId(42L);
        verifyNoMoreInteractions(notificacionRepository);
    }
}
