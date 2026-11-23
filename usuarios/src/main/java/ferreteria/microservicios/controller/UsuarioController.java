package ferreteria.microservicios.controller;

import ferreteria.microservicios.dto.LoginRequest;
import ferreteria.microservicios.dto.LoginResponse;
import ferreteria.microservicios.dto.RegistroRequest;
import ferreteria.microservicios.dto.UsuarioAdminRequest;
import ferreteria.microservicios.dto.UsuarioResponse;
import ferreteria.microservicios.dto.UsuarioUpdateRequest;
import ferreteria.microservicios.model.Usuario;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ferreteria.microservicios.service.UsuarioService;
import ferreteria.microservicios.util.JwtUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController 
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    //ESTO EXIGE ADMIN

    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    public List<UsuarioResponse> listar(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        exigirAdmin(authHeader);
        return usuarioService.listar().stream().map(UsuarioResponse::desde).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "bearerAuth")
    public UsuarioResponse crear(@Valid @RequestBody UsuarioAdminRequest r,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        exigirAdmin(authHeader);

        Usuario usuario = new Usuario();
        usuario.setRun(r.run());
        usuario.setNombre(r.nombre());
        usuario.setApellidos(r.apellidos());
        usuario.setMail(r.mail());
        usuario.setClave(r.clave());
        usuario.setTipoUsuario(r.tipoUsuario());
        usuario.setRegion(r.region());
        usuario.setComuna(r.comuna());
        usuario.setDireccion(r.direccion());
        usuario.setActivo(r.activo() == null || r.activo());

        return UsuarioResponse.desde(usuarioService.crear(usuario));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public UsuarioResponse actualizar(@PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateRequest r,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        exigirAdmin(authHeader);
        return UsuarioResponse.desde(usuarioService.actualizar(id, r));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "bearerAuth")
    public void eliminar(@PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        exigirAdmin(authHeader);
        usuarioService.eliminar(id);
    }

    @PatchMapping("/{id}/activo")
    @SecurityRequirement(name = "bearerAuth")
    public UsuarioResponse alternarActivo(@PathVariable Long id,
        @RequestHeader(value = "Authorization", required = false) String authHeader) {
            exigirAdmin(authHeader);
        return UsuarioResponse.desde(usuarioService.alternarActivo(id));
    }

    //ESTO EXIGE TOKEN

    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public UsuarioResponse obtener(@PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        exigirToken(authHeader);
        return UsuarioResponse.desde(usuarioService.obtener(id));
    }



    // MÉTODOS PÚBLICOS
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        Usuario usuario = usuarioService.login(request.mail(), request.clave());
        String token = jwtUtil.generarToken(usuario.getMail(), usuario.getTipoUsuario());
        return new LoginResponse(token, UsuarioResponse.desde(usuario));
    }

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponse registro(@Valid @RequestBody RegistroRequest r) {
        Usuario usuario = new Usuario();
        usuario.setRun(r.run());
        usuario.setNombre(r.nombre());
        usuario.setApellidos(r.apellidos());
        usuario.setMail(r.mail());
        usuario.setClave(r.clave());
        usuario.setRegion(r.region());
        usuario.setComuna(r.comuna());
        usuario.setDireccion(r.direccion());
        usuario.setTipoUsuario("Cliente");
        usuario.setActivo(true);

        Usuario creado = usuarioService.crear(usuario);
        String token = jwtUtil.generarToken(creado.getMail(), creado.getTipoUsuario());
        return new LoginResponse(token, UsuarioResponse.desde(creado));
    }



    //TOKEN
    private void exigirToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")
                || !jwtUtil.validarToken(authHeader.substring(7))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token requerido o inválido");
        }
    }

    //ADMIN
    private void exigirAdmin(String authHeader) {
        exigirToken(authHeader);
        String rol = jwtUtil.extraerRol(authHeader.substring(7));
        if (!"Administrador".equalsIgnoreCase(rol)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo un Administrador puede hacer esto");
        }
    }
    
}
