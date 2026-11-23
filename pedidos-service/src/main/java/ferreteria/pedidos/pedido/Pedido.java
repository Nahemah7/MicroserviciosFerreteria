package ferreteria.pedidos.pedido;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ferreteria.pedidos.carrito.CarritoResponse;
import ferreteria.pedidos.carrito.CompraResponse;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "carrito_id", nullable = false, unique = true)
    private String carritoId;

    @Column(name = "nombre_cliente", nullable = false)
    private String nombreCliente;

    @Column(name = "correo_cliente", nullable = false)
    private String correoCliente;

    @Column(nullable = false)
    private String estado;

    @Column(name = "total_clp", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalCLP;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;

    @Column(name = "confirmado_en")
    private Instant confirmadoEn;

    @ElementCollection
    @CollectionTable(name = "pedido_items", joinColumns = @JoinColumn(name = "pedido_id"))
    @OrderColumn(name = "posicion")
    private List<PedidoItem> items = new ArrayList<>();

    @Column(name = "notificacion_id")
    private Long notificacionId;

    @Column(name = "estado_notificacion", nullable = false)
    private String estadoNotificacion;

    protected Pedido() {
    }

    public Pedido(String carritoId, String nombreCliente, String correoCliente, CarritoResponse carrito) {
        this.carritoId = carritoId;
        this.nombreCliente = nombreCliente;
        this.correoCliente = correoCliente;
        this.estado = "PENDIENTE_CONFIRMACION";
        this.totalCLP = carrito.totalCLP();
        this.creadoEn = Instant.now();
        this.estadoNotificacion = "PENDIENTE";
        this.items = carrito.items().stream().map(PedidoItem::new).collect(Collectors.toCollection(ArrayList::new));
    }

    public void confirmar(CompraResponse compra) {
        this.estado = compra.estado();
        this.totalCLP = compra.totalCLP();
        this.confirmadoEn = compra.confirmadaEn();
    }

    public void rechazar() {
        this.estado = "RECHAZADO";
    }

    public void marcarPendienteConfirmacion() {
        this.estado = "PENDIENTE_CONFIRMACION";
    }

    public void registrarNotificacion(Long id) {
        this.notificacionId = id;
        this.estadoNotificacion = "REGISTRADA";
    }

    public void marcarNotificacionPendiente() {
        this.estadoNotificacion = "PENDIENTE";
    }

    public Long getId() {
        return id;
    }

    public String getCarritoId() {
        return carritoId;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public String getEstado() {
        return estado;
    }

    public BigDecimal getTotalCLP() {
        return totalCLP;
    }

    public Instant getCreadoEn() {
        return creadoEn;
    }

    public Instant getConfirmadoEn() {
        return confirmadoEn;
    }

    public List<PedidoItem> getItems() {
        return List.copyOf(items);
    }

    public Long getNotificacionId() {
        return notificacionId;
    }

    public String getEstadoNotificacion() {
        return estadoNotificacion;
    }
}
