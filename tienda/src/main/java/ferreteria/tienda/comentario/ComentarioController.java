package ferreteria.tienda.comentario;

import java.util.List;

import ferreteria.tienda.security.AdminTokenVerifier;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioController {
    private final ComentarioService comentarioService;
    private final AdminTokenVerifier adminTokenVerifier;

    public ComentarioController(ComentarioService comentarioService, AdminTokenVerifier adminTokenVerifier) {
        this.comentarioService = comentarioService;
        this.adminTokenVerifier = adminTokenVerifier;
    }

    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    public List<ComentarioResponse> listarTodos(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        adminTokenVerifier.requireAdmin(authorization);
        return comentarioService.listarTodos();
    }

    @PatchMapping("/{id}/visibilidad")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "bearerAuth")
    public void cambiarVisibilidad(@PathVariable Long id, @Valid @RequestBody VisibilidadRequest request,
                                   @RequestHeader(value = "Authorization", required = false) String authorization) {
        adminTokenVerifier.requireAdmin(authorization);
        comentarioService.cambiarVisibilidad(id, request.visible());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "bearerAuth")
    public void eliminar(@PathVariable Long id,
                         @RequestHeader(value = "Authorization", required = false) String authorization) {
        adminTokenVerifier.requireAdmin(authorization);
        comentarioService.eliminar(id);
    }
}
