package ferreteria.tienda.comentario;

import java.util.List;

import ferreteria.tienda.producto.ProductoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ComentarioService {
    private final ComentarioRepository comentarioRepository;
    private final ProductoRepository productoRepository;

    public ComentarioService(ComentarioRepository comentarioRepository, ProductoRepository productoRepository) {
        this.comentarioRepository = comentarioRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<ComentarioResponse> listarVisibles(String codigoProducto) {
        if (!productoRepository.existsById(codigoProducto)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");
        }
        return comentarioRepository.findByCodigoProductoAndVisibleTrueOrderByCreadoEnDesc(codigoProducto)
                .stream().map(ComentarioResponse::desde).toList();
    }

    @Transactional(readOnly = true)
    public List<ComentarioResponse> listarTodos() {
        return comentarioRepository.findAllByOrderByCreadoEnDesc()
                .stream().map(ComentarioResponse::desde).toList();
    }

    @Transactional
    public ComentarioResponse crear(String codigoProducto, ComentarioRequest request) {
        if (!productoRepository.existsById(codigoProducto)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");
        }
        String nombre = request.nombre() == null || request.nombre().isBlank()
                ? "Anónimo" : request.nombre().trim();
        Comentario comentario = comentarioRepository.save(
                new Comentario(codigoProducto, nombre, request.texto().trim()));
        return ComentarioResponse.desde(comentario);
    }

    @Transactional
    public void cambiarVisibilidad(Long id, boolean visible) {
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comentario no encontrado"));
        comentario.cambiarVisibilidad(visible);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!comentarioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comentario no encontrado");
        }
        comentarioRepository.deleteById(id);
    }
}
