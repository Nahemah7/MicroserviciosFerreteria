package ferreteria.tienda.carrito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import ferreteria.tienda.producto.Producto;
import ferreteria.tienda.producto.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {
    @Mock
    private CarritoRepository carritoRepository;
    @Mock
    private ProductoRepository productoRepository;

    private CarritoService carritoService;
    private Carrito carrito;
    private Producto producto;

    @BeforeEach
    void setUp() {
        carritoService = new CarritoService(carritoRepository, productoRepository);
        carrito = new Carrito("carrito-1");
        carrito.agregar(new ItemCarrito("MC001", 2));
        producto = new Producto("MC001", "Construcción", "Cemento", "Cemento", "Marca",
                "Saco", new BigDecimal("3000"), new BigDecimal("5000"), 5, 1);
        when(carritoRepository.findById("carrito-1")).thenReturn(Optional.of(carrito));
    }

    @Test
    void confirmarDescuentaStockYVacíaElCarrito() {
        when(productoRepository.buscarParaActualizacion("MC001")).thenReturn(Optional.of(producto));

        CompraResponse compra = carritoService.confirmar("carrito-1");

        assertEquals(3, producto.getStock());
        assertEquals(new BigDecimal("10000"), compra.totalCLP());
        assertEquals("CONFIRMADA", compra.estado());
        assertEquals(0, carrito.getItems().size());
    }

    @Test
    void confirmarNoDescuentaStockInsuficiente() {
        producto.cambiarStock(1);
        when(productoRepository.buscarParaActualizacion("MC001")).thenReturn(Optional.of(producto));

        assertThrows(ResponseStatusException.class, () -> carritoService.confirmar("carrito-1"));

        assertEquals(1, producto.getStock());
        assertEquals(1, carrito.getItems().size());
    }
}
