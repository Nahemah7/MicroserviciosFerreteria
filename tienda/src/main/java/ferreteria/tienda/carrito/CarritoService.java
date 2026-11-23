package ferreteria.tienda.carrito;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import ferreteria.tienda.producto.Producto;
import ferreteria.tienda.producto.ProductoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CarritoService {
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;

    public CarritoService(CarritoRepository carritoRepository, ProductoRepository productoRepository) {
        this.carritoRepository = carritoRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional
    public CarritoResponse crear() {
        return respuesta(carritoRepository.save(new Carrito(UUID.randomUUID().toString())));
    }

    @Transactional(readOnly = true)
    public CarritoResponse obtener(String id) {
        return respuesta(buscarCarrito(id));
    }

    @Transactional
    public CarritoResponse actualizarItem(String id, String codigo, int cantidad) {
        Carrito carrito = buscarCarrito(id);
        Producto producto = productoRepository.findById(codigo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        if (cantidad > producto.getStock()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La cantidad solicitada supera el stock disponible");
        }
        ItemCarrito item = carrito.getItems().stream()
                .filter(actual -> actual.getCodigoProducto().equals(codigo))
                .findFirst()
                .orElse(null);
        if (item == null) carrito.agregar(new ItemCarrito(codigo, cantidad));
        else item.cambiarCantidad(cantidad);
        return respuesta(carritoRepository.save(carrito));
    }

    @Transactional
    public CarritoResponse quitarItem(String id, String codigo) {
        Carrito carrito = buscarCarrito(id);
        carrito.getItems().stream()
                .filter(item -> item.getCodigoProducto().equals(codigo))
                .findFirst().ifPresent(carrito::quitar);
        return respuesta(carritoRepository.save(carrito));
    }

    @Transactional
    public void vaciar(String id) {
        Carrito carrito = buscarCarrito(id);
        carrito.vaciar();
        carritoRepository.save(carrito);
    }

    @Transactional
    public CompraResponse confirmar(String id) {
        Carrito carrito = buscarCarrito(id);
        if (carrito.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede confirmar un carrito vacío");
        }
        BigDecimal total = BigDecimal.ZERO;
        List<ItemCarrito> itemsOrdenados = carrito.getItems().stream()
                .sorted(Comparator.comparing(ItemCarrito::getCodigoProducto)).toList();
        for (ItemCarrito item : itemsOrdenados) {
            Producto producto = productoRepository.buscarParaActualizacion(item.getCodigoProducto())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                            "Un producto del carrito ya no está disponible"));
            if (item.getCantidad() > producto.getStock()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Stock insuficiente para el producto " + producto.getCodigo());
            }
            producto.cambiarStock(producto.getStock() - item.getCantidad());
            total = total.add(producto.getPVenta().multiply(BigDecimal.valueOf(item.getCantidad())));
        }
        carrito.vaciar();
        carritoRepository.save(carrito);
        return new CompraResponse(id, "CONFIRMADA", total, Instant.now());
    }

    private Carrito buscarCarrito(String id) {
        return carritoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado"));
    }

    private CarritoResponse respuesta(Carrito carrito) {
        List<ItemCarritoResponse> items = carrito.getItems().stream().map(item -> {
            Producto producto = productoRepository.findById(item.getCodigoProducto())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                            "Un producto del carrito ya no está disponible"));
            return new ItemCarritoResponse(producto.getCodigo(), producto.getNombre(), item.getCantidad(),
                    producto.getPVenta(), producto.getStock());
        }).toList();
        int cantidadTotal = items.stream().mapToInt(ItemCarritoResponse::cantidad).sum();
        BigDecimal total = items.stream()
                .map(item -> item.precioUnitario().multiply(BigDecimal.valueOf(item.cantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CarritoResponse(carrito.getId(), items, cantidadTotal, total);
    }
}
