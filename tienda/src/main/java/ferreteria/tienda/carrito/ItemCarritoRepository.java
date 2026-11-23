package ferreteria.tienda.carrito;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
    boolean existsByCodigoProducto(String codigoProducto);
}
