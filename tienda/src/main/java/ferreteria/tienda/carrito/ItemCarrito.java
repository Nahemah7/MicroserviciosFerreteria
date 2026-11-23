package ferreteria.tienda.carrito;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "items_carrito",
        uniqueConstraints = @UniqueConstraint(columnNames = {"carrito_id", "codigo_producto"}))
public class ItemCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;

    @Column(name = "codigo_producto", nullable = false, length = 40)
    private String codigoProducto;

    @Column(nullable = false)
    private int cantidad;

    protected ItemCarrito() {
    }

    public ItemCarrito(String codigoProducto, int cantidad) {
        this.codigoProducto = codigoProducto;
        this.cantidad = cantidad;
    }

    public Long getId() { return id; }
    public String getCodigoProducto() { return codigoProducto; }
    public int getCantidad() { return cantidad; }
    public void asignarCarrito(Carrito carrito) { this.carrito = carrito; }
    public void cambiarCantidad(int cantidad) { this.cantidad = cantidad; }
}
