package ferreteria.tienda.comentario;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ComentarioService {
    private final ComentarioRepository comentarioRepository;

    public ComentarioService(ComentarioRepository comentarioRepository) {
        this.comentarioRepository = comentarioRepository;
    }

    @Transactional(readOnly = true)
    public List<ComentarioResponse> listarTodos() {
        return comentarioRepository.findAllByOrderByCreadoEnDesc()
                .stream().map(ComentarioResponse::desde).toList();
    }

    @Transactional
    public ComentarioResponse crear(ComentarioRequest request) {
        Comentario contacto = comentarioRepository.save(
                new Comentario(request.nombre().trim(), request.correo().trim(), request.comentario().trim()));
        return ComentarioResponse.desde(contacto);
    }

    @Transactional
    public void cambiarVisibilidad(Long id, boolean visible) {
        Comentario contacto = comentarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contacto no encontrado"));
        contacto.cambiarVisibilidad(visible);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!comentarioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contacto no encontrado");
        }
        comentarioRepository.deleteById(id);
    }
}
