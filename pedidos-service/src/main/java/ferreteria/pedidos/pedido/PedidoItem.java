package ferreteria.pedidos.pedido;

import java.math.BigDecimal;

import ferreteria.pedidos.carrito.ItemCarritoResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PedidoItem {
    @Column(name = "codigo_producto", nullable = false)
    private String codigo;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private int cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    protected PedidoItem() {
    }

    public PedidoItem(ItemCarritoResponse item) {
        this.codigo = item.codigo();
        this.nombre = item.nombre();
        this.cantidad = item.cantidad();
        this.precioUnitario = item.precioUnitario();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }
}
