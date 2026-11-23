package ferreteria.tienda.producto;

import java.util.List;

import ferreteria.tienda.comentario.ComentarioRequest;
import ferreteria.tienda.comentario.ComentarioResponse;
import ferreteria.tienda.comentario.ComentarioService;
import ferreteria.tienda.security.AdminTokenVerifier;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    private final ProductoService productoService;
    private final ComentarioService comentarioService;
    private final AdminTokenVerifier adminTokenVerifier;

    public ProductoController(ProductoService productoService, ComentarioService comentarioService,
                              AdminTokenVerifier adminTokenVerifier) {
        this.productoService = productoService;
        this.comentarioService = comentarioService;
        this.adminTokenVerifier = adminTokenVerifier;
    }

    @GetMapping
    public List<Producto> listar(@RequestParam(required = false) String q,
                                 @RequestParam(required = false) String categoria) {
        return productoService.listar(q, categoria);
    }

    @GetMapping("/{codigo}")
    public Producto obtener(@PathVariable String codigo) {
        return productoService.obtener(codigo);
    }

    @GetMapping("/stock-bajo")
    @SecurityRequirement(name = "bearerAuth")
    public List<Producto> stockBajo(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        adminTokenVerifier.requireAdmin(authorization);
        return productoService.stockBajo();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "bearerAuth")
    public Producto crear(@Valid @RequestBody ProductoRequest request,
                          @RequestHeader(value = "Authorization", required = false) String authorization) {
        adminTokenVerifier.requireAdmin(authorization);
        return productoService.crear(request);
    }

    @PutMapping("/{codigo}")
    @SecurityRequirement(name = "bearerAuth")
    public Producto actualizar(@PathVariable String codigo, @Valid @RequestBody ProductoRequest request,
                               @RequestHeader(value = "Authorization", required = false) String authorization) {
        adminTokenVerifier.requireAdmin(authorization);
        return productoService.actualizar(codigo, request);
    }

    @PatchMapping("/{codigo}/stock")
    @SecurityRequirement(name = "bearerAuth")
    public Producto actualizarStock(@PathVariable String codigo, @Valid @RequestBody StockRequest request,
                                    @RequestHeader(value = "Authorization", required = false) String authorization) {
        adminTokenVerifier.requireAdmin(authorization);
        return productoService.actualizarStock(codigo, request.stock());
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "bearerAuth")
    public void eliminar(@PathVariable String codigo,
                         @RequestHeader(value = "Authorization", required = false) String authorization) {
        adminTokenVerifier.requireAdmin(authorization);
        productoService.eliminar(codigo);
    }

    @GetMapping("/{codigo}/comentarios")
    public List<ComentarioResponse> listarComentarios(@PathVariable String codigo) {
        return comentarioService.listarVisibles(codigo);
    }

    @PostMapping("/{codigo}/comentarios")
    @ResponseStatus(HttpStatus.CREATED)
    public ComentarioResponse crearComentario(@PathVariable String codigo,
                                              @Valid @RequestBody ComentarioRequest request) {
        return comentarioService.crear(codigo, request);
    }
}
