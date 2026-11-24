package ferreteria.microservicios.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import ferreteria.microservicios.model.Usuario;
import ferreteria.microservicios.repository.UsuarioRepository;
import ferreteria.microservicios.service.UsuarioService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsuariosInicialesRunner implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    @Value("${ADMIN_PASSWORD:admin123}")
    private String claveAdmin;

    @Override
    public void run(ApplicationArguments args) {

        if (usuarioRepository.count() > 0) return;

        crear("11.111.111-1", "Admin", "Sistema", "admin@ferreterialosmaestros.cl",
                claveAdmin, "Administrador", "Coquimbo", "La Serena", "Av. Francisco de Aguirre 123", true);
        crear("12.345.678-5", "Juan", "Pérez Soto", "juan.perez@gmail.com",
                "cliente123", "Cliente", "Coquimbo", "Coquimbo", "Los Aromos 456", true);
        crear("9.876.543-3", "María", "González López", "maria.gonzalez@gmail.com",
                "maria2024", "Contratista", "Coquimbo", "La Serena", "El Olivo 789", true);
        crear("15.222.333-1", "Pedro", "Rojas Díaz", "pedro.rojas@hotmail.com",
                "pedro2024", "Cliente", "Valparaíso", "Valparaíso", "Condell 321", false);
    }

    private void crear(String run, String nombre, String apellidos, String mail, String clave,
                       String tipoUsuario, String region, String comuna, String direccion, boolean activo) {
        Usuario u = new Usuario();
        u.setRun(run);
        u.setNombre(nombre);
        u.setApellidos(apellidos);
        u.setMail(mail);
        u.setClave(clave);
        u.setTipoUsuario(tipoUsuario);
        u.setRegion(region);
        u.setComuna(comuna);
        u.setDireccion(direccion);
        u.setActivo(activo);
        usuarioService.crear(u);
    }
}
