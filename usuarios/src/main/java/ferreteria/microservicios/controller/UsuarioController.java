package ferreteria.microservicios.controller;

import ferreteria.microservicios.dto.LoginRequest;
import ferreteria.microservicios.dto.LoginResponse;
import ferreteria.microservicios.dto.UsuarioResponse;
import ferreteria.microservicios.model.Usuario;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ferreteria.microservicios.service.UsuarioService;
import ferreteria.microservicios.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public List<UsuarioResponse> listar() {
        return usuarioService.listar().stream().map(UsuarioResponse::desde).toList();
    }

    @GetMapping("/{id}")
    public UsuarioResponse obtener(@PathVariable Long id) {
        return UsuarioResponse.desde(usuarioService.obtener(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse crear(@Valid @RequestBody Usuario usuario) {
        return UsuarioResponse.desde(usuarioService.crear(usuario));
    }

    @PutMapping("/{id}")
    public UsuarioResponse actualizar(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        return UsuarioResponse.desde(usuarioService.actualizar(id, usuario));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
    }




    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        Usuario usuario = usuarioService.login(request.mail(), request.clave());
        String token = jwtUtil.generarToken(usuario.getMail(), usuario.getTipoUsuario());
        return new LoginResponse(token, UsuarioResponse.desde(usuario));
    }
    
}
