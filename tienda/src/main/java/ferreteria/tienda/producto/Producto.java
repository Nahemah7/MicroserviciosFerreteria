package ferreteria.tienda.producto;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @Column(length = 40, nullable = false)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String categoria;

    @Column(nullable = false, length = 100)
    private String subcategoria;

    @Column(nullable = false, length = 180)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String marca;

    @Column(nullable = false, length = 40)
    private String unidad;

    @Column(name = "p_compra", nullable = false, precision = 12, scale = 2)
    private BigDecimal pCompra;

    @Column(name = "p_venta", nullable = false, precision = 12, scale = 2)
    private BigDecimal pVenta;

    @Column(nullable = false)
    private int stock;

    @Column(name = "stock_min", nullable = false)
    private int stockMin;

    protected Producto() {
    }

    public Producto(String codigo, String categoria, String subcategoria, String nombre, String marca,
                    String unidad, BigDecimal pCompra, BigDecimal pVenta, int stock, int stockMin) {
        this.codigo = codigo;
        this.categoria = categoria;
        this.subcategoria = subcategoria;
        this.nombre = nombre;
        this.marca = marca;
        this.unidad = unidad;
        this.pCompra = pCompra;
        this.pVenta = pVenta;
        this.stock = stock;
        this.stockMin = stockMin;
    }

    public String getCodigo() { return codigo; }
    public String getCategoria() { return categoria; }
    public String getSubcategoria() { return subcategoria; }
    public String getNombre() { return nombre; }
    public String getMarca() { return marca; }
    public String getUnidad() { return unidad; }
    public BigDecimal getPCompra() { return pCompra; }
    public BigDecimal getPVenta() { return pVenta; }
    public int getStock() { return stock; }
    public int getStockMin() { return stockMin; }

    public void actualizar(ProductoRequest request) {
        categoria = request.categoria();
        subcategoria = request.subcategoria();
        nombre = request.nombre();
        marca = request.marca();
        unidad = request.unidad();
        pCompra = request.pCompra();
        pVenta = request.pVenta();
        stock = request.stock();
        stockMin = request.stockMin();
    }

    public void cambiarStock(int stock) {
        this.stock = stock;
    }
}
