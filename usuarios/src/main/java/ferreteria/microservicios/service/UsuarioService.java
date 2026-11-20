package ferreteria.microservicios.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import ferreteria.microservicios.dto.UsuarioUpdateRequest;
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
        usuario.setClave(passwordEncoder.encode(usuario.getClave()));
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Long id, UsuarioUpdateRequest d) {
        Usuario existente = obtener(id);
        if (!existente.getMail().equals(d.mail())
                && usuarioRepository.existsByMail(d.mail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El mail ya está registrado");
        }
        existente.setNombre(d.nombre());
        existente.setApellidos(d.apellidos());
        existente.setMail(d.mail());
        existente.setTipoUsuario(d.tipoUsuario());
        existente.setRegion(d.region());
        existente.setComuna(d.comuna());
        existente.setDireccion(d.direccion());
        if (d.activo() != null) {
            existente.setActivo(d.activo());
        }
        if (d.clave() != null && !d.clave().isBlank()) {
            existente.setClave(passwordEncoder.encode(d.clave()));
        }
        return usuarioRepository.save(existente);
    }

    public void eliminar(Long id) {
        obtener(id);
        usuarioRepository.deleteById(id);
    }

    public Usuario login(String mail, String clave) {
        Usuario usuario = usuarioRepository.findByMail(mail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Credenciales incorrectas"));
        if (!passwordEncoder.matches(clave, usuario.getClave())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
        }
        if (!usuario.isActivo()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario inactivo");
        }
        return usuario;
    }

    private final PasswordEncoder passwordEncoder;

    public Usuario alternarActivo(Long id) {
        Usuario usuario = obtener(id);
        usuario.setActivo(!usuario.isActivo());
        return usuarioRepository.save(usuario);
    }
}
