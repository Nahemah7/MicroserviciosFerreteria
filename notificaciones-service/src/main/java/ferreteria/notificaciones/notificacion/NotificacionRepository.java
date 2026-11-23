package ferreteria.notificaciones.notificacion;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    Optional<Notificacion> findByPedidoId(Long pedidoId);
}
