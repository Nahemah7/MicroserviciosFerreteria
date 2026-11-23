package ferreteria.tienda.comentario;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "comentarios")
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String codigoProducto;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false, length = 1000)
    private String texto;

    @Column(nullable = false)
    private boolean visible;

    @Column(nullable = false)
    private Instant creadoEn;

    protected Comentario() {
    }

    public Comentario(String codigoProducto, String nombre, String texto) {
        this.codigoProducto = codigoProducto;
        this.nombre = nombre;
        this.texto = texto;
        visible = true;
        creadoEn = Instant.now();
    }

    public Long getId() { return id; }
    public String getCodigoProducto() { return codigoProducto; }
    public String getNombre() { return nombre; }
    public String getTexto() { return texto; }
    public boolean isVisible() { return visible; }
    public Instant getCreadoEn() { return creadoEn; }
    public void cambiarVisibilidad(boolean visible) { this.visible = visible; }
}
