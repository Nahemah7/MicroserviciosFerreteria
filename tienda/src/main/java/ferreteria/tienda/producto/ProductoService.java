package ferreteria.tienda.producto;

import java.util.List;
import java.util.Locale;

import ferreteria.tienda.carrito.ItemCarritoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final ItemCarritoRepository itemCarritoRepository;

    public ProductoService(ProductoRepository productoRepository, ItemCarritoRepository itemCarritoRepository) {
        this.productoRepository = productoRepository;
        this.itemCarritoRepository = itemCarritoRepository;
    }

    @Transactional(readOnly = true)
    public List<Producto> listar(String busqueda, String categoria) {
        String consulta = busqueda == null ? "" : busqueda.trim().toLowerCase(Locale.ROOT);
        String filtroCategoria = categoria == null ? "" : categoria.trim();
        return productoRepository.findAll().stream()
                .filter(p -> consulta.isEmpty()
                        || p.getCodigo().toLowerCase(Locale.ROOT).contains(consulta)
                        || p.getNombre().toLowerCase(Locale.ROOT).contains(consulta)
                        || p.getMarca().toLowerCase(Locale.ROOT).contains(consulta))
                .filter(p -> filtroCategoria.isEmpty()
                        || p.getCategoria().equalsIgnoreCase(filtroCategoria))
                .toList();
    }

    @Transactional(readOnly = true)
    public Producto obtener(String codigo) {
        return productoRepository.findById(codigo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Producto> stockBajo() {
        return productoRepository.findStockBajo();
    }

    @Transactional
    public Producto crear(ProductoRequest request) {
        if (productoRepository.existsById(request.codigo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un producto con ese código");
        }
        return productoRepository.save(new Producto(request.codigo(), request.categoria(), request.subcategoria(),
                request.nombre(), request.marca(), request.unidad(), request.pCompra(), request.pVenta(),
                request.stock(), request.stockMin()));
    }

    @Transactional
    public Producto actualizar(String codigo, ProductoRequest request) {
        if (!codigo.equals(request.codigo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El código del cuerpo debe coincidir con la URL");
        }
        Producto producto = obtener(codigo);
        producto.actualizar(request);
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto actualizarStock(String codigo, int stock) {
        Producto producto = obtener(codigo);
        producto.cambiarStock(stock);
        return productoRepository.save(producto);
    }

    @Transactional
    public void eliminar(String codigo) {
        Producto producto = obtener(codigo);
        if (itemCarritoRepository.existsByCodigoProducto(codigo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede eliminar: está presente en un carrito");
        }
        productoRepository.delete(producto);
    }
}
