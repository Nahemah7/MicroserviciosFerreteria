package ferreteria.microservicios.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import ferreteria.microservicios.model.Usuario;
import ferreteria.microservicios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario obtener(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    public Usuario crear(Usuario usuario) {
        if (usuarioRepository.existsByMail(usuario.getMail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El mail ya está registrado");
        }
        if (usuarioRepository.existsByRun(usuario.getRun())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El run ya está registrado");
        }
        usuario.setId(null);
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Long id, Usuario datos) {
        Usuario existente = obtener(id);

        if (!existente.getMail().equals(datos.getMail())
            && usuarioRepository.existsByMail(datos.getMail())) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "El mail ya está registrado");
        }

        existente.setNombre(datos.getNombre());
        existente.setApellidos(datos.getApellidos());
        existente.setMail(datos.getMail());
        existente.setClave(datos.getClave());
        existente.setTipoUsuario(datos.getTipoUsuario());
        existente.setRegion(datos.getRegion());
        existente.setComuna(datos.getComuna());
        existente.setDireccion(datos.getDireccion());
        existente.setActivo(datos.isActivo());

        return usuarioRepository.save(existente);
    }

    public void eliminar(Long id) {
        obtener(id);
        usuarioRepository.deleteById(id);
    }
}
