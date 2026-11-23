package ferreteria.tienda.carrito;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {
    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarritoResponse crear() {
        return carritoService.crear();
    }

    @GetMapping("/{id}")
    public CarritoResponse obtener(@PathVariable String id) {
        return carritoService.obtener(id);
    }

    @PutMapping("/{id}/items/{codigo}")
    public CarritoResponse actualizarItem(@PathVariable String id, @PathVariable String codigo,
                                          @Valid @RequestBody CantidadRequest request) {
        return carritoService.actualizarItem(id, codigo, request.cantidad());
    }

    @DeleteMapping("/{id}/items/{codigo}")
    public CarritoResponse quitarItem(@PathVariable String id, @PathVariable String codigo) {
        return carritoService.quitarItem(id, codigo);
    }

    @DeleteMapping("/{id}/items")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vaciar(@PathVariable String id) {
        carritoService.vaciar(id);
    }

    @PostMapping("/{id}/confirmar")
    public CompraResponse confirmar(@PathVariable String id) {
        return carritoService.confirmar(id);
    }
}
