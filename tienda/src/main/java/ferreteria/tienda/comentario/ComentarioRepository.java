package ferreteria.tienda.comentario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findAllByOrderByCreadoEnDesc();
}
