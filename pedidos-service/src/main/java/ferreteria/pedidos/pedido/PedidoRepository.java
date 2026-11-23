package ferreteria.pedidos.pedido;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Optional<Pedido> findByCarritoId(String carritoId);
}
