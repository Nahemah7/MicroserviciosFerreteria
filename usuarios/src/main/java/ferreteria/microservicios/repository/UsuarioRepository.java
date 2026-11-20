package ferreteria.microservicios.repository;

import ferreteria.microservicios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository <Usuario, Long> {

    Optional<Usuario> findByMail(String mail);

    boolean existsByMail(String mail);
    boolean existsByRun(String run);
}
